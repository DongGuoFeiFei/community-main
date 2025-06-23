<!-- src/views/NotFound.vue -->
<template>
  <div class="not-found-container">
    <div class="not-found-content">
      <div class="error-code">404</div>
      <h1 class="error-title">页面未找到</h1>
      <p class="error-message">您访问的页面不存在或已被移除</p>

      <div class="action-buttons">
        <el-button
            type="primary"
            size="large"
            @click="goBack"
            class="back-button"
        >
          <el-icon><ArrowLeft /></el-icon>
          返回上一页
        </el-button>

        <el-button
            size="large"
            @click="goHome"
            class="home-button"
        >
          <el-icon><HomeFilled /></el-icon>
          返回首页
        </el-button>
      </div>

      <div class="floating-elements">
        <div class="floating-circle circle-1"></div>
        <div class="floating-circle circle-2"></div>
        <div class="floating-circle circle-3"></div>
      </div>
    </div>

    <div class="illustration">
      <img src="@/assets/images/404-illustration.png" alt="404 illustration">
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { ArrowLeft, HomeFilled } from '@element-plus/icons-vue';

const router = useRouter();

const goBack = () => {
  router.go(-1);
};

const goHome = () => {
  router.push('/');
};
</script>

<style lang="scss" scoped>
.not-found-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100vh;
  padding: 0 5%;
  background-color: #f8f8f8;

  .not-found-content {
    max-width: 500px;

    .error-code {
      font-size: 120px;
      font-weight: 700;
      color: var(--el-color-primary);
      line-height: 1;
      margin-bottom: 20px;
    }

    .error-title {
      font-size: 32px;
      margin-bottom: 16px;
      color: #333;
    }

    .error-message {
      font-size: 18px;
      color: #666;
      margin-bottom: 32px;
    }

    .action-buttons {
      display: flex;
      gap: 16px;

      .back-button, .home-button {
        padding: 12px 24px;
        font-size: 16px;

        :deep(.el-icon) {
          margin-right: 8px;
        }
      }
    }
  }

  .illustration {
    max-width: 50%;

    img {
      width: 100%;
      height: auto;
    }
  }
}

@media (max-width: 768px) {
  .not-found-container {
    flex-direction: column;
    justify-content: center;
    text-align: center;
    padding: 0 20px;

    .not-found-content {
      margin-bottom: 40px;

      .error-code {
        font-size: 80px;
      }

      .error-title {
        font-size: 24px;
      }

      .error-message {
        font-size: 16px;
      }
    }

    .illustration {
      max-width: 80%;
    }
  }
}
.floating-elements {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: -1;

  .floating-circle {
    position: absolute;
    border-radius: 50%;
    background: rgba(var(--el-color-primary-rgb), 0.1);
    animation: float 15s infinite ease-in-out;

    &.circle-1 {
      width: 150px;
      height: 150px;
      top: 20%;
      left: 10%;
      animation-delay: 0s;
    }

    &.circle-2 {
      width: 200px;
      height: 200px;
      top: 60%;
      left: 70%;
      animation-delay: 3s;
    }

    &.circle-3 {
      width: 100px;
      height: 100px;
      top: 80%;
      left: 30%;
      animation-delay: 6s;
    }
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-50px) rotate(10deg);
  }
}
</style>