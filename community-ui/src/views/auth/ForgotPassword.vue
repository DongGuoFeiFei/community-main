<template>
  <div class="forgot-container">
    <el-card class="forgot-card">
      <template #header>
        <div class="card-header">
          <span>找回密码</span>
        </div>
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入注册时使用的邮箱"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleSubmit">发送重置链接</el-button>
          <el-button @click="goToLogin">返回登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="js">
import {ref} from 'vue';
import {useRouter} from 'vue-router';
import {ElMessage} from 'element-plus';
import {forgotPassword} from '../../../../community-admin/src/api/auth.js'; // 需要在api/index.js中添加此方法

const router = useRouter();
const loading = ref(false);
const formRef = ref(null);

const form = ref({
  email: '',
});

const rules = {
  email: [
    {required: true, message: '请输入邮箱地址', trigger: 'blur'},
    {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}
  ],
};

const handleSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return;
    loading.value = true;
    try {
      const res = await forgotPassword(form.value);
      if (res?.code === 200) {
        ElMessage.success('重置链接已发送至您的邮箱，请查收');
      } else {
        ElMessage.error(res?.msg || '发送失败');
      }
    } catch (error) {
      console.error('发送出错:', error);
      ElMessage.error('发送失败，请稍后重试');
    } finally {
      loading.value = false;
    }
  });
};

const goToLogin = () => {
  router.push('/login');
};
</script>

<style scoped lang="less">
.forgot-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(to right, #74ebd5, #acb6e5);
}

.forgot-card {
  width: 500px;
  padding: 20px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  border-radius: 12px;
  background-color: #fff;
}
</style>