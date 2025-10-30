package com.ruoyi.environment.mapper;

import java.util.List;
import com.ruoyi.environment.domain.RPipeTemp;

/**
 * 回水管温度Mapper接口
 * 
 * @author Wang
 * @date 2025-10-30
 */
public interface RPipeTempMapper 
{
    /**
     * 查询回水管温度
     * 
     * @param sectorId 回水管温度主键
     * @return 回水管温度
     */
    public RPipeTemp selectRPipeTempBySectorId(String sectorId);

    /**
     * 查询回水管温度列表
     * 
     * @param rPipeTemp 回水管温度
     * @return 回水管温度集合
     */
    public List<RPipeTemp> selectRPipeTempList(RPipeTemp rPipeTemp);

    /**
     * 新增回水管温度
     * 
     * @param rPipeTemp 回水管温度
     * @return 结果
     */
    public int insertRPipeTemp(RPipeTemp rPipeTemp);

    /**
     * 修改回水管温度
     * 
     * @param rPipeTemp 回水管温度
     * @return 结果
     */
    public int updateRPipeTemp(RPipeTemp rPipeTemp);

    /**
     * 删除回水管温度
     * 
     * @param sectorId 回水管温度主键
     * @return 结果
     */
    public int deleteRPipeTempBySectorId(String sectorId);

    /**
     * 批量删除回水管温度
     * 
     * @param sectorIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRPipeTempBySectorIds(String[] sectorIds);
}
