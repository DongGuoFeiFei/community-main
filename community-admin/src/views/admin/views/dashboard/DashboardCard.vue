<template>
  <el-card shadow="hover" class="dashboard-card" :style="cardStyle">
    <div class="card-content">
      <div class="card-icon" :style="iconStyle">
        <el-icon :size="24">
          <component :is="icon" />
        </el-icon>
      </div>
      <div class="card-info">
        <div class="card-title">{{ title }}</div>
        <div class="card-value">{{ value }}</div>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  title: String,
  value: [String, Number],
  icon: String,
  color: String
});

const cardStyle = computed(() => {
  if (!props.color) return {};
  return {
    '--el-color-primary': `var(--el-color-${props.color})`,
    '--el-color-primary-light-9': `var(--el-color-${props.color}-light-9)`
  };
});

const iconStyle = computed(() => {
  if (!props.color) return {};
  return {
    backgroundColor: `var(--el-color-${props.color}-light-9)`,
    color: `var(--el-color-${props.color})`
  };
});
</script>

<style lang="scss" scoped>
.dashboard-card {
  :deep(.el-card__body) {
    padding: 20px;
  }

  .card-content {
    display: flex;
    align-items: center;
  }

  .card-icon {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 48px;
    height: 48px;
    border-radius: 8px;
    margin-right: 16px;
    background-color: var(--el-color-primary-light-9);
    color: var(--el-color-primary);
  }

  .card-info {
    flex: 1;

    .card-title {
      font-size: 14px;
      color: var(--el-text-color-secondary);
      margin-bottom: 4px;
    }

    .card-value {
      font-size: 24px;
      font-weight: bold;
      color: var(--el-text-color-primary);
    }
  }
}
</style>