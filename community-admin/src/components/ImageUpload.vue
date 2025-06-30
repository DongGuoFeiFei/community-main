<template>
  <div class="image-upload">
    <el-upload
        action="#"
        :show-file-list="false"
        :auto-upload="false"
        :on-change="handleChange"
        accept="image/*"
    >
      <el-image
          v-if="imageUrl"
          :src="imageUrl"
          fit="cover"
          class="uploaded-image"
      />
      <el-icon v-else class="upload-icon"><Plus /></el-icon>
    </el-upload>

    <div class="actions" v-if="imageUrl">
      <el-button type="danger" size="small" @click="handleRemove">
        <el-icon><Delete /></el-icon>
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { Plus, Delete } from '@element-plus/icons-vue'
import { uploadImage } from '@/api/upload'

const props = defineProps({
  modelValue: String
})

const emit = defineEmits(['update:modelValue'])

const imageUrl = ref(props.modelValue || '')

watch(() => props.modelValue, (newVal) => {
  if (newVal !== imageUrl.value) {
    imageUrl.value = newVal
  }
})

const handleChange = async (file) => {
  try {
    const formData = new FormData()
    formData.append('file', file.raw)

    const res = await uploadImage(formData)
    imageUrl.value = res.data.url
    emit('update:modelValue', imageUrl.value)
  } catch (error) {
    console.error('上传失败:', error)
    ElMessage.error('图片上传失败')
  }
}

const handleRemove = () => {
  imageUrl.value = ''
  emit('update:modelValue', '')
}
</script>

<style lang="scss" scoped>
.image-upload {
  position: relative;
  width: 150px;
  height: 150px;
  border: 1px dashed #dcdfe6;
  border-radius: 4px;
  overflow: hidden;

  &:hover {
    border-color: #409eff;
  }

  .uploaded-image {
    width: 100%;
    height: 100%;
  }

  .upload-icon {
    font-size: 28px;
    color: #8c939d;
    width: 150px;
    height: 150px;
    line-height: 150px;
    text-align: center;
  }

  .actions {
    position: absolute;
    bottom: 0;
    right: 0;
    padding: 5px;
    background-color: rgba(0, 0, 0, 0.5);

    .el-button {
      padding: 5px;
      min-height: auto;
    }
  }
}
</style>