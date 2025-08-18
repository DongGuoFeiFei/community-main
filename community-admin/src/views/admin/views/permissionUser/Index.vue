<template>
  <div class="user-role-management">
    <div class="header">
      <h2>用户角色管理</h2>
    </div>

    <UserSearch
      @search="handleSearch"
      :roles="roles"
    />

    <div class="content">
      <UserList
        :users="userData.rows"
        :loading="loading"
        :total="userData.total"
        :page="pagination.page"
        :size="pagination.size"
        @page-change="handlePageChange"
        @size-change="handleSizeChange"
        @role-assignment="handleRoleAssignment"
      />
    </div>

    <el-dialog
      v-model="dialogVisible"
      title="用户角色分配"
      width="650px"
      :close-on-click-modal="false"
    >
      <UserRoleAssignment
        v-if="dialogVisible"
        :user="selectedUser"
        :roles="roles"
        @update-roles="handleUpdateRoles"
      />
    </el-dialog>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'

import {getRoleList, getUserList, updateUserRoles} from '@/api/userRole'
import {ElMessage} from 'element-plus'
import UserSearch from "@/views/admin/views/permissionUser/components/UserSearch.vue";
import UserList from "@/views/admin/views/permissionUser/components/UserList.vue";
import UserRoleAssignment
  from "@/views/admin/views/permissionUser/components/UserRoleAssignment.vue";

const loading = ref(false)
const userData = ref({
  rows: [],
  total: 0
})
const roles = ref([])
const selectedUser = ref(null)
const searchParams = ref({})
const pagination = ref({
  page: 1,
  size: 10
})
const dialogVisible = ref(false)

// 获取用户列表
const fetchUsers = async () => {
  try {
    loading.value = true
    const params = {
      ...searchParams.value,
      page: pagination.value.page,
      size: pagination.value.size
    }
    const res = await getUserList(params)
    userData.value = res.data
  } catch (error) {
    ElMessage.error('获取用户列表失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

// 获取角色列表
const fetchRoles = async () => {
  try {
    const res = await getRoleList()
    roles.value = res.data
  } catch (error) {
    ElMessage.error('获取角色列表失败: ' + error.message)
  }
}

// 处理搜索
const handleSearch = (params) => {
  searchParams.value = params
  pagination.value.page = 1
  fetchUsers()
}

// 处理分页变化
const handlePageChange = (newPage) => {
  pagination.value.page = newPage
  fetchUsers()
}

// 处理每页数量变化
const handleSizeChange = (newSize) => {
  pagination.value.size = newSize
  pagination.value.page = 1
  fetchUsers()
}

// 处理角色分配
const handleRoleAssignment = async (user) => {
  selectedUser.value = user
  dialogVisible.value = true
}

// 处理角色更新
const handleUpdateRoles = async (newRoles) => {
  try {
    const data = ref({
      id: selectedUser.value.userId,
      ids: newRoles
    })
    await updateUserRoles(data.value)
    ElMessage.success('角色更新成功')
    dialogVisible.value = false
    fetchUsers() // 刷新列表
  } catch (error) {
    ElMessage.error('更新角色失败: ' + error.message)
  }
}

onMounted(() => {
  fetchUsers()
  fetchRoles()
})
</script>

<style lang="scss" scoped>
.user-role-management {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .header {
    margin-bottom: 20px;

    h2 {
      margin: 0;
      font-size: 18px;
      color: #303133;
    }
  }

}
</style>
