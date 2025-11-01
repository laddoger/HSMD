package com.ruoyi.environment.mapper;

import com.ruoyi.environment.domain.Eparameters;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 环境参数Mapper接口
 *
 * 保持与现有前端及Service兼容：
 * - 新增方法名仍为 insertEparameters
 * - 其他查询/更新/删除方法不变
 */
@Mapper
public interface EparametersMapper
{
    /**
     * 查询环境参数
     *
     * @param globalEnvTemp 环境参数主键
     * @return 环境参数
     */
    Eparameters selectEparametersByGlobalEnvTemp(Long globalEnvTemp);

    /**
     * 查询环境参数列表
     *
     * @param eparameters 环境参数
     * @return 环境参数集合
     */
    List<Eparameters> selectEparametersList(Eparameters eparameters);

    /**
     * 新增环境参数
     *
     * @param eparameters 环境参数
     * @return 结果
     */
    int insertEparameters(Eparameters eparameters);

    /**
     * 修改环境参数
     *
     * @param eparameters 环境参数
     * @return 结果
     */
    int updateEparameters(Eparameters eparameters);

    /**
     * 删除环境参数
     *
     * @param globalEnvTemp 环境参数主键
     * @return 结果
     */
    int deleteEparametersByGlobalEnvTemp(Long globalEnvTemp);

    /**
     * 批量删除环境参数
     *
     * @param globalEnvTemps 需要删除的数据主键集合
     * @return 结果
     */
    int deleteEparametersByGlobalEnvTemps(Long[] globalEnvTemps);
}