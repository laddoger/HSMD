package com.ruoyi.environment.service;

import java.util.List;
import com.ruoyi.environment.domain.SVOpening;

/**
 * 喷雾阀门开度Service接口
 * 
 * @author Wang
 * @date 2025-10-31
 */
public interface ISVOpeningService 
{
    /**
     * 查询喷雾阀门开度
     * 
     * @param sectorId 喷雾阀门开度主键
     * @return 喷雾阀门开度
     */
    public SVOpening selectSVOpeningBySectorId(String sectorId);

    /**
     * 查询喷雾阀门开度列表
     * 
     * @param sVOpening 喷雾阀门开度
     * @return 喷雾阀门开度集合
     */
    public List<SVOpening> selectSVOpeningList(SVOpening sVOpening);

    /**
     * 新增喷雾阀门开度
     * 
     * @param sVOpening 喷雾阀门开度
     * @return 结果
     */
    public int insertSVOpening(SVOpening sVOpening);

    /**
     * 修改喷雾阀门开度
     * 
     * @param sVOpening 喷雾阀门开度
     * @return 结果
     */
    public int updateSVOpening(SVOpening sVOpening);

    /**
     * 批量删除喷雾阀门开度
     * 
     * @param sectorIds 需要删除的喷雾阀门开度主键集合
     * @return 结果
     */
    public int deleteSVOpeningBySectorIds(String[] sectorIds);

    /**
     * 删除喷雾阀门开度信息
     * 
     * @param sectorId 喷雾阀门开度主键
     * @return 结果
     */
    public int deleteSVOpeningBySectorId(String sectorId);
}
