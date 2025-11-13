<template>
  <div class="register-page">
    <!-- 装饰云朵 -->
    <div class="decoration-clouds">
      <span class="cloud-emoji c1">☁️</span>
      <span class="cloud-emoji c2">☁️</span>
    </div>
    
    <!-- 装饰粒子 -->
    <div class="decoration-particles">
      <span class="particle p1">✨</span>
      <span class="particle p2">💫</span>
      <span class="particle p3">🌸</span>
    </div>

    <div class="register-container">
      <!-- 欢迎横幅 -->
      <div class="welcome-banner">
        <div class="banner-content">
          <h1 class="banner-title">
            <span class="title-emoji">🌸</span>
            加入采芙蓉社区
            <span class="title-emoji">✨</span>
          </h1>
          <p class="banner-subtitle">开启你的精彩旅程</p>
        </div>
      </div>

      <!-- 注册卡片 -->
      <div class="register-card">
        <div class="card-header">
          <AuthBrand />
        </div>

        <div class="card-tips">
          <p>📖 知之为知之，不知为不知，是知也</p>
          <p>🤝 来者即是客，但也请您尽好作为客人的本分</p>
          <p>💝 世界上没有什么事情是理所当然的，请珍惜眼前</p>
        </div>

        <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" class="register-form">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="请输入用户名" clearable size="large">
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item label="昵称" prop="nickname">
            <el-input v-model="form.nickname" placeholder="请输入昵称" clearable size="large">
              <template #prefix>
                <el-icon><Avatar /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="form.email" placeholder="请输入邮箱" clearable size="large">
              <template #prefix>
                <el-icon><Message /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item label="验证码" prop="code">
            <div class="code-input">
              <el-input v-model="form.code" placeholder="请输入验证码" size="large">
                <template #prefix>
                  <el-icon><Key /></el-icon>
                </template>
              </el-input>
              <el-button
                type="primary"
                size="large"
                :disabled="isCountingDown || !canGetCode"
                @click="sendVerificationCode"
                class="code-btn"
              >
                {{ countdown > 0 ? `${countdown}s后重新获取` : '获取验证码' }}
              </el-button>
            </div>
          </el-form-item>
          
          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" placeholder="请输入密码" type="password" show-password size="large">
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="form.confirmPassword" placeholder="请再次输入密码" type="password" show-password size="large">
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          
          <el-form-item class="form-actions">
            <el-button type="primary" size="large" class="btn-register" :loading="loading" @click="handleRegister">
              <span v-if="!loading">立即注册 🚀</span>
              <span v-else>注册中...</span>
            </el-button>
          </el-form-item>

          <div class="form-links">
            <el-button link class="link-btn" @click="goToLogin">
              <span class="link-icon">🔑</span> 已有账号？去登录
            </el-button>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup lang="js">
import {computed, onUnmounted, ref} from 'vue';
import {useRouter} from 'vue-router';
import {ElMessage} from 'element-plus';
import { User, Lock, Avatar, Message, Key } from '@element-plus/icons-vue';
import AuthBrand from './components/AuthBrand.vue';
import {getEmailCode, register} from '@/api/auth.js';

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
.register-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  position: relative;
  overflow: hidden;
  background: linear-gradient(
    135deg,
    #e0f7fa 0%,
    #e8f5ff 20%,
    #e6f3ff 40%,
    #d4f1f4 60%,
    #e0e6ff 80%,
    #e6e6fa 100%
  );

  // 浮动气泡装饰
  &::before,
  &::after {
    content: "";
    position: fixed;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.2);
    filter: blur(60px);
    pointer-events: none;
    z-index: 0;
  }

  &::before {
    width: 400px;
    height: 400px;
    top: -100px;
    right: -100px;
    animation: floatBubble 20s infinite ease-in-out;
  }

  &::after {
    width: 300px;
    height: 300px;
    bottom: -80px;
    left: -80px;
    animation: floatBubble 15s infinite ease-in-out reverse;
  }
}

// 装饰云朵
.decoration-clouds {
  position: fixed;
  inset: 0;
  pointer-events: none;
  z-index: 1;

  .cloud-emoji {
    position: absolute;
    font-size: 60px;
    opacity: 0.3;
    animation: cloudFloat 6s ease-in-out infinite;

    &.c1 {
      top: 10%;
      left: 5%;
      animation-delay: 0s;
    }

    &.c2 {
      top: 20%;
      right: 10%;
      font-size: 80px;
      opacity: 0.2;
      animation-delay: 3s;
      animation-duration: 8s;
    }
  }
}

// 装饰粒子
.decoration-particles {
  position: fixed;
  inset: 0;
  pointer-events: none;
  z-index: 1;

  .particle {
    position: absolute;
    font-size: 24px;
    opacity: 0.5;
    animation: sparkle 3s ease-in-out infinite;

    &.p1 {
      top: 15%;
      right: 20%;
      animation-delay: 0s;
    }

    &.p2 {
      bottom: 25%;
      left: 15%;
      font-size: 20px;
      animation-delay: 1s;
    }

    &.p3 {
      top: 60%;
      right: 15%;
      animation-delay: 2s;
    }
  }
}

// 注册容器
.register-container {
  position: relative;
  z-index: 2;
  width: 100%;
  max-width: 580px;
  animation: slideInUp 0.8s cubic-bezier(0.34, 1.56, 0.64, 1);
}

// 欢迎横幅
.welcome-banner {
  position: relative;
  padding: 30px;
  margin-bottom: 24px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  border: 2px solid rgba(103, 58, 183, 0.15);
  box-shadow: 0 8px 32px rgba(103, 58, 183, 0.12);
  overflow: hidden;
  animation: bannerSlideIn 1s cubic-bezier(0.34, 1.56, 0.64, 1);

  // 顶部渐变装饰条
  &::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(90deg, #87ceeb, #9370db, #84c7d0, #87ceeb);
    background-size: 200% 100%;
    animation: gradientMove 3s linear infinite;
  }

  .banner-content {
    text-align: center;
    position: relative;
    z-index: 1;

    .banner-title {
      margin: 0 0 10px 0;
      font-size: 28px;
      font-weight: 800;
      background: linear-gradient(135deg, #5b9bd5, #9370db, #84c7d0);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      animation: titleGlow 2s ease-in-out infinite;

      .title-emoji {
        display: inline-block;
        animation: emojiSpin 3s linear infinite;
        margin: 0 8px;
      }
    }

    .banner-subtitle {
      margin: 0;
      font-size: 15px;
      color: #5b9bd5;
      font-weight: 500;
      letter-spacing: 0.5px;
    }
  }
}

// 注册卡片
.register-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  padding: 32px;
  box-shadow: 0 8px 32px rgba(103, 58, 183, 0.12);
  border: 2px solid rgba(103, 58, 183, 0.15);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  animation: fadeIn 0.8s ease-out;

  &:hover {
    box-shadow: 0 12px 48px rgba(103, 58, 183, 0.18);
    border-color: rgba(103, 58, 183, 0.25);
  }

  .card-header {
    display: flex;
    justify-content: center;
    margin-bottom: 20px;
    padding-bottom: 16px;
    border-bottom: 2px solid rgba(103, 58, 183, 0.1);
  }

  .card-tips {
    margin-bottom: 24px;
    padding: 16px;
    background: linear-gradient(135deg, rgba(227, 242, 253, 0.6) 0%, rgba(243, 229, 245, 0.6) 100%);
    border-radius: 12px;
    border: 1px solid rgba(103, 58, 183, 0.1);

    p {
      margin: 8px 0;
      font-size: 13px;
      color: #5e35b1;
      line-height: 1.6;

      &:first-child {
        margin-top: 0;
      }

      &:last-child {
        margin-bottom: 0;
      }
    }
  }
}

// 表单样式
.register-form {
  :deep(.el-form-item__label) {
    color: #5e35b1;
    font-weight: 600;
    font-size: 15px;
  }

  :deep(.el-input__wrapper) {
    border-radius: 12px;
    box-shadow: 0 0 0 1px rgba(103, 58, 183, 0.2) inset;
    transition: all 0.3s ease;

    &:hover {
      box-shadow: 0 0 0 1px rgba(103, 58, 183, 0.35) inset;
    }

    &.is-focus {
      box-shadow: 0 0 0 2px rgba(103, 58, 183, 0.4) inset;
    }
  }

  :deep(.el-input__prefix) {
    color: #9370db;
  }

  .code-input {
    display: flex;
    gap: 12px;

    .code-btn {
      width: 150px;
      flex-shrink: 0;
      border-radius: 12px;
      background: linear-gradient(135deg, #84c7d0, #9370db);
      border: none;
      box-shadow: 0 4px 12px rgba(103, 58, 183, 0.2);
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

      &:hover:not(:disabled) {
        transform: translateY(-2px);
        box-shadow: 0 6px 18px rgba(103, 58, 183, 0.3);
      }

      &:disabled {
        background: linear-gradient(135deg, #ccc, #999);
        cursor: not-allowed;
      }
    }
  }

  .form-actions {
    margin-top: 8px;
    margin-bottom: 0;

    :deep(.el-form-item__content) {
      margin-left: 0 !important;
    }
  }

  .btn-register {
    width: 100%;
    height: 48px;
    font-size: 16px;
    font-weight: 600;
    border: none;
    border-radius: 12px;
    background: linear-gradient(135deg, #5b9bd5, #9370db);
    box-shadow: 0 8px 24px rgba(103, 58, 183, 0.25);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

    &:hover:not(:disabled) {
      transform: translateY(-2px);
      box-shadow: 0 12px 32px rgba(103, 58, 183, 0.35);
    }

    &:active:not(:disabled) {
      transform: translateY(0);
    }
  }

  .form-links {
    display: flex;
    justify-content: center;
    margin-top: 16px;
    padding-top: 16px;
    border-top: 1px solid rgba(103, 58, 183, 0.1);

    .link-btn {
      color: #5b9bd5;
      font-size: 14px;
      transition: all 0.2s ease;

      .link-icon {
        margin-right: 4px;
      }

      &:hover {
        color: #9370db;
        transform: translateX(2px);
      }
    }
  }
}

// 动画定义
@keyframes floatBubble {
  0%, 100% {
    transform: translate(0, 0) scale(1);
  }
  25% {
    transform: translate(30px, -30px) scale(1.1);
  }
  50% {
    transform: translate(-20px, 20px) scale(0.9);
  }
  75% {
    transform: translate(20px, 30px) scale(1.05);
  }
}

@keyframes cloudFloat {
  0%, 100% {
    transform: translateY(0) translateX(0);
  }
  25% {
    transform: translateY(-20px) translateX(10px);
  }
  50% {
    transform: translateY(-10px) translateX(-10px);
  }
  75% {
    transform: translateY(-30px) translateX(5px);
  }
}

@keyframes sparkle {
  0%, 100% {
    opacity: 0.3;
    transform: scale(1) rotate(0deg);
  }
  50% {
    opacity: 0.8;
    transform: scale(1.2) rotate(180deg);
  }
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(50px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes bannerSlideIn {
  from {
    opacity: 0;
    transform: translateY(-30px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes titleGlow {
  0%, 100% {
    filter: drop-shadow(0 0 10px rgba(135, 206, 235, 0.5));
  }
  50% {
    filter: drop-shadow(0 0 20px rgba(135, 206, 235, 0.8));
  }
}

@keyframes emojiSpin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

@keyframes gradientMove {
  0% {
    background-position: 0% 0%;
  }
  100% {
    background-position: 200% 0%;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .register-page {
    padding: 20px 15px;
  }

  .register-container {
    max-width: 100%;
  }

  .welcome-banner {
    padding: 24px 20px;
    margin-bottom: 20px;

    .banner-title {
      font-size: 24px !important;
    }

    .banner-subtitle {
      font-size: 14px !important;
    }
  }

  .register-card {
    padding: 24px 20px;

    .card-tips {
      padding: 12px;

      p {
        font-size: 12px;
      }
    }
  }

  .decoration-clouds .cloud-emoji {
    &.c2 {
      display: none;
    }
  }

  .register-form {
    .code-input {
      flex-direction: column;
      gap: 8px;

      .code-btn {
        width: 100%;
      }
    }
  }
}

@media (max-width: 480px) {
  .welcome-banner {
    padding: 20px 16px;

    .banner-title {
      font-size: 20px !important;

      .title-emoji {
        margin: 0 4px;
      }
    }
  }

  .register-card {
    padding: 20px 16px;
  }
}
</style>
