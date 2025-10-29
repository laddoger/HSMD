package com.ruoyi.environment.service;

import java.util.List;
import com.ruoyi.environment.domain.GParameters;

/**
 * 全局参数Service接口
 * 
 * @author Wang
 * @date 2025-10-28
 */
public interface IGParametersService 
{
    /**
     * 查询全局参数
     * 
     * @param id 全局参数主键
     * @return 全局参数
     */
    public GParameters selectGParametersById(Long id);

    /**
     * 查询全局参数列表
     * 
     * @param gParameters 全局参数
     * @return 全局参数集合
     */
    public List<GParameters> selectGParametersList(GParameters gParameters);

    /**
     * 新增全局参数
     * 
     * @param gParameters 全局参数
     * @return 结果
     */
    public int insertGParameters(GParameters gParameters);

    /**
     * 修改全局参数
     * 
     * @param gParameters 全局参数
     * @return 结果
     */
    public int updateGParameters(GParameters gParameters);

    /**
     * 批量删除全局参数
     * 
     * @param ids 需要删除的全局参数主键集合
     * @return 结果
     */
    public int deleteGParametersByIds(Long[] ids);

    /**
     * 删除全局参数信息
     * 
     * @param id 全局参数主键
     * @return 结果
     */
    public int deleteGParametersById(Long id);
}
