package com.ruoyi.environment.service;

import java.util.List;
import com.ruoyi.environment.domain.Eparameters;

/**
 * 环境参数Service接口
 * 
 * @author Wang
 * @date 2025-10-29
 */
public interface IEparametersService 
{
    /**
     * 查询环境参数
     * 
     * @param globalEnvTemp 环境参数主键
     * @return 环境参数
     */
    public Eparameters selectEparametersByGlobalEnvTemp(Long globalEnvTemp);

    /**
     * 查询环境参数列表
     * 
     * @param eparameters 环境参数
     * @return 环境参数集合
     */
    public List<Eparameters> selectEparametersList(Eparameters eparameters);

    /**
     * 新增环境参数
     * 
     * @param eparameters 环境参数
     * @return 结果
     */
    public int insertEparameters(Eparameters eparameters);

    /**
     * 修改环境参数
     * 
     * @param eparameters 环境参数
     * @return 结果
     */
    public int updateEparameters(Eparameters eparameters);

    /**
     * 批量删除环境参数
     * 
     * @param globalEnvTemps 需要删除的环境参数主键集合
     * @return 结果
     */
    public int deleteEparametersByGlobalEnvTemps(Long[] globalEnvTemps);

    /**
     * 删除环境参数信息
     * 
     * @param globalEnvTemp 环境参数主键
     * @return 结果
     */
    public int deleteEparametersByGlobalEnvTemp(Long globalEnvTemp);
}
