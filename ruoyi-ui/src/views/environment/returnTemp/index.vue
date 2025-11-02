<template>
  <div class="return-temp-page">
    <div class="header">
      <h2>回水管温度</h2>
      <div class="ops">
        <el-button size="mini" type="primary" @click="fetchData" :loading="loading">刷新</el-button>
        <el-button size="mini" @click="prevPage" :disabled="currentPage === 1">‹</el-button>
        <span class="page-indicator">&lt;{{ currentPage }}/2&gt;</span>
        <el-button size="mini" @click="nextPage" :disabled="currentPage === 2">›</el-button>
        <span class="hint">展示最新 {{ targetCount }} 条数据；每行四列（每列显示一个扇区-三角的 L/R 温度）。</span>
      </div>
    </div>

    <el-card shadow="never">
      <el-table
        v-loading="loading"
        :data="tableRows"
        border
        size="small"
        style="width: 100%"
      >
        <!-- 列1：一组 扇区-三角 + L/R -->
        <el-table-column label="位置 / 温度" min-width="220">
          <template #default="scope">
            <div v-if="scope.row.col1" class="pair-cell">
              <div class="pair-label">{{ scope.row.col1.label }}</div>
              <div class="pair-values">
                <span>L: {{ scope.row.col1.L }}</span>
                <span class="sep">/</span>
                <span>R: {{ scope.row.col1.R }}</span>
              </div>
            </div>
          </template>
        </el-table-column>
        <!-- 列2 -->
        <el-table-column label="位置 / 温度" min-width="220">
          <template #default="scope">
            <div v-if="scope.row.col2" class="pair-cell">
              <div class="pair-label">{{ scope.row.col2.label }}</div>
              <div class="pair-values">
                <span>L: {{ scope.row.col2.L }}</span>
                <span class="sep">/</span>
                <span>R: {{ scope.row.col2.R }}</span>
              </div>
            </div>
          </template>
        </el-table-column>
        <!-- 列3 -->
        <el-table-column label="位置 / 温度" min-width="220">
          <template #default="scope">
            <div v-if="scope.row.col3" class="pair-cell">
              <div class="pair-label">{{ scope.row.col3.label }}</div>
              <div class="pair-values">
                <span>L: {{ scope.row.col3.L }}</span>
                <span class="sep">/</span>
                <span>R: {{ scope.row.col3.R }}</span>
              </div>
            </div>
          </template>
        </el-table-column>
        <!-- 列4 -->
        <el-table-column label="位置 / 温度" min-width="220">
          <template #default="scope">
            <div v-if="scope.row.col4" class="pair-cell">
              <div class="pair-label">{{ scope.row.col4.label }}</div>
              <div class="pair-values">
                <span>L: {{ scope.row.col4.L }}</span>
                <span class="sep">/</span>
                <span>R: {{ scope.row.col4.R }}</span>
              </div>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="!loading && !tableRows.length" class="empty">
        暂无数据
      </div>
    </el-card>
  </div>
</template>

<script>
// 使用若依生成的API（请确认文件路径与命名，通常为：@/api/environment/rpiptemp）
import { listRpiptemp } from '@/api/environment/rpiptemp'

export default {
  name: 'ReturnPipeTemp',
  data() {
    return {
      loading: false,
      targetCount: 348,      // 需求：最新 348 条
      rawList: [],           // 原始数据列表
      tableRows: [],         // 转成“每行两条数据（四列）”的表格数据
      refreshTimer: null,    // 定时器
      pairList: [],        // 聚合后的 (sector-triangle) 对，内含 L/R 两个温度
      currentPage: 1,      // 当前页（1 或 2）
    }
  },
  mounted() {
    this.fetchData()
    this.refreshTimer = setInterval(this.fetchData, 10000)
  },
  beforeUnmount() {
    if (this.refreshTimer) {
      clearInterval(this.refreshTimer)
      this.refreshTimer = null
    }
  },
  methods: {
    // 兼容若依响应结构，抽 rows
    extractRows(res) {
      const body = res && res.data ? res.data : res
      if (!body) return []
      if (Array.isArray(body)) return body
      if (Array.isArray(body.rows)) return body.rows
      if (body.list && Array.isArray(body.list)) return body.list
      if (body.data && Array.isArray(body.data)) return body.data
      if (body.data && Array.isArray(body.data.rows)) return body.data.rows
      return []
    },

    // 把原始数组 => 组装成 “每行两条数据（四列）”
    toTwoPerRow(list) {
      const rows = []
      for (let i = 0; i < list.length; i += 2) {
        const a = list[i]
        const b = list[i + 1]
        rows.push({
          aLabel: a ? `${a.sector_id ?? a.sector ?? a.sectorId}-${a.triangle_no ?? a.triangleNo}-${a.side}` : '',
          aValue: a ? this.formatTemp(a.temp_celsius ?? a.tempCelsius) : '',
          bLabel: b ? `${b.sector_id ?? b.sector ?? b.sectorId}-${b.triangle_no ?? b.triangleNo}-${b.side}` : '',
          bValue: b ? this.formatTemp(b.temp_celsius ?? b.tempCelsius) : ''
        })
      }
      return rows
    },

    // 将原始记录聚合为 (sector-triangle) 对，并拿最新一条的 L/R
    buildPairList(list) {
      // key: `${sector}-${triangle}`
      const map = new Map()
      for (const it of list) {
        const sector = it.sector_id ?? it.sector ?? it.sectorId
        const tri = it.triangle_no ?? it.triangleNo
        const side = (it.side || '').toUpperCase()
        const temp = it.temp_celsius ?? it.tempCelsius
        if (sector == null || tri == null || !side) continue
        const key = `${Number(sector)}-${Number(tri)}`
        if (!map.has(key)) {
          map.set(key, { label: key, L: '', R: '' })
        }
        const obj = map.get(key)
        // 因为查询已按 ts desc，先到先得即为最新
        if (side === 'L' && obj.L === '') obj.L = this.formatTemp(temp)
        if (side === 'R' && obj.R === '') obj.R = this.formatTemp(temp)
      }
      // 排序：sector 升序，triangle 升序
      const arr = Array.from(map.values()).sort((a, b) => {
        const [as, at] = a.label.split('-').map(n => Number(n))
        const [bs, bt] = b.label.split('-').map(n => Number(n))
        return as - bs || at - bt
      })
      this.pairList = arr
    },
    // 按当前页将 pairList 渲染为表格：每行四列（col1..col4），每列一个 pair（包含 L/R）
    buildRowsForPage() {
      const totalPairs = this.pairList.length
      const page1Count = Math.ceil(totalPairs / 2)
      const start = this.currentPage === 1 ? 0 : page1Count
      const end = this.currentPage === 1 ? page1Count : totalPairs
      const pagePairs = this.pairList.slice(start, end)

      const rows = []
      for (let i = 0; i < pagePairs.length; i += 4) {
        const r = {
          col1: pagePairs[i] || null,
          col2: pagePairs[i + 1] || null,
          col3: pagePairs[i + 2] || null,
          col4: pagePairs[i + 3] || null
        }
        rows.push(r)
      }
      this.tableRows = rows
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage -= 1
        this.buildRowsForPage()
      }
    },
    nextPage() {
      if (this.currentPage < 2) {
        this.currentPage += 1
        this.buildRowsForPage()
      }
    },

    formatTemp(v) {
      const n = Number(v)
      return Number.isFinite(n) ? n.toString() : ''
    },

    async fetchData() {
      try {
        this.loading = true

        const query = {
          pageNum: 1,
          pageSize: this.targetCount,
          orderByColumn: 'ts',
          isAsc: 'desc'
        }

        // 使用若依生成的列表接口
        const res = await listRpiptemp(query)

        const rows = this.extractRows(res) || []

        // 只保留需要展示的字段，组装 label/value
        const simplified = rows.map(it => ({
          sector_id: it.sector_id ?? it.sector ?? it.sectorId,
          triangle_no: it.triangle_no ?? it.triangleNo,
          side: it.side,
          temp_celsius: it.temp_celsius ?? it.tempCelsius
        }))

        // 构建 (sector-triangle) 对，并分页为两页，每行四列
        this.rawList = simplified
        this.buildPairList(simplified)
        // 保持当前页不变（初始化为 1）
        if (this.currentPage !== 1 && this.currentPage !== 2) {
          this.currentPage = 1
        }
        this.buildRowsForPage()
      } catch (e) {
        console.error('[ReturnPipeTemp] fetchData failed:', e)
        this.rawList = []
        this.tableRows = []
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.return-temp-page {
  padding: 16px;
}
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}
.header h2 {
  margin: 0;
  font-weight: 600;
}
.ops {
  display: flex;
  align-items: center;
  gap: 8px;
}
.hint {
  color: #666;
  font-size: 12px;
}
.empty {
  text-align: center;
  color: #888;
  padding: 18px 0;
}
.el-table {
  font-size: 16px;
  font-weight: 600;
}

.el-table th, .el-table td {
  font-size: 16px;
  font-weight: 600;
}
.pair-cell {
  display: flex;
  flex-direction: column;
  gap: 4px;
  line-height: 1.2;
}
.pair-label {
  font-weight: 700;
}
.pair-values {
  font-weight: 600;
}
.pair-values .sep {
  margin: 0 6px;
  opacity: 0.7;
}
.page-indicator {
  margin: 0 6px;
  color: #333;
  font-weight: 600;
}
</style>