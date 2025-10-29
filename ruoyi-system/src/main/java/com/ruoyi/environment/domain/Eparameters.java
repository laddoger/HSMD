package com.ruoyi.environment.domain;

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
    private Long globalEnvTemp;

    /** 环境湿度，单位% */
    @Excel(name = "环境湿度，单位%")
    private Long envHumidity;

    /** 环境风速，单位m/s */
    @Excel(name = "环境风速，单位m/s")
    private Long envWindSpeed;

    /** 环境风向，单位N/S */
    @Excel(name = "环境风向，单位N/S")
    private String envWindDir;

    /** 塔外环境温度，单位℃ */
    @Excel(name = "塔外环境温度，单位℃")
    private Long towerEnvTemp;

    /** 时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date ts;

    public void setGlobalEnvTemp(Long globalEnvTemp) 
    {
        this.globalEnvTemp = globalEnvTemp;
    }

    public Long getGlobalEnvTemp() 
    {
        return globalEnvTemp;
    }

    public void setEnvHumidity(Long envHumidity) 
    {
        this.envHumidity = envHumidity;
    }

    public Long getEnvHumidity() 
    {
        return envHumidity;
    }

    public void setEnvWindSpeed(Long envWindSpeed) 
    {
        this.envWindSpeed = envWindSpeed;
    }

    public Long getEnvWindSpeed() 
    {
        return envWindSpeed;
    }

    public void setEnvWindDir(String envWindDir) 
    {
        this.envWindDir = envWindDir;
    }

    public String getEnvWindDir() 
    {
        return envWindDir;
    }

    public void setTowerEnvTemp(Long towerEnvTemp) 
    {
        this.towerEnvTemp = towerEnvTemp;
    }

    public Long getTowerEnvTemp() 
    {
        return towerEnvTemp;
    }

    public void setTs(Date ts) 
    {
        this.ts = ts;
    }

    public Date getTs() 
    {
        return ts;
    }

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
