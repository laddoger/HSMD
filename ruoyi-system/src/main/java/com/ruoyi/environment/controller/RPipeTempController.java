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
import com.ruoyi.environment.domain.RPipeTemp;
import com.ruoyi.environment.service.IRPipeTempService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 回水管温度Controller
 * 
 * @author Wang
 * @date 2025-10-30
 */
@RestController
@RequestMapping("/environment/rpiptemp")
public class RPipeTempController extends BaseController
{
    @Autowired
    private IRPipeTempService rPipeTempService;

    /**
     * 查询回水管温度列表
     */
    @PreAuthorize("@ss.hasPermi('environment:rpiptemp:list')")
    @GetMapping("/list")
    public TableDataInfo list(RPipeTemp rPipeTemp)
    {
        startPage();
        List<RPipeTemp> list = rPipeTempService.selectRPipeTempList(rPipeTemp);
        return getDataTable(list);
    }

    /**
     * 导出回水管温度列表
     */
    @PreAuthorize("@ss.hasPermi('environment:rpiptemp:export')")
    @Log(title = "回水管温度", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RPipeTemp rPipeTemp)
    {
        List<RPipeTemp> list = rPipeTempService.selectRPipeTempList(rPipeTemp);
        ExcelUtil<RPipeTemp> util = new ExcelUtil<RPipeTemp>(RPipeTemp.class);
        util.exportExcel(response, list, "回水管温度数据");
    }

    /**
     * 获取回水管温度详细信息
     */
    @PreAuthorize("@ss.hasPermi('environment:rpiptemp:query')")
    @GetMapping(value = "/{sectorId}")
    public AjaxResult getInfo(@PathVariable("sectorId") String sectorId)
    {
        return success(rPipeTempService.selectRPipeTempBySectorId(sectorId));
    }

    /**
     * 新增回水管温度
     */
    @PreAuthorize("@ss.hasPermi('environment:rpiptemp:add')")
    @Log(title = "回水管温度", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RPipeTemp rPipeTemp)
    {
        return toAjax(rPipeTempService.insertRPipeTemp(rPipeTemp));
    }

    /**
     * 修改回水管温度
     */
    @PreAuthorize("@ss.hasPermi('environment:rpiptemp:edit')")
    @Log(title = "回水管温度", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RPipeTemp rPipeTemp)
    {
        return toAjax(rPipeTempService.updateRPipeTemp(rPipeTemp));
    }

    /**
     * 删除回水管温度
     */
    @PreAuthorize("@ss.hasPermi('environment:rpiptemp:remove')")
    @Log(title = "回水管温度", businessType = BusinessType.DELETE)
	@DeleteMapping("/{sectorIds}")
    public AjaxResult remove(@PathVariable String[] sectorIds)
    {
        return toAjax(rPipeTempService.deleteRPipeTempBySectorIds(sectorIds));
    }
}
