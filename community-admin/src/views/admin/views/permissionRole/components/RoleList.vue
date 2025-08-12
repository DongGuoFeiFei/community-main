<template>
  <div class="role-list-container">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>角色列表</span>
          <el-button type="primary" @click="handleCreate" icon="Plus">新增角色</el-button>
        </div>
      </template>

      <el-table
        :data="roleList"
        v-loading="loading"
        row-key="roleId"
        border
        style="width: 100%"
      >
        <el-table-column prop="roleName" label="角色名称" width="180"/>
        <el-table-column prop="roleKey" label="角色标识" width="180"/>
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
        <el-table-column prop="createTime" label="创建时间" width="180"/>
        <el-table-column prop="remark" label="备注"/>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" @click="handlePermission(row)">权限</el-button>
            <el-button
              size="small"
              type="danger"
              @click="handleDelete(row)"
              :disabled="row.isSystem === 1"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <RoleForm
      ref="roleFormRef"
      v-model="formVisible"
      :form-data="formData"
      @close="handleFormClose"
      @success="handleFormSuccess"
    />

    <RolePermission
      ref="rolePermissionRef"
      v-model="permissionVisible"
      :role-id="currentRoleId"
      @close="permissionVisible = false"
    />

  </div>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue';
import {ElMessage, ElMessageBox} from 'element-plus';
import {changeRoleStatus, deleteRole, getRoleList} from '@/api/role';
import RoleForm from './RoleForm.vue';
import RolePermission from './RolePermission.vue';

const roleList = ref([]);
const loading = ref(false);
const total = ref(0);
const formVisible = ref(false);
const permissionVisible = ref(false);
const currentRoleId = ref(null);
const roleFormRef = ref(null);
const rolePermissionRef = ref(null);

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  roleName: '',
  status: undefined
});

const formData = reactive({
  roleId: undefined,
  roleName: '',
  roleKey: '',
  roleSort: 0,
  status: 1,
  remark: ''
});

// 获取角色列表
const fetchRoleList = async () => {
  try {
    loading.value = true;
    const res = await getRoleList(queryParams);
    roleList.value = res.data.rows;
    total.value = res.data.total;
  } finally {
    loading.value = false;
  }
};

// 处理分页
const handleSizeChange = (val) => {
  queryParams.pageSize = val;
  fetchRoleList();
};

const handleCurrentChange = (val) => {
  queryParams.pageNum = val;
  fetchRoleList();
};

// 处理状态变更
const handleStatusChange = async (row) => {
  try {
    await changeRoleStatus(row.roleId, row.status);
    ElMessage.success('状态修改成功');
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1;
  }
};

// 处理创建角色
const handleCreate = () => {
  Object.assign(formData, {
    roleId: undefined,
    roleName: '',
    roleKey: '',
    roleSort: 0,
    status: 1,
    remark: ''
  });
  formVisible.value = true;
};

// 处理编辑角色
const handleEdit = (row) => {
  Object.assign(formData, row);
  formVisible.value = true;
};

// 处理权限分配
const handlePermission = (row) => {
  currentRoleId.value = row.roleId;
  permissionVisible.value = true;
};

// 处理删除角色
const handleDelete = (row) => {
  ElMessageBox.confirm(`确认删除角色 "${row.roleName}"?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteRole(row.roleId);
    ElMessage.success('删除成功');
    fetchRoleList();
  }).catch(() => {
  });
};

// 处理表单关闭
const handleFormClose = () => {
  formVisible.value = false;
};

// 处理表单提交成功
const handleFormSuccess = () => {
  formVisible.value = false;
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
  }

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
