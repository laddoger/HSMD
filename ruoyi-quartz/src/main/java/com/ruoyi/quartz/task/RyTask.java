package com.ruoyi.quartz.task;

import org.springframework.stereotype.Component;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;

/**
 * 定时任务调度（保留原有示例方法，并新增温度入库任务入口）
 *
 * Bean 名称固定为 "ryTask" —— 已在若依白名单中，可直接在“定时任务”里作为调用目标。
 */
@Component("ryTask")
public class RyTask
{
    /** --------------------- 新增：温度入库任务（无参） --------------------- */
    public void tempsRun()
    {
        try {
            Object bean = SpringUtils.getBean("returnPipeTempIngestService"); // beanName：类名首字母小写
            bean.getClass().getMethod("ingestOnce").invoke(bean);
        } catch (Exception e) {
            throw new IllegalStateException("Invoke tempsRun() failed: " + e.getMessage(), e);
        }
    }

    /** --------------------- 新增：温度入库任务（带路径参数） --------------------- */
    public void tempsRunWithPath(String filePath)
    {
        try {
            Object bean = SpringUtils.getBean("returnPipeTempIngestService");
            bean.getClass().getMethod("ingestOnceOverridePath", String.class).invoke(bean, filePath);
        } catch (Exception e) {
            throw new IllegalStateException("Invoke tempsRunWithPath() failed: " + e.getMessage(), e);
        }
    }

    public void weatherRun() {
        try {
            Object bean = com.ruoyi.common.utils.spring.SpringUtils.getBean("weatherIngestService");
            bean.getClass().getMethod("ingestOnce").invoke(bean);
        } catch (Exception e) {
            throw new IllegalStateException("Invoke weatherRun() failed: " + e.getMessage(), e);
        }
    }

    public void weatherRunWithPath(String filePath) {
        try {
            Object bean = com.ruoyi.common.utils.spring.SpringUtils.getBean("weatherIngestService");
            bean.getClass().getMethod("ingestOnceOverridePath", String.class).invoke(bean, filePath);
        } catch (Exception e) {
            throw new IllegalStateException("Invoke weatherRunWithPath() failed: " + e.getMessage(), e);
        }
    }
    /** --------------------- 以下为若依原有示例方法，保留不改 --------------------- */

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams()
    {
        System.out.println("执行无参方法");
    }
}