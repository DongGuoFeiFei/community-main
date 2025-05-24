<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <span>用户登录</span>
        </div>
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" type="password" show-password/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleLogin">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="js">
import {ref} from 'vue';
import {useRouter} from 'vue-router';
import {ElMessage} from 'element-plus';
import {login} from '../api';
import {localStore} from "@/stores/localStores.js";


const lStore = localStore()

const router = useRouter();
const loading = ref(false);
const formRef = ref(null);

const form = ref({
  username: '',
  password: '',
});

const rules = {
  username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
  password: [{required: true, message: '请输入密码', trigger: 'blur'}],
};

const handleLogin = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return;
    loading.value = true;
    try {
      const res = await login(form.value);
      if (res?.token) {
        // 将数据存入lStore.userInfo
        lStore.userInfo = {
          ...res
        }
        // 将token的相关信息放入lStore.tokenInfo
        lStore.tokenInfo.token = res.token
        lStore.tokenInfo.refreshTime = res.userInfo.lastLogin
        lStore.tokenInfo.expiresIn = res.expiresIn
        localStorage.setItem('token', res.token);
        ElMessage.success('登录成功');
        router.push('/index');
      } else {
        ElMessage.error(res?.msg || '登录失败');
      }
    } catch (error) {
      console.error('登录出错:', error);
      ElMessage.error('登录失败，请检查用户名或密码');
    } finally {
      loading.value = false;
    }
  });
};
</script>

<style scoped lang="less">
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(to right, #74ebd5, #acb6e5);
}

.login-card {
  width: 400px;
  padding: 20px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  border-radius: 12px;
  background-color: #fff;
}
</style>
