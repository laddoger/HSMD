package com.ruoyi.environment.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.environment.mapper.GParametersMapper;
import com.ruoyi.environment.domain.GParameters;
import com.ruoyi.environment.service.IGParametersService;

/**
 * 全局参数Service业务层处理
 * 
 * @author Wang
 * @date 2025-10-28
 */
@Service
public class GParametersServiceImpl implements IGParametersService 
{
    @Autowired
    private GParametersMapper gParametersMapper;

    /**
     * 查询全局参数
     * 
     * @param id 全局参数主键
     * @return 全局参数
     */
    @Override
    public GParameters selectGParametersById(Long id)
    {
        return gParametersMapper.selectGParametersById(id);
    }

    /**
     * 查询全局参数列表
     * 
     * @param gParameters 全局参数
     * @return 全局参数
     */
    @Override
    public List<GParameters> selectGParametersList(GParameters gParameters)
    {
        return gParametersMapper.selectGParametersList(gParameters);
    }

    /**
     * 新增全局参数
     * 
     * @param gParameters 全局参数
     * @return 结果
     */
    @Override
    public int insertGParameters(GParameters gParameters)
    {
        return gParametersMapper.insertGParameters(gParameters);
    }

    /**
     * 修改全局参数
     * 
     * @param gParameters 全局参数
     * @return 结果
     */
    @Override
    public int updateGParameters(GParameters gParameters)
    {
        return gParametersMapper.updateGParameters(gParameters);
    }

    /**
     * 批量删除全局参数
     * 
     * @param ids 需要删除的全局参数主键
     * @return 结果
     */
    @Override
    public int deleteGParametersByIds(Long[] ids)
    {
        return gParametersMapper.deleteGParametersByIds(ids);
    }

    /**
     * 删除全局参数信息
     * 
     * @param id 全局参数主键
     * @return 结果
     */
    @Override
    public int deleteGParametersById(Long id)
    {
        return gParametersMapper.deleteGParametersById(id);
    }
}
