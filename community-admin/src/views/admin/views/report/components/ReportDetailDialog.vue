<template>
  <el-dialog
    v-model="visible"
    title="举报详情"
    width="70%"
    top="5vh"
    destroy-on-close
    @close="handleClose"
  >
    <div v-loading="loading" class="report-detail-container">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="举报ID">{{ reportDetail.id }}</el-descriptions-item>
        <el-descriptions-item label="举报人ID">{{ reportDetail.reporterId }}</el-descriptions-item>
        <el-descriptions-item label="内容类型">
          <el-tag :type="getContentTypeTag(reportDetail.contentType)">
            {{ getContentTypeLabel(reportDetail.contentType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="内容ID">{{ reportDetail.contentId }}</el-descriptions-item>
        <el-descriptions-item label="举报类型">
          <el-tag :type="getReportTypeTag(reportDetail.reportType)">
            {{ getReportTypeLabel(reportDetail.reportType) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="举报时间">
          {{ formatDateTime(reportDetail.createdAt) }}
        </el-descriptions-item>
        <el-descriptions-item label="处理状态">
          <el-tag :type="getStatusTag(reportDetail.status)">
            {{ getStatusLabel(reportDetail.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item v-if="reportDetail.handlerId" label="处理人ID">
          {{ reportDetail.handlerId }}
        </el-descriptions-item>
        <el-descriptions-item v-if="reportDetail.updatedAt" label="处理时间">
          {{ formatDateTime(reportDetail.updatedAt) }}
        </el-descriptions-item>
        <el-descriptions-item v-if="reportDetail.handleResult" label="处理结果">
          <el-tag :type="getHandleResultTag(reportDetail.handleResult)">
            {{ getHandleResultLabel(reportDetail.handleResult) }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>

      <div class="section-container">
        <h3>举报描述</h3>
        <div class="content-box">
          {{ reportDetail.description || '无描述' }}
        </div>
      </div>

      <div v-if="reportDetail.evidence" class="section-container">
        <h3>举报证据</h3>
        <div class="evidence-container">
          <div v-for="(item, index) in parsedEvidence" :key="index" class="evidence-item">
            <el-image
              v-if="isImage(item)"
              :src="item"
              :preview-src-list="parsedEvidence"
              :initial-index="index"
              fit="cover"
              style="width: 150px; height: 150px"
            />
            <div v-else class="non-image-evidence">
              <el-link :href="item" target="_blank">{{ item }}</el-link>
            </div>
          </div>
        </div>
      </div>

      <div v-if="reportDetail.handleRemark" class="section-container">
        <h3>处理备注</h3>
        <div class="content-box">
          {{ reportDetail.handleRemark }}
        </div>
      </div>

      <div class="action-buttons">
        <el-button type="primary" @click="handleProcess">处理举报</el-button>
        <el-button v-if="reportDetail.status === 0" type="danger" @click="handleIgnore">
          忽略举报
        </el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import { getReportDetail, ignoreReport } from '@/api/moderation'

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true
  },
  reportId: {
    type: Number,
    required: true,
    validator: (value) => {
      return value > 0
    }
  }
})

const emit = defineEmits(['update:modelValue', 'refresh'])

const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const loading = ref(false)
const reportDetail = ref({
  id: null,
  reporterId: null,
  contentId: null,
  contentType: null,
  reportType: null,
  description: null,
  evidence: null,
  status: null,
  handlerId: null,
  handleResult: null,
  handleRemark: null,
  createdAt: null,
  updatedAt: null
})

// 解析证据
const parsedEvidence = computed(() => {
  try {
    if (!reportDetail.value.evidence) return []
    const evidence = JSON.parse(reportDetail.value.evidence)
    return Array.isArray(evidence) ? evidence : [evidence]
  } catch (e) {
    return []
  }
})

// 判断是否是图片
const isImage = (url) => {
  return /\.(jpg|jpeg|png|gif|webp)$/i.test(url)
}

// 获取举报详情
const getDetail = async () => {
  try {
    loading.value = true
    const res = await getReportDetail(props.reportId)
    reportDetail.value = res.data
  } catch (error) {
    console.error('获取举报详情失败:', error)
    ElMessage.error('获取举报详情失败')
  } finally {
    loading.value = false
  }
}

// 处理举报
const handleProcess = () => {
  emit('process', props.reportId)
}

// 忽略举报
const handleIgnore = async () => {
  try {
    await ignoreReport(props.reportId)
    ElMessage.success('忽略成功')
    emit('refresh')
    visible.value = false
  } catch (error) {
    ElMessage.error('忽略失败')
  }
}

// 关闭对话框
const handleClose = () => {
  visible.value = false
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
  const map = {
    1: 'warning',
    2: 'danger',
    3: 'danger',
    4: 'danger',
    5: ''
  }
  return map[type] || ''
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

// 获取处理结果标签
const getHandleResultTag = (result) => {
  const map = {
    1: 'danger',
    2: 'warning',
    3: 'danger',
    4: 'success'
  }
  return map[result] || ''
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

// 获取处理结果标签文本
const getHandleResultLabel = (result) => {
  const map = {
    1: '删除内容',
    2: '警告用户',
    3: '封禁用户',
    4: '无违规'
  }
  return map[result] || '未知'
}

watch(
  () => props.reportId,
  (newVal) => {
    if (newVal && visible.value) {
      getDetail()
    }
  },
  { immediate: true }
)
</script>

<style lang="scss" scoped>
.report-detail-container {
  padding: 10px;

  .section-container {
    margin-top: 20px;

    h3 {
      margin-bottom: 10px;
      color: #606266;
    }

    .content-box {
      padding: 10px;
      border: 1px solid #ebeef5;
      border-radius: 4px;
      background-color: #f5f7fa;
    }
  }

  .evidence-container {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    margin-top: 10px;

    .evidence-item {
      border: 1px solid #ebeef5;
      border-radius: 4px;
      overflow: hidden;
    }

    .non-image-evidence {
      padding: 10px;
      background-color: #f5f7fa;
      word-break: break-all;
    }
  }

  .action-buttons {
    margin-top: 20px;
    text-align: right;
  }
}
</style>
