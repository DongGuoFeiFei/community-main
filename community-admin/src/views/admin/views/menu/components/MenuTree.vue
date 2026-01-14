<template>
  <div class="menu-tree-container">
    <div class="header">
      <span>菜单树</span>
      <el-button type="primary" size="small" @click="handleAddRoot">添加根菜单</el-button>
    </div>
    <el-scrollbar height="calc(100vh - 180px)">
      <el-tree
        ref="treeRef"
        :data="menus"
        node-key="menuId"
        :props="defaultProps"
        :expand-on-click-node="false"
        :highlight-current="true"
        @node-click="handleNodeClick"
      >
        <template #default="{ node, data }">
          <span class="custom-tree-node">
            <el-icon v-if="data.icon && data.icon !== '#'" :size="16">
              <component :is="data.icon" />
            </el-icon>
            <span>{{ node.label }}</span>
            <span class="node-actions">
              <el-button link type="primary" size="small" @click.stop="handleAddChild(data)">
                <el-icon><Plus /></el-icon>
              </el-button>
<!--              <el-button link type="primary" size="small" @click.stop="handleEdit(data)">-->
<!--                <el-icon><Edit /></el-icon>-->
<!--              </el-button>-->
            </span>
          </span>
        </template>
      </el-tree>
    </el-scrollbar>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import {Edit, Plus} from "@element-plus/icons-vue";

const props = defineProps({
  menus: {
    type: Array,
    required: true
  }
})

const emit = defineEmits(['node-click', 'add', 'edit'])

console.log(props.menus)

const defaultProps = {
  children: 'children',
  label: 'menuName'
}

const treeRef = ref(null)

const handleNodeClick = (data) => {
  emit('node-click', data)
}

const handleAddRoot = () => {
  emit('add', 0)
}

const handleAddChild = (data) => {
  emit('add', data.menuId)
}

const handleEdit = (data) => {
  emit('edit', data)
}
</script>

<style lang="scss" scoped>
.menu-tree-container {
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
