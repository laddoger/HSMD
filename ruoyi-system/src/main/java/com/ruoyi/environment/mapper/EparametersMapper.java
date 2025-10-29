package com.ruoyi.environment.mapper;

import java.util.List;
import com.ruoyi.environment.domain.Eparameters;

/**
 * 环境参数Mapper接口
 * 
 * @author Wang
 * @date 2025-10-29
 */
public interface EparametersMapper 
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
     * 删除环境参数
     * 
     * @param globalEnvTemp 环境参数主键
     * @return 结果
     */
    public int deleteEparametersByGlobalEnvTemp(Long globalEnvTemp);

    /**
     * 批量删除环境参数
     * 
     * @param globalEnvTemps 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEparametersByGlobalEnvTemps(Long[] globalEnvTemps);
}
