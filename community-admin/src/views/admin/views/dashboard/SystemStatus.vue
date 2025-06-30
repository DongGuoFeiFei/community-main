<template>
  <div class="system-status">
    <h3 class="section-title">系统状态</h3>

    <div class="status-item">
      <div class="status-label">CPU 使用率</div>
      <el-progress
          :percentage="cpu"
          :color="getStatusColor(cpu)"
          :show-text="false"
      />
      <div class="status-value">{{ cpu }}%</div>
    </div>

    <div class="status-item">
      <div class="status-label">内存使用率</div>
      <el-progress
          :percentage="memory"
          :color="getStatusColor(memory)"
          :show-text="false"
      />
      <div class="status-value">{{ memory }}%</div>
    </div>

    <div class="status-item">
      <div class="status-label">磁盘使用率</div>
      <el-progress
          :percentage="disk"
          :color="getStatusColor(disk)"
          :show-text="false"
      />
      <div class="status-value">{{ disk }}%</div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  cpu: {
    type: Number,
    default: 0
  },
  memory: {
    type: Number,
    default: 0
  },
  disk: {
    type: Number,
    default: 0
  }
});

const getStatusColor = (value) => {
  if (value > 80) return '#f56c6c';
  if (value > 60) return '#e6a23c';
  return '#67c23a';
};
</script>

<style lang="scss" scoped>
.system-status {
  .section-title {
    margin-top: 0;
    margin-bottom: 20px;
    font-size: 18px;
    color: var(--el-text-color-primary);
  }

  .status-item {
    margin-bottom: 15px;

    .status-label {
      font-size: 14px;
      margin-bottom: 5px;
      color: var(--el-text-color-secondary);
    }

    .status-value {
      font-size: 12px;
      text-align: right;
      margin-top: 3px;
      color: var(--el-text-color-secondary);
    }

    :deep(.el-progress-bar) {
      padding-right: 0;
      margin-right: 0;
    }
  }
}
</style>