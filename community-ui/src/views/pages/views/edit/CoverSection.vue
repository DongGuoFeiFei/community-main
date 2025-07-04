<template>
  <div class="cover-section">
    <h3>文章封面</h3>
    <div class="cover-actions">
      <button @click="emit('select')" class="cover-button">
        {{ coverUrl ? '更换封面' : '选择封面' }}
      </button>
      <button
          v-if="coverUrl"
          @click="emit('remove')"
          class="cover-button remove-button"
      >
        移除封面
      </button>
    </div>
    <div v-if="coverUrl" class="cover-preview">
      <img :src="coverUrl" alt="封面预览" class="cover-image">
    </div>
  </div>
</template>

<script setup>
defineProps({
  coverUrl: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['select', 'remove'])
</script>

<style scoped>
.cover-section {
  margin-top: 30px;
  padding: 25px;
  border: 1px solid #e8e8e8;
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.03);
}

.cover-section h3 {
  margin: 0 0 20px 0;
  font-size: 18px;
  color: #333;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 8px;
}

.cover-section h3::before {
  content: "";
  display: inline-block;
  width: 4px;
  height: 16px;
  background-color: #1890ff;
  border-radius: 2px;
}

.cover-actions {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.cover-button {
  padding: 8px 16px;
  background-color: #f5f5f5;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
  font-size: 14px;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.cover-button:hover {
  background-color: #f0f0f0;
  border-color: #c0c0c0;
  transform: translateY(-1px);
}

.cover-button:active {
  transform: translateY(0);
}

.remove-button {
  background-color: #fff2f0;
  border-color: #ffccc7;
  color: #ff4d4f;
}

.remove-button:hover {
  background-color: #ffebe9;
  border-color: #ffa39e;
}

.cover-preview {
  margin-top: 15px;
  position: relative;
  transition: all 0.3s;
}

.cover-image {
  max-width: 100%;
  max-height: 300px;
  border-radius: 6px;
  border: 1px solid #f0f0f0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  display: block;
  transition: all 0.3s;
}

.cover-image:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

.cover-button::before {
  content: "🖼️";
  font-size: 16px;
}

.remove-button::before {
  content: "🗑️";
}

@media (max-width: 768px) {
  .cover-actions {
    flex-wrap: wrap;
  }
}
</style>