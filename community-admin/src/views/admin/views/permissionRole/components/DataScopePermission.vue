<template>
  <div class="data-scope-container">
    <el-form
      ref="formRef"
      :model="form"
      label-width="120px"
      class="data-scope-form"
    >
      <el-form-item label="数据范围" prop="dataScope">
        <el-select
          v-model="form.dataScope"
          placeholder="请选择数据范围"
          @change="handleDataScopeChange"
        >
          <el-option
            v-for="item in dataScopeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item
        v-if="showDeptSelect"
        label="选择部门"
        prop="deptIds"
      >
        <el-tree
          ref="deptTreeRef"
          :data="deptTree"
          :props="treeProps"
          node-key="deptId"
          show-checkbox
          default-expand-all
          highlight-current
          :check-strictly="true"
        />
      </el-form-item>

      <el-form-item>
        <el-button
          type="primary"
          @click="handleSubmit"
          :loading="submitting"
        >
          保存
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import {computed, onMounted, ref} from 'vue';
import {ElMessage} from 'element-plus';
import {getDeptTree} from '@/api/dept';
import {getRoleDataScope, updateRoleDataScope} from '@/api/dataScope';

const props = defineProps({
  roleId: {
    type: Number,
    required: true,
  },
});

const emit = defineEmits(['update:role-name']);

const formRef = ref(null);
const deptTreeRef = ref(null);
const deptTree = ref([]);
const submitting = ref(false);

const form = ref({
  dataScope: '',
  deptIds: [],
});

const dataScopeOptions = [
  {value: '1', label: '全部数据权限'},
  {value: '2', label: '自定义数据权限'},
  {value: '3', label: '本部门数据权限'},
  {value: '4', label: '本部门及以下数据权限'},
  {value: '5', label: '仅本人数据权限'},
];

const treeProps = {
  label: 'deptName',
  children: 'children',
};

const showDeptSelect = computed(() => {
  return form.value.dataScope === '2';
});

// 获取部门树
const fetchDeptTree = async () => {
  try {
    const res = await getDeptTree();
    deptTree.value = res.data;
  } catch (error) {
    console.error('获取部门树失败:', error);
    ElMessage.error('获取部门树失败');
  }
};

// 获取角色数据权限
const fetchRoleDataScope = async () => {
  try {
    const res = await getRoleDataScope(props.roleId);
    form.value.dataScope = res.data.dataScope;

    if (res.data.roleName) {
      emit('update:role-name', res.data.roleName);
    }

    if (res.data.deptIds && res.data.deptIds.length > 0) {
      deptTreeRef.value?.setCheckedKeys(res.data.deptIds);
    }
  } catch (error) {
    console.error('获取角色数据权限失败:', error);
    ElMessage.error('获取角色数据权限失败');
  }
};

// 数据范围变化
const handleDataScopeChange = (value) => {
  if (value !== '2') {
    deptTreeRef.value?.setCheckedKeys([]);
  }
};

// 提交表单
const handleSubmit = async () => {
  try {
    submitting.value = true;

    const data = {
      dataScope: form.value.dataScope,
    };

    if (form.value.dataScope === '2') {
      data.deptIds = deptTreeRef.value?.getCheckedKeys() || [];
    }

    await updateRoleDataScope(props.roleId, data);
    ElMessage.success('数据权限保存成功');
  } catch (error) {
    console.error('保存数据权限失败:', error);
    ElMessage.error('保存数据权限失败');
  } finally {
    submitting.value = false;
  }
};

onMounted(() => {
  fetchDeptTree();
  fetchRoleDataScope();
});
</script>

<style scoped lang="scss">
.data-scope-container {
  padding: 20px;

  .data-scope-form {
    max-width: 600px;
  }
}
</style>
