package com.ruoyi.environment.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.environment.mapper.SVOpeningMapper;
import com.ruoyi.environment.domain.SVOpening;
import com.ruoyi.environment.service.ISVOpeningService;

/**
 * 喷雾阀门开度Service业务层处理
 * 
 * @author Wang
 * @date 2025-10-31
 */
@Service
public class SVOpeningServiceImpl implements ISVOpeningService 
{
    @Autowired
    private SVOpeningMapper sVOpeningMapper;

    /**
     * 查询喷雾阀门开度
     * 
     * @param sectorId 喷雾阀门开度主键
     * @return 喷雾阀门开度
     */
    @Override
    public SVOpening selectSVOpeningBySectorId(String sectorId)
    {
        return sVOpeningMapper.selectSVOpeningBySectorId(sectorId);
    }

    /**
     * 查询喷雾阀门开度列表
     * 
     * @param sVOpening 喷雾阀门开度
     * @return 喷雾阀门开度
     */
    @Override
    public List<SVOpening> selectSVOpeningList(SVOpening sVOpening)
    {
        return sVOpeningMapper.selectSVOpeningList(sVOpening);
    }

    /**
     * 新增喷雾阀门开度
     * 
     * @param sVOpening 喷雾阀门开度
     * @return 结果
     */
    @Override
    public int insertSVOpening(SVOpening sVOpening)
    {
        return sVOpeningMapper.insertSVOpening(sVOpening);
    }

    /**
     * 修改喷雾阀门开度
     * 
     * @param sVOpening 喷雾阀门开度
     * @return 结果
     */
    @Override
    public int updateSVOpening(SVOpening sVOpening)
    {
        return sVOpeningMapper.updateSVOpening(sVOpening);
    }

    /**
     * 批量删除喷雾阀门开度
     * 
     * @param sectorIds 需要删除的喷雾阀门开度主键
     * @return 结果
     */
    @Override
    public int deleteSVOpeningBySectorIds(String[] sectorIds)
    {
        return sVOpeningMapper.deleteSVOpeningBySectorIds(sectorIds);
    }

    /**
     * 删除喷雾阀门开度信息
     * 
     * @param sectorId 喷雾阀门开度主键
     * @return 结果
     */
    @Override
    public int deleteSVOpeningBySectorId(String sectorId)
    {
        return sVOpeningMapper.deleteSVOpeningBySectorId(sectorId);
    }
}
