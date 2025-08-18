<template>
  <div class="category-list-container">
    <div class="header">
      <h2>文章分类管理</h2>
      <el-button type="primary" @click="handleAdd">添加分类</el-button>
    </div>

    <!-- 搜索和筛选区域 -->
    <div class="search-area">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="分类名称">
          <el-input
            v-model="searchForm.keyword"
            placeholder="请输入分类名称"
            clearable
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="状态">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            @change="handleSearch"
          >
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 表格区域 -->
    <el-table
      :data="tableData"
      row-key="category_id"
      border
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column prop="category_name" label="分类名称" width="180" />
      <el-table-column prop="category_slug" label="URL标识" width="180" />
      <el-table-column prop="article_count" label="文章数量" width="100" />
      <el-table-column prop="sort_order" label="排序" width="100">
        <template #default="{ row }">
          <el-input-number
            v-model="row.sort_order"
            :min="0"
            size="small"
            @change="handleSortChange(row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-switch
            v-model="row.status"
            :active-value="1"
            :inactive-value="0"
            @change="handleStatusChange(row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="created_at" label="创建时间" width="180">
        <template #default="{ row }">
          {{ formatDate(row.created_at) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页区域 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 表单对话框 -->
    <category-form
      v-model="formVisible"
      :form-data="currentCategory"
      :mode="formMode"
      @success="handleFormSuccess"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getCategories, deleteCategory, updateCategory } from '@/api/category';
import { formatDate } from '@/utils/date';
import CategoryForm from './CategoryForm.vue';

// 表格数据
const tableData = ref([]);
const loading = ref(false);

// 搜索表单
const searchForm = ref({
  keyword: '',
  status: ''
});

// 分页数据
const pagination = ref({
  current: 1,
  size: 10,
  total: 0
});

// 表单相关
const formVisible = ref(false);
const formMode = ref('add');
const currentCategory = ref({});

// 获取分类列表
const fetchCategories = async () => {
  try {
    loading.value = true;
    const params = {
      page: pagination.value.current,
      size: pagination.value.size,
      ...searchForm.value
    };

    const { data } = await getCategories(params);
    tableData.value = data.list || [];
    pagination.value.total = data.total || 0;
  } catch (error) {
    ElMessage.error(error.message || '获取分类列表失败');
  } finally {
    loading.value = false;
  }
};

// 搜索处理
const handleSearch = () => {
  pagination.value.current = 1; // 重置到第一页
  fetchCategories();
};

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    keyword: '',
    status: ''
  };
  handleSearch();
};

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.value.size = size;
  fetchCategories();
};

// 当前页变化
const handleCurrentChange = (current) => {
  pagination.value.current = current;
  fetchCategories();
};

// 添加分类
const handleAdd = () => {
  formMode.value = 'add';
  currentCategory.value = {
    category_name: '',
    category_slug: '',
    parent_id: null,
    description: '',
    cover_url: '',
    sort_order: 0,
    status: 1
  };
  formVisible.value = true;
};

// 编辑分类
const handleEdit = (row) => {
  formMode.value = 'edit';
  currentCategory.value = { ...row };
  formVisible.value = true;
};

// 删除分类
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除分类 "${row.category_name}" 吗?`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });

    await deleteCategory(row.category_id);
    ElMessage.success('删除成功');
    fetchCategories();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败');
    }
  }
};

// 状态变更
const handleStatusChange = async (row) => {
  try {
    await updateCategory({
      category_id: row.category_id,
      status: row.status
    });
    ElMessage.success('状态更新成功');
  } catch (error) {
    ElMessage.error(error.message || '状态更新失败');
    // 恢复原状态
    row.status = row.status === 1 ? 0 : 1;
  }
};

// 排序变更
const handleSortChange = async (row) => {
  try {
    await updateCategory({
      category_id: row.category_id,
      sort_order: row.sort_order
    });
    ElMessage.success('排序更新成功');
  } catch (error) {
    ElMessage.error(error.message || '排序更新失败');
  }
};

// 表单提交成功回调
const handleFormSuccess = () => {
  // 如果是第一页或者新增操作，保持在第一页
  if (formMode.value === 'add' || pagination.value.current === 1) {
    fetchCategories();
  } else {
    // 如果是编辑操作且不在第一页，保持当前页
    pagination.value.current = 1;
    fetchCategories();
  }
};

onMounted(() => {
  fetchCategories();
});
</script>

<style lang="scss" scoped>
.category-list-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }

  .search-area {
    margin-bottom: 20px;
    padding: 20px;
    background-color: #f5f7fa;
    border-radius: 4px;

    .search-form {
      display: flex;
      flex-wrap: wrap;
      gap: 16px; // 统一设置间距

      .el-form-item {
        margin-right: 0; // 清除默认右边距

        .el-input, .el-select {
          width: 140px; // 可以设置不同的宽度
        }
      }
    }
  }

  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
