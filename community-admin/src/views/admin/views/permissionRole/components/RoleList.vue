<template>
  <div class="role-list-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="header-title">角色管理</span>
          <div>
            <el-button type="primary" @click="handleCreate">
              <el-icon>
                <plus/>
              </el-icon>
              新增角色
            </el-button>
          </div>
        </div>
      </template>

      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="角色名称">
          <el-input
            v-model="searchForm.roleName"
            placeholder="请输入角色名称"
            clearable
          />
        </el-form-item>
        <el-form-item label="角色标识">
          <el-input
            v-model="searchForm.roleKey"
            placeholder="请输入角色标识"
            clearable
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="启用" :value="1"/>
            <el-option label="禁用" :value="0"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table
        v-loading="loading"
        :data="roleList"
        border
        row-key="roleId"
        @selection-change="handleSelectionChange"
        :fit="true"
      >
        <el-table-column prop="roleName" label="角色名称" :min-width="120"/>
        <el-table-column prop="roleKey" label="角色标识" :min-width="120"/>
        <el-table-column prop="roleSort" label="排序" width="80"/>
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
        <el-table-column prop="isSystem" label="系统内置" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isSystem ? 'success' : 'info'">
              {{ row.isSystem ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" :min-width="120"/>
        <el-table-column prop="createTime" label="创建时间" width="180"/>
        <el-table-column label="操作" width="260" fixed="right">
          <template #default="{ row }">
            <el-button
              size="small"
              type="success"
              @click="handlePermission(row)"
            >
              权限
            </el-button>
            <el-button
              size="small"
              type="primary"
              @click="handleEdit(row)"
              v-if="!row.isSystem"
            >
              编辑
            </el-button>
            <el-button
              size="small"
              type="danger"
              @click="handleDelete(row)"
              v-if="!row.isSystem"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 角色表单对话框 -->
    <role-form
      v-model="formVisible"
      :role-id="currentRoleId"
      @success="handleFormSuccess"
    />

    <role-permission
      v-model="permissionVisible"
      :role-id="currentRoleId"
    />
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import {Plus} from '@element-plus/icons-vue';
import {ElMessage, ElMessageBox} from 'element-plus';
import {changeRoleStatus, deleteRole, getRoleList} from '@/api/role';
import RoleForm from './RoleForm.vue';
import RolePermission from './RolePermission.vue';


// 搜索表单
const searchForm = ref({
  roleName: '',
  roleKey: '',
  status: null,
});

// 分页
const pagination = ref({
  current: 1,
  size: 10,
  total: 0,
});

// 表格数据
const loading = ref(false);
const roleList = ref([]);
const selectedRows = ref([]);

// 表单对话框
const formVisible = ref(false);
const currentRoleId = ref(null);

// 获取角色列表
const fetchRoleList = async () => {
  try {
    loading.value = true;
    const params = {
      ...searchForm.value,
      pageNum: pagination.value.current,
      pageSize: pagination.value.size,
    };
    const res = await getRoleList(params);
    roleList.value = res.data.rows;
    pagination.value.total = res.data.total;
  } catch (error) {
    console.error('获取角色列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 搜索
const handleSearch = () => {
  pagination.value.current = 1;
  fetchRoleList();
};

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    roleName: '',
    roleKey: '',
    status: null,
  };
  handleSearch();
};

// 分页变化
const handleSizeChange = (size) => {
  pagination.value.size = size;
  fetchRoleList();
};

const handleCurrentChange = (current) => {
  pagination.value.current = current;
  fetchRoleList();
};

// 表格选择
const handleSelectionChange = (selection) => {
  selectedRows.value = selection;
};

// 状态变更
const handleStatusChange = async (row) => {
  try {
    await changeRoleStatus(row.roleId, row.status);
    ElMessage.success('状态修改成功');
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1;
    ElMessage.error('状态修改失败');
  }
};

// 创建角色
const handleCreate = () => {
  currentRoleId.value = null;
  formVisible.value = true;
};

// 编辑角色
const handleEdit = (row) => {
  currentRoleId.value = row.roleId;
  formVisible.value = true;
};

// 权限管理
const permissionVisible = ref(false);

// 权限管理
const handlePermission = (row) => {
  currentRoleId.value = row.roleId;
  permissionVisible.value = true;
};

// 删除角色
const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除角色 "${row.roleName}" 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
    .then(async () => {
      try {
        await deleteRole([row.roleId]);
        ElMessage.success('删除成功');
        fetchRoleList();
      } catch (error) {
        ElMessage.error('删除失败');
      }
    })
    .catch(() => {
    });
};

// 表单提交成功
const handleFormSuccess = () => {
  fetchRoleList();
};

onMounted(() => {
  fetchRoleList();
});
</script>

<style scoped lang="scss">
.role-list-container {
  padding: 20px;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .header-title {
      font-size: 18px;
      font-weight: bold;
    }
  }

  .search-form {
    margin-bottom: 20px;
  }

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
