<template>
  <div class="data-scope-container">
    <el-form label-width="120px">
      <el-form-item label="资源类型">
        <el-select v-model="resourceType" placeholder="请选择资源类型" @change="handleResourceChange">
          <el-option
            v-for="item in resourceTypes"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="权限范围">
        <el-radio-group v-model="scopeType" @change="handleScopeChange">
          <el-radio :label="1">全部数据权限</el-radio>
          <el-radio :label="2">自定义数据权限</el-radio>
          <el-radio :label="3">本部门数据权限</el-radio>
          <el-radio :label="4">本部门及以下数据权限</el-radio>
          <el-radio :label="5">仅本人数据权限</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="选择部门" v-if="scopeType === 2">
        <el-tree
          ref="deptTreeRef"
          :data="deptTree"
          node-key="deptId"
          show-checkbox
          default-expand-all
          :props="treeProps"
          :default-checked-keys="checkedDeptKeys"
        />
      </el-form-item>
    </el-form>

    <div class="footer">
      <el-button type="primary" @click="handleSave">保存</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, defineProps } from 'vue';
import { ElMessage } from 'element-plus';
import { getRoleDataScope, updateRoleDataScope } from '@/api/dataScope';

const props = defineProps({
  roleId: Number
});

const resourceType = ref('user');
const scopeType = ref(1);
const deptTree = ref([]);
const checkedDeptKeys = ref([]);
const deptTreeRef = ref(null);

const resourceTypes = [
  { value: 'user', label: '用户数据' },
  { value: 'article', label: '文章数据' },
  { value: 'dept', label: '部门数据' }
];

const treeProps = {
  label: 'deptName',
  children: 'children'
};

// 获取数据权限配置
const fetchDataScope = async () => {
  const res = await getRoleDataScope(props.roleId, resourceType.value);
  scopeType.value = res.data.scopeType;
  deptTree.value = res.data.deptTree;
  checkedDeptKeys.value = res.data.customDeptIds || [];
};

// 处理资源类型变更
const handleResourceChange = () => {
  fetchDataScope();
};

// 处理权限范围变更
const handleScopeChange = () => {
  checkedDeptKeys.value = [];
};

// 保存数据权限
const handleSave = async () => {
  try {
    const customDeptIds = scopeType.value === 2 ? deptTreeRef.value.getCheckedKeys() : [];

    await updateRoleDataScope({
      roleId: props.roleId,
      resourceType: resourceType.value,
      scopeType: scopeType.value,
      customDeptIds
    });

    ElMessage.success('数据权限配置成功');
  } catch (error) {
    ElMessage.error(error.message || '数据权限配置失败');
  }
};

watch(() => props.roleId, () => {
  if (props.roleId) {
    fetchDataScope();
  }
}, { immediate: true });
</script>

<style scoped lang="scss">
.data-scope-container {
  padding: 20px;

  .footer {
    margin-top: 20px;
    text-align: center;
  }
}
</style>
