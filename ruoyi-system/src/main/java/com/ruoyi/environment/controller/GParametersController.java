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
import com.ruoyi.environment.domain.GParameters;
import com.ruoyi.environment.service.IGParametersService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 全局参数Controller
 * 
 * @author Wang
 * @date 2025-10-28
 */
@RestController
@RequestMapping("/environment/Gparameters")
public class GParametersController extends BaseController
{
    @Autowired
    private IGParametersService gParametersService;

    /**
     * 查询全局参数列表
     */
    @PreAuthorize("@ss.hasPermi('environment:Gparameters:list')")
    @GetMapping("/list")
    public TableDataInfo list(GParameters gParameters)
    {
        startPage();
        List<GParameters> list = gParametersService.selectGParametersList(gParameters);
        return getDataTable(list);
    }

    /**
     * 导出全局参数列表
     */
    @PreAuthorize("@ss.hasPermi('environment:Gparameters:export')")
    @Log(title = "全局参数", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GParameters gParameters)
    {
        List<GParameters> list = gParametersService.selectGParametersList(gParameters);
        ExcelUtil<GParameters> util = new ExcelUtil<GParameters>(GParameters.class);
        util.exportExcel(response, list, "全局参数数据");
    }

    /**
     * 获取全局参数详细信息
     */
    @PreAuthorize("@ss.hasPermi('environment:Gparameters:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(gParametersService.selectGParametersById(id));
    }

    /**
     * 新增全局参数
     */
    @PreAuthorize("@ss.hasPermi('environment:Gparameters:add')")
    @Log(title = "全局参数", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GParameters gParameters)
    {
        return toAjax(gParametersService.insertGParameters(gParameters));
    }

    /**
     * 修改全局参数
     */
    @PreAuthorize("@ss.hasPermi('environment:Gparameters:edit')")
    @Log(title = "全局参数", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GParameters gParameters)
    {
        return toAjax(gParametersService.updateGParameters(gParameters));
    }

    /**
     * 删除全局参数
     */
    @PreAuthorize("@ss.hasPermi('environment:Gparameters:remove')")
    @Log(title = "全局参数", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(gParametersService.deleteGParametersByIds(ids));
    }
}
