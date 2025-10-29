<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="扇区号" prop="sector">
        <el-input
          v-model="queryParams.sector"
          placeholder="请输入扇区号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="传感器号" prop="sensor">
        <el-input
          v-model="queryParams.sensor"
          placeholder="请输入传感器号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="记录时间" prop="recordTime">
        <el-date-picker clearable
          v-model="queryParams.recordTime"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择记录时间">
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
          v-hasPermi="['environment:speeddata:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['environment:speeddata:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['environment:speeddata:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['environment:speeddata:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="speeddataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="扇区号" align="center" prop="sector" />
      <el-table-column label="传感器号" align="center" prop="sensor" />
      <el-table-column label="风速 (m/s)" align="center" prop="windSpeed" />
      <el-table-column label="风向 (度)" align="center" prop="windDirection" />
      <el-table-column label="记录时间" align="center" prop="recordTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.recordTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['environment:speeddata:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['environment:speeddata:remove']">删除</el-button>
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

    <!-- 添加或修改风速数据对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="speeddataRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="扇区号" prop="sector">
          <el-input v-model="form.sector" placeholder="请输入扇区号" />
        </el-form-item>
        <el-form-item label="传感器号" prop="sensor">
          <el-input v-model="form.sensor" placeholder="请输入传感器号" />
        </el-form-item>
        <el-form-item label="风速 (m/s)" prop="windSpeed">
          <el-input v-model="form.windSpeed" placeholder="请输入风速 (m/s)" />
        </el-form-item>
        <el-form-item label="风向 (度)" prop="windDirection">
          <el-input v-model="form.windDirection" placeholder="请输入风向 (度)" />
        </el-form-item>
        <el-form-item label="记录时间" prop="recordTime">
          <el-date-picker clearable
            v-model="form.recordTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择记录时间">
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

<script setup name="Speeddata">
import { listSpeeddata, getSpeeddata, delSpeeddata, addSpeeddata, updateSpeeddata } from "@/api/environment/speeddata"

const { proxy } = getCurrentInstance()

const speeddataList = ref([])
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
    sector: null,
    sensor: null,
    recordTime: null
  },
  rules: {
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询风速数据列表 */
function getList() {
  loading.value = true
  listSpeeddata(queryParams.value).then(response => {
    speeddataList.value = response.rows
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
    id: null,
    sector: null,
    sensor: null,
    windSpeed: null,
    windDirection: null,
    recordTime: null
  }
  proxy.resetForm("speeddataRef")
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
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加风速数据"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getSpeeddata(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改风速数据"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["speeddataRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateSpeeddata(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addSpeeddata(form.value).then(response => {
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
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除风速数据编号为"' + _ids + '"的数据项？').then(function() {
    return delSpeeddata(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('environment/speeddata/export', {
    ...queryParams.value
  }, `speeddata_${new Date().getTime()}.xlsx`)
}

getList()
</script>
