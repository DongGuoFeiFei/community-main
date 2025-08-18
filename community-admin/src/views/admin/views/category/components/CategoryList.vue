<template>
  <div class="category-list-container">
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
      <el-table-column prop="sortOrder" label="排序" width="145">
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
  </div>
</template>

<script setup>
const props = defineProps({
  categories: {
    type: Array,
    required: true
  },
  loading: {
    type: Boolean,
    default: false
  }
});

defineEmits([
  'add',
  'edit',
  'delete',
  'status-change',
  'sort-change'
]);
</script>

<style lang="scss" scoped>
.category-list-container {
  height: 100%;
}
</style>
