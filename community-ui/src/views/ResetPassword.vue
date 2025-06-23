<template>
  <div class="reset-container">
    <el-card class="reset-card">
      <template #header>
        <div class="card-header">
          <span>重置密码</span>
        </div>
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="form.newPassword" placeholder="请输入新密码" type="password" show-password/>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" placeholder="请再次输入新密码" type="password" show-password/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleReset">重置密码</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="js">
import {onMounted, ref} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {ElMessage} from 'element-plus';
import {resetPassword} from '../api';

const route = useRoute();
const router = useRouter();
const loading = ref(false);
const formRef = ref(null);

const form = ref({
  token: '',
  newPassword: '',
  confirmPassword: '',
});

const rules = {
  newPassword: [
    {required: true, message: '请输入新密码', trigger: 'blur'},
    {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
  ],
  confirmPassword: [
    {required: true, message: '请再次输入密码', trigger: 'blur'},
    {
      validator: (rule, value, callback) => {
        if (value !== form.value.newPassword) {
          callback(new Error('两次输入的密码不一致'));
        } else {
          callback();
        }
      }, trigger: 'blur'
    }
  ],
};

onMounted(() => {
  form.value.token = route.query.token;
  if (!form.value.token) {
    ElMessage.error('无效的重置链接');
    router.push('/login');
  }
});

const handleReset = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return;
    loading.value = true;
    try {
      const {confirmPassword, ...resetData} = form.value;
      const res = await resetPassword(resetData);

      if (res?.code === 200) {
        ElMessage.success('密码重置成功');
        router.push('/login');
      } else {
        ElMessage.error(res?.msg || '密码重置失败');
      }
    } catch (error) {
      console.error('重置出错:', error);
      ElMessage.error('密码重置失败，请稍后重试');
    } finally {
      loading.value = false;
    }
  });
};
</script>

<style scoped lang="less">
.reset-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(to right, #74ebd5, #acb6e5);
}

.reset-card {
  width: 500px;
  padding: 20px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  border-radius: 12px;
  background-color: #fff;
}
</style>