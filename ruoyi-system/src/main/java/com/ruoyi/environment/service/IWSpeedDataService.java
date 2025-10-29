package com.ruoyi.environment.service;

import java.util.List;
import com.ruoyi.environment.domain.WSpeedData;

/**
 * 风速数据Service接口
 * 
 * @author Wang
 * @date 2025-10-28
 */
public interface IWSpeedDataService 
{
    /**
     * 查询风速数据
     * 
     * @param id 风速数据主键
     * @return 风速数据
     */
    public WSpeedData selectWSpeedDataById(Long id);

    /**
     * 查询风速数据列表
     * 
     * @param wSpeedData 风速数据
     * @return 风速数据集合
     */
    public List<WSpeedData> selectWSpeedDataList(WSpeedData wSpeedData);

    /**
     * 新增风速数据
     * 
     * @param wSpeedData 风速数据
     * @return 结果
     */
    public int insertWSpeedData(WSpeedData wSpeedData);

    /**
     * 修改风速数据
     * 
     * @param wSpeedData 风速数据
     * @return 结果
     */
    public int updateWSpeedData(WSpeedData wSpeedData);

    /**
     * 批量删除风速数据
     * 
     * @param ids 需要删除的风速数据主键集合
     * @return 结果
     */
    public int deleteWSpeedDataByIds(Long[] ids);

    /**
     * 删除风速数据信息
     * 
     * @param id 风速数据主键
     * @return 结果
     */
    public int deleteWSpeedDataById(Long id);
}
