<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <div class="card-header">
          <span>用户注册</span>
        </div>
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称"/>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱"/>
        </el-form-item>
        <el-form-item label="验证码" prop="code">
          <div class="code-input">
            <el-input v-model="form.code" placeholder="请输入验证码"/>
            <el-button
                type="primary"
                :disabled="isCountingDown || !canGetCode"
                @click="sendVerificationCode"
                class="code-btn"
            >
              {{ countdown > 0 ? `${countdown}s后重新获取` : '获取验证码' }}
            </el-button>
          </div>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" type="password" show-password/>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" placeholder="请再次输入密码" type="password" show-password/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleRegister">注册</el-button>
          <el-button @click="goToLogin">已有账号？去登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="js">
import {computed, onUnmounted, ref} from 'vue';
import {useRouter} from 'vue-router';
import {ElMessage} from 'element-plus';
import {getEmailCode, register} from '../../../../community-admin/src/api/auth.js';

const router = useRouter();
const loading = ref(false);
const formRef = ref(null);
const countdown = ref(0);
const countdownTimer = ref(null);
const isCountingDown = computed(() => countdown.value > 0);

const form = ref({
  username: '',
  nickname: '',
  email: '',
  password: '',
  confirmPassword: '',
  code: ''
});

// 5分钟有效期（300秒）
const CODE_EXPIRE_TIME = 300;

// 计算是否可以获取验证码
const canGetCode = computed(() => {
  return form.value.username && form.value.nickname && form.value.email;
});

// 自定义密码验证规则
const validatePassword = (rule, value, callback) => {
  if (value !== form.value.password) {
    callback(new Error('两次输入的密码不一致'));
  } else {
    callback();
  }
};

const rules = {
  username: [
    {required: true, message: '请输入用户名', trigger: 'blur'},
    {min: 1, max: 16, message: '长度在 1 到 16 个字符', trigger: 'blur'},
    {pattern: /^[a-zA-Z0-9_]+$/, message: '只能包含字母、数字和下划线', trigger: 'blur'}
  ],
  nickname: [
    {required: true, message: '请输入昵称', trigger: 'blur'},
    {min: 1, max: 16, message: '长度在 1 到 16 个字符', trigger: 'blur'}
  ],
  email: [
    {required: true, message: '请输入邮箱地址', trigger: 'blur'},
    {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}
  ],
  code: [
    {required: true, message: '请输入验证码', trigger: 'blur'},
    {min: 4, max: 4, message: '验证码长度为4位', trigger: 'blur'}
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
  ],
  confirmPassword: [
    {required: true, message: '请再次输入密码', trigger: 'blur'},
    {validator: validatePassword, trigger: 'blur'}
  ],
};

// 发送验证码
const sendVerificationCode = async () => {
  try {
    // 验证用户名、昵称和邮箱
    await Promise.all([
      formRef.value.validateField('username'),
      formRef.value.validateField('nickname'),
      formRef.value.validateField('email')
    ]);

    const res = await getEmailCode({
      username: form.value.username,
      nickname: form.value.nickname,
      email: form.value.email
    });

    if (res?.code === 200) {
      ElMessage.success('验证码已发送至邮箱，5分钟内有效');
      startCountdown();
    } else {
      ElMessage.error(res?.msg || '验证码发送失败');
    }
  } catch (error) {
    console.error('发送验证码出错:', error);
    ElMessage.error('验证码发送失败，请检查表单信息');
  }
};

// 开始倒计时
const startCountdown = () => {
  countdown.value = 30;
  // countdown.value = CODE_EXPIRE_TIME;
  countdownTimer.value = setInterval(() => {
    countdown.value--;
    if (countdown.value <= 0) {
      clearInterval(countdownTimer.value);
    }
  }, 1000);
};

// 处理注册
const handleRegister = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return;
    loading.value = true;
    try {
      // 移除确认密码字段，不发送到后端
      const {confirmPassword, ...registerData} = form.value;
      const res = await register(registerData);

      if (res?.code === 200) {
        await router.push('/login');
        ElMessage.success('注册成功');
      } else {
        ElMessage.error(res?.msg || '注册失败');
      }
    } catch (error) {
      console.error('注册出错:', error);
      ElMessage.error('注册失败，请稍后重试');
    } finally {
      loading.value = false;
    }
  });
};

const goToLogin = () => {
  router.push('/login');
};

// 组件卸载时清除定时器
onUnmounted(() => {
  if (countdownTimer.value) {
    clearInterval(countdownTimer.value);
  }
});
</script>

<style scoped lang="scss">
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(to right, #74ebd5, #acb6e5);
}

.register-card {
  width: 500px;
  padding: 20px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  border-radius: 12px;
  background-color: #fff;

  .code-input {
    display: flex;
    gap: 10px;

    .code-btn {
      width: 140px;
      flex-shrink: 0;
    }
  }
}
</style>