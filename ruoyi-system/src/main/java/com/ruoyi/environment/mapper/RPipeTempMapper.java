package com.ruoyi.environment.mapper;

import java.util.List;
import com.ruoyi.environment.domain.RPipeTemp;

/**
 * 回水管温度Mapper接口
 *
 * 保留所有原有接口以兼容前端页面；
 * 新增复合主键( sector_id, triangle_no, side, ts )相关方法及批量UPSERT。
 *
 * @author Wang
 * @date 2025-10-30
 */
public interface RPipeTempMapper
{
    /**
     * 查询回水管温度（按 sectorId）
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
     * 新增回水管温度（若存在相同复合键则自动更新）
     *
     * @param rPipeTemp 回水管温度
     * @return 结果
     */
    public int insertRPipeTemp(RPipeTemp rPipeTemp);

    /**
     * 修改回水管温度（按复合键更新，仅更新温度）
     *
     * @param rPipeTemp 回水管温度
     * @return 结果
     */
    public int updateRPipeTemp(RPipeTemp rPipeTemp);

    /**
     * 删除回水管温度（按 sectorId 删除，保留旧接口）
     *
     * @param sectorId 回水管温度主键
     * @return 结果
     */
    public int deleteRPipeTempBySectorId(String sectorId);

    /**
     * 批量删除回水管温度（按 sectorId 批量删，保留旧接口）
     *
     * @param sectorIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRPipeTempBySectorIds(String[] sectorIds);



    /* ===================== 新增方法（复合键 + 批量操作） ===================== */

    /**
     * 通过复合主键查询
     *
     * @param sectorId 扇区号
     * @param triangleNo 三角号
     * @param side 管路方向 L/R
     * @param ts 时间戳
     * @return 回水管温度记录
     */
    public RPipeTemp selectRPipeTempByKey(String sectorId, Integer triangleNo, String side, java.sql.Timestamp ts);

    /**
     * 批量UPSERT回水管温度
     * 若 (sector_id, triangle_no, side, ts) 已存在则更新温度，否则插入。
     *
     * @param list 回水管温度集合
     * @return 影响行数
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