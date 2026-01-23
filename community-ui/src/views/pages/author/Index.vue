<script setup>
import AuthorProfile from "@/views/pages/author/components/AuthorProfile.vue";
import Footer from "@/components/Footer.vue";
import HomeHeader from "@/components/Header.vue";
import Live2DViewer from "@/components/Live2D/Live2DViewer.vue";
</script>

<template>
  <Live2DViewer />
  <div class="author-page-container">
    <!-- 动漫风格装饰元素 -->
    <div class="anime-decorations">
      <!-- 浮动云朵 -->
      <div class="cloud cloud-1"></div>
      <div class="cloud cloud-2"></div>
      <div class="cloud cloud-3"></div>
      
      <!-- 闪烁星星 -->
      <div class="star star-1">★</div>
      <div class="star star-2">✦</div>
      <div class="star star-3">✧</div>
      <div class="star star-4">★</div>
      <div class="star star-5">✦</div>
      <div class="star star-6">✧</div>
      

      <!-- 漂浮心形 -->
      <div class="heart heart-1">♥</div>
      <div class="heart heart-2">♥</div>
      
      <!-- 音符装饰 -->
      <div class="note note-1">♪</div>
      <div class="note note-2">♫</div>
      
      <!-- 气泡 -->
      <div class="bubble bubble-1"></div>
      <div class="bubble bubble-2"></div>
      <div class="bubble bubble-3"></div>
      <div class="bubble bubble-4"></div>
      <div class="bubble bubble-5"></div>
    </div>

    <!-- 顶部装饰波浪 -->
    <div class="top-wave-decoration">
      <svg viewBox="0 0 1200 120" preserveAspectRatio="none">
        <path d="M0,50 C300,100 900,0 1200,50 L1200,0 L0,0 Z" fill="rgba(147, 197, 253, 0.15)"/>
      </svg>
    </div>

    <el-container class="main-container">
      <el-header>
        <HomeHeader />
      </el-header>
      <el-main>
        <div class="content-wrapper">
          <AuthorProfile />
        </div>
      </el-main>
      <el-footer>
        <Footer />
      </el-footer>
    </el-container>

    <!-- 底部装饰波浪 -->
    <div class="bottom-wave-decoration">
      <svg viewBox="0 0 1200 120" preserveAspectRatio="none">
        <path d="M0,50 C300,0 900,100 1200,50 L1200,120 L0,120 Z" fill="rgba(192, 132, 252, 0.12)"/>
      </svg>
    </div>
  </div>

  <!-- todo 隐私设置，是否展示页面数据 -->
</template>

<style scoped lang="scss">
.author-page-container {
  min-height: 100vh;
  width: 100%;
  position: relative;
  overflow-x: hidden;
  background: linear-gradient(
    135deg,
    #e0f7fa 0%,
    #f0f4ff 25%,
    #f3e8ff 50%,
    #dcfce7 75%,
    #e6f3ff 100%
  );

  // 添加全局光晕效果
  &::before,
  &::after {
    content: "";
    position: fixed;
    border-radius: 50%;
    filter: blur(100px);
    pointer-events: none;
    z-index: 0;
  }

  &::before {
    width: 600px;
    height: 600px;
    top: -200px;
    right: -200px;
    background: radial-gradient(circle, rgba(167, 139, 250, 0.2), transparent 70%);
    animation: pulse-glow 8s ease-in-out infinite;
  }

  &::after {
    width: 500px;
    height: 500px;
    bottom: -150px;
    left: -150px;
    background: radial-gradient(circle, rgba(134, 239, 172, 0.2), transparent 70%);
    animation: pulse-glow 10s ease-in-out infinite reverse;
  }

  // 动漫装饰元素容器
  .anime-decorations {
    position: fixed;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    pointer-events: none;
    z-index: 1;
    overflow: hidden;

    // 云朵样式
    .cloud {
      position: absolute;
      background: linear-gradient(135deg, rgba(255, 255, 255, 0.6) 0%, rgba(224, 242, 254, 0.4) 100%);
      border-radius: 100px;
      opacity: 0.7;
      animation: float-cloud 25s ease-in-out infinite;

      &::before,
      &::after {
        content: '';
        position: absolute;
        background: inherit;
        border-radius: 100px;
      }
    }

    .cloud-1 {
      width: 140px;
      height: 45px;
      top: 12%;
      left: 8%;
      animation-delay: 0s;

      &::before {
        width: 55px;
        height: 55px;
        top: -28px;
        left: 15px;
      }

      &::after {
        width: 65px;
        height: 45px;
        top: -22px;
        right: 15px;
      }
    }

    .cloud-2 {
      width: 120px;
      height: 40px;
      top: 25%;
      right: 12%;
      animation-delay: 8s;

      &::before {
        width: 50px;
        height: 50px;
        top: -25px;
        left: 18px;
      }

      &::after {
        width: 55px;
        height: 40px;
        top: -18px;
        right: 18px;
      }
    }

    .cloud-3 {
      width: 110px;
      height: 35px;
      bottom: 30%;
      left: 15%;
      animation-delay: 16s;

      &::before {
        width: 45px;
        height: 45px;
        top: -22px;
        left: 12px;
      }

      &::after {
        width: 50px;
        height: 35px;
        top: -15px;
        right: 12px;
      }
    }

    // 星星闪烁
    .star {
      position: absolute;
      font-size: 22px;
      color: #fbbf24;
      animation: twinkle-star 3s ease-in-out infinite;
      text-shadow: 0 0 10px rgba(251, 191, 36, 0.5);
    }

    .star-1 {
      top: 18%;
      right: 20%;
      animation-delay: 0s;
    }

    .star-2 {
      top: 35%;
      left: 12%;
      animation-delay: 1s;
    }

    .star-3 {
      top: 55%;
      right: 15%;
      animation-delay: 2s;
    }

    .star-4 {
      bottom: 25%;
      left: 18%;
      animation-delay: 0.5s;
    }

    .star-5 {
      bottom: 40%;
      right: 25%;
      animation-delay: 1.5s;
    }

    .star-6 {
      top: 70%;
      left: 25%;
      animation-delay: 2.5s;
    }

    // 樱花飘落动画已移除
    
    .sakura {
      display: none;
    }


    // 心形漂浮
    .heart {
      position: absolute;
      font-size: 20px;
      color: #f472b6;
      animation: float-heart 8s ease-in-out infinite;
      opacity: 0.6;
    }

    .heart-1 {
      top: 40%;
      left: 8%;
      animation-delay: 0s;
    }

    .heart-2 {
      bottom: 35%;
      right: 10%;
      animation-delay: 4s;
    }

    // 音符跳动
    .note {
      position: absolute;
      font-size: 26px;
      color: #a78bfa;
      animation: note-bounce 4s ease-in-out infinite;
      opacity: 0.7;
    }

    .note-1 {
      top: 45%;
      right: 8%;
      animation-delay: 0s;
    }

    .note-2 {
      bottom: 45%;
      left: 10%;
      animation-delay: 2s;
    }

    // 气泡上升
    .bubble {
      position: absolute;
      border-radius: 50%;
      background: radial-gradient(circle at 30% 30%, rgba(147, 197, 253, 0.4), rgba(59, 130, 246, 0.1));
      animation: bubble-rise 18s ease-in-out infinite;
    }

    .bubble-1 {
      width: 70px;
      height: 70px;
      bottom: -70px;
      left: 12%;
      animation-delay: 0s;
    }

    .bubble-2 {
      width: 90px;
      height: 90px;
      bottom: -90px;
      left: 35%;
      animation-delay: 6s;
    }

    .bubble-3 {
      width: 60px;
      height: 60px;
      bottom: -60px;
      right: 30%;
      animation-delay: 12s;
    }

    .bubble-4 {
      width: 80px;
      height: 80px;
      bottom: -80px;
      right: 15%;
      animation-delay: 3s;
    }

    .bubble-5 {
      width: 55px;
      height: 55px;
      bottom: -55px;
      left: 60%;
      animation-delay: 9s;
    }
  }

  // 顶部波浪装饰
  .top-wave-decoration {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 120px;
    z-index: 1;
    pointer-events: none;

    svg {
      width: 100%;
      height: 100%;
    }
  }

  // 底部波浪装饰
  .bottom-wave-decoration {
    position: fixed;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 120px;
    z-index: 1;
    pointer-events: none;

    svg {
      width: 100%;
      height: 100%;
    }
  }

  // 主容器
  .main-container {
    position: relative;
    z-index: 2;
    min-height: 100vh;
    padding-top: 80px;

    .content-wrapper {
      animation: fade-in-up 0.8s ease-out;
    }
  }
}

// 动画定义
@keyframes float-cloud {
  0%, 100% {
    transform: translate(0, 0);
  }
  25% {
    transform: translate(30px, -20px);
  }
  50% {
    transform: translate(-20px, 15px);
  }
  75% {
    transform: translate(25px, 25px);
  }
}

@keyframes twinkle-star {
  0%, 100% {
    opacity: 0.3;
    transform: scale(1) rotate(0deg);
  }
  50% {
    opacity: 1;
    transform: scale(1.3) rotate(180deg);
  }
}

@keyframes float-heart {
  0%, 100% {
    transform: translateY(0) scale(1);
  }
  25% {
    transform: translateY(-20px) scale(1.1);
  }
  50% {
    transform: translateY(-10px) scale(0.9);
  }
  75% {
    transform: translateY(-25px) scale(1.05);
  }
}

@keyframes note-bounce {
  0%, 100% {
    transform: translateY(0) rotate(-10deg);
  }
  25% {
    transform: translateY(-15px) rotate(5deg);
  }
  50% {
    transform: translateY(-5px) rotate(-5deg);
  }
  75% {
    transform: translateY(-20px) rotate(10deg);
  }
}

@keyframes bubble-rise {
  0% {
    transform: translateY(0) scale(1);
    opacity: 0;
  }
  10% {
    opacity: 0.6;
  }
  90% {
    opacity: 0.6;
  }
  100% {
    transform: translateY(-110vh) scale(1.5);
    opacity: 0;
  }
}

@keyframes pulse-glow {
  0%, 100% {
    transform: scale(1);
    opacity: 0.3;
  }
  50% {
    transform: scale(1.2);
    opacity: 0.5;
  }
}

@keyframes fade-in-up {
  0% {
    opacity: 0;
    transform: translateY(30px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

// Element Plus 样式覆盖
:deep(.el-header) {
  background: transparent;
  height: auto !important;
  padding: 0;
  overflow: visible !important;
}

:deep(.el-main) {
  background: transparent;
  overflow: visible !important;
  padding: 30px 20px;
}

:deep(.el-footer) {
  background: transparent;
  height: auto !important;
  padding: 0;
  overflow: visible !important;
}

:deep(.el-container) {
  overflow: visible !important;
  background: transparent;
}

// 响应式设计
@media (max-width: 768px) {
  .author-page-container {
    .main-container {
      padding-top: 70px;
    }

    .anime-decorations {
      .cloud,
      .heart,
      .note {
        display: none;
      }

      .star {
        font-size: 18px;
      }

      .bubble {
        width: 40px !important;
        height: 40px !important;
      }
    }

    :deep(.el-main) {
      padding: 20px 10px;
    }
  }
}
</style>
