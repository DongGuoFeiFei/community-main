<script setup>
import UserHeader from "@/components/UserHeader.vue";
import UserFooter from "@/components/UserFooter.vue";
import WelcomePage from "@/components/WelcomePage.vue";
import Live2DViewer from "@/components/Live2D/Live2DViewer.vue";
import CherryBlossom from "@/components/Particles/CherryBlossom.vue";
import FloatingHearts from "@/components/Particles/FloatingHearts.vue";
</script>

<template>
  <Live2DViewer />
  <!-- 樱花飞舞特效 -->
  <!-- <CherryBlossom /> -->
  <!-- 浮动心形粒子 -->
  <!-- <FloatingHearts /> -->

  <div class="main-container">
    <!-- 背景装饰：云朵和天空 -->
    <div class="background-decoration">
      <div class="cloud cloud-1"></div>
      <div class="cloud cloud-2"></div>
      <div class="cloud cloud-3"></div>
      <div class="cloud cloud-4"></div>
      <!-- 粉色小区域点缀 -->
      <div class="pink-accent accent-1"></div>
      <div class="pink-accent accent-2"></div>
      <div class="pink-accent accent-3"></div>
      <!-- 星星装饰 -->
      <div class="star star-1">✨</div>
      <div class="star star-2">⭐</div>
      <div class="star star-3">✨</div>
      <div class="star star-4">⭐</div>
      <div class="star star-5">✨</div>
    </div>

    <div class="common-layout">
      <el-container class="layout-container">
        <el-header class="header">
          <user-header />
        </el-header>
        <el-container class="content-container">
          <el-main class="main-content">
            <router-view v-slot="{ Component, route }">
              <transition name="page-fade" mode="out-in" appear>
                <component
                  :is="Component"
                  v-if="Component"
                  :key="route.path"
                ></component>
                <div v-else key="welcome">
                  <WelcomePage />
                </div>
              </transition>
            </router-view>
          </el-main>
        </el-container>
        <el-footer class="footer">
          <UserFooter />
        </el-footer>
      </el-container>
    </div>
  </div>
</template>

<style scoped lang="scss">
// 动漫风格配色
$sky-blue: #87ceeb; // 天空蓝
$mint-blue: #b0e0e6; // 薄荷蓝
$lavender: #e6e6fa; // 薰衣草紫
$light-purple: #dda0dd; // 淡紫色
$mint-green: #98fb98; // 薄荷绿
$soft-green: #90ee90; // 嫩绿色
$pink-accent: #ffb6c1; // 粉色点缀

.main-container {
  position: relative;
  min-height: 100vh;
  width: 100%;
  padding-top: 80px; // 为固定头部预留空间，避免内容被覆盖
  display: flex;
  flex-direction: column;
  // 与主页保持一致的背景渐变
  background: linear-gradient(
    135deg,
    #e0f7fa 0%,
    /* 薄荷蓝 */ #e8f5ff 20%,
    /* 天空蓝 */ #e6f3ff 40%,
    #d4f1f4 60%,
    /* 淡青 */ #e0e6ff 80%,
    /* 淡紫 */ #e6e6fa 100% /* 薰衣草 */
  );
  // 禁止水平滚动，允许内容自然延伸，使用浏览器滚动
  overflow-x: hidden;
  overflow-y: visible;

  // 添加浮动气泡装饰（与主页保持一致）
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

  // 背景装饰层
  .background-decoration {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 0;
    pointer-events: none;
    overflow: hidden;

    // 云朵样式
    .cloud {
      position: absolute;
      background: rgba(255, 255, 255, 0.7);
      border-radius: 50px;
      opacity: 0.6;
      animation: float 20s infinite ease-in-out;

      &::before,
      &::after {
        content: "";
        position: absolute;
        background: rgba(255, 255, 255, 0.7);
        border-radius: 50px;
      }

      &.cloud-1 {
        width: 120px;
        height: 40px;
        top: 10%;
        left: 10%;
        animation-duration: 25s;

        &::before {
          width: 60px;
          height: 60px;
          top: -30px;
          left: 10px;
        }

        &::after {
          width: 80px;
          height: 50px;
          top: -20px;
          right: 10px;
        }
      }

      &.cloud-2 {
        width: 100px;
        height: 35px;
        top: 20%;
        right: 15%;
        animation-duration: 30s;
        animation-delay: -5s;

        &::before {
          width: 50px;
          height: 50px;
          top: -25px;
          left: 15px;
        }

        &::after {
          width: 70px;
          height: 45px;
          top: -15px;
          right: 15px;
        }
      }

      &.cloud-3 {
        width: 140px;
        height: 45px;
        top: 60%;
        left: 5%;
        animation-duration: 35s;
        animation-delay: -10s;

        &::before {
          width: 70px;
          height: 70px;
          top: -35px;
          left: 20px;
        }

        &::after {
          width: 90px;
          height: 55px;
          top: -25px;
          right: 20px;
        }
      }

      &.cloud-4 {
        width: 110px;
        height: 38px;
        top: 75%;
        right: 8%;
        animation-duration: 28s;
        animation-delay: -15s;

        &::before {
          width: 55px;
          height: 55px;
          top: -28px;
          left: 12px;
        }

        &::after {
          width: 75px;
          height: 48px;
          top: -18px;
          right: 12px;
        }
      }
    }

    // 粉色小区域点缀
    .pink-accent {
      position: absolute;
      border-radius: 50%;
      background: radial-gradient(
        circle,
        rgba(255, 182, 193, 0.4) 0%,
        rgba(255, 182, 193, 0.1) 50%,
        transparent 100%
      );
      animation: pulse 4s infinite ease-in-out;

      &.accent-1 {
        width: 200px;
        height: 200px;
        top: 15%;
        right: 20%;
        animation-delay: 0s;
      }

      &.accent-2 {
        width: 150px;
        height: 150px;
        top: 50%;
        left: 15%;
        animation-delay: 1.5s;
      }

      &.accent-3 {
        width: 180px;
        height: 180px;
        bottom: 20%;
        right: 10%;
        animation-delay: 3s;
      }
    }

    // 星星装饰
    .star {
      position: absolute;
      font-size: 24px;
      opacity: 0.6;
      animation: twinkle 3s ease-in-out infinite;
      pointer-events: none;

      &.star-1 {
        top: 25%;
        left: 8%;
        animation-delay: 0s;
        font-size: 20px;
      }

      &.star-2 {
        top: 35%;
        right: 12%;
        animation-delay: 0.5s;
        font-size: 28px;
      }

      &.star-3 {
        top: 55%;
        left: 12%;
        animation-delay: 1s;
        font-size: 22px;
      }

      &.star-4 {
        top: 65%;
        right: 8%;
        animation-delay: 1.5s;
        font-size: 26px;
      }

      &.star-5 {
        top: 80%;
        left: 50%;
        animation-delay: 2s;
        font-size: 20px;
      }
    }
  }

  .common-layout {
    flex: 1;
    display: flex;
    min-height: 100%;
    position: relative;
    z-index: 1;
    width: 100%;
    // 禁止内部滚动，只使用浏览器滚动
    overflow-x: hidden;
    overflow-y: visible;

    .layout-container {
      display: flex;
      flex-direction: column;
      width: 100%;
      min-height: 100%;
      // 禁止内部滚动
      overflow-x: hidden;
      overflow-y: visible;

      .header {
        padding: 0;
        height: 80px; // 与首页Header高度一致
        position: relative; // UserHeader组件本身已经是fixed定位，这里只需要相对定位
        z-index: 200; // 根据开发规范，sticky元素使用200
        // 禁止内部滚动
        overflow: visible;
      }

      .content-container {
        flex: 1;
        display: flex;
        flex-direction: column;
        // 禁止内部滚动
        overflow-x: hidden;
        overflow-y: visible;

        .main-content {
          flex: 1;
          padding: 30px 20px; // 与主页保持一致的内边距
          max-width: 1200px;
          width: 100%;
          margin: 0 auto;
          position: relative;
          z-index: 1;
          // 禁止内部滚动，只使用浏览器滚动
          overflow-x: hidden;
          overflow-y: visible;

          // 内容区毛玻璃效果增强
          &::before {
            content: "";
            position: absolute;
            top: -20px;
            left: -20px;
            right: -20px;
            bottom: -20px;
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(5px);
            border-radius: 30px;
            z-index: -1;
            pointer-events: none;
          }
        }
      }

      .footer {
        padding: 0;
        position: relative;
        z-index: 1;
        // 禁止内部滚动
        overflow: visible;
      }
    }
  }

  // 深度选择器：确保所有 Element Plus 组件都不产生内部滚动条
  :deep(.el-container) {
    overflow-x: hidden !important;
    overflow-y: visible !important;
    height: auto !important;
    max-height: none !important;
  }

  :deep(.el-main) {
    overflow-x: hidden !important;
    overflow-y: visible !important;
    height: auto !important;
    max-height: none !important;
  }

  :deep(.el-footer) {
    overflow-x: hidden !important;
    overflow-y: visible !important;
    height: auto !important;
  }

  :deep(.el-header) {
    overflow-x: hidden !important;
    overflow-y: visible !important;
    height: auto !important;
  }
}

// 浮动气泡动画（与主页保持一致）
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

// 云朵浮动动画
@keyframes float {
  0%,
  100% {
    transform: translateX(0) translateY(0);
  }
  25% {
    transform: translateX(20px) translateY(-10px);
  }
  50% {
    transform: translateX(-15px) translateY(5px);
  }
  75% {
    transform: translateX(10px) translateY(-5px);
  }
}

// 粉色点缀脉冲动画
@keyframes pulse {
  0%,
  100% {
    transform: scale(1);
    opacity: 0.3;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.5;
  }
}

// 星星闪烁动画
@keyframes twinkle {
  0%,
  100% {
    opacity: 0.4;
    transform: scale(1) rotate(0deg);
  }
  50% {
    opacity: 0.9;
    transform: scale(1.2) rotate(180deg);
  }
}

// 页面切换过渡动画
.page-fade-enter-active {
  animation: fadeInUp 0.5s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

.page-fade-leave-active {
  animation: fadeOutDown 0.3s ease-in;
}

@keyframes fadeInUp {
  0% {
    opacity: 0;
    transform: translateY(20px) scale(0.95);
  }
  100% {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

@keyframes fadeOutDown {
  0% {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
  100% {
    opacity: 0;
    transform: translateY(-20px) scale(0.95);
  }
}
</style>
