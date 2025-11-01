package com.ruoyi.environment.service;

import java.util.List;
import com.ruoyi.environment.domain.RPipeTemp;

/**
 * 回水管温度Service接口
 * 
 * @author Wang
 * @date 2025-10-30
 */
public interface IRPipeTempService 
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
     * 批量删除回水管温度
     * 
     * @param sectorIds 需要删除的回水管温度主键集合
     * @return 结果
     */
    public int deleteRPipeTempBySectorIds(String[] sectorIds);

    /**
     * 删除回水管温度信息
     * 
     * @param sectorId 回水管温度主键
     * @return 结果
     */
    public int deleteRPipeTempBySectorId(String sectorId);
    /**
     * 通过复合主键查询
     *
     * @param sectorId 扇区号
     * @param triangleNo 三角号
     * @param side 管路方向 L/R
     * @param ts 时间戳
     * @return 回水管温度
     */
    public RPipeTemp selectRPipeTempByKey(String sectorId, Integer triangleNo, String side, java.sql.Timestamp ts);

    /**
     * 批量UPSERT回水管温度
     * 若 (sector_id, triangle_no, side, ts) 已存在则更新温度，否则插入。
     *
     * @param list 回水管温度集合
     * @return 结果
     */
    public int batchUpsertRPipeTemp(java.util.List<RPipeTemp> list);

    /**
     * 通过复合主键删除单条记录
     *
     * @param sectorId 扇区号
     * @param triangleNo 三角号
     * @param side 管路方向 L/R
     * @param ts 时间戳
     * @return 删除结果
     */
    public int deleteRPipeTempByKey(String sectorId, Integer triangleNo, String side, java.sql.Timestamp ts);

    /**
     * 批量通过复合主键删除记录
     *
     * @param list 回水管温度集合（仅需4个键字段）
     * @return 删除结果
     */
    public int deleteRPipeTempByKeys(java.util.List<RPipeTemp> list);
}
