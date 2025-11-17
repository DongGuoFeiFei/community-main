<template>
  <div class="user-welcome-page">
    <!-- 用户欢迎卡片 - 带彩虹渐变标题栏 -->
    <el-card class="welcome-card">
      <!-- 彩虹渐变标题栏 -->
      <div class="rainbow-header"></div>

      <div class="welcome-header">
        <el-avatar
          @click="openViewer"
          class="avatar-clickable"
          :size="80"
          :src="store.baseURL + userInfo.userInfo.avatar || defaultAvatar"
        />
        <!-- 图片查看器 -->
        <el-image-viewer
          v-if="showViewer"
          :url-list="[
            store.baseURL + userInfo.userInfo.avatar || defaultAvatar,
          ]"
          :initial-index="0"
          @close="closeViewer"
        />
        <div class="welcome-text">
          <h2 class="welcome-title">
            欢迎回来, {{ userInfo.userInfo.nickname }}!
          </h2>
          <p class="welcome-subtitle">
            今天是 {{ currentDate }}，祝您度过愉快的一天
          </p>
        </div>
      </div>

      <!-- 用户数据概览 - 萌系动物耳朵造型卡片 -->
      <div class="user-stats">
        <div class="stat-item stat-card-ear">
          <div class="stat-value">{{ userStats.postCount || 0 }}</div>
          <div class="stat-label">发布的帖子</div>
        </div>
        <div class="stat-item stat-card-ear">
          <div class="stat-value">{{ userStats.followers || 0 }}</div>
          <div class="stat-label">粉丝</div>
        </div>
        <div class="stat-item stat-card-ear">
          <div class="stat-value">{{ userStats.following || 0 }}</div>
          <div class="stat-label">关注</div>
        </div>
      </div>
    </el-card>

    <!-- 快速操作区域 -->
    <div class="quick-actions">
      <h3 class="section-title">
        <span class="title-icon">✨</span>
        快速开始
        <span class="title-icon">✨</span>
      </h3>
      <div class="actions-grid">
        <el-card
          shadow="hover"
          class="action-card action-card-anime"
          @click="navigateTo('/editor')"
          @mouseenter="handleCardHover"
        >
          <div class="card-icon-wrapper">
            <el-icon :size="36">
              <Edit />
            </el-icon>
          </div>
          <span>发布新内容</span>
        </el-card>
        <el-card
          shadow="hover"
          class="action-card action-card-anime"
          @click="navigateTo('/myself/notifications')"
          @mouseenter="handleCardHover"
        >
          <div class="card-icon-wrapper">
            <el-icon :size="36">
              <Message />
            </el-icon>
          </div>
          <span>查看消息</span>
        </el-card>
        <el-card
          shadow="hover"
          class="action-card action-card-anime"
          @click="navigateTo('/myself/profile')"
          @mouseenter="handleCardHover"
        >
          <div class="card-icon-wrapper">
            <el-icon :size="36">
              <Setting />
            </el-icon>
          </div>
          <span>账户设置</span>
        </el-card>
        <el-card
          shadow="hover"
          class="action-card action-card-anime"
          @click="navigateTo('/myself/collections')"
          @mouseenter="handleCardHover"
        >
          <div class="card-icon-wrapper">
            <el-icon :size="36">
              <Star />
            </el-icon>
          </div>
          <span>收藏内容</span>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { Edit, Message, Setting, Star } from "@element-plus/icons-vue";
import { localStores } from "@/stores/localStores.js";
import { getUserStats } from "@/api/user.js";

// 默认头像
const defaultAvatar = ref("/images/default-avatar.png");

// 获取用户信息和状态
const store = localStores();
const userInfo = computed(() => store.userInfo);

// 用户统计数据
const userStats = ref({
  postCount: 0,
  followers: 0,
  following: 0,
});

// 最近活动
const recentActivities = ref([
  { time: "2023-05-15 14:30", content: "您发布了新帖子《Vue3最佳实践》" },
  { time: "2023-05-14 09:15", content: "您的评论获得了3个赞" },
  { time: "2023-05-13 18:45", content: '您关注了用户"前端开发者"' },
]);

// 当前日期
const currentDate = computed(() => {
  const now = new Date();
  return now.toLocaleDateString("zh-CN", {
    year: "numeric",
    month: "long",
    day: "numeric",
    weekday: "long",
  });
});

const router = useRouter();

// 导航方法
const navigateTo = (path) => {
  router.push(path);
};

const showViewer = ref(false);

const openViewer = () => {
  if (store.baseURL + userInfo.value.userInfo.avatar) {
    showViewer.value = true;
  }
};

const closeViewer = () => {
  showViewer.value = false;
};

// 处理卡片悬停事件 - 触发樱花飞舞特效
const handleCardHover = (event) => {
  // 创建临时樱花效果
  const card = event.currentTarget;
  const rect = card.getBoundingClientRect();
  const cherryCount = 5;

  for (let i = 0; i < cherryCount; i++) {
    setTimeout(() => {
      const cherry = document.createElement("div");
      cherry.textContent = "🌸";
      cherry.style.position = "fixed";
      cherry.style.left = rect.left + rect.width / 2 + "px";
      cherry.style.top = rect.top + rect.height / 2 + "px";
      cherry.style.fontSize = "20px";
      cherry.style.pointerEvents = "none";
      cherry.style.zIndex = "3000";

      // 随机方向和距离
      const randomX = (Math.random() - 0.5) * 100;
      const randomY = -50 - Math.random() * 50;
      const randomRotate = Math.random() * 360;

      cherry.style.setProperty("--random-x", randomX + "px");
      cherry.style.setProperty("--random-y", randomY + "px");
      cherry.style.animation = `cherryFloat 2s ease-out forwards`;
      cherry.style.animationDelay = `${i * 0.1}s`;

      document.body.appendChild(cherry);

      setTimeout(() => {
        if (cherry.parentNode) {
          cherry.remove();
        }
      }, 2000);
    }, i * 50);
  }
};

// 加载用户数据
onMounted(async () => {
  try {
    console.log(userInfo.value);
    const response = await getUserStats(userInfo.value.userInfo.userId);
    if (response.code === 200) {
      userStats.value = response.data;
    }
  } catch (error) {
    console.error("获取用户统计数据失败:", error);
  }
});
</script>

<style lang="scss" scoped>
// 动漫风格配色
$sky-blue: #87ceeb;
$mint-blue: #b0e0e6;
$lavender: #e6e6fa;
$light-purple: #dda0dd;
$mint-green: #98fb98;
$soft-green: #90ee90;
$pink-accent: #ffb6c1;
$cotton-white: #fff8f0;

.avatar-clickable {
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
  border: 3px solid rgba(255, 182, 193, 0.3);
  box-shadow: 0 4px 15px rgba(135, 206, 235, 0.3);

  &:hover {
    transform: scale(1.1) rotate(5deg);
    box-shadow: 0 6px 20px rgba(135, 206, 235, 0.5);
    border-color: rgba(255, 182, 193, 0.6);
  }
}

.user-welcome-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;

  .welcome-card {
    margin-bottom: 24px;
    border-radius: 20px;
    overflow: hidden;
    background: rgba(255, 255, 255, 0.9);
    backdrop-filter: blur(10px);
    box-shadow: 0 8px 32px rgba(135, 206, 235, 0.2),
      0 0 0 1px rgba(255, 255, 255, 0.5) inset;
    border: 2px solid rgba(135, 206, 235, 0.3);
    position: relative;
    transition: all 0.4s cubic-bezier(0.68, -0.55, 0.265, 1.55);

    // 卡片光泽效果
    &::after {
      content: "";
      position: absolute;
      top: 0;
      left: -100%;
      width: 100%;
      height: 100%;
      background: linear-gradient(
        90deg,
        transparent,
        rgba(255, 255, 255, 0.4),
        transparent
      );
      transition: left 0.6s;
    }

    &:hover {
      transform: translateY(-8px) scale(1.01);
      box-shadow: 0 16px 48px rgba(135, 206, 235, 0.4),
        0 0 0 2px rgba(221, 160, 221, 0.3) inset;
      border-color: rgba(221, 160, 221, 0.5);

      &::after {
        left: 100%;
      }
    }

    // 彩虹渐变标题栏
    .rainbow-header {
      height: 6px;
      background: linear-gradient(
        90deg,
        #ff6b9d 0%,
        #c44569 16.66%,
        #f8b500 33.33%,
        #98fb98 50%,
        #87ceeb 66.66%,
        #dda0dd 83.33%,
        #ffb6c1 100%
      );
      position: relative;
      overflow: hidden;

      // 漫画风格速度线装饰
      &::before {
        content: "";
        position: absolute;
        top: 0;
        left: -100%;
        width: 100%;
        height: 100%;
        background: repeating-linear-gradient(
          90deg,
          transparent,
          transparent 10px,
          rgba(255, 255, 255, 0.3) 10px,
          rgba(255, 255, 255, 0.3) 12px
        );
        animation: speedLine 3s linear infinite;
      }
    }

    .welcome-header {
      display: flex;
      align-items: center;
      padding: 30px 20px;
      gap: 24px;
      background: linear-gradient(
        135deg,
        rgba(255, 255, 255, 0.9) 0%,
        rgba(224, 247, 250, 0.5) 100%
      );

      .welcome-text {
        flex: 1;

        .welcome-title {
          margin: 0;
          font-size: 28px;
          background: linear-gradient(135deg, #87ceeb 0%, #dda0dd 100%);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          background-clip: text;
          font-weight: 600;
          animation: titleGlow 3s ease-in-out infinite;
        }

        .welcome-subtitle {
          margin: 8px 0 0;
          font-size: 15px;
          color: #666;
          font-weight: 400;
        }
      }
    }

    .user-stats {
      display: flex;
      justify-content: space-around;
      padding: 30px 20px;
      background: linear-gradient(
        135deg,
        rgba(255, 248, 240, 0.8) 0%,
        rgba(224, 247, 250, 0.6) 100%
      );
      gap: 20px;

      .stat-item {
        flex: 1;
        text-align: center;
        padding: 20px 15px;
        position: relative;
        transition: all 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);

        // 萌系动物耳朵造型
        &.stat-card-ear {
          background: linear-gradient(
            135deg,
            rgba(255, 255, 255, 0.9) 0%,
            rgba(152, 251, 152, 0.3) 100%
          );
          border-radius: 20px;
          border: 2px solid rgba(152, 251, 152, 0.4);
          box-shadow: 0 4px 15px rgba(152, 251, 152, 0.2);

          // 左耳朵
          &::before {
            content: "";
            position: absolute;
            top: -15px;
            left: 20%;
            width: 0;
            height: 0;
            border-left: 15px solid transparent;
            border-right: 15px solid transparent;
            border-bottom: 25px solid rgba(152, 251, 152, 0.6);
            transform: rotate(-20deg);
            transition: all 0.3s ease;
          }

          // 右耳朵
          &::after {
            content: "";
            position: absolute;
            top: -15px;
            right: 20%;
            width: 0;
            height: 0;
            border-left: 15px solid transparent;
            border-right: 15px solid transparent;
            border-bottom: 25px solid rgba(152, 251, 152, 0.6);
            transform: rotate(20deg);
            transition: all 0.3s ease;
          }

          &:hover {
            transform: translateY(-8px) scale(1.05);
            box-shadow: 0 8px 25px rgba(152, 251, 152, 0.4);

            &::before {
              transform: rotate(-25deg) scale(1.1);
            }

            &::after {
              transform: rotate(25deg) scale(1.1);
            }
          }
        }

        .stat-value {
          font-size: 32px;
          font-weight: bold;
          background: linear-gradient(135deg, #87ceeb 0%, #dda0dd 100%);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          background-clip: text;
          margin-bottom: 8px;
          transition: all 0.3s ease;
        }

        .stat-label {
          font-size: 14px;
          color: #666;
          font-weight: 500;
        }

        &:hover .stat-value {
          transform: scale(1.2);
        }
      }
    }
  }

  .quick-actions {
    margin-bottom: 24px;
    position: relative;

    // 背景装饰
    &::before {
      content: "";
      position: absolute;
      top: -10px;
      left: 50%;
      transform: translateX(-50%);
      width: 100px;
      height: 3px;
      background: linear-gradient(
        90deg,
        transparent,
        rgba(135, 206, 235, 0.5),
        rgba(221, 160, 221, 0.5),
        rgba(135, 206, 235, 0.5),
        transparent
      );
      border-radius: 2px;
    }

    .section-title {
      font-size: 24px;
      margin: 30px 0 28px;
      text-align: center;
      background: linear-gradient(
        135deg,
        #87ceeb 0%,
        #dda0dd 50%,
        #98fb98 100%
      );
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      font-weight: 700;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 16px;
      position: relative;
      text-shadow: 0 2px 10px rgba(135, 206, 235, 0.3);

      // 标题下方的装饰线
      &::after {
        content: "";
        position: absolute;
        bottom: -10px;
        left: 50%;
        transform: translateX(-50%);
        width: 60px;
        height: 2px;
        background: linear-gradient(
          90deg,
          transparent,
          #87ceeb,
          #dda0dd,
          transparent
        );
        border-radius: 2px;
      }

      .title-icon {
        font-size: 22px;
        animation: sparkle 2s ease-in-out infinite;
        filter: drop-shadow(0 2px 4px rgba(255, 215, 0, 0.5));

        &:nth-child(1) {
          animation-delay: 0s;
        }

        &:nth-child(3) {
          animation-delay: 1s;
        }
      }
    }

    .actions-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
      gap: 20px;

      .action-card {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 140px;
        cursor: pointer;
        border-radius: 20px;
        background: rgba(255, 255, 255, 0.9);
        backdrop-filter: blur(10px);
        border: 2px solid rgba(135, 206, 235, 0.3);
        position: relative;
        overflow: hidden;

        // 弹性动画效果
        &.action-card-anime {
          transition: all 0.4s cubic-bezier(0.68, -0.55, 0.265, 1.55);
          position: relative;
          overflow: visible;

          // 悬停时的光晕效果
          &::before {
            content: "";
            position: absolute;
            top: 50%;
            left: 50%;
            width: 0;
            height: 0;
            border-radius: 50%;
            background: radial-gradient(
              circle,
              rgba(135, 206, 235, 0.3) 0%,
              transparent 70%
            );
            transform: translate(-50%, -50%);
            transition: all 0.5s ease;
            pointer-events: none;
            z-index: -1;
          }

          &:hover {
            transform: translateY(-12px) scale(1.06);
            box-shadow: 0 16px 40px rgba(135, 206, 235, 0.5),
              0 0 20px rgba(221, 160, 221, 0.3);
            border-color: rgba(135, 206, 235, 0.7);

            &::before {
              width: 200px;
              height: 200px;
            }
          }

          &:active {
            transform: translateY(-8px) scale(1.02);
            transition: all 0.1s ease;
          }
        }

        .card-icon-wrapper {
          margin-bottom: 12px;
          transition: all 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
        }

        &:hover .card-icon-wrapper {
          transform: rotate(10deg) scale(1.2);
        }

        .el-icon {
          color: #87ceeb;
          transition: all 0.3s ease;
        }

        &:hover .el-icon {
          color: #dda0dd;
        }

        span {
          font-size: 16px;
          font-weight: 500;
          color: #666;
          transition: all 0.3s ease;
        }

        &:hover span {
          color: #87ceeb;
        }
      }
    }
  }
}

// 速度线动画
@keyframes speedLine {
  0% {
    left: -100%;
  }
  100% {
    left: 100%;
  }
}

// 标题发光动画
@keyframes titleGlow {
  0%,
  100% {
    filter: drop-shadow(0 0 5px rgba(135, 206, 235, 0.5));
  }
  50% {
    filter: drop-shadow(0 0 15px rgba(221, 160, 221, 0.8));
  }
}

// 闪烁动画
@keyframes sparkle {
  0%,
  100% {
    opacity: 1;
    transform: scale(1);
  }
  50% {
    opacity: 0.5;
    transform: scale(1.2);
  }
}
</style>
