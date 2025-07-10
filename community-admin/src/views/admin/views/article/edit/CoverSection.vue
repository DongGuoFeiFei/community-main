<template>
  <div class="cover-section">
    <div class="cover-container">
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
  </div>
</template>

<script setup>
const props=defineProps({
  coverUrl: {
    type: String,
    default: ''
  }
})

console.log(props.coverUrl)

const emit = defineEmits(['select', 'remove'])
</script>

<style scoped lang="scss">
.cover-section {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
  padding: 30px;
  background-color: #fafafa;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  }
}

.cover-container {
  width: 100%;
  max-width: 800px;
  text-align: center;
  padding: 30px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.03);
}

h3 {
  margin: 0 0 25px 0;
  font-size: 20px;
  color: #333;
  font-weight: 600;
  position: relative;
  display: inline-block;
  padding-bottom: 10px;

  &::after {
    content: "";
    position: absolute;
    bottom: 0;
    left: 50%;
    transform: translateX(-50%);
    width: 50px;
    height: 3px;
    background: linear-gradient(90deg, #1890ff, #36cfc9);
    border-radius: 3px;
  }
}

.cover-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-bottom: 25px;
}

.cover-button {
  padding: 10px 20px;
  background-color: #f5f5f5;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
  font-size: 14px;
  font-weight: 500;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);

  &:hover {
    background-color: #e6f7ff;
    color: #1890ff;
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(24, 144, 255, 0.2);
  }

  &:active {
    transform: translateY(0);
  }

  &::before {
    content: "🖼️";
    font-size: 16px;
  }
}

.remove-button {
  background-color: #fff2f0;
  color: #ff4d4f;

  &:hover {
    background-color: #ffebe9;
    box-shadow: 0 4px 8px rgba(255, 77, 79, 0.2);
  }

  &::before {
    content: "🗑️";
  }
}

.cover-preview {
  margin: 25px auto 0;
  max-width: 100%;
  position: relative;
  transition: all 0.3s;
}

.cover-image {
  max-width: 100%;
  max-height: 400px;
  border-radius: 8px;
  border: 1px solid #f0f0f0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  display: block;
  margin: 0 auto;
  transition: all 0.3s;

  &:hover {
    transform: scale(1.02);
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
  }
}

@media (max-width: 768px) {
  .cover-section {
    padding: 20px;
  }

  .cover-container {
    padding: 20px;
  }

  .cover-actions {
    flex-direction: column;
    align-items: center;
  }

  .cover-button {
    width: 100%;
    justify-content: center;
  }
}
</style>