<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="时间" prop="recordTime">
        <el-date-picker clearable
          v-model="queryParams.recordTime"
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
          v-hasPermi="['environment:Gparameters:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['environment:Gparameters:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['environment:Gparameters:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['environment:Gparameters:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="GparametersList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="机组负荷" align="center" prop="unitLoad" />
      <el-table-column label="背压" align="center" prop="backPressure" />
      <el-table-column label="出口温度" align="center" prop="outletTemperature" />
      <el-table-column label="水泵频率" align="center" prop="pumpFrequency" />
      <el-table-column label="水泵水压" align="center" prop="pumpPressure" />
      <el-table-column label="泵电流" align="center" prop="pumpCurrent" />
      <el-table-column label="出口流量" align="center" prop="outletFlow" />
      <el-table-column label="时间" align="center" prop="recordTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.recordTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['environment:Gparameters:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['environment:Gparameters:remove']">删除</el-button>
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

    <!-- 添加或修改全局参数对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="GparametersRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="机组负荷" prop="unitLoad">
          <el-input v-model="form.unitLoad" placeholder="请输入机组负荷" />
        </el-form-item>
        <el-form-item label="背压" prop="backPressure">
          <el-input v-model="form.backPressure" placeholder="请输入背压" />
        </el-form-item>
        <el-form-item label="出口温度" prop="outletTemperature">
          <el-input v-model="form.outletTemperature" placeholder="请输入出口温度" />
        </el-form-item>
        <el-form-item label="水泵频率" prop="pumpFrequency">
          <el-input v-model="form.pumpFrequency" placeholder="请输入水泵频率" />
        </el-form-item>
        <el-form-item label="水泵水压" prop="pumpPressure">
          <el-input v-model="form.pumpPressure" placeholder="请输入水泵水压" />
        </el-form-item>
        <el-form-item label="泵电流" prop="pumpCurrent">
          <el-input v-model="form.pumpCurrent" placeholder="请输入泵电流" />
        </el-form-item>
        <el-form-item label="出口流量" prop="outletFlow">
          <el-input v-model="form.outletFlow" placeholder="请输入出口流量" />
        </el-form-item>
        <el-form-item label="时间" prop="recordTime">
          <el-date-picker clearable
            v-model="form.recordTime"
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

<script setup name="Gparameters">
import { listGparameters, getGparameters, delGparameters, addGparameters, updateGparameters } from "@/api/environment/Gparameters"

const { proxy } = getCurrentInstance()

const GparametersList = ref([])
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
    recordTime: null
  },
  rules: {
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询全局参数列表 */
function getList() {
  loading.value = true
  listGparameters(queryParams.value).then(response => {
    GparametersList.value = response.rows
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
    unitLoad: null,
    backPressure: null,
    outletTemperature: null,
    pumpFrequency: null,
    pumpPressure: null,
    pumpCurrent: null,
    outletFlow: null,
    recordTime: null
  }
  proxy.resetForm("GparametersRef")
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
  title.value = "添加全局参数"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getGparameters(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改全局参数"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["GparametersRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateGparameters(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addGparameters(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除全局参数编号为"' + _ids + '"的数据项？').then(function() {
    return delGparameters(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('environment/Gparameters/export', {
    ...queryParams.value
  }, `Gparameters_${new Date().getTime()}.xlsx`)
}

getList()
</script>
