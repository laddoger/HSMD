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
}
