<template>
  <el-dialog
    v-model="visible"
    title="处理举报"
    width="50%"
    destroy-on-close
    @close="handleClose"
  >
    <div v-loading="loading" class="process-dialog-container">
      <el-form
        ref="processFormRef"
        :model="processForm"
        :rules="processRules"
        label-width="120px"
        label-position="right"
      >
        <el-form-item label="处理结果" prop="handleResult">
          <el-select
            v-model="processForm.handleResult"
            placeholder="请选择处理结果"
            style="width: 100%"
            @change="handleResultChange"
          >
            <el-option
              v-for="item in handleResultOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item
          v-if="showBanDays"
          label="封禁天数"
          prop="banDays"
        >
          <el-input-number
            v-model="processForm.banDays"
            :min="1"
            :max="365"
            controls-position="right"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="处理备注" prop="handleRemark">
          <el-input
            v-model="processForm.handleRemark"
            type="textarea"
            :rows="4"
            placeholder="请输入处理备注"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>

      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit">提交</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { processReport } from '@/api/moderation'

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true
  },
  reportId: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['update:modelValue', 'refresh'])

const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const loading = ref(false)
const processFormRef = ref(null)

const processForm = ref({
  handleResult: null,
  handleRemark: '',
  banDays: 7
})

const processRules = {
  handleResult: [
    { required: true, message: '请选择处理结果', trigger: 'change' }
  ],
  handleRemark: [
    { required: true, message: '请输入处理备注', trigger: 'blur' }
  ],
  banDays: [
    { required: true, message: '请输入封禁天数', trigger: 'blur' }
  ]
}

const handleResultOptions = [
  { value: 1, label: '删除内容' },
  { value: 2, label: '警告用户' },
  { value: 3, label: '封禁用户' },
  { value: 4, label: '无违规' }
]

// 是否显示封禁天数
const showBanDays = computed(() => {
  return processForm.value.handleResult === 3
})

// 处理结果变化
const handleResultChange = (value) => {
  if (value !== 3) {
    processForm.value.banDays = null
  } else {
    processForm.value.banDays = 7
  }
}

// 提交处理
const handleSubmit = async () => {
  try {
    await processFormRef.value.validate()
    loading.value = true

    const params = {
      reportId: props.reportId,
      ...processForm.value
    }

    await processReport(params)
    ElMessage.success('处理成功')
    emit('refresh')
    visible.value = false
  } catch (error) {
    if (error !== 'cancel') {
      console.error('处理举报失败:', error)
      ElMessage.error('处理举报失败')
    }
  } finally {
    loading.value = false
  }
}

// 关闭对话框
const handleClose = () => {
  processFormRef.value?.resetFields()
  visible.value = false
}

// 重置表单
const resetForm = () => {
  processForm.value = {
    handleResult: null,
    handleRemark: '',
    banDays: 7
  }
}

watch(
  () => props.modelValue,
  (newVal) => {
    if (newVal) {
      resetForm()
    }
  }
)
</script>

<style lang="scss" scoped>
.process-dialog-container {
  padding: 10px 20px;

  .dialog-footer {
    margin-top: 20px;
    text-align: right;
  }
}
</style>
