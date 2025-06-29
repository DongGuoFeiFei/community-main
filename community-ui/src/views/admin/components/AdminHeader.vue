<template>
  <div class="admin-header">
    <div class="left">
      <el-icon @click="toggleCollapse">
        <Fold v-if="!isCollapse" />
        <Expand v-else />
      </el-icon>
      <span class="title">后台管理系统</span>
    </div>
    <!-- 其他头部内容 -->
  </div>
</template>

<script setup>
import { Fold, Expand } from '@element-plus/icons-vue';

const emit = defineEmits(['toggle-collapse']);

const toggleCollapse = () => {
  emit('toggle-collapse');
};
</script>

<style lang="scss" scoped>
.admin-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 20px;

  .left {
    display: flex;
    align-items: center;

    .el-icon {
      font-size: 20px;
      margin-right: 15px;
      cursor: pointer;

      &:hover {
        color: var(--el-color-primary);
      }
    }

    .title {
      font-size: 18px;
      font-weight: 500;
    }
  }
}
</style>