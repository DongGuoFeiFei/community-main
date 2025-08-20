<template>
  <el-dialog
    :model-value="modelValue"
    title="添加特权"
    width="500px"
    @update:modelValue="$emit('update:modelValue', $event)"
    @close="handleClose"
  >
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="特权名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入特权名称" />
      </el-form-item>
      <el-form-item label="特权描述" prop="description">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="请输入特权描述"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  modelValue: Boolean
})

const emit = defineEmits(['update:modelValue', 'addPrivilege'])

const formRef = ref(null)
const form = ref({
  name: '',
  description: ''
})

const rules = ref({
  name: [
    { required: true, message: '请输入特权名称', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入特权描述', trigger: 'blur' }
  ]
})

const handleClose = () => {
  emit('update:modelValue', false)
  formRef.value?.resetFields()
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    emit('addPrivilege', { ...form.value })
    handleClose()
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}
</script>
