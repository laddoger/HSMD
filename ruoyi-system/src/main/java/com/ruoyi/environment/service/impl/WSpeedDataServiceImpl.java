package com.ruoyi.environment.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.environment.mapper.WSpeedDataMapper;
import com.ruoyi.environment.domain.WSpeedData;
import com.ruoyi.environment.service.IWSpeedDataService;

/**
 * 风速数据Service业务层处理
 * 
 * @author Wang
 * @date 2025-10-28
 */
@Service
public class WSpeedDataServiceImpl implements IWSpeedDataService 
{
    @Autowired
    private WSpeedDataMapper wSpeedDataMapper;

    /**
     * 查询风速数据
     * 
     * @param id 风速数据主键
     * @return 风速数据
     */
    @Override
    public WSpeedData selectWSpeedDataById(Long id)
    {
        return wSpeedDataMapper.selectWSpeedDataById(id);
    }

    /**
     * 查询风速数据列表
     * 
     * @param wSpeedData 风速数据
     * @return 风速数据
     */
    @Override
    public List<WSpeedData> selectWSpeedDataList(WSpeedData wSpeedData)
    {
        return wSpeedDataMapper.selectWSpeedDataList(wSpeedData);
    }

    /**
     * 新增风速数据
     * 
     * @param wSpeedData 风速数据
     * @return 结果
     */
    @Override
    public int insertWSpeedData(WSpeedData wSpeedData)
    {
        return wSpeedDataMapper.insertWSpeedData(wSpeedData);
    }

    /**
     * 修改风速数据
     * 
     * @param wSpeedData 风速数据
     * @return 结果
     */
    @Override
    public int updateWSpeedData(WSpeedData wSpeedData)
    {
        return wSpeedDataMapper.updateWSpeedData(wSpeedData);
    }

    /**
     * 批量删除风速数据
     * 
     * @param ids 需要删除的风速数据主键
     * @return 结果
     */
    @Override
    public int deleteWSpeedDataByIds(Long[] ids)
    {
        return wSpeedDataMapper.deleteWSpeedDataByIds(ids);
    }

    /**
     * 删除风速数据信息
     * 
     * @param id 风速数据主键
     * @return 结果
     */
    @Override
    public int deleteWSpeedDataById(Long id)
    {
        return wSpeedDataMapper.deleteWSpeedDataById(id);
    }
}
