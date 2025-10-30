package com.ruoyi.environment.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 回水管温度对象 return_pipe_temp
 * 
 * @author Wang
 * @date 2025-10-30
 */
public class RPipeTemp extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 扇区号 */
    @Excel(name = "扇区号")
    private String sectorId;

    /** 三角 */
    @Excel(name = "三角")
    private String triangleNo;

    /** 回水管方向：L=左，R=右 */
    @Excel(name = "回水管方向：L=左，R=右")
    private String side;

    /** 温度℃ */
    @Excel(name = "温度℃")
    private Long tempCelsius;

    /** 数据时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "数据时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date ts;

    public void setSectorId(String sectorId) 
    {
        this.sectorId = sectorId;
    }

    public String getSectorId() 
    {
        return sectorId;
    }

    public void setTriangleNo(String triangleNo) 
    {
        this.triangleNo = triangleNo;
    }

    public String getTriangleNo() 
    {
        return triangleNo;
    }

    public void setSide(String side) 
    {
        this.side = side;
    }

    public String getSide() 
    {
        return side;
    }

    public void setTempCelsius(Long tempCelsius) 
    {
        this.tempCelsius = tempCelsius;
    }

    public Long getTempCelsius() 
    {
        return tempCelsius;
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
            .append("triangleNo", getTriangleNo())
            .append("side", getSide())
            .append("tempCelsius", getTempCelsius())
            .append("ts", getTs())
            .toString();
    }
}
