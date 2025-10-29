<template>
  <div class="sectors-page">
    <div class="header">
      <h2>间冷塔 — 扇区视图</h2>
      <p class="subtitle">点击任一扇区查看该扇区的详细参数（数据为占位，可接后端）</p>
    </div>

    <div class="svg-wrapper" ref="wrap">
      <!-- 四角全局参数卡 -->
      <div class="corner-card top-left" v-if="globalParams">
        <div class="card-title">水泵频率</div>
        <div class="card-value">{{ globalParams.pumpFrequency }}</div>
      </div>

      <div class="corner-card top-right" v-if="globalParams">
        <div class="card-title">水泵水压</div>
        <div class="card-value">{{ globalParams.pumpPressure }}</div>
      </div>

      <div class="corner-card bottom-left" v-if="globalParams">
        <div class="card-title">变频泵电流</div>
        <div class="card-value">{{ globalParams.inverterCurrent }}</div>
      </div>

      <div class="corner-card bottom-right" v-if="globalParams">
        <div class="card-title">管道出口流量</div>
        <div class="card-value">{{ globalParams.outletFlow }}</div>
      </div>

      <!-- SVG 圆环 -->
      <svg
          :width="svgSize"
          :height="svgSize"
          :viewBox="`0 0 ${svgSize} ${svgSize}`"
      >
        <circle
            :cx="cx"
            :cy="cy"
            :r="innerRadius - 6"
            class="center-circle"
        />
        <g v-for="i in totalSectors" :key="i">
          <path
              :d="sectorPath(i - 1)"
              :fill="sectorColor(i - 1)"
              stroke="#ffffff"
              stroke-width="1"
              class="sector-path"
              @click="onSectorClick(i)"
          />
          <text
              :x="labelPos(i - 1).x"
              :y="labelPos(i - 1).y"
              class="sector-number"
              text-anchor="middle"
              alignment-baseline="middle"
              pointer-events="none"
          >
            {{ i }}
          </text>
        </g>
      </svg>
    </div>

    <el-dialog
        :title="modalTitle"
        v-model="modalVisible"
        append-to-body
        width="520px"
        :before-close="handleClose"
    >
      <div v-if="loading" class="modal-loading">加载中...</div>

      <div v-else class="modal-body">
        <el-descriptions column="1" border>
          <el-descriptions-item label="扇区ID">{{ modalData.id }}</el-descriptions-item>
          <el-descriptions-item label="名称">{{ modalData.name }}</el-descriptions-item>
          <el-descriptions-item label="温度 (℃)">{{ modalData.temp }}</el-descriptions-item>
          <el-descriptions-item label="湿度 (%)">{{ modalData.humidity }}</el-descriptions-item>
          <el-descriptions-item label="PM2.5 (µg/m³)">{{ modalData.pm25 }}</el-descriptions-item>
          <el-descriptions-item label="CO₂ (ppm)">{{ modalData.co2 }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ modalData.status }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="modalVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getSectorDetail, mockSectorDetail, getGlobalParams, mockGlobalParams } from '@/api/environment/sectors'

export default {
  name: 'EnvironmentSectors',
  data() {
    return {
      // modal
      modalVisible: false,
      modalTitle: '',
      modalData: {},
      loading: false,
      useMock: true, // 切到 false 使用真实后端接口

      // svg sizing
      svgSize: 600,
      cx: 300,
      cy: 300,
      outerRadius: 260,
      innerRadius: 110,
      totalSectors: 12,

      // global params
      globalParams: null,
      refreshTimer: null,
      refreshIntervalMs: 5000 // 每 5 秒刷新一次全局参数（可调整或设为 null 关闭定时）
    }
  },
  mounted() {
    this.updateSize()
    window.addEventListener('resize', this.updateSize)

    // 加载全局参数（立即）并启动定时刷新（若 refreshIntervalMs 为 null 则不启动）
    this.fetchGlobalParams()
    if (this.refreshIntervalMs) {
      this.refreshTimer = setInterval(this.fetchGlobalParams, this.refreshIntervalMs)
    }
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.updateSize)
    if (this.refreshTimer) clearInterval(this.refreshTimer)
  },
  methods: {
    // 扇区点击
    onSectorClick(id) {
      console.log('sector clicked:', id)
      this.openSector(id)
    },
    openSector(id) {
      this.modalTitle = `扇区 ${id} — 参数`
      this.modalVisible = true
      this.loading = true
      if (this.useMock) {
        mockSectorDetail(id).then(res => {
          this.modalData = res.data
          this.loading = false
        })
      } else {
        getSectorDetail(id)
            .then(res => {
              this.modalData = res.data || (res.data && res.data.data) || {}
              this.loading = false
            })
            .catch(() => {
              this.modalData = {}
              this.loading = false
              this.$message.error('获取扇区数据失败')
            })
      }
    },
    handleClose(done) {
      this.modalVisible = false
      setTimeout(() => { this.modalData = {} }, 200)
      done && done()
    },

    // 全局参数拉取
    fetchGlobalParams() {
      if (this.useMock) {
        mockGlobalParams().then(res => {
          this.globalParams = res.data
        }).catch(() => {
          this.globalParams = null
        })
      } else {
        getGlobalParams().then(res => {
          // 根据后端返回结构调整解析
          this.globalParams = res.data || (res.data && res.data.data) || null
        }).catch(() => {
          this.globalParams = null
          this.$message.error('获取全局参数失败')
        })
      }
    },

    // SVG sector path / util functions
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
    sectorColor(index) {
      const palette = [
        '#e6f2ff', '#d9eef9', '#cfeaf5', '#bfe0ef',
        '#fff4db', '#ffe8b8', '#ffd999', '#ffcc66',
        '#eaf7e6', '#d0f0d1', '#bfe6bf', '#a8d99f'
      ]
      return palette[index % palette.length]
    },

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
.subtitle { margin: 0 0 12px; color: #666; font-size: 13px; }

/* 主容器 */
.svg-wrapper {
  position: relative; /* 关键：四角卡片绝对定位基于该容器 */
  width: 100%;
  height: calc(100vh - 200px);
  min-height: 420px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fafafa;
  border-radius: 6px;
  padding: 12px;
  box-sizing: border-box;
}

/* 四角卡片通用样式 */
.corner-card {
  position: absolute;
  width: 168px;
  padding: 10px;
  box-sizing: border-box;
  background: #ffffff;
  border: 1px solid #e6e6e6;
  border-radius: 6px;
  text-align: left;
  z-index: 5; /* 高于 svg */
  font-size: 13px;
}
.corner-card .card-title { color:#666; font-size:12px; margin-bottom:6px; }
.corner-card .card-value { font-weight:700; font-size:16px; color:#2f6fb2; }

/* 四角定位 */
.top-left { left: 16px; top: 16px; }
.top-right { right: 16px; top: 16px; }
.bottom-left { left: 16px; bottom: 16px; }
.bottom-right { right: 16px; bottom: 16px; }

/* center circle style */
.center-circle {
  fill: #ffffff;
  stroke: #e6e6e6;
  stroke-width: 2;
}

/* sector path cursor */
.sector-path {
  cursor: pointer;
  transition: fill 0.08s linear;
}
.sector-path:hover { filter: brightness(0.96); }

/* sector number style */
.sector-number {
  font-size: 18px;
  font-weight: 600;
  fill: #2f6fb2;
  pointer-events: none;
}

/* modal loading */
.modal-loading { text-align:center; padding:24px; color:#666; }
</style>