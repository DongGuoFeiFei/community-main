<template>
  <div class="forgot-page">
    <!-- 装饰云朵 -->
    <div class="decoration-clouds">
      <Icon icon="fluent:cloud-24-filled" class="cloud-icon c1" />
      <Icon icon="fluent:cloud-24-filled" class="cloud-icon c2" />
      <Icon icon="fluent:cloud-24-filled" class="cloud-icon c3" />
    </div>

    <!-- 装饰粒子 -->
    <div class="decoration-particles">
      <Icon icon="ph:sparkle-fill" class="particle p1" />
      <Icon icon="ph:star-fill" class="particle p2" />
      <Icon icon="ph:flower-fill" class="particle p3" />
      <Icon icon="ph:heart-fill" class="particle p4" />
    </div>

    <div class="forgot-container">
      <!-- 标题横幅 -->
      <div class="title-banner">
        <div class="banner-content">
          <div class="icon-wrapper">
            <Icon icon="ph:key-duotone" class="key-icon" />
          </div>
          <h1 class="banner-title">找回密码</h1>
          <p class="banner-subtitle">别担心，我们会帮你找回账号</p>
        </div>
      </div>

      <!-- 找回密码卡片 -->
      <div class="forgot-card">
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="80px"
          class="forgot-form"
        >
          <el-form-item label="邮箱" prop="email">
            <el-input
              v-model="form.email"
              placeholder="请输入注册时使用的邮箱"
              clearable
              size="large"
            >
              <template #prefix>
                <el-icon><Message /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item class="form-actions">
            <el-button
              type="primary"
              size="large"
              class="btn-submit"
              :loading="loading"
              @click="handleSubmit"
            >
              <span v-if="!loading">
                <Icon icon="ph:paper-plane-tilt-fill" class="btn-icon" />
                发送重置链接
              </span>
              <span v-else>发送中...</span>
            </el-button>
          </el-form-item>

          <div class="form-links">
            <el-button link class="link-btn" @click="goToLogin">
              <Icon icon="ph:arrow-left-bold" class="link-icon" />
              返回登录
            </el-button>
          </div>
        </el-form>

        <!-- 提示信息 -->
        <div class="tips-box">
          <div class="tips-header">
            <Icon icon="ph:info-fill" class="tips-icon" />
            <span class="tips-title">温馨提示</span>
          </div>
          <ul class="tips-list">
            <li>重置链接将发送至您的注册邮箱</li>
            <li>链接有效期为30分钟，请及时使用</li>
            <li>如未收到邮件，请检查垃圾邮件箱</li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { Message } from "@element-plus/icons-vue";
import { Icon } from "@iconify/vue";
import { forgotPassword } from "@/api/auth.js";

// 表单类型定义
type ForgotForm = {
  email: string;
};

const router = useRouter();
const loading = ref(false);
const formRef = ref();

// 表单数据
const form = reactive<ForgotForm>({
  email: "",
});

// 表单验证规则
const rules = {
  email: [
    { required: true, message: "请输入邮箱地址", trigger: "blur" },
    {
      type: "email",
      message: "请输入正确的邮箱地址",
      trigger: ["blur", "change"],
    },
  ],
};

/**
 * 处理表单提交
 */
const handleSubmit = () => {
  formRef.value.validate(async (valid: boolean) => {
    if (!valid) return;
    loading.value = true;
    try {
      const res: any = await forgotPassword(form);
      if (res?.code === 200) {
        ElMessage.success("重置链接已发送至您的邮箱，请查收");
        // 3秒后跳转到登录页
        setTimeout(() => {
          router.push("/login");
        }, 3000);
      } else {
        ElMessage.error(res?.msg || "发送失败");
      }
    } catch (error: any) {
      console.error("发送出错:", error);
      ElMessage.error(error?.response?.data?.msg || "发送失败，请稍后重试");
    } finally {
      loading.value = false;
    }
  });
};

/**
 * 返回登录页
 */
const goToLogin = () => {
  router.push("/login");
};
</script>

<style scoped lang="scss">
// 页面主容器
.forgot-page {
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

  .cloud-icon {
    position: absolute;
    font-size: 60px;
    color: rgba(135, 206, 235, 0.3);
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

    &.c3 {
      bottom: 15%;
      left: 8%;
      font-size: 70px;
      opacity: 0.25;
      animation-delay: 5s;
      animation-duration: 10s;
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
    color: rgba(147, 112, 219, 0.5);
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
      color: rgba(132, 199, 208, 0.5);
    }

    &.p4 {
      bottom: 40%;
      right: 25%;
      font-size: 18px;
      animation-delay: 2.5s;
      color: rgba(255, 182, 193, 0.5);
    }
  }
}

// 找回密码容器
.forgot-container {
  position: relative;
  z-index: 2;
  width: 100%;
  max-width: 480px;
  animation: slideInUp 0.8s cubic-bezier(0.34, 1.56, 0.64, 1);
}

// 标题横幅
.title-banner {
  position: relative;
  padding: 32px;
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

    .icon-wrapper {
      display: inline-flex;
      align-items: center;
      justify-content: center;
      width: 80px;
      height: 80px;
      margin-bottom: 16px;
      background: linear-gradient(135deg, #5b9bd5, #9370db);
      border-radius: 50%;
      box-shadow: 0 8px 24px rgba(103, 58, 183, 0.3);
      animation: iconFloat 3s ease-in-out infinite;

      .key-icon {
        font-size: 40px;
        color: white;
      }
    }

    .banner-title {
      margin: 0 0 8px 0;
      font-size: 26px;
      font-weight: 800;
      background: linear-gradient(135deg, #5b9bd5, #9370db, #84c7d0);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      animation: titleGlow 2s ease-in-out infinite;
    }

    .banner-subtitle {
      margin: 0;
      font-size: 14px;
      color: #5b9bd5;
      font-weight: 500;
      letter-spacing: 0.5px;
    }
  }
}

// 找回密码卡片
.forgot-card {
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
}

// 表单样式
.forgot-form {
  margin-bottom: 24px;

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

  .btn-submit {
    width: 100%;
    height: 48px;
    font-size: 16px;
    font-weight: 600;
    border: none;
    border-radius: 12px;
    background: linear-gradient(135deg, #5b9bd5, #9370db);
    box-shadow: 0 8px 24px rgba(103, 58, 183, 0.25);
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

    .btn-icon {
      margin-right: 6px;
      vertical-align: middle;
    }

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
        vertical-align: middle;
      }

      &:hover {
        color: #9370db;
        transform: translateX(-2px);
      }
    }
  }
}

// 提示信息框
.tips-box {
  padding: 20px;
  background: linear-gradient(
    135deg,
    rgba(135, 206, 235, 0.1),
    rgba(147, 112, 219, 0.1)
  );
  border-radius: 12px;
  border: 1px solid rgba(103, 58, 183, 0.15);

  .tips-header {
    display: flex;
    align-items: center;
    margin-bottom: 12px;
    color: #5e35b1;

    .tips-icon {
      font-size: 20px;
      margin-right: 8px;
    }

    .tips-title {
      font-size: 15px;
      font-weight: 600;
    }
  }

  .tips-list {
    margin: 0;
    padding-left: 28px;
    list-style: none;

    li {
      position: relative;
      font-size: 13px;
      color: #5b9bd5;
      line-height: 1.8;
      padding-left: 12px;

      &::before {
        content: "•";
        position: absolute;
        left: 0;
        color: #9370db;
        font-weight: bold;
      }
    }
  }
}

// 动画定义
@keyframes floatBubble {
  0%,
  100% {
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
  0%,
  100% {
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
  0%,
  100% {
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
  0%,
  100% {
    filter: drop-shadow(0 0 10px rgba(135, 206, 235, 0.5));
  }
  50% {
    filter: drop-shadow(0 0 20px rgba(135, 206, 235, 0.8));
  }
}

@keyframes iconFloat {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
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
  .forgot-page {
    padding: 20px 15px;
  }

  .forgot-container {
    max-width: 100%;
  }

  .title-banner {
    padding: 24px 20px;
    margin-bottom: 20px;

    .banner-content {
      .icon-wrapper {
        width: 70px;
        height: 70px;
        margin-bottom: 12px;

        .key-icon {
          font-size: 35px;
        }
      }

      .banner-title {
        font-size: 22px;
      }

      .banner-subtitle {
        font-size: 13px;
      }
    }
  }

  .forgot-card {
    padding: 24px 20px;
  }

  .decoration-clouds .cloud-icon {
    &.c3 {
      display: none;
    }
  }

  .decoration-particles .particle {
    &.p4 {
      display: none;
    }
  }
}

@media (max-width: 480px) {
  .title-banner {
    padding: 20px 16px;

    .banner-content {
      .icon-wrapper {
        width: 60px;
        height: 60px;

        .key-icon {
          font-size: 30px;
        }
      }

      .banner-title {
        font-size: 20px;
      }
    }
  }

  .forgot-card {
    padding: 20px 16px;
  }

  .tips-box {
    padding: 16px;

    .tips-list li {
      font-size: 12px;
    }
  }
}
</style>
