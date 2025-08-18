<template>
  <div class="category-management">
    <!-- 搜索区域 -->
    <div class="search-area">
      <el-form :inline="true" @submit.prevent class="search-form">
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
            <el-option label="启用" :value="1"/>
            <el-option label="禁用" :value="0"/>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-row :gutter="10">
      <el-col :span="6">
        <category-tree
          :categories="treeData"
          @add-root="handleAddRoot"
          @add-child="handleAddChild"
          @edit="handleTreeEdit"
          @delete="handleTreeDelete"
        />
      </el-col>
      <el-col :span="18">
        <category-list
          :categories="categories"
          :loading="loading"
          @add="handleAddRoot"
          @edit="handleEdit"
          @delete="handleDelete"
          @status-change="handleStatusChange"
          @sort-change="handleSortChange"
        />
      </el-col>
    </el-row>

    <!-- 分页区域 - 移动到页面底部 -->
    <div class="pagination-area">
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

    <category-form
      v-model="formVisible"
      :form-data="currentCategory"
      :mode="formMode"
      @submit="handleFormSubmit"
    />
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import {ElMessage, ElMessageBox} from 'element-plus';
import {
  addCategory,
  deleteCategory,
  getCategories,
  getCategoryTree,
  updateCategory
} from '@/api/category';
import CategoryList from './components/CategoryList.vue';
import CategoryTree from './components/CategoryTree.vue';
import CategoryForm from './components/CategoryForm.vue';

// 列表数据
const categories = ref([]);
const treeData = ref([]);
const loading = ref(false);

// 搜索和分页
const searchForm = ref({
  keyword: '',
  status: ''
});
const pagination = ref({
  current: 1,
  size: 10,
  total: 0
});

// 表单相关
const formVisible = ref(false);
const formMode = ref('add');
const currentCategory = ref({});

// 初始化加载数据
const fetchData = async () => {
  try {
    loading.value = true;

    // 并行获取列表和树形数据
    const [listRes, treeRes] = await Promise.all([
      getCategories({
        page: pagination.value.current,
        size: pagination.value.size,
        ...searchForm.value
      }),
      getCategoryTree()
    ]);

    categories.value = listRes.data.rows || [];
    pagination.value.total = listRes.data.total || 0;
    treeData.value = treeRes.data || [];
  } catch (error) {
    ElMessage.error(error.message || '获取数据失败');
  } finally {
    loading.value = false;
  }
};

// 搜索处理
const handleSearch = () => {
  pagination.value.current = 1;
  fetchData();
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
  fetchData();
};

// 当前页变化
const handleCurrentChange = (current) => {
  pagination.value.current = current;
  fetchData();
};

// 添加根分类
const handleAddRoot = () => {
  formMode.value = 'add';
  currentCategory.value = {
    categoryName: '',
    categorySlug: '',
    parentId: null,
    description: '',
    coverUrl: '',
    sortOrder: 0,
    status: 1
  };
  formVisible.value = true;
};

// 添加子分类
const handleAddChild = (parent) => {
  formMode.value = 'add';
  currentCategory.value = {
    categoryName: '',
    categorySlug: '',
    parentId: parent.id,
    description: '',
    coverUrl: '',
    sortOrder: 0,
    status: 1
  };
  formVisible.value = true;
};

// 编辑分类
const handleEdit = (category) => {
  formMode.value = 'edit';
  currentCategory.value = {...category};
  formVisible.value = true;
};

// 树形组件编辑
const handleTreeEdit = (category) => {
  handleEdit(category);
};

// 删除分类
const handleDelete = async (category) => {
  await confirmDelete(category);
};

// 树形组件删除
const handleTreeDelete = async (category) => {
  await confirmDelete(category);
};

// 确认删除
const confirmDelete = async (category) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除分类 "${category.categoryName}" 吗?`,
      '提示',
      {type: 'warning'}
    );

    await deleteCategory(category.id);
    ElMessage.success('删除成功');
    fetchData();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败');
    }
  }
};

// 状态变更
const handleStatusChange = async (category) => {
  try {
    await updateCategory({
      categoryId: category.id,
      status: category.status
    });
    ElMessage.success('状态更新成功');
  } catch (error) {
    ElMessage.error(error.message || '状态更新失败');
    // 恢复原状态
    category.status = category.status === 1 ? 0 : 1;
  }
};

// 排序变更
const handleSortChange = async (category) => {
  try {
    await updateCategory({
      categoryId: category.id,
      sortOrder: category.sortOrder
    });
    ElMessage.success('排序更新成功');
  } catch (error) {
    ElMessage.error(error.message || '排序更新失败');
  }
};

// 表单提交
const handleFormSubmit = async (formData) => {
  try {
    if (formMode.value === 'add') {
      await addCategory(formData);
      ElMessage.success('添加成功');
    } else {
      await updateCategory(formData);
      ElMessage.success('更新成功');
    }

    formVisible.value = false;
    fetchData();
  } catch (error) {
    ElMessage.error(error.message || '操作失败');
  }
};

// 初始化加载
onMounted(() => {
  fetchData();
});
</script>

<style lang="scss" scoped>
.category-management {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  min-height: calc(100vh - 40px); // 确保页面有足够高度

  .search-area {
    margin-bottom: 20px;
    padding: 20px;
    background-color: #f5f7fa;
    border-radius: 4px;

    .search-form {
      display: flex;
      flex-wrap: wrap;
      gap: 16px;

      .el-form-item {
        margin-right: 0;

        .el-input, .el-select {
          width: 160px;
        }
      }
    }
  }

  .pagination-area {
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #eee;
    display: flex;
    justify-content: flex-end;
  }

  .el-row {
    flex: 1; // 让内容区域占据剩余空间
    margin-bottom: 20px;
  }
}
</style>
