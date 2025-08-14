<template>
  <div class="menu-permission-container">
    <div class="permission-header">
      <el-input
        v-model="filterText"
        placeholder="输入关键字过滤菜单"
        clearable
        class="filter-input"
      />
      <el-button type="primary" @click="handleSave" :loading="submitting">
        保存
      </el-button>
    </div>

    <el-tree
      ref="treeRef"
      :data="menuTree"
      :props="treeProps"
      node-key="menuId"
      show-checkbox
      default-expand-all
      highlight-current
      :filter-node-method="filterNode"
      :check-strictly="checkStrictly"
      @check="handleCheck"
    >
      <template #default="{ node, data }">
        <span class="custom-tree-node">
          <el-icon v-if="data.icon">
            <component :is="data.icon"/>
          </el-icon>
          <span>{{ node.label }}</span>
          <span class="menu-type-tag">
            <el-tag size="small" effect="plain">
              {{ menuTypeMap[data.menuType] }}
            </el-tag>
          </span>
        </span>
      </template>
    </el-tree>
  </div>
</template>

<script setup>
import {onMounted, ref, watch} from 'vue';
import {ElMessage} from 'element-plus';
import {getMenuTree, getRoleMenus, updateRoleMenus} from '@/api/menu';

const props = defineProps({
  roleId: {
    type: Number,
    required: true,
  },
});

const menuTree = ref([]);
const treeRef = ref(null);
const filterText = ref('');
const checkStrictly = ref(false);
const submitting = ref(false);

const menuTypeMap = {
  M: '目录',
  C: '菜单',
  F: '按钮',
};

const treeProps = {
  label: 'menuName',
  children: 'children',
};

// 过滤菜单
const filterNode = (value, data) => {
  if (!value) return true;
  return data.menuName.includes(value);
};

// 监听过滤文本变化
watch(filterText, (val) => {
  treeRef.value?.filter(val);
});

// 获取菜单树
const fetchMenuTree = async () => {
  try {
    const res = await getMenuTree();
    menuTree.value = res.data;
  } catch (error) {
    console.error('获取菜单树失败:', error);
    ElMessage.error('获取菜单树失败');
  }
};

// 获取角色菜单权限
const fetchRoleMenus = async () => {
  try {
    if (!props.roleId) return;

    const res = await getRoleMenus(props.roleId);
    const checkedKeys = res.data;

    // 设置选中节点
    checkStrictly.value = true;
    treeRef.value?.setCheckedKeys(checkedKeys);
    checkStrictly.value = false;
  } catch (error) {
    console.error('获取角色菜单权限失败:', error);
    ElMessage.error('获取角色菜单权限失败');
  }
};

// 节点选中事件
const handleCheck = () => {
  // 可以在这里处理选中逻辑
};

// 保存权限
const handleSave = async () => {
  try {
    submitting.value = true;
    const checkedKeys = treeRef.value.getCheckedKeys();
    const halfCheckedKeys = treeRef.value.getHalfCheckedKeys();
    const menuIds = [...checkedKeys, ...halfCheckedKeys];

    await updateRoleMenus(props.roleId, menuIds);
    ElMessage.success('权限保存成功');
  } catch (error) {
    console.error('保存菜单权限失败:', error);
    ElMessage.error('保存菜单权限失败');
  } finally {
    submitting.value = false;
  }
};

// 初始化获取菜单树
onMounted(() => {
  fetchMenuTree();
});

// 监听roleId变化，重新获取权限数据
watch(() => props.roleId, (newVal) => {
  if (newVal) {
    fetchRoleMenus();
  }
}, {immediate: true});
</script>

<style scoped lang="scss">
.menu-permission-container {
  .permission-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 15px;

    .filter-input {
      width: 300px;
    }
  }

  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    font-size: 14px;
    padding-right: 8px;

    .el-icon {
      margin-right: 6px;
    }

    .menu-type-tag {
      margin-left: 10px;
    }
  }
}
</style>
