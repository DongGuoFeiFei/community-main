<template>
  <el-drawer
    v-model="visible"
    :title="`角色详情 - ${roleInfo.roleName}`"
    size="50%"
    :close-on-click-modal="false"
  >
    <el-descriptions :column="1" border>
      <el-descriptions-item label="角色ID">
        {{ roleInfo.roleId }}
      </el-descriptions-item>
      <el-descriptions-item label="角色名称">
        {{ roleInfo.roleName }}
      </el-descriptions-item>
      <el-descriptions-item label="角色标识">
        {{ roleInfo.roleKey }}
      </el-descriptions-item>
      <el-descriptions-item label="显示顺序">
        {{ roleInfo.roleSort }}
      </el-descriptions-item>
      <el-descriptions-item label="状态">
        <el-tag :type="roleInfo.status === 1 ? 'success' : 'danger'">
          {{ roleInfo.status === 1 ? '启用' : '禁用' }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="系统内置">
        <el-tag :type="roleInfo.isSystem ? 'success' : 'info'">
          {{ roleInfo.isSystem ? '是' : '否' }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="创建时间">
        {{ roleInfo.createTime }}
      </el-descriptions-item>
      <el-descriptions-item label="更新时间">
        {{ roleInfo.updateTime || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="备注">
        {{ roleInfo.remark || '-' }}
      </el-descriptions-item>
    </el-descriptions>

    <div class="user-list-container">
      <div class="header">
        <span class="title">关联用户</span>
        <el-button type="primary" size="small" @click="handleAddUser">
          添加用户
        </el-button>
      </div>
      <el-table :data="userList" border>
        <el-table-column prop="userId" label="用户ID" width="100"/>
        <el-table-column prop="username" label="用户名"/>
        <el-table-column prop="nickname" label="昵称"/>
        <el-table-column prop="deptName" label="部门"/>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button
              type="danger"
              size="small"
              @click="handleRemoveUser(row)"
            >
              移除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </el-drawer>
</template>

<script setup>
import {computed, ref, watch} from 'vue';
import {getRoleDetail} from '@/api/role';
import {getRoleUsers} from '@/api/user';

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  roleId: {
    type: Number,
    default: null,
  },
});

const emit = defineEmits(['update:modelValue']);

const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
});

const roleInfo = ref({});
const userList = ref([]);

// 获取角色详情
const fetchRoleDetail = async () => {
  try {
    const res = await getRoleDetail(props.roleId);
    roleInfo.value = res.data;
  } catch (error) {
    console.error('获取角色详情失败:', error);
  }
};

// 获取角色关联用户
const fetchRoleUsers = async () => {
  try {
    const res = await getRoleUsers(props.roleId);
    userList.value = res.data;
  } catch (error) {
    console.error('获取角色用户失败:', error);
  }
};

// 添加用户
const handleAddUser = () => {
  // 实现添加用户逻辑
  console.log('添加用户');
};

// 移除用户
const handleRemoveUser = (row) => {
  // 实现移除用户逻辑
  console.log('移除用户:', row);
};

watch(
  () => props.roleId,
  (newVal) => {
    if (newVal) {
      fetchRoleDetail();
      fetchRoleUsers();
    }
  },
  {immediate: true}
);
</script>

<style scoped lang="scss">
.user-list-container {
  margin-top: 20px;

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;

    .title {
      font-size: 16px;
      font-weight: bold;
    }
  }
}
</style>
