<template>
  <el-dialog
    :model-value="modelValue"
    :title="formData.roleId ? '编辑角色' : '新增角色'"
    width="600px"
    @update:model-value="$emit('update:modelValue', $event)"
    @close="$emit('close')"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="100px"
    >
      <el-form-item label="角色名称" prop="roleName">
        <el-input v-model="formData.roleName" placeholder="请输入角色名称" />
      </el-form-item>

      <el-form-item label="角色标识" prop="roleKey">
        <el-input v-model="formData.roleKey" placeholder="请输入角色标识" />
      </el-form-item>

      <el-form-item label="显示顺序" prop="roleSort">
        <el-input-number v-model="formData.roleSort" :min="0" />
      </el-form-item>

      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio :label="1">启用</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="formData.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注"
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue';
import { ElMessage } from 'element-plus';
import { addRole, updateRole } from '@/api/role';

const props = defineProps({
  visible: Boolean,
  formData: {
    type: Object,
    default: () => ({})
  }
});

const emit = defineEmits(['close', 'success', 'update:visible']);

const formRef = ref(null);

const rules = {
  roleName: [
    { required: true, message: '角色名称不能为空', trigger: 'blur' }
  ],
  roleKey: [
    { required: true, message: '角色标识不能为空', trigger: 'blur' }
  ],
  roleSort: [
    { required: true, message: '显示顺序不能为空', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
};

const handleClose = () => {
  formRef.value?.resetFields();
  emit('update:visible', false);
  emit('close');
};

const handleSubmit = async () => {
  try {
    await formRef.value.validate();

    if (props.formData.roleId) {
      await updateRole(props.formData);
      ElMessage.success('修改成功');
    } else {
      await addRole(props.formData);
      ElMessage.success('添加成功');
    }

    emit('success');
    handleClose();
  } catch (error) {
    if (error.message) {
      ElMessage.error(error.message);
    }
  }
};
</script>
