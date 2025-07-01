

<template>
  <div class="user-management">
    <UserSearch @search="handleSearch" />
    <UserList
        :users="users"
        :loading="loading"
        :pagination="pagination"
        @edit="handleEdit"
        @delete="handleDelete"
        @batch-delete="handleBatchDelete"
        @page-change="handlePageChange"
    />
    <UserDetailDialog
        v-model="dialogVisible"
        :user="currentUser"
        @confirm="handleConfirm"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getUsers, deleteUser, batchDeleteUsers, updateUser } from '@/api/userList.js';
import UserSearch from "@/views/admin/views/user/components/UserSearch.vue";
import UserList from "@/views/admin/views/user/components/UserList.vue";
import UserDetailDialog from "@/views/admin/views/user/components/UserDetailDialog.vue";


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
    users.value = res.data.list;
    pagination.value.total = res.data.total;
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
  currentUser.value = { ...user };
  dialogVisible.value = true;
};

// 删除用户
const handleDelete = async (userId) => {
  try {
    await deleteUser(userId);
    await fetchUsers();
  } catch (error) {
    console.error('删除用户失败:', error);
  }
};

// 批量删除用户
const handleBatchDelete = async (userIds) => {
  try {
    await batchDeleteUsers(userIds);
    await fetchUsers();
  } catch (error) {
    console.error('批量删除用户失败:', error);
  }
};

// 分页变化
const handlePageChange = (page) => {
  pagination.value.current = page;
  fetchUsers();
};

// 确认更新用户
const handleConfirm = async (userData) => {
  try {
    await updateUser(userData.id, userData);
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