<template>
  <div class="user-list">
    <div class="operation-bar">
      <div class="operation-buttons">
        <el-button type="danger" :disabled="!selectedUsers.length" @click="handleBatchDelete">
          批量删除
        </el-button>
        <el-button type="primary" @click="handleAddUser">
          添加用户
        </el-button>
      </div>
    </div>

    <el-table
        :data="users"
        border
        v-loading="loading"
        @selection-change="handleSelectionChange"
        style="width: 100%"
    >
      <el-table-column type="selection" width="55"/>
      <el-table-column prop="userId" label="ID" width="80"/>
      <el-table-column prop="username" label="用户名" width="120"/>
      <el-table-column prop="nickname" label="昵称" width="120"/>
      <el-table-column prop="email" label="邮箱" width="180"/>
      <el-table-column prop="phone" label="手机号" width="120"/>
      <el-table-column prop="createTime" label="注册时间" width="180">
        <template #default="{ row }">
          {{ formatDate(row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="激活" width="100">
        <template #default="{ row }">
          <el-tag :type="row.isActive === 1 ? 'success' : 'danger'">
            {{ row.isActive === 1 ? '正常' : '封禁' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-select
              placeholder="激活状态"
              clearable
              filterable
              style="width: 100px"
          >
            <el-option
                v-for="(label, value) in STATUS_ACTIVE"
                :key="value"
                :label="label"
                :value="Number(value)"
                @click="handleApproval(row,value)"
            />
          </el-select>

          <el-button size="small" type="danger" @click="handleDelete(row.userId)">
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
import {ref} from 'vue';
import {formatDate} from '@/utils/date.js';
import {STATUS_ACTIVE} from "@/utils/staticData.js";

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
  'active-change',
  "handle-add-user",
]);

const selectedUsers = ref([]);

const handleSelectionChange = (selection) => {
  selectedUsers.value = selection.map(user => user.id);
};

const handleEdit = (user) => {
  emit('edit', user);
};

const handleAddUser = () => {
  emit('handle-add-user')
}

const handleDelete = (userId) => {
  emit('delete', userId);
};

const handleBatchDelete = () => {
  emit('batch-delete', selectedUsers.value);
};

const handlePageChange = (page) => {
  emit('page-change', page);
};
const handleApproval = (row, status) => {
  emit('active-change', row, status)
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
    display: flex;
    justify-content: flex-end; /* 将内容靠右对齐 */

    .operation-buttons {
      display: flex;
      gap: 10px; /* 按钮之间的间距 */
    }
  }

  .pagination {
    padding: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
