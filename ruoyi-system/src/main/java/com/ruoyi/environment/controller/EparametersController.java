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
import com.ruoyi.environment.domain.Eparameters;
import com.ruoyi.environment.service.IEparametersService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 环境参数Controller
 * 
 * @author Wang
 * @date 2025-10-29
 */
@RestController
@RequestMapping("/environment/Eparameters")
public class EparametersController extends BaseController
{
    @Autowired
    private IEparametersService eparametersService;

    /**
     * 查询环境参数列表
     */
    @PreAuthorize("@ss.hasPermi('environment:Eparameters:list')")
    @GetMapping("/list")
    public TableDataInfo list(Eparameters eparameters)
    {
        startPage();
        List<Eparameters> list = eparametersService.selectEparametersList(eparameters);
        return getDataTable(list);
    }

    /**
     * 导出环境参数列表
     */
    @PreAuthorize("@ss.hasPermi('environment:Eparameters:export')")
    @Log(title = "环境参数", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Eparameters eparameters)
    {
        List<Eparameters> list = eparametersService.selectEparametersList(eparameters);
        ExcelUtil<Eparameters> util = new ExcelUtil<Eparameters>(Eparameters.class);
        util.exportExcel(response, list, "环境参数数据");
    }

    /**
     * 获取环境参数详细信息
     */
    @PreAuthorize("@ss.hasPermi('environment:Eparameters:query')")
    @GetMapping(value = "/{globalEnvTemp}")
    public AjaxResult getInfo(@PathVariable("globalEnvTemp") Long globalEnvTemp)
    {
        return success(eparametersService.selectEparametersByGlobalEnvTemp(globalEnvTemp));
    }

    /**
     * 新增环境参数
     */
    @PreAuthorize("@ss.hasPermi('environment:Eparameters:add')")
    @Log(title = "环境参数", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Eparameters eparameters)
    {
        return toAjax(eparametersService.insertEparameters(eparameters));
    }

    /**
     * 修改环境参数
     */
    @PreAuthorize("@ss.hasPermi('environment:Eparameters:edit')")
    @Log(title = "环境参数", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Eparameters eparameters)
    {
        return toAjax(eparametersService.updateEparameters(eparameters));
    }

    /**
     * 删除环境参数
     */
    @PreAuthorize("@ss.hasPermi('environment:Eparameters:remove')")
    @Log(title = "环境参数", businessType = BusinessType.DELETE)
	@DeleteMapping("/{globalEnvTemps}")
    public AjaxResult remove(@PathVariable Long[] globalEnvTemps)
    {
        return toAjax(eparametersService.deleteEparametersByGlobalEnvTemps(globalEnvTemps));
    }
}
