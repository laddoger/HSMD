<template>
  <div class="sectors-page">
    <div class="header">
      <h2>间冷塔 — 扇区视图</h2>
      <p class="subtitle">左侧为 5 项全局参数（来自后端 Eparameters）；右侧为 12 扇区环（已接后端）。每个扇区内显示「平均风速 / 风向(传感器1)」，点击查看 3 个传感器的风速与风向详情。</p>
    </div>

    <!-- 主布局：左侧参数栏 + 右侧环形扇区 -->
    <div class="main-layout">
      <!-- 左侧 5 张全局参数卡片（来自后端 Eparameters） -->
      <div class="left-panel" v-if="global5">
        <div class="param-card">
          <div class="card-title">全局环境温度</div>
          <div class="card-value">{{ global5.envTemp }}</div>
        </div>
        <div class="param-card">
          <div class="card-title">全局环境湿度</div>
          <div class="card-value">{{ global5.envHumidity }}</div>
        </div>
        <div class="param-card">
          <div class="card-title">全局环境风速</div>
          <div class="card-value">{{ global5.windSpeed }}</div>
        </div>
        <div class="param-card">
          <div class="card-title">全局环境风向</div>
          <div class="card-value">{{ global5.windDirection }}</div>
        </div>
        <div class="param-card">
          <div class="card-title">塔外环境温度</div>
          <div class="card-value">{{ global5.towerOuterTemp }}</div>
        </div>
      </div>

      <!-- 右侧环形 SVG 区域（扇区数据来自后端） -->
      <div class="svg-wrapper" ref="wrap">
        <svg
            :width="svgSize"
            :height="svgSize"
            :viewBox="`0 0 ${svgSize} ${svgSize}`"
        >
          <!-- 中心圆（塔体占位） -->
          <circle
              :cx="cx"
              :cy="cy"
              :r="innerRadius - 6"
              class="center-circle"
          />
          <!-- 12 个扇形 -->
          <g v-for="i in totalSectors" :key="i">
            <path
                :d="sectorPath(i - 1)"
                :fill="sectorColor(i - 1)"
                stroke="#ffffff"
                stroke-width="1"
                class="sector-path"
                @click="onSectorClick(i)"
            />
            <!-- 扇区内展示：平均风速 + 风向(传感器1) -->
            <text
                :x="labelPos(i - 1).x"
                :y="labelPos(i - 1).y - 8"
                class="sector-line speed"
                text-anchor="middle"
                alignment-baseline="middle"
                pointer-events="none"
            >
              {{ avgSpeedText(i) }}
            </text>
            <text
                :x="labelPos(i - 1).x"
                :y="labelPos(i - 1).y + 12"
                class="sector-line direction"
                text-anchor="middle"
                alignment-baseline="middle"
                pointer-events="none"
            >
              {{ dirSensor1Text(i) }}
            </text>
            <!-- 扇区编号：沿内环显示 -->
            <text
                :x="idPos(i - 1).x"
                :y="idPos(i - 1).y"
                class="sector-id"
                text-anchor="middle"
                alignment-baseline="middle"
                pointer-events="none"
            >
              {{ i }}
            </text>
          </g>
        </svg>
      </div>
    </div>

    <!-- 扇区详情弹窗：显示三个传感器风速与风向 -->
    <el-dialog
        :title="modalTitle"
        v-model="modalVisible"
        append-to-body
        width="560px"
        :before-close="handleClose"
    >
      <div v-if="loading" class="modal-loading">加载中...</div>

      <div v-else class="modal-body">
        <el-descriptions column="2" border style="margin-bottom: 12px">
          <el-descriptions-item label="扇区">{{ modalDetail.sectorId }}</el-descriptions-item>
          <el-descriptions-item label="平均风速">{{ modalDetail.avgSpeed }} m/s</el-descriptions-item>
        </el-descriptions>

        <el-table :data="modalDetail.rows" border size="small">
          <el-table-column prop="sensorId" label="传感器" width="90" align="center"/>
          <el-table-column prop="speed" label="风速 (m/s)" width="120" align="center"/>
          <el-table-column prop="direction" label="风向" align="center"/>
        </el-table>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="modalVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// 从你刚配置好的 API 文件导入
import { getAllSectorsWind, getSectorWind, getGlobal5 } from '@/api/environment/sectors'

export default {
  name: 'EnvironmentSectors',
  data() {
    return {
      // 弹窗
      modalVisible: false,
      modalTitle: '',
      loading: false,
      modalDetail: { sectorId: null, avgSpeed: '--', rows: [] },

      // 数据来源开关
      useMockGlobal: false,  // 全局五项从后端获取（Eparameters）
      useMockSectors: false, // 扇区改为真实后端

      // 环形 SVG 尺寸
      svgSize: 600,
      cx: 300,
      cy: 300,
      outerRadius: 260,
      innerRadius: 110,
      totalSectors: 12,

      // 左侧 5 项全局参数
      global5: null,
      refreshTimer: null,
      refreshIntervalMs: 5000, // 刷新周期（可调，设为 null/0 可关闭）

      // 扇区风速风向数据缓存：{ [sectorId]: { speeds: [s1,s2,s3], directions: [d1,d2,d3] } }
      sectorWindMap: {}
    }
  },
  mounted() {
    // 初始化布局
    this.updateSize()
    window.addEventListener('resize', this.updateSize)

    // 左侧 5 项
    this.fetchGlobal5()
    if (this.refreshIntervalMs) {
      this.refreshTimer = setInterval(this.fetchGlobal5, this.refreshIntervalMs)
    }

    // 右侧 12 扇区（后端）
    this.fetchAllSectorsWind()
    // 如需周期刷新，从数据库取最新风速，可启用：
    // this.sectorTimer = setInterval(this.fetchAllSectorsWind, 3000)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.updateSize)
    if (this.refreshTimer) clearInterval(this.refreshTimer)
    if (this.sectorTimer) clearInterval(this.sectorTimer)
  },
  methods: {
    /* ------------------------- 左侧五项参数：mock/后端 ------------------------- */
    async fetchGlobal5() {
      try {
        if (this.useMockGlobal) {
          // 兜底：若切回本地模拟
          const dirs = ['N', 'NE', 'E', 'SE', 'S', 'SW', 'W', 'NW']
          const pick = dirs[Math.floor(Math.random() * dirs.length)]
          this.global5 = {
            envTemp: `${(22 + Math.random() * 6).toFixed(1)} ℃`,
            envHumidity: `${(40 + Math.random() * 30).toFixed(0)} %`,
            windSpeed: `${(1 + Math.random() * 5).toFixed(1)} m/s`,
            windDirection: pick,
            towerOuterTemp: `${(20 + Math.random() * 6).toFixed(1)} ℃`
          }
        } else {
          // 使用若依生成的 Eparameters 列表接口（通过 sectors.js 的 getGlobal5 适配）
          // 期望返回：{ data: { envTemp, envHumidity, windSpeed, windDirection, towerOuterTemp, recordTime } }
          const { data } = await getGlobal5()
          if (!data) {
            this.global5 = null
            return
          }
          // 将数字值格式化为带单位的字符串以匹配现有模板展示
          const fmt = (v, unit) => (v === null || v === undefined || v === '') ? '--' : `${v} ${unit}`
          this.global5 = {
            envTemp: fmt(data.envTemp, '℃'),
            envHumidity: fmt(data.envHumidity, '%'),
            windSpeed: fmt(data.windSpeed, 'm/s'),
            windDirection: data.windDirection ?? '--',
            towerOuterTemp: fmt(data.towerOuterTemp, '℃')
          }
        }
      } catch (e) {
        console.error('[fetchGlobal5] failed:', e)
        // 失败时不抛出，避免打断页面：显示为 null 或保留上次数据
        if (!this.global5) this.global5 = null
      }
    },

    /* ------------------------- 扇区风速/风向：后端 ------------------------- */
    async fetchAllSectorsWind() {
      try {
        if (this.useMockSectors) {
          // （若临时调试可回退到 mock，此分支保留占位）
          const map = {}
          for (let i = 1; i <= this.totalSectors; i++) {
            map[i] = { speeds: [2.0, 3.0, 4.0], directions: ['NE', 'E', 'SE'] }
          }
          this.sectorWindMap = map
        } else {
          // 调用后端聚合接口（我们在 sectors.js 里已写好）
          const map = await getAllSectorsWind(this.totalSectors)
          this.sectorWindMap = map
        }
      } catch (e) {
        console.error('[fetchAllSectorsWind] failed:', e)
      }
    },

    /* ------------------------- 扇区内文案计算 ------------------------- */
    avgSpeedText(sectorId) {
      const d = this.sectorWindMap[sectorId]
      if (!d || !Array.isArray(d.speeds) || d.speeds.length < 1) return '— m/s'
      const valid = d.speeds.filter(v => typeof v === 'number' && !isNaN(v))
      if (!valid.length) return '— m/s'
      const avg = valid.reduce((a,b)=>a+b,0) / valid.length
      return `${avg.toFixed(1)} m/s`
    },
    dirSensor1Text(sectorId) {
      const d = this.sectorWindMap[sectorId]
      if (!d || !Array.isArray(d.directions) || d.directions.length < 1) return '—'
      return d.directions[0] || '—' // 传感器1的风向
    },

    /* ------------------------- 扇区点击 & 详情 ------------------------- */
    async onSectorClick(id) {
      this.modalTitle = `扇区 ${id} — 风速/风向详情`
      this.modalVisible = true
      this.loading = true

      try {
        let d = this.sectorWindMap[id]
        if (!d) {
          // 若未缓存该扇区，单独请求一次
          d = await getSectorWind(id)
          // 写入缓存，避免下次再取
          this.$set(this.sectorWindMap, id, d)
        }
        const speeds = d.speeds || []
        const directions = d.directions || []
        const rows = [1,2,3].map(n => ({
          sensorId: `#${n}`,
          speed: (typeof speeds[n-1] === 'number' && !isNaN(speeds[n-1])) ? speeds[n-1] : '--',
          direction: directions[n-1] || '--'
        }))
        const validSpeeds = speeds.filter(v => typeof v === 'number' && !isNaN(v))
        const avg = validSpeeds.length ? (validSpeeds.reduce((a,b)=>a+b,0) / validSpeeds.length).toFixed(2) : '--'
        this.modalDetail = { sectorId: id, avgSpeed: avg, rows }
      } catch (e) {
        console.error('[onSectorClick] failed:', e)
        this.modalDetail = {
          sectorId: id,
          avgSpeed: '--',
          rows: [1,2,3].map(n => ({ sensorId:`#${n}`, speed:'--', direction:'--' }))
        }
      } finally {
        this.loading = false
      }
    },
    handleClose(done) {
      this.modalVisible = false
      done && done()
    },

    /* ------------------------- SVG 计算：扇形路径 / 文本位置 ------------------------- */
    sectorPath(index) {
      const slice = 360 / this.totalSectors
      const startAngle = -90 + index * slice
      const endAngle = startAngle + slice

      const outerStart = this.polarToCartesian(this.cx, this.cy, this.outerRadius, endAngle)
      const outerEnd = this.polarToCartesian(this.cx, this.cy, this.outerRadius, startAngle)
      const innerStart = this.polarToCartesian(this.cx, this.cy, this.innerRadius, startAngle)
      const innerEnd = this.polarToCartesian(this.cx, this.cy, this.innerRadius, endAngle)

      const largeArcFlag = slice > 180 ? 1 : 0

      const d = [
        `M ${outerStart.x} ${outerStart.y}`,
        `A ${this.outerRadius} ${this.outerRadius} 0 ${largeArcFlag} 0 ${outerEnd.x} ${outerEnd.y}`,
        `L ${innerStart.x} ${innerStart.y}`,
        `A ${this.innerRadius} ${this.innerRadius} 0 ${largeArcFlag} 1 ${innerEnd.x} ${innerEnd.y}`,
        'Z'
      ].join(' ')
      return d
    },
    polarToCartesian(cx, cy, radius, angleDeg) {
      const angleRad = (angleDeg * Math.PI) / 180.0
      return {
        x: cx + radius * Math.cos(angleRad),
        y: cy + radius * Math.sin(angleRad)
      }
    },
    labelPos(index) {
      const slice = 360 / this.totalSectors
      const startAngle = -90 + index * slice
      const midAngle = startAngle + slice / 2
      const labelRadius = (this.innerRadius + this.outerRadius) / 2
      return this.polarToCartesian(this.cx, this.cy, labelRadius, midAngle)
    },
    idPos(index) {
      // 计算扇区编号的位置：位于内环稍外（内半径 + 6），沿扇区中心角
      const slice = 360 / this.totalSectors
      const startAngle = -90 + index * slice
      const midAngle = startAngle + slice / 2
      const radius = this.innerRadius + 6
      return this.polarToCartesian(this.cx, this.cy, radius, midAngle)
    },
    sectorColor(index) {
      const palette = [
        '#e6f2ff', '#d9eef9', '#cfeaf5', '#bfe0ef',
        '#fff4db', '#ffe8b8', '#ffd999', '#ffcc66',
        '#eaf7e6', '#d0f0d1', '#bfe6bf', '#a8d99f'
      ]
      return palette[index % palette.length]
    },

    /* ------------------------- 布局尺寸自适应 ------------------------- */
    updateSize() {
      this.$nextTick(() => {
        const wrap = this.$refs.wrap
        if (!wrap) return
        const rect = wrap.getBoundingClientRect()
        const size = Math.floor(Math.min(rect.width, rect.height))
        const bounded = Math.max(340, Math.min(900, size))
        this.svgSize = bounded
        this.cx = bounded / 2
        this.cy = bounded / 2
        this.outerRadius = Math.floor(bounded * 0.45)
        this.innerRadius = Math.floor(bounded * 0.18)
      })
    }
  }
}
</script>

<style scoped>
.sectors-page {
  padding: 16px;
}
.header h2 { margin: 0 0 6px; font-weight: 600; }
.subtitle { margin: 0 12px 12px 0; color: #666; font-size: 13px; }

/* 主布局：左侧参数栏 + 右侧环形区 */
.main-layout {
  display: flex;
  gap: 16px;
  align-items: stretch;
  min-height: calc(100vh - 200px);
}

/* 左侧参数栏 */
.left-panel {
  flex: 0 0 220px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 8px 0;
}
.param-card {
  background: #fff;
  border: 1px solid #e6e6e6;
  border-radius: 6px;
  padding: 10px 12px;
}
.param-card .card-title {
  color: #666;
  font-size: 12px;
  margin-bottom: 6px;
}
.param-card .card-value {
  font-weight: 700;
  font-size: 16px;
  color: #2f6fb2;
}

/* 右侧 SVG 容器 */
.svg-wrapper {
  position: relative;
  flex: 1 1 auto;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fafafa;
  border-radius: 6px;
  padding: 12px;
  box-sizing: border-box;
}

/* 中心圆 */
.center-circle {
  fill: #ffffff;
  stroke: #e6e6e6;
  stroke-width: 2;
}

/* 扇形与文本样式（简洁、无动画） */
.sector-path { cursor: pointer; }
.sector-path:hover { filter: brightness(0.96); }

/* 两行文本：平均风速 + 风向(传感器1) */
.sector-line {
  font-weight: 600;
  fill: #2f6fb2;
  pointer-events: none;
}
.sector-line.speed { font-size: 14px; }
.sector-line.direction { font-size: 12px; }

/* 弹窗 loading */
.modal-loading { text-align:center; padding:24px; color:#666; }
</style>

/* 扇区编号：沿内环 */
.sector-id {
font-size: 11px;
font-weight: 700;
fill: #333;
opacity: 0.9;
}