<template>
  <div class="category-tree-container">
    <div class="header">
      <span>分类树</span>
      <el-button type="primary" size="small" @click="$emit('add-root')">添加根分类</el-button>
    </div>
    <el-scrollbar height="calc(100vh - 250px)">
      <el-tree
        :data="categories"
        node-key="id"
        :props="defaultProps"
        :expand-on-click-node="false"
        :highlight-current="true"
        @node-click="$emit('node-click', $event)"
      >
        <template #default="{ node, data }">
          <span class="custom-tree-node">
            <span>{{ node.label }}</span>
            <span class="node-actions">
              <el-button link type="primary" size="small" @click.stop="$emit('add-child', data)">
                <el-icon><Plus /></el-icon>
              </el-button>
              <el-button link type="primary" size="small" @click.stop="$emit('edit', data)">
                <el-icon><Edit /></el-icon>
              </el-button>
              <el-button link type="danger" size="small" @click.stop="$emit('delete', data)">
                <el-icon><Delete /></el-icon>
              </el-button>
            </span>
          </span>
        </template>
      </el-tree>
    </el-scrollbar>
  </div>
</template>

<script setup>
import { Plus, Edit, Delete } from '@element-plus/icons-vue';

const props = defineProps({
  categories: {
    type: Array,
    required: true
  }
});
console.log(props.id)

const emit = defineEmits(['node-click', 'add-root', 'add-child', 'edit', 'delete']);

const defaultProps = {
  children: 'categoryList',
  label: 'categoryName'
};
</script>

<style lang="scss" scoped>
.category-tree-container {
  background-color: #fff;
  border-radius: 4px;
  padding: 10px;
  height: auto;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
    padding: 0 5px;
  }

  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    font-size: 14px;
    padding-right: 8px;

    .el-icon {
      margin-right: 5px;
    }

    .node-actions {
      margin-left: auto;
      opacity: 0;
      transition: opacity 0.3s;
    }
  }

  :deep(.el-tree-node__content:hover) {
    .node-actions {
      opacity: 1;
    }
  }
}
</style>
