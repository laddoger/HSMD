<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="扇区号" prop="sectorId">
        <el-input
          v-model="queryParams.sectorId"
          placeholder="请输入扇区号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="三角" prop="triangleNo">
        <el-input
          v-model="queryParams.triangleNo"
          placeholder="请输入三角"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="方向" prop="side">
        <el-input
          v-model="queryParams.side"
          placeholder="请输入方向(LR/左右)"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="时间" prop="ts">
        <el-date-picker clearable
          v-model="queryParams.ts"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['environment:rpiptemp:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['environment:rpiptemp:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['environment:rpiptemp:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['environment:rpiptemp:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="rpiptempList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="扇区号" align="center" prop="sectorId" />
      <el-table-column label="三角" align="center" prop="triangleNo" />
      <el-table-column label="方向(LR/左右)" align="center" prop="side" />
      <el-table-column label="温度℃" align="center" prop="tempCelsius" />
      <el-table-column label="时间" align="center" prop="ts" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.ts, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['environment:rpiptemp:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['environment:rpiptemp:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改回水管温度对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="rpiptempRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="扇区号" prop="sectorId">
          <el-input v-model="form.sectorId" placeholder="请输入扇区号" />
        </el-form-item>
        <el-form-item label="三角" prop="triangleNo">
          <el-input v-model="form.triangleNo" placeholder="请输入三角" />
        </el-form-item>
        <el-form-item label="方向(LR/左右)" prop="side">
          <el-input v-model="form.side" placeholder="请输入方向(LR/左右)" />
        </el-form-item>
        <el-form-item label="温度℃" prop="tempCelsius">
          <el-input v-model="form.tempCelsius" placeholder="请输入温度℃" />
        </el-form-item>
        <el-form-item label="时间" prop="ts">
          <el-date-picker clearable
            v-model="form.ts"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Rpiptemp">
import { listRpiptemp, getRpiptemp, delRpiptemp, addRpiptemp, updateRpiptemp } from "@/api/environment/rpiptemp"

const { proxy } = getCurrentInstance()

const rpiptempList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    sectorId: null,
    triangleNo: null,
    side: null,
    ts: null
  },
  rules: {
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询回水管温度列表 */
function getList() {
  loading.value = true
  listRpiptemp(queryParams.value).then(response => {
    rpiptempList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 取消按钮
function cancel() {
  open.value = false
  reset()
}

// 表单重置
function reset() {
  form.value = {
    sectorId: null,
    triangleNo: null,
    side: null,
    tempCelsius: null,
    ts: null
  }
  proxy.resetForm("rpiptempRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.sectorId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加回水管温度"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _sectorId = row.sectorId || ids.value
  getRpiptemp(_sectorId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改回水管温度"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["rpiptempRef"].validate(valid => {
    if (valid) {
      if (form.value.sectorId != null) {
        updateRpiptemp(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addRpiptemp(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _sectorIds = row.sectorId || ids.value
  proxy.$modal.confirm('是否确认删除回水管温度编号为"' + _sectorIds + '"的数据项？').then(function() {
    return delRpiptemp(_sectorIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('environment/rpiptemp/export', {
    ...queryParams.value
  }, `rpiptemp_${new Date().getTime()}.xlsx`)
}

getList()
</script>
