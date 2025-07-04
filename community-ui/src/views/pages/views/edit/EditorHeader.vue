<template>
  <div class="editor-header">
    <button @click="emit('back')" class="back-button">← 返回</button>
    <input
        v-model="title"
        type="text"
        placeholder="输入文章标题"
        class="title-input"
        @input="updateTitle"
    />
    <el-button-group>
      <el-button type="primary" @click="emit('save', 1)">{{ isEditMode ? '更新草稿' : '保存草稿' }}</el-button>
      <el-button type="success" @click="emit('save', 0)">{{ isEditMode ? '更新文章' : '发布文章' }}</el-button>
    </el-button-group>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  title: {
    type: String,
    default: ''
  },
  isEditMode: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['back', 'save', 'update:title'])

const title = ref(props.title)

watch(() => props.title, (newVal) => {
  title.value = newVal
})

const updateTitle = () => {
  emit('update:title', title.value)
}
</script>

<style scoped>
.editor-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  gap: 15px;
}

.title-input {
  flex: 1;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  transition: all 0.3s;
  box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.05);
}

.title-input:focus {
  border-color: #1890ff;
  outline: none;
  box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.1), 0 0 0 2px rgba(24, 144, 255, 0.2);
}

.back-button {
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s;
  background-color: #f0f0f0;
  color: #333;
}

.back-button:hover {
  background-color: #e0e0e0;
}

@media (max-width: 768px) {
  .editor-header {
    flex-wrap: wrap;
  }

  .title-input {
    order: 1;
    width: 100%;
    margin-top: 10px;
  }
}
</style>