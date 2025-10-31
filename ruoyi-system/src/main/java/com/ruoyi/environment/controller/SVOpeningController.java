package com.ruoyi.environment.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.environment.domain.SVOpening;
import com.ruoyi.environment.service.ISVOpeningService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 喷雾阀门开度Controller
 * 
 * @author Wang
 * @date 2025-10-31
 */
@RestController
@RequestMapping("/environment/opening")
public class SVOpeningController extends BaseController
{
    @Autowired
    private ISVOpeningService sVOpeningService;

    /**
     * 查询喷雾阀门开度列表
     */
    @PreAuthorize("@ss.hasPermi('environment:opening:list')")
    @GetMapping("/list")
    public TableDataInfo list(SVOpening sVOpening)
    {
        startPage();
        List<SVOpening> list = sVOpeningService.selectSVOpeningList(sVOpening);
        return getDataTable(list);
    }

    /**
     * 导出喷雾阀门开度列表
     */
    @PreAuthorize("@ss.hasPermi('environment:opening:export')")
    @Log(title = "喷雾阀门开度", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SVOpening sVOpening)
    {
        List<SVOpening> list = sVOpeningService.selectSVOpeningList(sVOpening);
        ExcelUtil<SVOpening> util = new ExcelUtil<SVOpening>(SVOpening.class);
        util.exportExcel(response, list, "喷雾阀门开度数据");
    }

    /**
     * 获取喷雾阀门开度详细信息
     */
    @PreAuthorize("@ss.hasPermi('environment:opening:query')")
    @GetMapping(value = "/{sectorId}")
    public AjaxResult getInfo(@PathVariable("sectorId") String sectorId)
    {
        return success(sVOpeningService.selectSVOpeningBySectorId(sectorId));
    }

    /**
     * 新增喷雾阀门开度
     */
    @PreAuthorize("@ss.hasPermi('environment:opening:add')")
    @Log(title = "喷雾阀门开度", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SVOpening sVOpening)
    {
        return toAjax(sVOpeningService.insertSVOpening(sVOpening));
    }

    /**
     * 修改喷雾阀门开度
     */
    @PreAuthorize("@ss.hasPermi('environment:opening:edit')")
    @Log(title = "喷雾阀门开度", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SVOpening sVOpening)
    {
        return toAjax(sVOpeningService.updateSVOpening(sVOpening));
    }

    /**
     * 删除喷雾阀门开度
     */
    @PreAuthorize("@ss.hasPermi('environment:opening:remove')")
    @Log(title = "喷雾阀门开度", businessType = BusinessType.DELETE)
	@DeleteMapping("/{sectorIds}")
    public AjaxResult remove(@PathVariable String[] sectorIds)
    {
        return toAjax(sVOpeningService.deleteSVOpeningBySectorIds(sectorIds));
    }
}
