package com.ruoyi.environment.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 风速数据对象 wind_speed_data
 * 
 * @author Wang
 * @date 2025-10-29
 */
public class WSpeedData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 扇区号 */
    @Excel(name = "扇区号")
    private Long sector;

    /** 传感器号 */
    @Excel(name = "传感器号")
    private Long sensor;

    /** 风速 (m/s) */
    @Excel(name = "风速 (m/s)")
    private BigDecimal windSpeed;

    /** 风向 (度) */
    @Excel(name = "风向 (度)")
    private BigDecimal windDirection;

    /** 记录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "记录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date recordTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setSector(Long sector) 
    {
        this.sector = sector;
    }

    public Long getSector() 
    {
        return sector;
    }

    public void setSensor(Long sensor) 
    {
        this.sensor = sensor;
    }

    public Long getSensor() 
    {
        return sensor;
    }

    public void setWindSpeed(BigDecimal windSpeed) 
    {
        this.windSpeed = windSpeed;
    }

    public BigDecimal getWindSpeed() 
    {
        return windSpeed;
    }

    public void setWindDirection(BigDecimal windDirection) 
    {
        this.windDirection = windDirection;
    }

    public BigDecimal getWindDirection() 
    {
        return windDirection;
    }

    public void setRecordTime(Date recordTime) 
    {
        this.recordTime = recordTime;
    }

    public Date getRecordTime() 
    {
        return recordTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("sector", getSector())
            .append("sensor", getSensor())
            .append("windSpeed", getWindSpeed())
            .append("windDirection", getWindDirection())
            .append("recordTime", getRecordTime())
            .toString();
    }
}
