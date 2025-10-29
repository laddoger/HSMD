// src/api/environment/sectors.js
import request from '@/utils/request'

// 获取单个扇区详情（占位路径，后续替换为后端真实接口）
export function getSectorDetail(sectorId) {
    return request({
        url: `/environment/sector/${sectorId}`, // 代理会把 /dev-api 转发到后端
        method: 'get'
    })
}

// 为方便开发，提供一个本地模拟数据函数（前端使用时可切换）：
export function mockSectorDetail(sectorId) {
    // 返回 Promise，模拟网络延迟
    return new Promise(resolve => {
        setTimeout(() => {
            resolve({
                data: {
                    id: sectorId,
                    name: `第 ${sectorId} 扇区`,
                    temp: (20 + sectorId % 5 + Math.random()*2).toFixed(1),
                    humidity: (45 + sectorId % 10).toFixed(0),
                    pm25: (10 + sectorId % 20).toFixed(0),
                    co2: (400 + sectorId * 5),
                    status: sectorId % 2 === 0 ? '正常' : '报警'
                }
            })
        }, 300)
    })
}
// --- 在 src/api/environment/sectors.js 追加 ---

// 获取全局参数（后端接口占位）
export function getGlobalParams() {
    return request({
        url: `/environment/globalParams`, // 后端真实接口请按实际改写
        method: 'get'
    })
}

// 前端 mock 全局参数（方便开发）
export function mockGlobalParams() {
    return new Promise(resolve => {
        setTimeout(() => {
            resolve({
                data: {
                    pumpFrequency: `${(28 + Math.random()*4).toFixed(1)} Hz`,
                    pumpPressure: `${(0.9 + Math.random()*0.4).toFixed(2)} MPa`,
                    inverterCurrent: `${(12 + Math.random()*3).toFixed(1)} A`,
                    outletFlow: `${(120 + Math.random()*20).toFixed(0)} m³/h`
                }
            })
        }, 200)
    })
}
