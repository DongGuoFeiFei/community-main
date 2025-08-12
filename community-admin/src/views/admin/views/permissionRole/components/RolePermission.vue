<template>
  <el-dialog
    :model-value="modelValue"
    :title="`分配权限 - ${roleName}`"
    width="800px"
    @update:model-value="$emit('update:modelValue', $event)"
    @close="$emit('close')"
  >
    <el-tabs v-model="activeTab">
      <el-tab-pane label="菜单权限" name="menu">
        <el-tree
          ref="menuTreeRef"
          :data="menuTree"
          node-key="menuId"
          show-checkbox
          default-expand-all
          :props="treeProps"
          :default-checked-keys="checkedMenuKeys"
        />
      </el-tab-pane>

      <el-tab-pane label="数据权限" name="dataScope">
        <RoleDataScope :role-id="roleId" />
      </el-tab-pane>

      <el-tab-pane label="关联用户" name="user">
        <RoleUser :role-id="roleId" />
      </el-tab-pane>
    </el-tabs>

    <template #footer v-if="activeTab === 'menu'">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSaveMenu">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch, defineProps, defineEmits } from 'vue';
import { ElMessage } from 'element-plus';
import { getMenuTree, getRoleMenus, updateRoleMenus } from '@/api/menu';
import RoleDataScope from './RoleDataScope.vue';
import RoleUser from './RoleUser.vue';

const props = defineProps({
  modelValue: Boolean,
  roleId: Number
});

const emit = defineEmits(['update:modelValue', 'close']);

const activeTab = ref('menu');
const menuTree = ref([]);
const checkedMenuKeys = ref([]);
const menuTreeRef = ref(null);
const roleName = ref('');

const treeProps = {
  label: 'menuName',
  children: 'children'
};

// 获取菜单树
const fetchMenuTree = async () => {
  const res = await getMenuTree();
  menuTree.value = res.data;
};

// 获取角色菜单权限
const fetchRoleMenus = async () => {
  const res = await getRoleMenus(props.roleId);
  checkedMenuKeys.value = res.data;
  roleName.value = res.roleName;
};

// 保存菜单权限
const handleSaveMenu = async () => {
  try {
    const checkedKeys = menuTreeRef.value.getCheckedKeys();
    const halfCheckedKeys = menuTreeRef.value.getHalfCheckedKeys();
    const menuIds = [...checkedKeys, ...halfCheckedKeys];

    await updateRoleMenus(props.roleId, menuIds);
    ElMessage.success('权限分配成功');
    emit('close');
  } catch (error) {
    ElMessage.error(error.message || '权限分配失败');
  }
};

const handleClose = () => {
  emit('close');
};

watch(() => props.visible, (val) => {
  if (val) {
    fetchMenuTree();
    fetchRoleMenus();
  }
}, { immediate: true });
</script>
