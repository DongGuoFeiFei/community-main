<template>
  <el-dialog
    v-model="visible"
    :title="`权限分配 - ${roleName}`"
    width="900px"
    :close-on-click-modal="false"
    @closed="handleClosed"
    @open="handleOpen"
  >
    <el-tabs v-model="activeTab">
      <el-tab-pane label="菜单权限" name="menu">
        <menu-permission
          :role-id="roleId"
        />
      </el-tab-pane>
      <el-tab-pane label="数据权限" name="dataScope">
        <data-scope-permission
          :role-id="roleId"
        />
      </el-tab-pane>
      <el-tab-pane label="接口权限" name="api">
        <api-permission
          :role-id="roleId"
        />
      </el-tab-pane>
    </el-tabs>
  </el-dialog>
</template>

<script setup>
import {computed, ref, watch} from 'vue';
import MenuPermission from './MenuPermission.vue';
import DataScopePermission from './DataScopePermission.vue';
import ApiPermission from './ApiPermission.vue';
import {getRoleDetail} from "@/api/role.js";
import {ElMessage} from "element-plus";

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

const activeTab = ref('menu');
const roleName = ref('');

const handleClosed = () => {
  roleName.value = '';
  activeTab.value = 'menu';
};

const handleOpen = () => {
  if (props.roleId) {
    fetchRoleDetail();
  }
};

const fetchRoleDetail = async () => {
  try {
    const res = await getRoleDetail(props.roleId);
    roleName.value = res.data.roleName;
  } catch (error) {
    console.error('获取角色名称:', error);
    ElMessage.error('获取角色名称失败');
  }
};

</script>
