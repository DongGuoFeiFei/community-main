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

