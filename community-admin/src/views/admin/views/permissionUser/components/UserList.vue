<template>
  <div class="user-list-container">
    <el-table :data="users" style="width: 100%" v-loading="loading" border>
      <el-table-column prop="userId" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" width="120" show-overflow-tooltip />
      <el-table-column prop="nickname" label="昵称" width="120" show-overflow-tooltip />
      <el-table-column prop="email" label="邮箱" width="180" show-overflow-tooltip />
      <el-table-column prop="phone" label="电话" width="120" show-overflow-tooltip />
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column prop="isActive" label="状态" width="90" align="center">
        <template #default="{ row }">
          <el-tag :type="row.isActive === '1' ? 'success' : 'danger'" size="small">
            {{ row.isActive === '1' ? '激活' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="roles" label="角色" min-width="180" show-overflow-tooltip>
        <template #default="{ row }">
          <div class="role-tags">
            <el-tag
              v-for="role in row.roles"
              :key="role.roleId"
              type="info"
              size="small"
              class="role-tag"
            >
              {{ role.roleName }}
            </el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="180" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small" @click="handleRoleAssignment(row)">
            分配角色
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="size"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        :page-sizes="[10, 20, 50, 100]"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  users: {
    type: Array,
    required: true
  },
  loading: {
    type: Boolean,
    default: false
  },
  total: {
    type: Number,
    required: true
  },
  page: {
    type: Number,
    default: 1
  },
  size: {
    type: Number,
    default: 10
  }
})

const emit = defineEmits(['page-change', 'size-change', 'role-assignment', 'edit'])

const currentPage = ref(props.page)

watch(() => props.page, (newPage) => {
  currentPage.value = newPage
})

const handleRoleAssignment = (user) => {
  emit('role-assignment', user)
}

const handleEdit = (user) => {
  emit('edit', user)
}

const handlePageChange = (newPage) => {
  emit('page-change', newPage)
}

const handleSizeChange = (newSize) => {
  emit('size-change', newSize)
}
</script>

<style lang="scss" scoped>
.user-list-container {
  .pagination {
    margin-top: 15px;
    display: flex;
    justify-content: flex-end;
  }

  .role-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 4px;
  }
}
</style>
