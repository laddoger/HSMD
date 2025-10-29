<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
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
          v-hasPermi="['environment:Eparameters:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['environment:Eparameters:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['environment:Eparameters:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['environment:Eparameters:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="EparametersList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="全局环境温度，单位℃" align="center" prop="globalEnvTemp" />
      <el-table-column label="环境湿度，单位%" align="center" prop="envHumidity" />
      <el-table-column label="环境风速，单位m/s" align="center" prop="envWindSpeed" />
      <el-table-column label="环境风向，单位N/S" align="center" prop="envWindDir" />
      <el-table-column label="塔外环境温度，单位℃" align="center" prop="towerEnvTemp" />
      <el-table-column label="时间" align="center" prop="ts" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.ts, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['environment:Eparameters:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['environment:Eparameters:remove']">删除</el-button>
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

    <!-- 添加或修改环境参数对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="EparametersRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="全局环境温度，单位℃" prop="globalEnvTemp">
          <el-input v-model="form.globalEnvTemp" placeholder="请输入全局环境温度，单位℃" />
        </el-form-item>
        <el-form-item label="环境湿度，单位%" prop="envHumidity">
          <el-input v-model="form.envHumidity" placeholder="请输入环境湿度，单位%" />
        </el-form-item>
        <el-form-item label="环境风速，单位m/s" prop="envWindSpeed">
          <el-input v-model="form.envWindSpeed" placeholder="请输入环境风速，单位m/s" />
        </el-form-item>
        <el-form-item label="环境风向，单位N/S" prop="envWindDir">
          <el-input v-model="form.envWindDir" placeholder="请输入环境风向，单位N/S" />
        </el-form-item>
        <el-form-item label="塔外环境温度，单位℃" prop="towerEnvTemp">
          <el-input v-model="form.towerEnvTemp" placeholder="请输入塔外环境温度，单位℃" />
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

<script setup name="Eparameters">
import { listEparameters, getEparameters, delEparameters, addEparameters, updateEparameters } from "@/api/environment/Eparameters"

const { proxy } = getCurrentInstance()

const EparametersList = ref([])
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
    ts: null
  },
  rules: {
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询环境参数列表 */
function getList() {
  loading.value = true
  listEparameters(queryParams.value).then(response => {
    EparametersList.value = response.rows
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
    globalEnvTemp: null,
    envHumidity: null,
    envWindSpeed: null,
    envWindDir: null,
    towerEnvTemp: null,
    ts: null
  }
  proxy.resetForm("EparametersRef")
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
  ids.value = selection.map(item => item.globalEnvTemp)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  open.value = true
  title.value = "添加环境参数"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _globalEnvTemp = row.globalEnvTemp || ids.value
  getEparameters(_globalEnvTemp).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改环境参数"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["EparametersRef"].validate(valid => {
    if (valid) {
      if (form.value.globalEnvTemp != null) {
        updateEparameters(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addEparameters(form.value).then(response => {
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
  const _globalEnvTemps = row.globalEnvTemp || ids.value
  proxy.$modal.confirm('是否确认删除环境参数编号为"' + _globalEnvTemps + '"的数据项？').then(function() {
    return delEparameters(_globalEnvTemps)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('environment/Eparameters/export', {
    ...queryParams.value
  }, `Eparameters_${new Date().getTime()}.xlsx`)
}

getList()
</script>
