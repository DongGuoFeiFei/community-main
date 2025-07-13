<!-- /views/admin/user/components/UserDetailDialog.vue -->
<template>
  <el-dialog
      v-model="visible"
      :title="form.id ? '编辑用户' : '新增用户'"
      width="600px"
      @close="handleClose"
  >
    <el-form
        :model="form"
        label-width="100px"
        :rules="rules"
        ref="formRef"
    >
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username"/>
      </el-form-item>
      <el-form-item label="昵称" prop="nickname">
        <el-input v-model="form.nickname"/>
      </el-form-item>
      <el-form-item v-if="isEditor" label="邮箱" prop="email">
        <el-input v-model="form.email" type="email"/>
      </el-form-item>
      <el-form-item v-if="isEditor" label="手机号" prop="phone">
        <el-input v-model="form.phone"/>
      </el-form-item>
      <el-form-item label="状态" prop="isActive">
        <el-radio-group v-model="form.isActive">
          <el-radio :label="1">正常</el-radio>
          <el-radio :label="0">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      <!--      <el-form-item label="个人简介" prop="bio">-->
      <!--        <el-input v-model="form.bio" type="textarea" :rows="3"/>-->
      <!--      </el-form-item>-->
    </el-form>

    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm">确认</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import {ref, watch} from 'vue';

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false,
  },
  user: {
    type: Object,
    default: () => ({}),
  },
  isEditor: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['update:modelValue', 'confirm']);

const visible = ref(false);
const formRef = ref(null);

// 初始化完整的表单数据
const initFormData = () => ({
  userId: '',
  username: '',
  nickname: '',
  email: '',
  phone: '',
  isActive: '',
});

const form = ref(initFormData());

const rules = {
  username: [
    {required: true, message: '请输入用户名', trigger: 'blur'},
    {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'},
  ],
  email: [
    {required: true, message: '请输入邮箱', trigger: 'blur'},
    {type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur'},
  ],
  isActive: [
    {required: true, message: '请选择状态', trigger: 'change'},
  ],
};

watch(
    () => props.modelValue,
    (val) => {
      visible.value = val;
      if (val) {
        if (props.isEditor) {
          form.value = {
            ...initFormData(),
            ...props.user
          }
          console.log(form.value)
        } else {
          form.value.isActive = null
          form.value.userId = null
        }
      }
    }
);

watch(
    () => visible.value,
    (val) => {
      emit('update:modelValue', val);
    }
);

const handleClose = () => {
  visible.value = false;
  formRef.value?.resetFields();
};

const handleConfirm = async () => {
  try {
    await formRef.value.validate();

    emit('confirm', form.value, props.isEditor);
    handleClose();
  } catch (error) {
    console.error('表单验证失败:', error);
  }
};
</script>