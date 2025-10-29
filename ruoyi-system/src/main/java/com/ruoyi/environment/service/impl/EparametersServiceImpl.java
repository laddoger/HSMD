package com.ruoyi.environment.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.environment.mapper.EparametersMapper;
import com.ruoyi.environment.domain.Eparameters;
import com.ruoyi.environment.service.IEparametersService;

/**
 * 环境参数Service业务层处理
 * 
 * @author Wang
 * @date 2025-10-29
 */
@Service
public class EparametersServiceImpl implements IEparametersService 
{
    @Autowired
    private EparametersMapper eparametersMapper;

    /**
     * 查询环境参数
     * 
     * @param globalEnvTemp 环境参数主键
     * @return 环境参数
     */
    @Override
    public Eparameters selectEparametersByGlobalEnvTemp(Long globalEnvTemp)
    {
        return eparametersMapper.selectEparametersByGlobalEnvTemp(globalEnvTemp);
    }

    /**
     * 查询环境参数列表
     * 
     * @param eparameters 环境参数
     * @return 环境参数
     */
    @Override
    public List<Eparameters> selectEparametersList(Eparameters eparameters)
    {
        return eparametersMapper.selectEparametersList(eparameters);
    }

    /**
     * 新增环境参数
     * 
     * @param eparameters 环境参数
     * @return 结果
     */
    @Override
    public int insertEparameters(Eparameters eparameters)
    {
        return eparametersMapper.insertEparameters(eparameters);
    }

    /**
     * 修改环境参数
     * 
     * @param eparameters 环境参数
     * @return 结果
     */
    @Override
    public int updateEparameters(Eparameters eparameters)
    {
        return eparametersMapper.updateEparameters(eparameters);
    }

    /**
     * 批量删除环境参数
     * 
     * @param globalEnvTemps 需要删除的环境参数主键
     * @return 结果
     */
    @Override
    public int deleteEparametersByGlobalEnvTemps(Long[] globalEnvTemps)
    {
        return eparametersMapper.deleteEparametersByGlobalEnvTemps(globalEnvTemps);
    }

    /**
     * 删除环境参数信息
     * 
     * @param globalEnvTemp 环境参数主键
     * @return 结果
     */
    @Override
    public int deleteEparametersByGlobalEnvTemp(Long globalEnvTemp)
    {
        return eparametersMapper.deleteEparametersByGlobalEnvTemp(globalEnvTemp);
    }
}
