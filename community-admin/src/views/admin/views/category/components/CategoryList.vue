<template>
  <div class="category-list-container">
    <div class="header">
      <h2>文章分类管理</h2>
      <el-button type="primary" @click="$emit('add')">添加分类</el-button>
    </div>

    <!-- 搜索和筛选区域 -->
    <div class="search-area">
      <el-form :inline="true" @submit.prevent class="search-form">
        <el-form-item label="分类名称">
          <el-input
            v-model="search.keyword"
            placeholder="请输入分类名称"
            clearable
            @clear="handleSearch"
            @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="状态">
          <el-select
            v-model="search.status"
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

    <!-- 表格区域 -->
    <el-table
      :data="categories"
      row-key="category_id"
      border
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column prop="categoryName" label="分类名称" width="180"/>
      <el-table-column prop="categorySlug" label="URL标识" width="180"/>
      <el-table-column prop="articleCount" label="文章数量" width="100"/>
      <el-table-column prop="sortOrder" label="排序" width="100">
        <template #default="{ row }">
          <el-input-number
            v-model="row.sortOrder"
            :min="0"
            size="small"
            @change="$emit('sort-change', row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-switch
            v-model="row.status"
            :active-value="1"
            :inactive-value="0"
            @change="$emit('status-change', row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" width="180"/>
      <el-table-column label="操作" width="180" fixed="right" >
        <template #default="{ row }">
          <el-button size="small" @click="$emit('edit', row)">编辑</el-button>
          <el-button size="small" type="danger" @click="$emit('delete', row)">删除</el-button>
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
  </div>
</template>

<script setup>
import {ref, watch} from 'vue';

const props = defineProps({
  categories: {
    type: Array,
    required: true
  },
  loading: {
    type: Boolean,
    default: false
  },
  pagination: {
    type: Object,
    required: true
  }
});

const emit = defineEmits([
  'search',
  'add',
  'edit',
  'delete',
  'status-change',
  'sort-change'
]);

// 搜索表单
const search = ref({
  keyword: '',
  status: ''
});

// 分页数据
const pagination = ref({
  current: 1,
  size: 10,
  total: 0
});

// 监听父组件分页变化
watch(() => props.pagination, (val) => {
  pagination.value = {...val};
}, {deep: true});

// 搜索处理
const handleSearch = () => {
  emit('search', {...search.value});
};

// 重置搜索
const resetSearch = () => {
  search.value = {
    keyword: '',
    status: ''
  };
  handleSearch();
};

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.value.size = size;
  emit('search', {...search.value});
};

// 当前页变化
const handleCurrentChange = (current) => {
  pagination.value.current = current;
  emit('search', {...search.value});
};
</script>

<style lang="scss" scoped>
.category-list-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;

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
          width: 160px; // 可以设置不同的宽度
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
