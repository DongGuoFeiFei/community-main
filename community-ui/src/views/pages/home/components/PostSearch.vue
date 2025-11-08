<script setup lang="ts">
defineProps<{
  title: string
}>()

const emit = defineEmits<{
  (e: 'update:title', value: string): void
  (e: 'search'): void
}>()

const handleSearch = (): void => {
  emit('search')
}

const handleClear = (): void => {
  emit('update:title', '')
  emit('search')
}
</script>

<template>
  <div class="post-search">
    <el-input
        :model-value="title"
        @update:model-value="val => $emit('update:title', val)"
        placeholder="搜索文章标题"
        prefix-icon="ep:search"
        @keyup.enter="handleSearch"
        clearable
        @clear="handleClear"
        class="search-input"
    />
  </div>
</template>

<style scoped lang="scss">
.post-search {
  margin-bottom: 20px;
  position: relative;
  animation: bounceIn 0.6s cubic-bezier(0.34, 1.56, 0.64, 1);

  // 漫画风装饰星星
  &::before {
    content: '✨';
    position: absolute;
    left: -30px;
    top: 50%;
    transform: translateY(-50%) rotate(-15deg);
    font-size: 24px;
    animation: twinkle 2s infinite ease-in-out;
  }

  &::after {
    content: '⭐';
    position: absolute;
    right: -30px;
    top: 50%;
    transform: translateY(-50%) rotate(15deg);
    font-size: 20px;
    animation: twinkle 2.5s infinite ease-in-out;
  }

  :deep(.search-input) {
    width: 100%;
    height: 50px;

    .el-input__wrapper {
      border-radius: 25px !important; // 大圆角
      background: rgba(255, 255, 255, 0.85);
      backdrop-filter: blur(10px);
      border: 3px solid rgba(135, 206, 235, 0.3);
      box-shadow: 
        0 8px 20px rgba(135, 206, 235, 0.2),
        inset 0 2px 8px rgba(255, 255, 255, 0.5);
      transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
      padding: 5px 20px;

      &:hover {
        transform: translateY(-3px) scale(1.02);
        box-shadow: 
          0 12px 28px rgba(135, 206, 235, 0.3),
          inset 0 2px 8px rgba(255, 255, 255, 0.6),
          0 0 20px rgba(135, 206, 235, 0.4); // 外发光
        border-color: rgba(135, 206, 235, 0.6);
      }

      &.is-focus {
        transform: scale(1.03);
        box-shadow: 
          0 0 0 4px rgba(135, 206, 235, 0.2),
          0 12px 28px rgba(135, 206, 235, 0.3),
          inset 0 2px 8px rgba(255, 255, 255, 0.7),
          0 0 30px rgba(135, 206, 235, 0.5);
        border-color: #87CEEB;
      }
    }

    .el-input__inner {
      font-size: 15px;
      color: #555;
      font-weight: 500;
      
      &::placeholder {
        color: #9370DB;
        font-weight: 400;
      }
    }

    .el-input__prefix {
      font-size: 20px;
      color: #87CEEB;
    }
  }
}

@keyframes bounceIn {
  0% {
    opacity: 0;
    transform: scale(0.3) rotate(-5deg);
  }
  50% {
    opacity: 1;
    transform: scale(1.05) rotate(1deg);
  }
  70% {
    transform: scale(0.95) rotate(-0.5deg);
  }
  100% {
    transform: scale(1) rotate(0deg);
  }
}

@keyframes twinkle {
  0%, 100% {
    opacity: 0.6;
    transform: translateY(-50%) rotate(-15deg) scale(1);
  }
  50% {
    opacity: 1;
    transform: translateY(-50%) rotate(-15deg) scale(1.3);
  }
}
</style>