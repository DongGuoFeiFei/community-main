

<template>
  <div class="user-list">
    <div class="operation-bar">
      <el-button type="danger" :disabled="!selectedUsers.length" @click="handleBatchDelete">
        批量删除
      </el-button>
    </div>

    <el-table
        :data="users"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        style="width: 100%"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="nickname" label="昵称" width="120" />
      <el-table-column prop="email" label="邮箱" width="180" />
      <el-table-column prop="phone" label="手机号" width="120" />
      <el-table-column prop="createTime" label="注册时间" width="180">
        <template #default="{ row }">
          {{ formatDate(row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row.id)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
          v-model:current-page="pagination.current"
          :page-size="pagination.pageSize"
          :total="pagination.total"
          layout="total, prev, pager, next, jumper"
          @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { formatDate } from '@/utils/date.js';

const props = defineProps({
  users: {
    type: Array,
    default: () => [],
  },
  loading: {
    type: Boolean,
    default: false,
  },
  pagination: {
    type: Object,
    default: () => ({
      current: 1,
      pageSize: 10,
      total: 0,
    }),
  },
});

const emit = defineEmits([
  'edit',
  'delete',
  'batch-delete',
  'page-change',
]);

const selectedUsers = ref([]);

const handleSelectionChange = (selection) => {
  selectedUsers.value = selection.map(user => user.id);
};

const handleEdit = (user) => {
  emit('edit', user);
};

const handleDelete = (userId) => {
  emit('delete', userId);
};

const handleBatchDelete = () => {
  emit('batch-delete', selectedUsers.value);
};

const handlePageChange = (page) => {
  emit('page-change', page);
};
</script>

<style lang="scss" scoped>
.user-list {
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);

  .operation-bar {
    padding: 10px 20px;
    border-bottom: 1px solid #ebeef5;
  }

  .pagination {
    padding: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>