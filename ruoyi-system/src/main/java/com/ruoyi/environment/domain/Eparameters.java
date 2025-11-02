package com.ruoyi.environment.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 环境参数对象 Eparameters
 *
 * @author Wang
 * @date 2025-10-29
 */
public class Eparameters extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 全局环境温度，单位℃ */
    @Excel(name = "全局环境温度，单位℃")
    private BigDecimal globalEnvTemp;

    /** 环境湿度，单位% */
    @Excel(name = "环境湿度，单位%")
    private BigDecimal envHumidity;

    /** 环境风速，单位m/s */
    @Excel(name = "环境风速，单位m/s")
    private BigDecimal envWindSpeed;

    /** 环境风向，单位°/10（已支持小数） */
    @Excel(name = "环境风向，单位°/10")
    private BigDecimal envWindDir;

    /** 塔外环境温度，单位℃ */
    @Excel(name = "塔外环境温度，单位℃")
    private BigDecimal towerEnvTemp;

    /** 时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date ts;

    public BigDecimal getGlobalEnvTemp() { return globalEnvTemp; }
    public void setGlobalEnvTemp(BigDecimal globalEnvTemp) { this.globalEnvTemp = globalEnvTemp; }

    public BigDecimal getEnvHumidity() { return envHumidity; }
    public void setEnvHumidity(BigDecimal envHumidity) { this.envHumidity = envHumidity; }

    public BigDecimal getEnvWindSpeed() { return envWindSpeed; }
    public void setEnvWindSpeed(BigDecimal envWindSpeed) { this.envWindSpeed = envWindSpeed; }

    public BigDecimal getEnvWindDir() { return envWindDir; }
    public void setEnvWindDir(BigDecimal envWindDir) { this.envWindDir = envWindDir; }

    public BigDecimal getTowerEnvTemp() { return towerEnvTemp; }
    public void setTowerEnvTemp(BigDecimal towerEnvTemp) { this.towerEnvTemp = towerEnvTemp; }

    public Date getTs() { return ts; }
    public void setTs(Date ts) { this.ts = ts; }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("globalEnvTemp", getGlobalEnvTemp())
                .append("envHumidity", getEnvHumidity())
                .append("envWindSpeed", getEnvWindSpeed())
                .append("envWindDir", getEnvWindDir())
                .append("towerEnvTemp", getTowerEnvTemp())
                .append("ts", getTs())
                .toString();
    }
}