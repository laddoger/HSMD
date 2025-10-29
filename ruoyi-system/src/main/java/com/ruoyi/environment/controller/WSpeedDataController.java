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
import com.ruoyi.environment.domain.WSpeedData;
import com.ruoyi.environment.service.IWSpeedDataService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 风速数据Controller
 * 
 * @author Wang
 * @date 2025-10-28
 */
@RestController
@RequestMapping("/environment/speeddata")
public class WSpeedDataController extends BaseController
{
    @Autowired
    private IWSpeedDataService wSpeedDataService;

    /**
     * 查询风速数据列表
     */
    @PreAuthorize("@ss.hasPermi('environment:speeddata:list')")
    @GetMapping("/list")
    public TableDataInfo list(WSpeedData wSpeedData)
    {
        startPage();
        List<WSpeedData> list = wSpeedDataService.selectWSpeedDataList(wSpeedData);
        return getDataTable(list);
    }

    /**
     * 导出风速数据列表
     */
    @PreAuthorize("@ss.hasPermi('environment:speeddata:export')")
    @Log(title = "风速数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WSpeedData wSpeedData)
    {
        List<WSpeedData> list = wSpeedDataService.selectWSpeedDataList(wSpeedData);
        ExcelUtil<WSpeedData> util = new ExcelUtil<WSpeedData>(WSpeedData.class);
        util.exportExcel(response, list, "风速数据数据");
    }

    /**
     * 获取风速数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('environment:speeddata:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(wSpeedDataService.selectWSpeedDataById(id));
    }

    /**
     * 新增风速数据
     */
    @PreAuthorize("@ss.hasPermi('environment:speeddata:add')")
    @Log(title = "风速数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WSpeedData wSpeedData)
    {
        return toAjax(wSpeedDataService.insertWSpeedData(wSpeedData));
    }

    /**
     * 修改风速数据
     */
    @PreAuthorize("@ss.hasPermi('environment:speeddata:edit')")
    @Log(title = "风速数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WSpeedData wSpeedData)
    {
        return toAjax(wSpeedDataService.updateWSpeedData(wSpeedData));
    }

    /**
     * 删除风速数据
     */
    @PreAuthorize("@ss.hasPermi('environment:speeddata:remove')")
    @Log(title = "风速数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(wSpeedDataService.deleteWSpeedDataByIds(ids));
    }
}
