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
          v-hasPermi="['environment:opening:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['environment:opening:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['environment:opening:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['environment:opening:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="openingList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="扇区号" align="center" prop="sectorId" />
      <el-table-column label="当前开度值" align="center" prop="currentOpening" />
      <el-table-column label="建议开度值" align="center" prop="suggestedOpening" />
      <el-table-column label="时间" align="center" prop="ts" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.ts, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['environment:opening:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['environment:opening:remove']">删除</el-button>
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

    <!-- 添加或修改喷雾阀门开度对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="openingRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="扇区号" prop="sectorId">
          <el-input v-model="form.sectorId" placeholder="请输入扇区号" />
        </el-form-item>
        <el-form-item label="当前开度值" prop="currentOpening">
          <el-input v-model="form.currentOpening" placeholder="请输入当前开度值" />
        </el-form-item>
        <el-form-item label="建议开度值" prop="suggestedOpening">
          <el-input v-model="form.suggestedOpening" placeholder="请输入建议开度值" />
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

<script setup name="Opening">
import { listOpening, getOpening, delOpening, addOpening, updateOpening } from "@/api/environment/opening"

const { proxy } = getCurrentInstance()

const openingList = ref([])
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
    ts: null
  },
  rules: {
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询喷雾阀门开度列表 */
function getList() {
  loading.value = true
  listOpening(queryParams.value).then(response => {
    openingList.value = response.rows
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
    currentOpening: null,
    suggestedOpening: null,
    ts: null
  }
  proxy.resetForm("openingRef")
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
  title.value = "添加喷雾阀门开度"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _sectorId = row.sectorId || ids.value
  getOpening(_sectorId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改喷雾阀门开度"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["openingRef"].validate(valid => {
    if (valid) {
      if (form.value.sectorId != null) {
        updateOpening(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addOpening(form.value).then(response => {
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
  proxy.$modal.confirm('是否确认删除喷雾阀门开度编号为"' + _sectorIds + '"的数据项？').then(function() {
    return delOpening(_sectorIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('environment/opening/export', {
    ...queryParams.value
  }, `opening_${new Date().getTime()}.xlsx`)
}

getList()
</script>
