// src/api/environment/sectors.js

// 说明：
// - 保留扇区风速相关方法（依赖 wind_speed_data 列表接口）
// - 全局 5 项环境参数改为使用若依生成的 Eparameters 列表接口
// - 兼容多种返回包装（rows/list/data）与字段命名（snake/camel）

import { listSpeeddata } from '@/api/environment/speeddata'
import { listEparameters } from '@/api/environment/Eparameters'

/** 统一抽取 RuoYi 列表响应里的 rows（兼容多种返回包装） */
function extractRows(res) {
    const body = (res && res.data) ? res.data : res
    if (!body) return []
    if (Array.isArray(body)) return body
    if (Array.isArray(body.rows)) return body.rows
    if (body.list && Array.isArray(body.list)) return body.list
    if (body.data && Array.isArray(body.data)) return body.data
    if (body.data && Array.isArray(body.data.rows)) return body.data.rows
    return []
}

/** 角度 -> 16 方位（如 0/360→N, 90→E），若不是数值则原样返回字符串 */
function degToCompass(val) {
    const n = Number(val)
    if (Number.isNaN(n)) return String(val ?? '')
    const dirs = ['N','NNE','NE','ENE','E','ESE','SE','SSE','S','SSW','SW','WSW','W','WNW','NW','NNW']
    const idx = Math.round((((n % 360) + 360) % 360) / 22.5) % 16
    return dirs[idx]
}

/**
 * 取“单个扇区 + 单个传感器”的最新一条风速记录
 * @param {number} sectorId 扇区号（1..12）
 * @param {number} sensorId 传感器号（1..3）
 * @returns {Promise<{speed:number|null, direction:string|number|null, raw:any|null}>}
 */
async function latestBySectorSensor(sectorId, sensorId) {
    const query = {
        pageNum: 1,
        pageSize: 1,
        orderByColumn: 'record_time',
        isAsc: 'desc',
        sector: sectorId,
        sensor: sensorId
    }
    const res = await listSpeeddata(query)
    const rows = extractRows(res)
    const item = rows[0]
    if (!item) {
        return { speed: null, direction: null, raw: null }
    }
    // 字段兼容（下划线/驼峰）
    const speedRaw = item.wind_speed ?? item.windSpeed
    const dirRaw = item.wind_direction ?? item.windDirection
    const speed = speedRaw != null ? Number(speedRaw) : null
    const direction = dirRaw != null ? degToCompass(dirRaw) : null
    return { speed: Number.isFinite(speed) ? speed : null, direction, raw: item }
}

/**
 * 获取单个扇区的“3 个传感器”的最新风速/风向
 * 返回结构：{ speeds:[s1,s2,s3], directions:[d1,d2,d3] }
 */
export async function getSectorWind(sectorId) {
    const [r1, r2, r3] = await Promise.all([
        latestBySectorSensor(sectorId, 1),
        latestBySectorSensor(sectorId, 2),
        latestBySectorSensor(sectorId, 3)
    ])
    return {
        speeds: [r1.speed, r2.speed, r3.speed].map(v => (Number.isFinite(v) ? Number(v.toFixed(1)) : null)),
        directions: [r1.direction, r2.direction, r3.direction]
    }
}

/**
 * 并发获取所有扇区（默认 12 个）风速/风向
 * 返回：{ 1:{speeds:[...],directions:[...]}, 2:{...}, ... }
 */
export async function getAllSectorsWind(totalSectors = 12) {
    const tasks = []
    for (let i = 1; i <= totalSectors; i++) {
        tasks.push(
            getSectorWind(i)
                .then(data => [i, data])
                .catch(() => [i, { speeds: [null, null, null], directions: [null, null, null] }])
        )
    }
    const entries = await Promise.all(tasks)
    const map = {}
    entries.forEach(([sectorId, data]) => { map[sectorId] = data })
    return map
}

/**
 * （推荐）获取“左侧 5 项全局参数”的最新记录（来自 Eparameters）
 * 按 ts 字段倒序取 1 条。
 * 返回：{ data: { envTemp, envHumidity, windSpeed, windDirection, towerOuterTemp, recordTime } }
 */
export async function getGlobal5() {
    const query = {
        pageNum: 1,
        pageSize: 1,
        orderByColumn: 'ts', // 与 Eparameters.ts 字段一致
        isAsc: 'desc'
    }
    const res = await listEparameters(query)
    const rows = extractRows(res)
    const item = rows && rows[0] ? rows[0] : null
    if (!item) return { data: null }

    // 兼容后端字段命名（蛇形/驼峰）
    const globalEnvTemp = item.global_env_temp ?? item.globalEnvTemp ?? null
    const envHumidity   = item.env_humidity ?? item.envHumidity ?? null
    const windSpeed     = item.env_wind_speed ?? item.envWindSpeed ?? item.wind_speed ?? item.windSpeed ?? null
    const windDirection = item.env_wind_dir ?? item.envWindDir ?? item.wind_direction ?? item.windDirection ?? null // Eparameters 为 N/S，直接显示
    const towerEnvTemp  = item.tower_env_temp ?? item.towerEnvTemp ?? item.tower_outer_temp ?? item.towerOuterTemp ?? null
    const recordTime    = item.ts ?? item.record_time ?? item.recordTime ?? item.createTime ?? item.create_time ?? null

    return {
        data: {
            envTemp: globalEnvTemp,
            envHumidity: envHumidity,
            windSpeed: windSpeed,
            windDirection: windDirection,
            towerOuterTemp: towerEnvTemp,
            recordTime: recordTime
        }
    }
}

/** 若需原始字段风格（蛇形优先），可导出该辅助函数 */
export async function getLatestEparametersRaw() {
    const res = await listEparameters({ pageNum: 1, pageSize: 1, orderByColumn: 'ts', isAsc: 'desc' })
    const rows = extractRows(res)
    const item = rows && rows[0] ? rows[0] : null
    if (!item) return { data: null }
    return {
        data: {
            global_env_temp: item.global_env_temp ?? item.globalEnvTemp ?? null,
            env_humidity: item.env_humidity ?? item.envHumidity ?? null,
            env_wind_speed: item.env_wind_speed ?? item.envWindSpeed ?? item.wind_speed ?? item.windSpeed ?? null,
            env_wind_dir: item.env_wind_dir ?? item.envWindDir ?? item.wind_direction ?? item.windDirection ?? null,
            tower_env_temp: item.tower_env_temp ?? item.towerEnvTemp ?? item.tower_outer_temp ?? item.towerOuterTemp ?? null,
            ts: item.ts ?? item.record_time ?? item.recordTime ?? item.createTime ?? item.create_time ?? null
        }
    }
}