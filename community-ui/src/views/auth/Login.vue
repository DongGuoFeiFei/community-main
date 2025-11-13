<template>
  <div class="login-page">
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

    <div class="login-container">
      <!-- 欢迎横幅 -->
      <div class="welcome-banner">
        <div class="banner-content">
          <h1 class="banner-title">
            <span class="title-emoji">🌸</span>
            欢迎来到采芙蓉社区
            <span class="title-emoji">✨</span>
          </h1>
          <p class="banner-subtitle">探索有趣的内容，结识志同道合的朋友</p>
        </div>
      </div>

      <!-- 登录卡片 -->
      <div class="login-card">
        <div class="card-header">
          <AuthBrand />
        </div>

        <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" class="login-form">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username" placeholder="请输入用户名" clearable size="large">
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" placeholder="请输入密码" type="password" show-password size="large">
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item label="验证码" prop="captchaCode">
            <CaptchaInput :model="form" :captcha-image="captchaImage" @refresh="refreshCaptcha" />
          </el-form-item>

          <el-form-item class="form-actions">
            <el-button type="primary" size="large" class="btn-login" :loading="loading" @click="handleLogin">
              <span v-if="!loading">进入社区 🚀</span>
              <span v-else>登录中...</span>
            </el-button>
          </el-form-item>

          <div class="form-links">
            <el-button link class="link-btn" @click="goToRegister">
              <span class="link-icon">📝</span> 注册新账号
            </el-button>
            <el-button link class="link-btn" @click="goToForgotPassword">
              <span class="link-icon">🔑</span> 找回密码
            </el-button>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import AuthBrand from './components/AuthBrand.vue'
import CaptchaInput from './components/CaptchaInput.vue'
import { getCaptcha, login } from '@/api/auth.js'
import { localStores } from '@/stores/localStores.js'

type LoginForm = {
  username: string
  password: string
  captchaCode: string
  captchaKey: string
}

const lStore = localStores()
const router = useRouter()
const loading = ref(false)
const formRef = ref()
const captchaImage = ref('')

const form = reactive<LoginForm>({
  username: '',
  password: '',
  captchaCode: '',
  captchaKey: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  captchaCode: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

const refreshCaptcha = async () => {
  try {
    const res = await getCaptcha()
    captchaImage.value = res.data.image
    form.captchaKey = res.data.code
  } catch (error) {
    console.error('获取验证码失败:', error)
    ElMessage.error('获取验证码失败')
  }
}

onMounted(() => {
  refreshCaptcha()
})

const handleLogin = () => {
  formRef.value.validate(async (valid: boolean) => {
    if (!valid) return
    loading.value = true
    try {
      const res: any = await login(form)
      if (res?.token) {
        lStore.userInfo = { ...res }
        lStore.tokenInfo.token = res.token
        lStore.tokenInfo.refreshTime = res.userInfo.lastLogin
        lStore.tokenInfo.expiresIn = res.expiresIn
        localStorage.setItem('token', res.token)
        ElMessage.success('登录成功')
        router.push('/')
      } else {
        ElMessage.error(res?.msg || '登录失败')
        refreshCaptcha()
      }
    } catch (error: any) {
      console.error('登录出错:', error)
      ElMessage.error(error?.response?.data?.msg || '登录失败，请检查用户名或密码')
      refreshCaptcha()
    } finally {
      loading.value = false
    }
  })
}

const goToRegister = () => router.push('/register')
const goToForgotPassword = () => router.push('/forgot-password')
</script>

<style scoped lang="scss">
.login-page {
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

// 登录容器
.login-container {
  position: relative;
  z-index: 2;
  width: 100%;
  max-width: 520px;
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

// 登录卡片
.login-card {
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
    margin-bottom: 24px;
    padding-bottom: 20px;
    border-bottom: 2px solid rgba(103, 58, 183, 0.1);
  }
}

// 表单样式
.login-form {
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

  .form-actions {
    margin-top: 8px;
    margin-bottom: 0;

    :deep(.el-form-item__content) {
      margin-left: 0 !important;
    }
  }

  .btn-login {
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
    justify-content: space-between;
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
  .login-page {
    padding: 20px 15px;
  }

  .login-container {
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

  .login-card {
    padding: 24px 20px;
  }

  .decoration-clouds .cloud-emoji {
    &.c2 {
      display: none;
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

  .login-card {
    padding: 20px 16px;
  }

  .login-form {
    .form-links {
      flex-direction: column;
      gap: 8px;
      align-items: center;
    }
  }
}
</style>
