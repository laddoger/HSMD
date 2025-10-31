package com.ruoyi.environment.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 喷雾阀门开度对象 spray_valve_opening
 * 
 * @author Wang
 * @date 2025-10-31
 */
public class SVOpening extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 扇区号 */
    @Excel(name = "扇区号")
    private String sectorId;

    /** 当前开度值 */
    @Excel(name = "当前开度值")
    private String currentOpening;

    /** 建议开度值 */
    @Excel(name = "建议开度值")
    private String suggestedOpening;

    /** 时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:SS")
    @Excel(name = "时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:SS")
    private Date ts;

    public void setSectorId(String sectorId) 
    {
        this.sectorId = sectorId;
    }

    public String getSectorId() 
    {
        return sectorId;
    }

    public void setCurrentOpening(String currentOpening) 
    {
        this.currentOpening = currentOpening;
    }

    public String getCurrentOpening() 
    {
        return currentOpening;
    }

    public void setSuggestedOpening(String suggestedOpening) 
    {
        this.suggestedOpening = suggestedOpening;
    }

    public String getSuggestedOpening() 
    {
        return suggestedOpening;
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
            .append("sectorId", getSectorId())
            .append("currentOpening", getCurrentOpening())
            .append("suggestedOpening", getSuggestedOpening())
            .append("ts", getTs())
            .toString();
    }
}
