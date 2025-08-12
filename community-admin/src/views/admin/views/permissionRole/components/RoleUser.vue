<template>
  <div class="role-user-container">
    <el-transfer
      v-model="selectedUserIds"
      :data="userList"
      :titles="['所有用户', '已分配用户']"
      :props="{
        key: 'userId',
        label: 'nickname'
      }"
      filterable
      filter-placeholder="请输入用户名"
    />

    <div class="footer">
      <el-button type="primary" @click="handleSave">保存</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, defineProps } from 'vue';
import { ElMessage } from 'element-plus';
import { getRoleUsers, assignUsersToRole, removeUsersFromRole } from '@/api/user';

const props = defineProps({
  roleId: Number
});

const userList = ref([]);
const selectedUserIds = ref([]);
const originalUserIds = ref([]);

// 获取角色用户
const fetchRoleUsers = async () => {
  const res = await getRoleUsers(props.roleId);
  userList.value = res.data.allUsers;
  selectedUserIds.value = res.data.assignedUserIds;
  originalUserIds.value = [...res.data.assignedUserIds];
};

// 保存用户分配
const handleSave = async () => {
  try {
    const addedUserIds = selectedUserIds.value.filter(
      id => !originalUserIds.value.includes(id)
    );

    const removedUserIds = originalUserIds.value.filter(
      id => !selectedUserIds.value.includes(id)
    );

    if (addedUserIds.length > 0) {
      await assignUsersToRole(props.roleId, addedUserIds);
    }

    if (removedUserIds.length > 0) {
      await removeUsersFromRole(props.roleId, removedUserIds);
    }

    ElMessage.success('用户分配成功');
    originalUserIds.value = [...selectedUserIds.value];
  } catch (error) {
    ElMessage.error(error.message || '用户分配失败');
  }
};

watch(() => props.roleId, () => {
  if (props.roleId) {
    fetchRoleUsers();
  }
}, { immediate: true });
</script>

<style scoped lang="scss">
.role-user-container {
  padding: 20px;

  .footer {
    margin-top: 20px;
    text-align: center;
  }
}
</style>
