package com.ruoyi.environment.ingest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.environment.domain.RPipeTemp;
import com.ruoyi.environment.ingest.dto.TempsJson;
import com.ruoyi.environment.mapper.RPipeTempMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 定时读取 temperature_data.json 文件并写入 return_pipe_temp 表
 */
@Service
public class ReturnPipeTempIngestService {

    private static final Logger log = LoggerFactory.getLogger(ReturnPipeTempIngestService.class);

    @Autowired
    private RPipeTempMapper mapper;

    private final ObjectMapper om = new ObjectMapper();

    @Value("${ingest.temps.path}")
    private String filePath;
    @Value("${ingest.temps.charset:UTF-8}")
    private String charset;
    @Value("${ingest.temps.stableMillis:300}")
    private long stableMillis;
    @Value("${ingest.temps.minCelsius:-50}")
    private int minC;
    @Value("${ingest.temps.maxCelsius:150}")
    private int maxC;

    /** 单次采集：读取文件 -> 解析 -> 批量UPSERT */
    @Transactional(rollbackFor = Exception.class)
    public void ingestOnce() {
        Path p = Paths.get(filePath);
        if (!Files.exists(p)) {
            log.warn("temperature_data file not found: {}", filePath);
            return;
        }
        try {
            if (!isStable(p, stableMillis)) {
                log.debug("file not stable yet: {}", filePath);
                return;
            }

            String json = new String(Files.readAllBytes(p), Charset.forName(charset));
            TempsJson root = om.readValue(json, TempsJson.class);

            // 统一时间戳：优先 data.timestamp -> root.timestamp -> 文件mtime -> now
            Timestamp ts = Timestamp.from(resolveInstant(root, p));

            // 展开并过滤：5..10扇区最多15个三角，其余最多14个
            List<RPipeTemp> batch = new ArrayList<>(600);
            if (root.getData() == null || root.getData().getSectors() == null) {
                log.warn("no sectors in temperature_data");
                return;
            }
            for (TempsJson.Sector s : root.getData().getSectors()) {
                Integer sector = s.getSector();
                if (sector == null) continue;
                int maxTriangle = (sector >= 5 && sector <= 10) ? 15 : 14;

                if (s.getReturnWaterTemps() == null) continue;
                for (TempsJson.Rwt r : s.getReturnWaterTemps()) {
                    if (r.getTriangle() == null) continue;
                    int tri = r.getTriangle();
                    if (tri < 1 || tri > maxTriangle) {
                        log.warn("skip out-of-range triangle: sector={}, triangle={}", sector, tri);
                        continue;
                    }
                    addIfValid(batch, sector, tri, "L", r.getLeftTemp(), ts);
                    addIfValid(batch, sector, tri, "R", r.getRightTemp(), ts);
                }
            }

            if (!batch.isEmpty()) {
                mapper.batchUpsertRPipeTemp(batch);
                log.info("return_pipe_temp upsert rows={}", batch.size());
            } else {
                log.info("return_pipe_temp nothing to upsert");
            }

        } catch (Exception e) {
            log.error("Ingest return_pipe_temp error", e);
            throw new RuntimeException(e);
        }
    }

    private void addIfValid(List<RPipeTemp> batch, int sector, int tri, String side, Double temp, Timestamp ts) {
        if (temp == null || temp.isNaN() || temp.isInfinite()) return;
        long rounded = Math.round(temp);
        if (rounded < minC || rounded > maxC) {
            log.warn("discard temp out of range: s={}, t={}, side={}, val={}", sector, tri, side, temp);
            return;
        }
        RPipeTemp e = new RPipeTemp();
        // 你的项目里 Mapper/Service 多处以 String sectorId 使用，因此这里用 String
        e.setSectorId(String.valueOf(sector));
        e.setTriangleNo(String.valueOf(tri));
        e.setSide(side);
        e.setTempCelsius(Long.valueOf(rounded));
        e.setTs(ts);
        batch.add(e);
    }

    private boolean isStable(Path p, long stableMs) throws Exception {
        BasicFileAttributes a1 = Files.readAttributes(p, BasicFileAttributes.class);
        long s1 = a1.size();
        FileTime t1 = a1.lastModifiedTime();
        Thread.sleep(stableMs);
        BasicFileAttributes a2 = Files.readAttributes(p, BasicFileAttributes.class);
        return s1 == a2.size() && t1.equals(a2.lastModifiedTime());
    }

    private Instant resolveInstant(TempsJson root, Path p) {
        String ts1 = null, ts2 = null;
        if (root != null) {
            if (root.getData() != null) ts1 = root.getData().getTimestamp();
            ts2 = root.getTimestamp();
        }
        String tsStr = (notBlank(ts1) ? ts1 : (notBlank(ts2) ? ts2 : null));
        if (tsStr != null) {
            // 示例时间格式： "yyyy-MM-dd HH:mm:ss"
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime ldt = LocalDateTime.parse(tsStr, fmt);
            return ldt.atZone(ZoneId.systemDefault()).toInstant();
        }
        try {
            return Files.getLastModifiedTime(p).toInstant();
        } catch (Exception e) {
            return Instant.now();
        }
    }

    private boolean notBlank(String s) { return s != null && !s.trim().isEmpty(); }

    // 在 ReturnPipeTempIngestService.java 中添加这个可选方法（类内任意位置）：
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