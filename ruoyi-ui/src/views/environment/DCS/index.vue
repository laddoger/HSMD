<template>
  <div class="page">
    <!-- 顶部：全局参数，仅三项 -->
    <div class="global-panel">
      <div class="kpi">
        <div class="kpi-label">机组负荷</div>
        <div class="kpi-value">
          {{ global.unitLoad ?? '--' }}
          <span class="unit">MW</span>
        </div>
      </div>
      <div class="kpi">
        <div class="kpi-label">背压</div>
        <div class="kpi-value">
          {{ global.backPressure ?? '--' }}
          <span class="unit">kPa</span>
        </div>
      </div>
      <div class="kpi">
        <div class="kpi-label">出口温度</div>
        <div class="kpi-value">
          {{ global.outletTemperature ?? '--' }}
          <span class="unit">℃</span>
        </div>
      </div>
    </div>

    <!-- 当前扇区阀门开度 -->
    <div class="sectors-block">
      <div class="section-title">12个扇区当前喷雾阀门开度（%）</div>
      <div class="sectors-grid">
        <div v-for="i in 12" :key="'c'+i" class="sector-card">
          <div class="sector-name">扇区 {{ i }}</div>
          <div class="sector-value">
            {{ currentOpen[i-1] }}<span class="unit">%</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 背景图 -->
    <div class="dcs-bg" :style="{ backgroundImage: `url('${bgUrl}')` }">
      <div class="data-overlay">
        <div
            v-for="box in boxes"
            :key="box.id"
            class="data-point"
            :style="{ top: box.top + '%', left: box.left + '%' }"
        >
          <div class="line" v-for="(ln, i) in box.lines" :key="i">
            <span class="label">{{ ln.label }}：</span>
            <span class="value">{{ ln.value }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 建议开度 -->
    <div class="sectors-block">
      <div class="section-title">12个扇区建议喷雾阀门开度（%）</div>
      <div class="sectors-grid">
        <div v-for="i in 12" :key="'s'+i" class="sector-card">
          <div class="sector-name">扇区 {{ i }}</div>
          <div class="sector-value">
            {{ suggestedOpen[i-1] }}<span class="unit">%</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted, onUnmounted } from 'vue'
import { listGparameters } from '@/api/environment/Gparameters'
import { listOpening } from '@/api/environment/opening'

// 背景图路径
const bgUrl = `${import.meta.env.BASE_URL}CAD/img.png`

// 全局参数对象（字段名与若依返回的驼峰一致）
const global = reactive({
  unitLoad: null,
  backPressure: null,
  outletTemperature: null,
  pumpFrequency: null,
  pumpPressure: null,
  pumpCurrent: null,
  outletFlow: null,
})

// 背景图数据框（显示频率、电流、压力、流量）
const boxes = reactive([
  {
    id: 'pump',
    top: 51.85,
    left: 39.45,
    lines: [
      { label: '水泵电流', value: '' },
      { label: '水泵频率', value: '' }
    ]
  },
  {
    id: 'outlet',
    top: 52.07,
    left: 71.12,
    lines: [
      { label: '出口压力', value: '' },
      { label: '出口流量', value: '' }
    ]
  }
])

// 扇区开度（随机）
const currentOpen = reactive(Array.from({ length: 12 }, () => 0))
const suggestedOpen = reactive(Array.from({ length: 12 }, () => 0))

// 获取后端全局参数
async function fetchGlobalParams() {
  try {
    const res = await listGparameters({})
    if (res.rows && res.rows.length > 0) {
      // 若依接口返回驼峰字段：unitLoad, backPressure...
      const latest = res.rows[res.rows.length - 1]
      Object.assign(global, latest)

      boxes[0].lines[0].value = `${latest.pumpCurrent ?? '--'} A`
      boxes[0].lines[1].value = `${latest.pumpFrequency ?? '--'} Hz`
      boxes[1].lines[0].value = `${latest.pumpPressure ?? '--'} MPa`
      boxes[1].lines[1].value = `${latest.outletFlow ?? '--'} m³/h`
    } else {
      console.warn('未获取到全局参数数据')
    }
  } catch (err) {
    console.error('获取全局参数失败：', err)
  }
}


async function fetchOpeningData() {
  try {
    const res = await listOpening({ pageNum: 1, pageSize: 50 })
    console.log('开度接口返回结果:', res)

    // 若依返回格式兼容
    const rows = res?.rows || res?.data?.rows || []

    if (rows.length > 0) {
      for (let i = 0; i < 12; i++) {
        // 注意 sectorId 有可能是字符串
        const row = rows.find(r =>
            parseInt(r.sectorId || r.sector_id) === i + 1
        )
        currentOpen[i] = Number(row?.currentOpening ?? row?.current_opening ?? 0)
        suggestedOpen[i] = Number(row?.suggestedOpening ?? row?.suggested_opening ?? 0)
      }
    } else {
      console.warn('未获取到阀门开度数据')
    }
  } catch (err) {
    console.error('获取阀门开度数据失败：', err)
  }
}

let refreshTimer = null
onMounted(() => {
  fetchGlobalParams()
  fetchOpeningData()
  refreshTimer = setInterval(() => {
    fetchGlobalParams()
    fetchOpeningData()
  }, 10000)
})
onUnmounted(() => {
  if (refreshTimer) {
    clearInterval(refreshTimer)
    refreshTimer = null
  }
})
</script>

<style scoped>
.page {
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  background: #fff;
}

/* 顶部三个全局参数 */
.global-panel {
  display: grid;
  grid-template-columns: repeat(3, minmax(200px, 1fr));
  gap: 12px;
}
.kpi {
  border: 1px solid #e6e6ee;
  border-radius: 8px;
  padding: 10px 12px;
  background: #fafbff;
}
.kpi-label {
  font-size: 13px;
  color: #556;
  margin-bottom: 6px;
}
.kpi-value {
  font-size: 22px;
  font-weight: 700;
  color: #1f5aa6;
}
.unit {
  font-size: 12px;
  color: #6b7a90;
  margin-left: 6px;
}

/* 扇区块 */
.sectors-block {
  border: 1px solid #e6e6ee;
  border-radius: 8px;
  background: #fff;
  padding: 10px 12px 14px;
}
.section-title {
  font-size: 14px;
  font-weight: 600;
  color: #324;
  margin-bottom: 8px;
}
.sectors-grid {
  display: grid;
  gap: 10px;
  grid-template-columns: repeat(6, minmax(120px, 1fr));
}
.sector-card {
  border: 1px solid #e6e6ee;
  border-radius: 8px;
  padding: 10px 12px;
  background: #fdfefe;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.sector-name {
  font-size: 13px;
  color: #445065;
}
.sector-value {
  font-size: 18px;
  font-weight: 700;
  color: #0f66c3;
}
.sector-value .unit {
  font-size: 12px;
  color: #6b7a90;
  margin-left: 4px;
}

/* 背景图 */
.dcs-bg {
  position: relative;
  width: 100%;
  height: 60vh;
  min-height: 420px;
  background-repeat: no-repeat;
  background-position: center;
  background-size: contain;
  background-color: #ffffff;
  border: 1px solid #e6e6ee;
  border-radius: 8px;
  overflow: hidden;
}
.data-overlay {
  position: absolute;
  inset: 0;
}
.data-point {
  position: absolute;
  transform: translate(-50%, -50%);
  background: rgba(255, 255, 255, 0.86);
  border: 1px solid #007acc;
  border-radius: 8px;
  padding: 8px 12px;
  min-width: 150px;
  font-size: 14px;
  color: #111;
  user-select: none;
  box-shadow: 0 1px 6px rgba(0,0,0,0.15);
}
.line + .line { margin-top: 4px; }
.label { font-weight: 600; color: #005fa3; margin-right: 4px; }
.value { font-weight: 500; color: #000; }
</style>