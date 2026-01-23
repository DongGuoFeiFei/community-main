<template>
  <div class="welcome-container">
    <!-- 浮动装饰元素 -->
    <div class="floating-decorations">
      <div class="cloud cloud-1"></div>
      <div class="cloud cloud-2"></div>
      <div class="cloud cloud-3"></div>
      <div class="star star-1">★</div>
      <div class="star star-2">★</div>
      <div class="star star-3">✦</div>
      <div class="bubble bubble-1"></div>
      <div class="bubble bubble-2"></div>
      <div class="bubble bubble-3"></div>
    </div>

    <!-- 欢迎卡片 -->
    <el-card class="welcome-card">
      <!-- 彩虹渐变标题栏 -->
      <div class="rainbow-header"></div>
      
      <div class="welcome-content">
        <div class="avatar-wrapper">
          <div class="avatar-glow"></div>
          <el-avatar :size="120" :src="userAvatar" class="user-avatar">
            <el-icon :size="60">
              <User/>
            </el-icon>
          </el-avatar>
          <div class="avatar-sparkles">
            <span class="sparkle">✨</span>
            <span class="sparkle">✨</span>
            <span class="sparkle">✨</span>
          </div>
        </div>
        
        <div class="welcome-text">
          <h1 class="welcome-title">
            <span class="wave-text">欢迎回来，{{ username }}！</span>
          </h1>
          <div class="greeting-box">
            <el-icon class="greeting-icon" :size="20">
              <Sunny v-if="currentHour >= 6 && currentHour < 18"/>
              <Moon v-else/>
            </el-icon>
            <p class="welcome-subtext">{{ currentDate }} · {{ greetingMessage }}</p>
          </div>
        </div>
      </div>

      <!-- 状态卡片 -->
      <div class="status-cards">
        <div class="status-card status-card-1">
          <div class="card-icon-wrapper">
            <el-icon :size="32" class="card-icon">
              <CircleCheckFilled/>
            </el-icon>
          </div>
          <div class="card-info">
            <div class="card-title">系统状态</div>
            <div class="card-value">运行正常</div>
          </div>
          <div class="card-decoration">✓</div>
        </div>

        <div class="status-card status-card-2">
          <div class="card-icon-wrapper">
            <el-icon :size="32" class="card-icon">
              <Connection/>
            </el-icon>
          </div>
          <div class="card-info">
            <div class="card-title">网络连接</div>
            <div class="card-value">稳定畅通</div>
          </div>
          <div class="card-decoration">◆</div>
        </div>

        <div class="status-card status-card-3">
          <div class="card-icon-wrapper">
            <el-icon :size="32" class="card-icon">
              <Sunny/>
            </el-icon>
          </div>
          <div class="card-info">
            <div class="card-title">今日心情</div>
            <div class="card-value">元气满满</div>
          </div>
          <div class="card-decoration">♪</div>
        </div>
      </div>

      <!-- 快捷操作区 -->
      <div class="quick-actions">
        <div class="action-title">
          <el-icon :size="18">
            <Star/>
          </el-icon>
          <span>快捷操作</span>
        </div>
        <div class="action-buttons">
          <el-button type="primary" :icon="Document" round>查看文章</el-button>
          <el-button type="success" :icon="ChatDotRound" round>消息通知</el-button>
          <el-button type="info" :icon="User" round>用户管理</el-button>
          <el-button type="warning" :icon="DataAnalysis" round>数据统计</el-button>
        </div>
      </div>
    </el-card>

    <!-- 底部装饰波浪 -->
    <div class="wave-decoration">
      <svg viewBox="0 0 1200 120" preserveAspectRatio="none">
        <path d="M0,50 C300,100 900,0 1200,50 L1200,120 L0,120 Z" fill="rgba(147, 197, 253, 0.1)"/>
      </svg>
    </div>
  </div>
</template>

<script setup>
import {computed, ref} from 'vue'
import {
  CircleCheckFilled,
  User,
  Sunny,
  Moon,
  Connection,
  Star,
  Document,
  ChatDotRound,
  DataAnalysis
} from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import {localStores} from "@/stores/localStores.js";

const store = localStores()

// 用户信息
const username = ref(store.userInfo.userInfo.nickname || '管理员')
const userAvatar = ref(store.baseURL + (store.userInfo.avatarUrl || '/default-avatar.png'))

// 当前日期和时间
const currentDate = computed(() => dayjs().format('YYYY年MM月DD日 dddd'))
const currentHour = computed(() => dayjs().hour())

// 根据时间显示不同的问候语
const greetingMessage = computed(() => {
  const hour = dayjs().hour()
  if (hour < 6) return '凌晨好，新的一天开始了'
  if (hour < 9) return '早上好，今天也要元气满满'
  if (hour < 12) return '上午好，工作顺利吗'
  if (hour < 14) return '中午好，记得午休哦'
  if (hour < 18) return '下午好，保持专注'
  if (hour < 22) return '晚上好，今天辛苦了'
  return '夜深了，早点休息吧'
})
</script>

<style lang="scss" scoped>
.welcome-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 70vh;
  position: relative;
  padding: 20px;
  user-select: none;

  .welcome-card {
    width: 100%;
    max-width: 900px;
    border-radius: 16px;
    box-shadow: 0 6px 30px rgba(0, 0, 0, 0.1);
    overflow: hidden;

    .welcome-content {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 40px 40px 20px;
      text-align: center;
      background: linear-gradient(135deg, var(--el-color-primary-light-9), #fff);

      .user-avatar {
        margin-bottom: 20px;
        background-color: var(--el-color-primary-light-8);
        color: var(--el-color-primary);
        border: 3px solid var(--el-color-primary-light-5);
      }

      .welcome-text {
        h1 {
          margin: 0;
          font-size: 32px;
          color: var(--el-text-color-primary);
          font-weight: 600;
        }

        .welcome-subtext {
          margin: 12px 0 0;
          font-size: 18px;
          color: var(--el-text-color-secondary);
        }
      }
    }

    .status-cards {
      display: flex;
      justify-content: space-around;
      padding: 20px 0;
      margin: 20px 0;
      border-top: 1px solid var(--el-border-color-light);
      border-bottom: 1px solid var(--el-border-color-light);

      .status-card {
        width: 30%;
        border-radius: 10px;
        transition: transform 0.3s;

        &:hover {
          transform: translateY(-5px);
        }

        .card-content {
          display: flex;
          align-items: center;
          padding: 15px;

          .el-icon {
            margin-right: 15px;
          }

          .card-title {
            font-size: 14px;
            color: var(--el-text-color-secondary);
          }

          .card-value {
            font-size: 18px;
            font-weight: 500;
            margin-top: 5px;
          }
        }
      }
    }


  }

  .decoration-elements {
    position: absolute;
    width: 100%;
    height: 100%;
    pointer-events: none;
    z-index: -1;

    .decoration-circle {
      position: absolute;
      width: 250px;
      height: 250px;
      border-radius: 50%;
      background-color: var(--el-color-primary-light-9);
      top: 10%;
      right: 10%;
      opacity: 0.8;
    }

    .decoration-square {
      position: absolute;
      width: 180px;
      height: 180px;
      background-color: var(--el-color-primary-light-9);
      bottom: 15%;
      left: 10%;
      transform: rotate(15deg);
      opacity: 0.8;
    }

    .decoration-triangle {
      position: absolute;
      width: 0;
      height: 0;
      border-left: 100px solid transparent;
      border-right: 100px solid transparent;
      border-bottom: 180px solid var(--el-color-primary-light-9);
      top: 30%;
      left: 5%;
      transform: rotate(-15deg);
      opacity: 0.6;
    }
  }
}

@media (max-width: 768px) {
  .welcome-container {
    .welcome-card {
      .welcome-content {
        padding: 30px 20px;
      }

      .status-cards {
        flex-direction: column;
        align-items: center;

        .status-card {
          width: 80%;
          margin-bottom: 15px;
        }
      }
    }

    .decoration-elements {
      display: none;
    }
  }
}
</style>

/* 动漫风格美化样式 */

/* 浮动装饰元素 */
.floating-decorations {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
  overflow: hidden;

  /* 云朵动画 */
  .cloud {
    position: absolute;
    background: linear-gradient(135deg, #e0f2fe 0%, #bae6fd 100%);
    border-radius: 100px;
    opacity: 0.6;
    animation: float 20s ease-in-out infinite;

    &::before,
    &::after {
      content: '';
      position: absolute;
      background: inherit;
      border-radius: 100px;
    }
  }

  .cloud-1 {
    width: 120px;
    height: 40px;
    top: 10%;
    left: 10%;
    animation-delay: 0s;

    &::before {
      width: 50px;
      height: 50px;
      top: -25px;
      left: 10px;
    }

    &::after {
      width: 60px;
      height: 40px;
      top: -20px;
      right: 10px;
    }
  }

  .cloud-2 {
    width: 100px;
    height: 35px;
    top: 20%;
    right: 15%;
    animation-delay: 5s;

    &::before {
      width: 45px;
      height: 45px;
      top: -20px;
      left: 15px;
    }

    &::after {
      width: 50px;
      height: 35px;
      top: -15px;
      right: 15px;
    }
  }

  .cloud-3 {
    width: 90px;
    height: 30px;
    bottom: 25%;
    left: 20%;
    animation-delay: 10s;

    &::before {
      width: 40px;
      height: 40px;
      top: -18px;
      left: 10px;
    }

    &::after {
      width: 45px;
      height: 30px;
      top: -12px;
      right: 10px;
    }
  }

  /* 星星闪烁 */
  .star {
    position: absolute;
    font-size: 24px;
    color: #fbbf24;
    animation: twinkle 3s ease-in-out infinite;
  }

  .star-1 {
    top: 15%;
    right: 25%;
    animation-delay: 0s;
  }

  .star-2 {
    top: 60%;
    right: 10%;
    animation-delay: 1s;
  }

  .star-3 {
    bottom: 20%;
    right: 30%;
    animation-delay: 2s;
  }

  /* 气泡上浮 */
  .bubble {
    position: absolute;
    border-radius: 50%;
    background: radial-gradient(circle at 30% 30%, rgba(167, 139, 250, 0.3), rgba(139, 92, 246, 0.1));
    animation: bubble-rise 15s ease-in-out infinite;
  }

  .bubble-1 {
    width: 60px;
    height: 60px;
    bottom: -60px;
    left: 15%;
    animation-delay: 0s;
  }

  .bubble-2 {
    width: 80px;
    height: 80px;
    bottom: -80px;
    left: 50%;
    animation-delay: 5s;
  }

  .bubble-3 {
    width: 50px;
    height: 50px;
    bottom: -50px;
    right: 20%;
    animation-delay: 10s;
  }
}

/* 欢迎卡片样式 */
.welcome-card {
  position: relative;
  z-index: 1;
  border: none !important;
  box-shadow: 0 10px 40px rgba(139, 92, 246, 0.15) !important;
  transition: transform 0.3s ease, box-shadow 0.3s ease;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 15px 50px rgba(139, 92, 246, 0.25) !important;
  }

  /* 彩虹渐变标题栏 */
  .rainbow-header {
    height: 6px;
    background: linear-gradient(90deg,
      #93c5fd 0%,
      #a78bfa 25%,
      #c084fc 50%,
      #86efac 75%,
      #93c5fd 100%
    );
    animation: rainbow-slide 8s linear infinite;
    background-size: 200% 100%;
  }

  .welcome-content {
    background: linear-gradient(135deg,
      rgba(224, 242, 254, 0.5) 0%,
      rgba(243, 232, 255, 0.3) 50%,
      rgba(220, 252, 231, 0.2) 100%
    );
    padding: 50px 40px 30px;
    position: relative;

    /* 头像包装器 */
    .avatar-wrapper {
      position: relative;
      margin-bottom: 25px;

      /* 头像光晕效果 */
      .avatar-glow {
        position: absolute;
        width: 140px;
        height: 140px;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        background: radial-gradient(circle, rgba(167, 139, 250, 0.4), transparent 70%);
        border-radius: 50%;
        animation: pulse-glow 2s ease-in-out infinite;
      }

      .user-avatar {
        position: relative;
        z-index: 1;
        border: 4px solid #fff;
        box-shadow: 0 8px 24px rgba(139, 92, 246, 0.3);
        transition: transform 0.3s ease;

        &:hover {
          transform: scale(1.05) rotate(5deg);
        }
      }

      /* 头像周围的闪光 */
      .avatar-sparkles {
        position: absolute;
        width: 100%;
        height: 100%;
        top: 0;
        left: 0;

        .sparkle {
          position: absolute;
          font-size: 20px;
          animation: sparkle-rotate 4s linear infinite;

          &:nth-child(1) {
            top: 0;
            right: 10px;
            animation-delay: 0s;
          }

          &:nth-child(2) {
            bottom: 10px;
            left: 0;
            animation-delay: 1.3s;
          }

          &:nth-child(3) {
            top: 10px;
            left: -10px;
            animation-delay: 2.6s;
          }
        }
      }
    }

    /* 欢迎文字 */
    .welcome-text {
      .welcome-title {
        margin: 0 0 20px;
        font-size: 36px;
        font-weight: 700;
        background: linear-gradient(135deg, #6366f1, #a855f7, #ec4899);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;

        .wave-text {
          display: inline-block;
          animation: wave-animation 2s ease-in-out infinite;
        }
      }

      .greeting-box {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
        padding: 12px 24px;
        background: rgba(255, 255, 255, 0.7);
        border-radius: 50px;
        backdrop-filter: blur(10px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);

        .greeting-icon {
          color: #fbbf24;
          animation: icon-bounce 2s ease-in-out infinite;
        }

        .welcome-subtext {
          margin: 0;
          font-size: 16px;
          color: #64748b;
          font-weight: 500;
        }
      }
    }
  }

  /* 状态卡片 */
  .status-cards {
    display: flex;
    justify-content: space-between;
    gap: 20px;
    padding: 30px 20px;
    background: linear-gradient(180deg, rgba(255, 255, 255, 0.5), rgba(248, 250, 252, 0.8));

    .status-card {
      flex: 1;
      position: relative;
      padding: 24px 20px;
      border-radius: 16px;
      overflow: hidden;
      cursor: pointer;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

      &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        opacity: 0;
        transition: opacity 0.3s ease;
      }

      &:hover {
        transform: translateY(-8px) scale(1.02);

        &::before {
          opacity: 1;
        }

        .card-decoration {
          transform: scale(1.5) rotate(360deg);
        }
      }

      /* 卡片1 - 蓝色系 */
      &.status-card-1 {
        background: linear-gradient(135deg, #dbeafe 0%, #bfdbfe 100%);
        border: 2px solid #93c5fd;

        &::before {
          background: linear-gradient(135deg, rgba(147, 197, 253, 0.3), rgba(59, 130, 246, 0.1));
        }

        .card-icon {
          color: #3b82f6;
        }
      }

      /* 卡片2 - 紫色系 */
      &.status-card-2 {
        background: linear-gradient(135deg, #f3e8ff 0%, #e9d5ff 100%);
        border: 2px solid #c084fc;

        &::before {
          background: linear-gradient(135deg, rgba(192, 132, 252, 0.3), rgba(168, 85, 247, 0.1));
        }

        .card-icon {
          color: #a855f7;
        }
      }

      /* 卡片3 - 绿色系 */
      &.status-card-3 {
        background: linear-gradient(135deg, #dcfce7 0%, #bbf7d0 100%);
        border: 2px solid #86efac;

        &::before {
          background: linear-gradient(135deg, rgba(134, 239, 172, 0.3), rgba(74, 222, 128, 0.1));
        }

        .card-icon {
          color: #22c55e;
        }
      }

      .card-icon-wrapper {
        display: flex;
        justify-content: center;
        margin-bottom: 12px;

        .card-icon {
          animation: icon-float 3s ease-in-out infinite;
        }
      }

      .card-info {
        text-align: center;

        .card-title {
          font-size: 14px;
          color: #64748b;
          margin-bottom: 8px;
          font-weight: 500;
        }

        .card-value {
          font-size: 20px;
          font-weight: 700;
          color: #1e293b;
        }
      }

      .card-decoration {
        position: absolute;
        top: 10px;
        right: 10px;
        font-size: 40px;
        opacity: 0.15;
        transition: all 0.5s cubic-bezier(0.68, -0.55, 0.265, 1.55);
      }
    }
  }

  /* 快捷操作区 */
  .quick-actions {
    padding: 30px 20px;
    background: linear-gradient(180deg, rgba(248, 250, 252, 0.8), rgba(255, 255, 255, 0.9));

    .action-title {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
      margin-bottom: 20px;
      font-size: 18px;
      font-weight: 600;
      color: #475569;

      .el-icon {
        color: #fbbf24;
        animation: icon-spin 4s linear infinite;
      }
    }

    .action-buttons {
      display: flex;
      justify-content: center;
      gap: 15px;
      flex-wrap: wrap;

      .el-button {
        font-weight: 500;
        padding: 12px 24px;
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-3px);
          box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
        }
      }
    }
  }
}

/* 底部波浪装饰 */
.wave-decoration {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 120px;
  z-index: 0;

  svg {
    width: 100%;
    height: 100%;
  }
}

/* 动画定义 */
@keyframes float {
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
    transform: translateY(-25px) translateX(5px);
  }
}

@keyframes twinkle {
  0%, 100% {
    opacity: 0.3;
    transform: scale(1);
  }
  50% {
    opacity: 1;
    transform: scale(1.2);
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
    transform: translateY(-100vh) scale(1.5);
    opacity: 0;
  }
}

@keyframes rainbow-slide {
  0% {
    background-position: 0% 50%;
  }
  100% {
    background-position: 200% 50%;
  }
}

@keyframes pulse-glow {
  0%, 100% {
    transform: translate(-50%, -50%) scale(1);
    opacity: 0.5;
  }
  50% {
    transform: translate(-50%, -50%) scale(1.1);
    opacity: 0.8;
  }
}

@keyframes sparkle-rotate {
  0% {
    transform: rotate(0deg) scale(1);
    opacity: 0.5;
  }
  50% {
    transform: rotate(180deg) scale(1.2);
    opacity: 1;
  }
  100% {
    transform: rotate(360deg) scale(1);
    opacity: 0.5;
  }
}

@keyframes wave-animation {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-5px);
  }
}

@keyframes icon-bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-5px);
  }
}

@keyframes icon-float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

@keyframes icon-spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .welcome-container {
    padding: 10px;

    .welcome-card {
      .welcome-content {
        padding: 30px 20px;

        .welcome-text {
          .welcome-title {
            font-size: 28px;
          }

          .greeting-box {
            .welcome-subtext {
              font-size: 14px;
            }
          }
        }
      }

      .status-cards {
        flex-direction: column;
        gap: 15px;
      }

      .quick-actions {
        .action-buttons {
          flex-direction: column;

          .el-button {
            width: 100%;
          }
        }
      }
    }

    .floating-decorations {
      .cloud,
      .star,
      .bubble {
        display: none;
      }
    }
  }
}
