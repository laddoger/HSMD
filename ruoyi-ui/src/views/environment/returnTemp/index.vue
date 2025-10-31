<template>
  <div class="return-temp-page">
    <div class="header">
      <h2>回水管温度</h2>
      <div class="ops">
        <el-button size="mini" type="primary" @click="fetchData" :loading="loading">刷新</el-button>
        <span class="hint">展示最新 {{ targetCount }} 条数据；每行两条数据，共四列。</span>
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
        <!-- 第一条数据 -->
        <el-table-column label="位置" prop="aLabel" min-width="140" />
        <el-table-column label="温度(℃)" prop="aValue" min-width="110" />

        <!-- 第二条数据 -->
        <el-table-column label="位置" prop="bLabel" min-width="140" />
        <el-table-column label="温度(℃)" prop="bValue" min-width="110" />
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

        // 把数据按“两条为一行”分组
        this.rawList = simplified
        this.tableRows = this.toTwoPerRow(simplified)
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
</style>