package com.ruoyi.environment.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.environment.mapper.RPipeTempMapper;
import com.ruoyi.environment.domain.RPipeTemp;
import com.ruoyi.environment.service.IRPipeTempService;

/**
 * 回水管温度Service业务层处理
 * 
 * @author Wang
 * @date 2025-10-30
 */
@Service
public class RPipeTempServiceImpl implements IRPipeTempService 
{
    @Autowired
    private RPipeTempMapper rPipeTempMapper;

    /**
     * 查询回水管温度
     * 
     * @param sectorId 回水管温度主键
     * @return 回水管温度
     */
    @Override
    public RPipeTemp selectRPipeTempBySectorId(String sectorId)
    {
        return rPipeTempMapper.selectRPipeTempBySectorId(sectorId);
    }

    /**
     * 查询回水管温度列表
     * 
     * @param rPipeTemp 回水管温度
     * @return 回水管温度
     */
    @Override
    public List<RPipeTemp> selectRPipeTempList(RPipeTemp rPipeTemp)
    {
        return rPipeTempMapper.selectRPipeTempList(rPipeTemp);
    }

    /**
     * 新增回水管温度
     * 
     * @param rPipeTemp 回水管温度
     * @return 结果
     */
    @Override
    public int insertRPipeTemp(RPipeTemp rPipeTemp)
    {
        return rPipeTempMapper.insertRPipeTemp(rPipeTemp);
    }

    /**
     * 修改回水管温度
     * 
     * @param rPipeTemp 回水管温度
     * @return 结果
     */
    @Override
    public int updateRPipeTemp(RPipeTemp rPipeTemp)
    {
        return rPipeTempMapper.updateRPipeTemp(rPipeTemp);
    }

    /**
     * 批量删除回水管温度
     * 
     * @param sectorIds 需要删除的回水管温度主键
     * @return 结果
     */
    @Override
    public int deleteRPipeTempBySectorIds(String[] sectorIds)
    {
        return rPipeTempMapper.deleteRPipeTempBySectorIds(sectorIds);
    }

    /**
     * 删除回水管温度信息
     * 
     * @param sectorId 回水管温度主键
     * @return 结果
     */
    @Override
    public int deleteRPipeTempBySectorId(String sectorId)
    {
        return rPipeTempMapper.deleteRPipeTempBySectorId(sectorId);
    }
    /**
     * 通过复合主键查询
     *
     * @param sectorId 扇区号
     * @param triangleNo 三角号
     * @param side 管路方向 L/R
     * @param ts 时间戳
     * @return 回水管温度
     */
    @Override
    public RPipeTemp selectRPipeTempByKey(String sectorId, Integer triangleNo, String side, java.sql.Timestamp ts)
    {
        return rPipeTempMapper.selectRPipeTempByKey(sectorId, triangleNo, side, ts);
    }

    /**
     * 批量UPSERT回水管温度
     * 若 (sector_id, triangle_no, side, ts) 已存在则更新温度，否则插入。
     *
     * @param list 回水管温度集合
     * @return 结果
     */
    @Override
    public int batchUpsertRPipeTemp(java.util.List<RPipeTemp> list)
    {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return rPipeTempMapper.batchUpsertRPipeTemp(list);
    }

    /**
     * 通过复合主键删除单条记录
     *
     * @param sectorId 扇区号
     * @param triangleNo 三角号
     * @param side 管路方向 L/R
     * @param ts 时间戳
     * @return 删除结果
     */
    @Override
    public int deleteRPipeTempByKey(String sectorId, Integer triangleNo, String side, java.sql.Timestamp ts)
    {
        return rPipeTempMapper.deleteRPipeTempByKey(sectorId, triangleNo, side, ts);
    }

    /**
     * 批量通过复合主键删除记录
     *
     * @param list 回水管温度集合（仅需4个键字段）
     * @return 删除结果
     */
    @Override
    public int deleteRPipeTempByKeys(java.util.List<RPipeTemp> list)
    {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return rPipeTempMapper.deleteRPipeTempByKeys(list);
    }
}
