<template>
  <div class="category-tree-container">
    <el-tree
      :data="treeData"
      node-key="category_id"
      :props="defaultProps"
      :expand-on-click-node="false"
      :highlight-current="true"
      :default-expand-all="true"
      @node-click="handleNodeClick"
    >
      <template #default="{ node, data }">
        <span class="custom-tree-node">
          <span>{{ node.label }}</span>
          <span class="node-actions">
            <el-button size="small" type="primary" @click.stop="handleEdit(data)">编辑</el-button>
            <el-button size="small" type="danger" @click.stop="handleDelete(data)">删除</el-button>
          </span>
        </span>
      </template>
    </el-tree>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getCategoryTree, deleteCategory } from '@/api/category';

const emit = defineEmits(['node-click', 'edit', 'delete']);

const treeData = ref([]);
const loading = ref(false);

const defaultProps = {
  children: 'children',
  label: 'category_name'
};

// 获取分类树
const fetchCategoryTree = async () => {
  try {
    loading.value = true;
    const { data } = await getCategoryTree();
    treeData.value = data;
  } catch (error) {
    ElMessage.error(error.message || '获取分类树失败');
  } finally {
    loading.value = false;
  }
};

// 节点点击事件
const handleNodeClick = (data) => {
  emit('node-click', data);
};

// 编辑节点
const handleEdit = (data) => {
  emit('edit', data);
};

// 删除节点
const handleDelete = async (data) => {
  try {
    await ElMessageBox.confirm(`确定要删除分类 "${data.category_name}" 吗?`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });

    await deleteCategory(data.category_id);
    ElMessage.success('删除成功');
    fetchCategoryTree();
    emit('delete', data);
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败');
    }
  }
};

onMounted(() => {
  fetchCategoryTree();
});
</script>

<style lang="scss" scoped>
.category-tree-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);

  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;

    .node-actions {
      margin-left: 10px;
    }
  }

  :deep(.el-tree-node__content) {
    height: 40px;
  }
}
</style>
