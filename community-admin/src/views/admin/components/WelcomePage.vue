<template>
  <div class="welcome-container">
    <!-- 欢迎卡片 -->
    <el-card class="welcome-card">
      <div class="welcome-content">
        <el-avatar :size="120" :src="userAvatar" class="user-avatar">
          <el-icon :size="60">
            <User/>
          </el-icon>
        </el-avatar>
        <div class="welcome-text">
          <h1>欢迎回来，{{ username }}！</h1>
          <p class="welcome-subtext">今天是 {{ currentDate }}，{{ greetingMessage }}</p>
        </div>
      </div>

      <div class="status-cards">
        <el-card shadow="hover" class="status-card">
          <div class="card-content">
            <el-icon :size="30" color="#67C23A">
              <SuccessFilled/>
            </el-icon>
            <div>
              <div class="card-title">确定登录</div>
              <div class="card-value">yes, sir</div>
            </div>
          </div>
        </el-card>

        <el-card shadow="hover" class="status-card">
          <div class="card-content">
            <el-icon :size="30" color="#E6A23C">
              <SuccessFilled/>
            </el-icon>
            <div>
              <div class="card-title">调整网络</div>
              <div class="card-value">yes, sir</div>
            </div>
          </div>
        </el-card>

        <el-card shadow="hover" class="status-card">
          <div class="card-content">
            <el-icon :size="30" color="#409EFF">
              <SuccessFilled/>
            </el-icon>
            <div>
              <div class="card-title">调整心情</div>
              <div class="card-value">yes, sir</div>
            </div>
          </div>
        </el-card>
      </div>

    </el-card>

    <!-- 装饰性元素 -->
    <div class="decoration-elements">
      <div class="decoration-circle"></div>
      <div class="decoration-square"></div>
      <div class="decoration-triangle"></div>
    </div>
  </div>
</template>

<script setup>
import {computed, ref} from 'vue'
import {SuccessFilled, User} from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import {localStores} from "@/stores/localStores.js";

const store = localStores()
// 用户信息
const username = ref(store.userInfo.userInfo.nickname || '管理员')
const userAvatar = ref(store.baseURL + (store.userInfo.avatarUrl || '/default-avatar.png'))

// 当前日期和时间
const currentDate = computed(() => dayjs().format('YYYY年MM月DD日 dddd'))

// 根据时间显示不同的问候语
const greetingMessage = computed(() => {
  const hour = dayjs().hour()
  if (hour < 6) return '凌晨好，新的一天开始了！'
  if (hour < 9) return '早上好，今天也要元气满满！'
  if (hour < 12) return '上午好，工作顺利吗？'
  if (hour < 14) return '中午好，记得午休哦！'
  if (hour < 18) return '下午好，保持专注！'
  if (hour < 22) return '晚上好，今天辛苦了！'
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