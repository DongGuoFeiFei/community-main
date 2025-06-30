<template>
  <div class="quick-actions">
    <h3 class="section-title">快速操作</h3>

    <div class="action-buttons">
      <el-button
          v-for="action in actions"
          :key="action.name"
          :type="action.type"
          :icon="action.icon"
          @click="emitAction(action.name)"
      >
        {{ action.label }}
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { defineEmits } from 'vue';

const emit = defineEmits(['action']);

const actions = [
  { name: 'clearCache', label: '清除缓存', icon: 'Delete', type: 'danger' },
  { name: 'backup', label: '数据备份', icon: 'Upload', type: 'warning' },
  { name: 'refresh', label: '刷新数据', icon: 'Refresh', type: 'primary' },
  { name: 'settings', label: '系统设置', icon: 'Setting', type: '' }
];

const emitAction = (actionName) => {
  emit('action', actionName);
};
</script>

<style lang="scss" scoped>
.quick-actions {
  .section-title {
    margin-top: 0;
    margin-bottom: 20px;
    font-size: 18px;
    color: var(--el-text-color-primary);
  }

  .action-buttons {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
    gap: 10px;

    .el-button {
      width: 100%;
    }
  }
}
</style>