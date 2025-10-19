<template>
  <div class="api-permission-container">
    <div class="permission-header">
      <el-input
        v-model="filterText"
        placeholder="输入关键字过滤接口"
        clearable
        class="filter-input"
      />
      <el-button type="primary" @click="handleSave" :loading="submitting">
        保存
      </el-button>
    </div>

    <el-tree
      ref="treeRef"
      :data="apiTree"
      :props="treeProps"
      node-key="apiId"
      show-checkbox
      default-expand-all
      highlight-current
      :check-strictly="checkStrictly"
      :filter-node-method="filterNode"
    >
      <template #default="{ node, data }">
        <span class="custom-tree-node">
          <span>{{ node.label }}</span>
          <span class="api-method-tag">
            <el-tag :type="getMethodTagType(data.httpMethod)" size="small">
              {{ data.httpMethod }}
            </el-tag>
          </span>
          <span class="api-path">{{ data.apiPath }}</span>
        </span>
      </template>
    </el-tree>
  </div>
</template>

<script setup>
import {onMounted, ref, watch} from 'vue';
import {ElMessage} from 'element-plus';
import {getApiTree, getRoleApis, updateRoleApis} from '@/api/api';

const props = defineProps({
  roleId: {
    type: Number,
    required: true,
  },
});

const treeRef = ref(null);
const apiTree = ref([]);
const filterText = ref('');
const filterFunc = ref('')
const checkStrictly = ref(false);
const submitting = ref(false);

const treeProps = {
  label: 'apiName',
  children: 'children',
};

// 过滤接口
const filterNode = (value, data) => {
  if (!value) return true;
  return (
    data.apiName.includes(value) ||
    data.apiPath.includes(value) ||
    data.httpMethod.includes(value.toUpperCase())
  );
};

// 获取方法标签类型
const getMethodTagType = (method) => {
  const methodMap = {
    GET: 'success',
    POST: 'warning',
    PUT: 'primary',
    DELETE: 'danger',
    PATCH: 'info',
  };
  return methodMap[method] || 'info';
};

// 获取API树
const fetchApiTree = async () => {
  try {
    const res = await getApiTree();
    apiTree.value = res.data;
  } catch (error) {
    console.error('获取API树失败:', error);
    ElMessage.error('获取API树失败');
  }
};

// 获取角色API权限
const fetchRoleApis = async () => {
  try {
    const res = await getRoleApis(props.roleId);
    const checkedKeys = res.data || [];

    checkStrictly.value = true;
    treeRef.value?.setCheckedKeys(checkedKeys);
    checkStrictly.value = false;
  } catch (error) {
    console.error('获取角色API权限失败:', error);
    ElMessage.error('获取角色API权限失败');
  }
};

// 保存权限
const handleSave = async () => {
  try {
    submitting.value = true;
    const checkedKeys = treeRef.value?.getCheckedKeys() || [];

    await updateRoleApis(props.roleId, checkedKeys);
    ElMessage.success('接口权限保存成功');
  } catch (error) {
    console.error('保存接口权限失败:', error);
    ElMessage.error('保存接口权限失败');
  } finally {
    submitting.value = false;
  }
};

// 监听过滤文本变化
watch(filterText, (val) => {
  treeRef.value?.filter(val);
});

onMounted(() => {
  fetchApiTree();
});

watch(() => props.roleId, (newVal) => {
  if (newVal) {
    fetchRoleApis();
  }
}, {immediate: true});
</script>

<style scoped lang="scss">
.api-permission-container {
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

    .api-method-tag {
      margin-left: 10px;
      width: 60px;
      text-align: center;
    }

    .api-path {
      margin-left: 10px;
      color: #666;
      font-family: monospace;
    }
  }
}
</style>
