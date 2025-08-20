<template>
  <div class="report-management-container">
    <el-card shadow="never">
      <div class="filter-container">
        <el-form :inline="true" :model="queryParams" class="demo-form-inline">
          <el-form-item label="举报类型">
            <el-select
              v-model="queryParams.reportType"
              placeholder="请选择举报类型"
              clearable
              style="width: 150px"
            >
              <el-option
                v-for="item in reportTypeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="处理状态">
            <el-select
              v-model="queryParams.status"
              placeholder="请选择状态"
              clearable
              style="width: 150px"
            >
              <el-option
                v-for="item in statusOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="内容类型">
            <el-select
              v-model="queryParams.contentType"
              placeholder="请选择内容类型"
              clearable
              style="width: 150px"
            >
              <el-option
                v-for="item in contentTypeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleQuery">查询</el-button>
            <el-button @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table
        v-loading="loading"
        :data="reportList"
        border
        style="width: 100%"
        @row-click="handleRowClick"
      >
        <el-table-column prop="id" label="ID" width="80"/>
        <el-table-column prop="contentType" label="内容类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getContentTypeTag(row.contentType)">
              {{ getContentTypeLabel(row.contentType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="contentId" label="内容ID" width="100"/>
        <el-table-column prop="reportType" label="举报类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getReportTypeTag(row.reportType)">
              {{ getReportTypeLabel(row.reportType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="举报描述" min-width="200" show-overflow-tooltip/>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)">
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="举报时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click.stop="handleProcess(row)">处理</el-button>
            <el-button size="small" type="danger" @click.stop="handleIgnore(row)">忽略</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <ReportDetailDialog
      v-model="detailDialogVisible"
      :report-id="currentReportId"
      @refresh="getList"
    />
    <ReportProcessDialog
      v-model="processDialogVisible"
      :report-id="currentReportId"
      @refresh="getList"
    />
  </div>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import dayjs from 'dayjs'
import {getReportList, ignoreReport} from '@/api/moderation'
import ReportDetailDialog from './ReportDetailDialog.vue'
import ReportProcessDialog from './ReportProcessDialog.vue'

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  reportType: undefined,
  status: undefined,
  contentType: undefined
})

const loading = ref(false)
const reportList = ref([])
const total = ref(0)
const currentReportId = ref(null)
const detailDialogVisible = ref(false)
const processDialogVisible = ref(false)

// 枚举选项
const reportTypeOptions = [
  {value: 1, label: '垃圾广告'},
  {value: 2, label: '色情低俗'},
  {value: 3, label: '虚假信息'},
  {value: 4, label: '侵权'},
  {value: 5, label: '其他'}
]

const statusOptions = [
  {value: 0, label: '待处理'},
  {value: 1, label: '已处理'},
  {value: 2, label: '已忽略'}
]

const contentTypeOptions = [
  {value: 12, label: '文章'},
  {value: 13, label: '作者'},
  {value: 14, label: '评论'},
  {value: 15, label: '回复'}
]

// 获取举报列表
const getList = async () => {
  try {
    loading.value = true
    const res = await getReportList(queryParams)
    reportList.value = res.data.rows
    total.value = res.data.total
  } catch (error) {
    console.error('获取举报列表失败:', error)
    ElMessage.error('获取举报列表失败')
  } finally {
    loading.value = false
  }
}

// 查询
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

// 重置查询
const resetQuery = () => {
  queryParams.reportType = undefined
  queryParams.status = undefined
  queryParams.contentType = undefined
  handleQuery()
}

// 分页大小变化
const handleSizeChange = (val) => {
  queryParams.pageSize = val
  getList()
}

// 当前页变化
const handleCurrentChange = (val) => {
  queryParams.pageNum = val
  getList()
}

// 行点击事件
const handleRowClick = (row) => {
  if (!row?.id) {
    ElMessage.error('无效的举报ID')
    return
  }
  currentReportId.value = row.id
  detailDialogVisible.value = true
}

// 处理举报
const handleProcess = (row) => {
  if (!row?.id) {
    ElMessage.error('无效的举报ID')
    return
  }
  currentReportId.value = Number(row.id) // 确保转换为数字
  processDialogVisible.value = true
}

// 忽略举报
const handleIgnore = async (row) => {
  if (!row?.id) {
    ElMessage.error('无效的举报ID')
    return
  }
  try {
    await ElMessageBox.confirm('确定要忽略此举报吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await ignoreReport(row.id)
    ElMessage.success('忽略成功')
    getList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('忽略失败')
    }
  }
}

// 格式化时间
const formatDateTime = (date) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm:ss')
}

// 获取内容类型标签
const getContentTypeTag = (type) => {
  const map = {
    1: '',
    2: 'info',
    3: 'success'
  }
  return map[type] || ''
}

// 获取举报类型标签
const getReportTypeTag = (type) => {
  return type
}

// 获取状态标签
const getStatusTag = (status) => {
  const map = {
    0: 'warning',
    1: 'success',
    2: 'info'
  }
  return map[status] || ''
}

// 获取内容类型标签文本
const getContentTypeLabel = (type) => {
  return type
}

// 获取举报类型标签文本
const getReportTypeLabel = (type) => {
  return type
}

// 获取状态标签文本
const getStatusLabel = (status) => {
  return status
}

onMounted(() => {
  getList()
})
</script>

<style lang="scss" scoped>
.report-management-container {
  padding: 20px;

  .filter-container {
    margin-bottom: 20px;
  }

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }

  .el-table {
    margin-top: 20px;

    :deep(.el-table__row) {
      cursor: pointer;
    }
  }
}
</style>
