package com.ruoyi.environment.ingest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.environment.domain.Eparameters;
import com.ruoyi.environment.domain.WSpeedData;
import com.ruoyi.environment.ingest.dto.WeatherJson;
import com.ruoyi.environment.mapper.EparametersMapper;
import com.ruoyi.environment.mapper.WSpeedDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 读取 weather_data.json：
 * 1) weather_data 数组各指标取【最大值】写入 Eparameters
 * 2) sector_wind_speeds 写入 wind_speed_data
 */
@Service
public class WeatherIngestService {

    private static final Logger log = LoggerFactory.getLogger(WeatherIngestService.class);

    private final ObjectMapper om = new ObjectMapper();
    private final EparametersMapper eparametersMapper;
    private final WSpeedDataMapper wSpeedDataMapper;

    public WeatherIngestService(EparametersMapper eparametersMapper, WSpeedDataMapper wSpeedDataMapper) {
        this.eparametersMapper = eparametersMapper;
        this.wSpeedDataMapper = wSpeedDataMapper;
    }

    @Value("${ingest.weather.path}")
    private String filePath;
    @Value("${ingest.weather.charset:UTF-8}")
    private String charset;
    @Value("${ingest.weather.stableMillis:1500}")
    private long stableMillis;

    @Transactional(rollbackFor = Exception.class)
    public void ingestOnce() {
        Path p = Paths.get(filePath);
        if (!Files.exists(p)) {
            log.warn("[weather] file not found: {}", filePath);
            return;
        }
        try {
            if (!isStable(p, stableMillis)) {
                log.info("[weather] file not stable yet, skip: {}", filePath);
                return;
            }

            String json = new String(Files.readAllBytes(p), Charset.forName(charset));
            WeatherJson root = om.readValue(json, WeatherJson.class);

            // 统一时间戳：优先 data.timestamp -> root.timestamp -> 文件mtime -> now
            Instant inst = resolveInstant(root, p);
            // Eparameters.ts / wind_speed_data.record_time 使用 java.util.Date
            Date tsDate = Date.from(inst);

            /* -------- 1) weather_data → 取最大值，写入 Eparameters -------- */
            double maxWindSpeed = Double.NEGATIVE_INFINITY;
            double maxWindDir   = Double.NEGATIVE_INFINITY; // 原始角度(°*10)
            double maxTemp      = Double.NEGATIVE_INFINITY;
            double maxHumidity  = Double.NEGATIVE_INFINITY;
            double maxPressure  = Double.NEGATIVE_INFINITY;

            if (root.getData() != null && root.getData().getWeatherData() != null) {
                for (WeatherJson.PlatformBlock pb : root.getData().getWeatherData()) {
                    WeatherJson.PlatformData d = pb.getData();
                    if (d == null) continue;
                    if (d.getWindSpeed()     != null) maxWindSpeed = Math.max(maxWindSpeed, d.getWindSpeed());
                    if (d.getWindDirection() != null) maxWindDir   = Math.max(maxWindDir,   d.getWindDirection());
                    if (d.getTemperature()   != null) maxTemp      = Math.max(maxTemp,      d.getTemperature());
                    if (d.getHumidity()      != null) maxHumidity  = Math.max(maxHumidity,  d.getHumidity());
                    if (d.getPressure()      != null) maxPressure  = Math.max(maxPressure,  d.getPressure());
                }
            }

            if (Double.isFinite(maxWindSpeed) || Double.isFinite(maxWindDir) ||
                    Double.isFinite(maxTemp) || Double.isFinite(maxHumidity) || Double.isFinite(maxPressure)) {

                Eparameters e = new Eparameters();
                if (Double.isFinite(maxTemp))      e.setGlobalEnvTemp(Long.valueOf(Math.round(maxTemp)));
                if (Double.isFinite(maxHumidity))  e.setEnvHumidity(Long.valueOf(Math.round(maxHumidity)));
                if (Double.isFinite(maxWindSpeed)) e.setEnvWindSpeed(Long.valueOf(Math.round(maxWindSpeed)));
                if (Double.isFinite(maxPressure))  e.setTowerEnvTemp(Long.valueOf(Math.round(maxPressure)));

                // 风向：先 ÷10 再四舍五入为整数（单位：°/10），env_wind_dir 为 SMALLINT -> Java 用 Integer
                if (Double.isFinite(maxWindDir)) {
                    int dirDiv10 = (int) Math.round(maxWindDir / 10.0);
                    e.setEnvWindDir(dirDiv10);
                }

                e.setTs(tsDate);
                // 若依代码生成的规范命名：insertEparameters
                eparametersMapper.insertEparameters(e);

                log.info("[weather] Eparameters inserted ts={}, max(ws={},dir/10={},temp={},hum={},pres={})",
                        tsDate,
                        Double.isFinite(maxWindSpeed) ? maxWindSpeed : null,
                        Double.isFinite(maxWindDir) ? (maxWindDir / 10.0) : null,
                        Double.isFinite(maxTemp) ? maxTemp : null,
                        Double.isFinite(maxHumidity) ? maxHumidity : null,
                        Double.isFinite(maxPressure) ? maxPressure : null
                );
            } else {
                log.warn("[weather] no valid platform metrics");
            }

            /* -------- 2) sector_wind_speeds → wind_speed_data -------- */
            List<WSpeedData> rows = new ArrayList<>(64);
            if (root.getData() != null && root.getData().getSectorWindSpeeds() != null) {
                for (WeatherJson.SectorWind sw : root.getData().getSectorWindSpeeds()) {
                    if (sw == null || sw.getSector() == null || sw.getWindSpeeds() == null) continue;
                    Integer sector = sw.getSector();
                    for (Map.Entry<String, Double> en : sw.getWindSpeeds().entrySet()) {
                        Integer sensor = parseTriangleKey(en.getKey()); // "triangle_5" -> 5
                        if (sensor == null) continue;
                        Double val = en.getValue();
                        if (val == null || val.isNaN() || val.isInfinite()) continue;

                        WSpeedData w = new WSpeedData();
                        w.setSector(sector);
                        w.setSensor(sensor);
                        w.setWindSpeed(new BigDecimal(String.format(Locale.ROOT, "%.2f", val)));
                        w.setWindDirection(new BigDecimal("0.00")); // JSON 未提供，默认 0
                        w.setRecordTime(tsDate);
                        rows.add(w);
                    }
                }
            }

            if (!rows.isEmpty()) {
                wSpeedDataMapper.batchInsert(rows);
                log.info("[weather] wind_speed_data insert rows={}", rows.size());
            } else {
                log.info("[weather] wind_speed_data nothing to insert");
            }

        } catch (Exception e) {
            log.error("[weather] ingest error", e);
            throw new RuntimeException(e);
        }
    }

    /* ================= helpers ================= */

    private boolean isStable(Path p, long stableMs) throws Exception {
        BasicFileAttributes a1 = Files.readAttributes(p, BasicFileAttributes.class);
        long s1 = a1.size();
        FileTime t1 = a1.lastModifiedTime();
        Thread.sleep(stableMs);
        BasicFileAttributes a2 = Files.readAttributes(p, BasicFileAttributes.class);
        return s1 == a2.size() && t1.equals(a2.lastModifiedTime());
    }

    private Instant resolveInstant(WeatherJson root, Path p) {
        String ts1 = null, ts2 = null;
        if (root != null) {
            if (root.getData() != null) ts1 = root.getData().getTimestamp();
            ts2 = root.getTimestamp();
        }
        String tsStr = (notBlank(ts1) ? ts1 : (notBlank(ts2) ? ts2 : null));
        if (tsStr != null) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime ldt = LocalDateTime.parse(tsStr, fmt);
            return ldt.atZone(ZoneId.systemDefault()).toInstant();
        }
        try { return Files.getLastModifiedTime(p).toInstant(); }
        catch (Exception e) { return Instant.now(); }
    }

    private boolean notBlank(String s) { return s != null && !s.trim().isEmpty(); }

    private Integer parseTriangleKey(String key) {
        if (key == null) return null;
        // match "triangle_5" -> 5
        int idx = key.indexOf('_');
        if (idx < 0 || idx == key.length() - 1) return null;
        try { return Integer.parseInt(key.substring(idx + 1)); }
        catch (NumberFormatException e) { return null; }
    }

    /** 支持 Quartz 传入路径的便捷包装 */
    public void ingestOnceOverridePath(String overridePath) {
        String original = this.filePath;
        try {
            this.filePath = overridePath;
            ingestOnce();
        } finally {
            this.filePath = original;
        }
    }
}