<template>
  <div class="user-management">
    <UserSearch @search="handleSearch"/>

    <UserList
        :users="users"
        :loading="loading"
        :pagination="pagination"
        @edit="handleEdit"
        @delete="handleDelete"
        @batch-delete="handleBatchDelete"
        @page-change="handlePageChange"
        @active-change="handleActiveChange"
    />

    <UserDetailDialog
        v-model="dialogVisible"
        :user="currentUser"
        @confirm="handleConfirm"
    />

  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import {activeChange, batchDeleteUsers, deleteUser, getUsers, updateUser} from '@/api/userList.js';
import UserSearch from "@/views/admin/views/user/components/UserSearch.vue";
import UserList from "@/views/admin/views/user/components/UserList.vue";
import UserDetailDialog from "@/views/admin/views/user/components/UserDetailDialog.vue";
import {ElMessage, ElMessageBox} from "element-plus";
// todo 完善修改框，修改数据内容

const users = ref([]);
const loading = ref(false);
const dialogVisible = ref(false);
const currentUser = ref(null);

// 分页参数
const pagination = ref({
  current: 1,
  pageSize: 10,
  total: 0,
});

// 搜索参数
const searchParams = ref({});

// 获取用户列表
const fetchUsers = async () => {
  try {
    loading.value = true;
    const params = {
      page: pagination.value.current,
      size: pagination.value.pageSize,
      ...searchParams.value,
    };
    const res = await getUsers(params);
    users.value = res.data.rows;
    pagination.value.total = res.data.total;

    console.log(users.value)
  } catch (error) {
    console.error('获取用户列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 搜索处理
const handleSearch = (params) => {
  searchParams.value = params;
  pagination.value.current = 1;
  fetchUsers();
};

// 编辑用户
const handleEdit = (user) => {
  currentUser.value = {...user};
  console.log(currentUser.value)
  dialogVisible.value = true;
};

// 删除用户
const handleDelete = async (userId) => {

  ElMessageBox.confirm(`确定删除用户吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteUser(userId);
      await fetchUsers();
    } catch (error) {
      console.error('失败:', error);
      ElMessage.error('失败');
    }
  }).catch(() => {
  });
};

// 批量删除用户
const handleBatchDelete = async (userIds) => {

  ElMessageBox.confirm(`确定删除用户吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await batchDeleteUsers(userIds);
      await fetchUsers();
    } catch (error) {
      console.error('失败:', error);
      ElMessage.error('失败');
    }
  }).catch(() => {
  });
};

// 分页变化
const handlePageChange = (page) => {
  pagination.value.current = page;
  fetchUsers();
};

const handleActiveChange = (row, status) => {
  // 处理逻辑
  console.log(row, status);
  ElMessageBox.confirm(`确定修改用户 "${row.username}" 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const data = ref({
        id: row.userId,
        status: status
      })
      await activeChange(data.value);
      ElMessage.success('成功');
      fetchUsers();
    } catch (error) {
      console.error('失败:', error);
      ElMessage.error('失败');
    }
  }).catch(() => {
  });
};

// 确认更新用户
const handleConfirm = async (userData) => {
  console.log(userData)
  try {
    await updateUser(userData.userId, userData);
    dialogVisible.value = false;
    await fetchUsers();
  } catch (error) {
    console.error('更新用户失败:', error);
  }
};

onMounted(() => {
  fetchUsers();
});
</script>

<style lang="scss" scoped>
.user-management {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}
</style>