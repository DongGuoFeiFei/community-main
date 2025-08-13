<template>
  <el-dialog
    v-model="visible"
    :title="title"
    width="600px"
    :close-on-click-modal="false"
    @closed="handleClosed"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="100px"
      label-position="right"
    >
      <el-form-item label="角色名称" prop="roleName">
        <el-input
          v-model="formData.roleName"
          placeholder="请输入角色名称"
          clearable
        />
      </el-form-item>
      <el-form-item label="角色标识" prop="roleKey">
        <el-input
          v-model="formData.roleKey"
          placeholder="请输入角色标识"
          clearable
        />
      </el-form-item>
      <el-form-item label="显示顺序" prop="roleSort">
        <el-input-number
          v-model="formData.roleSort"
          :min="0"
          controls-position="right"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio :value="1">启用</el-radio>
          <el-radio :value="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注信息"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import {computed, ref, watch} from 'vue';
import {ElMessage} from 'element-plus';
import {createRole, getRoleDetail, updateRole} from '@/api/role';

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

const emit = defineEmits(['update:modelValue', 'success']);

const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value),
});

const title = computed(() => (props.roleId ? '编辑角色' : '新增角色'));

// 表单数据
const formData = ref({
  roleId: null,
  roleName: '',
  roleKey: '',
  roleSort: 0,
  status: 1,
  remark: '',
});

// 表单验证规则
const rules = ref({
  roleName: [
    {required: true, message: '请输入角色名称', trigger: 'blur'},
    {max: 50, message: '长度不能超过50个字符', trigger: 'blur'},
  ],
  roleKey: [
    {required: true, message: '请输入角色标识', trigger: 'blur'},
    {max: 50, message: '长度不能超过50个字符', trigger: 'blur'},
  ],
  roleSort: [{required: true, message: '请输入显示顺序', trigger: 'blur'}],
});

// 表单引用
const formRef = ref(null);
const submitting = ref(false);

// 获取角色详情
const fetchRoleDetail = async () => {
  try {
    const res = await getRoleDetail(props.roleId);
    formData.value = res.data;
  } catch (error) {
    console.error('获取角色详情失败:', error);
    ElMessage.error('获取角色详情失败');
    visible.value = false;
  }
};

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate();
    submitting.value = true;

    if (props.roleId) {
      await updateRole(formData.value);
      ElMessage.success('修改成功');
    } else {
      await createRole(formData.value);
      ElMessage.success('创建成功');
    }

    emit('success');
    visible.value = false;
  } catch (error) {
    console.error('提交失败:', error);
    if (error && error.errorFields) return;
    ElMessage.error('提交失败');
  } finally {
    submitting.value = false;
  }
};

// 对话框关闭
const handleClosed = () => {
  formRef.value.resetFields();
  formData.value = {
    roleId: null,
    roleName: '',
    roleKey: '',
    roleSort: 0,
    status: 1,
    remark: '',
  };
};

// 监听角色ID变化
watch(
  () => props.modelValue,
  (visible) => {
    if (visible && props.roleId) {
      fetchRoleDetail();
    }
  },
  {immediate: true}
);
</script>
