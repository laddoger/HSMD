package com.ruoyi.environment.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 全局参数对象 global_parameters
 * 
 * @author Wang
 * @date 2025-10-28
 */
public class GParameters extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 机组负荷 */
    @Excel(name = "机组负荷")
    private BigDecimal unitLoad;

    /** 背压 */
    @Excel(name = "背压")
    private BigDecimal backPressure;

    /** 出口温度 */
    @Excel(name = "出口温度")
    private BigDecimal outletTemperature;

    /** 水泵频率 */
    @Excel(name = "水泵频率")
    private BigDecimal pumpFrequency;

    /** 水泵水压 */
    @Excel(name = "水泵水压")
    private BigDecimal pumpPressure;

    /** 泵电流 */
    @Excel(name = "泵电流")
    private BigDecimal pumpCurrent;

    /** 出口流量 */
    @Excel(name = "出口流量")
    private BigDecimal outletFlow;

    /** 时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date recordTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setUnitLoad(BigDecimal unitLoad) 
    {
        this.unitLoad = unitLoad;
    }

    public BigDecimal getUnitLoad() 
    {
        return unitLoad;
    }

    public void setBackPressure(BigDecimal backPressure) 
    {
        this.backPressure = backPressure;
    }

    public BigDecimal getBackPressure() 
    {
        return backPressure;
    }

    public void setOutletTemperature(BigDecimal outletTemperature) 
    {
        this.outletTemperature = outletTemperature;
    }

    public BigDecimal getOutletTemperature() 
    {
        return outletTemperature;
    }

    public void setPumpFrequency(BigDecimal pumpFrequency) 
    {
        this.pumpFrequency = pumpFrequency;
    }

    public BigDecimal getPumpFrequency() 
    {
        return pumpFrequency;
    }

    public void setPumpPressure(BigDecimal pumpPressure) 
    {
        this.pumpPressure = pumpPressure;
    }

    public BigDecimal getPumpPressure() 
    {
        return pumpPressure;
    }

    public void setPumpCurrent(BigDecimal pumpCurrent) 
    {
        this.pumpCurrent = pumpCurrent;
    }

    public BigDecimal getPumpCurrent() 
    {
        return pumpCurrent;
    }

    public void setOutletFlow(BigDecimal outletFlow) 
    {
        this.outletFlow = outletFlow;
    }

    public BigDecimal getOutletFlow() 
    {
        return outletFlow;
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
            .append("unitLoad", getUnitLoad())
            .append("backPressure", getBackPressure())
            .append("outletTemperature", getOutletTemperature())
            .append("pumpFrequency", getPumpFrequency())
            .append("pumpPressure", getPumpPressure())
            .append("pumpCurrent", getPumpCurrent())
            .append("outletFlow", getOutletFlow())
            .append("recordTime", getRecordTime())
            .toString();
    }
}
