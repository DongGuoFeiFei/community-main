<template>
  <div class="user-welcome-page">
    <!-- 顶部装饰 -->
    <div class="page-decoration">
      <span class="deco-star star-1">⭐</span>
      <span class="deco-star star-2">✨</span>
      <span class="deco-star star-3">💫</span>
      <span class="deco-star star-4">⭐</span>
    </div>

    <!-- 用户欢迎卡片 - 带彩虹渐变标题栏 -->
    <div class="welcome-card">
      <!-- 彩虹渐变标题栏 -->
      <div class="rainbow-header">
        <div class="rainbow-shine"></div>
      </div>

      <div class="welcome-header">
        <div class="avatar-container">
          <el-avatar
            @click="openViewer"
            class="avatar-clickable"
            :size="100"
            :src="store.baseURL + userInfo.userInfo.avatar || defaultAvatar"
          />
          <div class="avatar-decoration">
            <span class="deco-circle circle-1"></span>
            <span class="deco-circle circle-2"></span>
            <span class="deco-circle circle-3"></span>
          </div>
          <div class="avatar-stars">
            <span class="star-icon">⭐</span>
            <span class="star-icon">✨</span>
            <span class="star-icon">⭐</span>
          </div>
        </div>
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
            <span class="title-emoji">🌸</span>
            欢迎回来, {{ userInfo.userInfo.nickname }}!
            <span class="title-emoji">✨</span>
          </h2>
          <p class="welcome-subtitle">
            <span class="subtitle-icon">📅</span>
            {{ currentDate }}
          </p>
          <p class="welcome-greeting">
            <span class="greeting-icon">💖</span>
            祝您度过愉快的一天
          </p>
        </div>
      </div>

      <!-- 用户数据概览 - 萌系动物耳朵造型卡片 -->
      <div class="user-stats">
        <div class="stat-item stat-card-ear stat-posts">
          <div class="stat-icon">📝</div>
          <div class="stat-value">{{ userStats.postCount || 0 }}</div>
          <div class="stat-label">发布的帖子</div>
        </div>
        <div class="stat-item stat-card-ear stat-followers">
          <div class="stat-icon">👥</div>
          <div class="stat-value">{{ userStats.followers || 0 }}</div>
          <div class="stat-label">粉丝</div>
        </div>
        <div class="stat-item stat-card-ear stat-following">
          <div class="stat-icon">💫</div>
          <div class="stat-value">{{ userStats.following || 0 }}</div>
          <div class="stat-label">关注</div>
        </div>
      </div>
    </div>

    <!-- 快速操作区域 -->
    <div class="quick-actions">
      <h3 class="section-title">
        <span class="title-line"></span>
        <span class="title-icon">✨</span>
        <span class="title-text">快速开始</span>
        <span class="title-icon">✨</span>
        <span class="title-line"></span>
      </h3>
      <div class="actions-grid">
        <div
          class="action-card action-card-anime action-edit"
          @click="navigateTo('/editor')"
         
        >
          <div class="card-bg-decoration"></div>
          <div class="card-icon-wrapper">
            <el-icon :size="40">
              <Edit />
            </el-icon>
          </div>
          <span class="card-title">发布新内容</span>
          <span class="card-subtitle">创作你的精彩</span>
        </div>
        <div
          class="action-card action-card-anime action-message"
          @click="navigateTo('/myself/notifications')"
        
        >
          <div class="card-bg-decoration"></div>
          <div class="card-icon-wrapper">
            <el-icon :size="40">
              <Message />
            </el-icon>
          </div>
          <span class="card-title">查看消息</span>
          <span class="card-subtitle">不错过任何动态</span>
        </div>
        <div
          class="action-card action-card-anime action-setting"
          @click="navigateTo('/myself/profile')"
        
        >
          <div class="card-bg-decoration"></div>
          <div class="card-icon-wrapper">
            <el-icon :size="40">
              <Setting />
            </el-icon>
          </div>
          <span class="card-title">账户设置</span>
          <span class="card-subtitle">管理个人信息</span>
        </div>
        <div
          class="action-card action-card-anime action-star"
          @click="navigateTo('/myself/collections')"
         
        >
          <div class="card-bg-decoration"></div>
          <div class="card-icon-wrapper">
            <el-icon :size="40">
              <Star />
            </el-icon>
          </div>
          <span class="card-title">收藏内容</span>
          <span class="card-subtitle">查看我的收藏</span>
        </div>
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
$gradient-1: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
$gradient-2: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
$gradient-3: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
$gradient-4: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);

.user-welcome-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0;
  position: relative;
  overflow: visible;

  // 页面装饰
  .page-decoration {
    position: absolute;
    top: -20px;
    left: 0;
    right: 0;
    height: 100px;
    pointer-events: none;
    z-index: 0;

    .deco-star {
      position: absolute;
      font-size: 24px;
      animation: starFloat 3s ease-in-out infinite;
      filter: drop-shadow(0 2px 8px rgba(255, 215, 0, 0.5));

      &.star-1 {
        top: 10px;
        left: 10%;
        animation-delay: 0s;
      }

      &.star-2 {
        top: 30px;
        left: 30%;
        animation-delay: 0.5s;
      }

      &.star-3 {
        top: 20px;
        right: 30%;
        animation-delay: 1s;
      }

      &.star-4 {
        top: 40px;
        right: 10%;
        animation-delay: 1.5s;
      }
    }
  }

  .welcome-card {
    margin-bottom: 40px;
    border-radius: 30px;
    overflow: visible;
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(20px);
    box-shadow: 0 15px 50px rgba(135, 206, 235, 0.25),
      0 0 0 3px rgba(255, 255, 255, 0.6) inset;
    border: 3px solid rgba(135, 206, 235, 0.3);
    position: relative;
    transition: all 0.5s cubic-bezier(0.68, -0.55, 0.265, 1.55);
    animation: cardSlideIn 1s cubic-bezier(0.34, 1.56, 0.64, 1);

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
        rgba(255, 255, 255, 0.5),
        transparent
      );
      transition: left 0.8s;
      pointer-events: none;
    }

    &:hover {
      transform: translateY(-10px);
      box-shadow: 0 20px 60px rgba(135, 206, 235, 0.4),
        0 0 0 4px rgba(221, 160, 221, 0.4) inset;
      border-color: rgba(221, 160, 221, 0.6);

      &::after {
        left: 100%;
      }
    }

    // 彩虹渐变标题栏
    .rainbow-header {
      height: 8px;
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
      background-size: 200% 100%;
      animation: rainbowMove 5s linear infinite;
      position: relative;
      overflow: hidden;
      border-radius: 27px 27px 0 0;

      // 光泽效果
      .rainbow-shine {
        position: absolute;
        top: 0;
        left: -100%;
        width: 50%;
        height: 100%;
        background: linear-gradient(
          90deg,
          transparent,
          rgba(255, 255, 255, 0.6),
          transparent
        );
        animation: shineMove 3s ease-in-out infinite;
      }
    }

    .welcome-header {
      display: flex;
      align-items: center;
      padding: 40px 35px;
      gap: 35px;
      background: linear-gradient(
        135deg,
        rgba(255, 255, 255, 0.95) 0%,
        rgba(224, 247, 250, 0.6) 100%
      );
      position: relative;

      .avatar-container {
        position: relative;
        flex-shrink: 0;

        .avatar-clickable {
          cursor: pointer;
          transition: all 0.4s cubic-bezier(0.68, -0.55, 0.265, 1.55);
          border: 4px solid rgba(255, 255, 255, 0.9);
          box-shadow: 0 8px 25px rgba(135, 206, 235, 0.4),
            inset 0 2px 10px rgba(255, 255, 255, 0.5);

          &:hover {
            transform: scale(1.15) rotate(8deg);
            box-shadow: 0 12px 35px rgba(135, 206, 235, 0.6);
            border-color: rgba(255, 182, 193, 0.8);
          }
        }

        .avatar-decoration {
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -50%);
          pointer-events: none;

          .deco-circle {
            position: absolute;
            border-radius: 50%;
            border: 2px solid rgba(135, 206, 235, 0.3);

            &.circle-1 {
              width: 120px;
              height: 120px;
              top: -60px;
              left: -60px;
              animation: circleRotate 8s linear infinite;
            }

            &.circle-2 {
              width: 140px;
              height: 140px;
              top: -70px;
              left: -70px;
              animation: circleRotate 10s linear infinite reverse;
              border-color: rgba(221, 160, 221, 0.3);
            }

            &.circle-3 {
              width: 160px;
              height: 160px;
              top: -80px;
              left: -80px;
              animation: circleRotate 12s linear infinite;
              border-color: rgba(152, 251, 152, 0.3);
            }
          }
        }

        .avatar-stars {
          position: absolute;
          top: -15px;
          right: -15px;
          display: flex;
          gap: 5px;

          .star-icon {
            font-size: 18px;
            animation: starTwinkle 2s ease-in-out infinite;
            filter: drop-shadow(0 2px 8px rgba(255, 215, 0, 0.5));

            &:nth-child(1) {
              animation-delay: 0s;
            }

            &:nth-child(2) {
              animation-delay: 0.5s;
            }

            &:nth-child(3) {
              animation-delay: 1s;
            }
          }
        }
      }

      .welcome-text {
        flex: 1;

        .welcome-title {
          margin: 0 0 12px 0;
          font-size: 32px;
          background: linear-gradient(135deg, #5b9bd5 0%, #9370db 50%, #84c7d0 100%);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          background-clip: text;
          font-weight: 700;
          animation: titleGlow 3s ease-in-out infinite;
          display: flex;
          align-items: center;
          gap: 12px;

          .title-emoji {
            font-size: 28px;
            animation: emojiFloat 2s ease-in-out infinite;

            &:nth-child(1) {
              animation-delay: 0s;
            }

            &:nth-child(3) {
              animation-delay: 1s;
            }
          }
        }

        .welcome-subtitle {
          margin: 0 0 8px 0;
          font-size: 16px;
          color: #5b9bd5;
          font-weight: 500;
          display: flex;
          align-items: center;
          gap: 8px;

          .subtitle-icon {
            font-size: 18px;
          }
        }

        .welcome-greeting {
          margin: 0;
          font-size: 15px;
          color: #9370db;
          font-weight: 500;
          display: flex;
          align-items: center;
          gap: 8px;

          .greeting-icon {
            font-size: 16px;
            animation: heartBeat 1.5s ease-in-out infinite;
          }
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
    margin-bottom: 40px;
    position: relative;

    .section-title {
      font-size: 28px;
      margin: 0 0 35px;
      text-align: center;
      background: linear-gradient(
        135deg,
        #5b9bd5 0%,
        #9370db 50%,
        #84c7d0 100%
      );
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      font-weight: 800;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 20px;
      position: relative;
      animation: titleGlow 3s ease-in-out infinite;

      .title-line {
        flex: 1;
        height: 3px;
        background: linear-gradient(
          90deg,
          transparent,
          rgba(135, 206, 235, 0.5),
          rgba(221, 160, 221, 0.5),
          transparent
        );
        border-radius: 2px;
        max-width: 150px;
      }

      .title-text {
        white-space: nowrap;
      }

      .title-icon {
        font-size: 26px;
        animation: sparkle 2s ease-in-out infinite;
        filter: drop-shadow(0 2px 8px rgba(255, 215, 0, 0.6));

        &:nth-child(2) {
          animation-delay: 0s;
        }

        &:nth-child(4) {
          animation-delay: 1s;
        }
      }
    }

    .actions-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
      gap: 25px;

      .action-card {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        min-height: 180px;
        padding: 30px 20px;
        cursor: pointer;
        border-radius: 25px;
        background: rgba(255, 255, 255, 0.95);
        backdrop-filter: blur(20px);
        border: 3px solid;
        position: relative;
        overflow: hidden;

        .card-bg-decoration {
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          opacity: 0;
          transition: opacity 0.4s ease;
          pointer-events: none;
        }

        &.action-edit {
          border-color: rgba(135, 206, 235, 0.4);
          box-shadow: 0 8px 25px rgba(135, 206, 235, 0.2);

          .card-bg-decoration {
            background: linear-gradient(135deg, rgba(135, 206, 235, 0.1), rgba(176, 224, 230, 0.1));
          }

          .el-icon {
            color: #87ceeb;
          }

          &:hover {
            border-color: rgba(135, 206, 235, 0.7);
            box-shadow: 0 15px 45px rgba(135, 206, 235, 0.4);
          }
        }

        &.action-message {
          border-color: rgba(221, 160, 221, 0.4);
          box-shadow: 0 8px 25px rgba(221, 160, 221, 0.2);

          .card-bg-decoration {
            background: linear-gradient(135deg, rgba(221, 160, 221, 0.1), rgba(230, 230, 250, 0.1));
          }

          .el-icon {
            color: #dda0dd;
          }

          &:hover {
            border-color: rgba(221, 160, 221, 0.7);
            box-shadow: 0 15px 45px rgba(221, 160, 221, 0.4);
          }
        }

        &.action-setting {
          border-color: rgba(152, 251, 152, 0.4);
          box-shadow: 0 8px 25px rgba(152, 251, 152, 0.2);

          .card-bg-decoration {
            background: linear-gradient(135deg, rgba(152, 251, 152, 0.1), rgba(144, 238, 144, 0.1));
          }

          .el-icon {
            color: #98fb98;
          }

          &:hover {
            border-color: rgba(152, 251, 152, 0.7);
            box-shadow: 0 15px 45px rgba(152, 251, 152, 0.4);
          }
        }

        &.action-star {
          border-color: rgba(255, 182, 193, 0.4);
          box-shadow: 0 8px 25px rgba(255, 182, 193, 0.2);

          .card-bg-decoration {
            background: linear-gradient(135deg, rgba(255, 182, 193, 0.1), rgba(255, 192, 203, 0.1));
          }

          .el-icon {
            color: #ffb6c1;
          }

          &:hover {
            border-color: rgba(255, 182, 193, 0.7);
            box-shadow: 0 15px 45px rgba(255, 182, 193, 0.4);
          }
        }

        // 弹性动画效果
        &.action-card-anime {
          transition: all 0.5s cubic-bezier(0.68, -0.55, 0.265, 1.55);

          &:hover {
            transform: translateY(-15px) scale(1.05);

            .card-bg-decoration {
              opacity: 1;
            }

            .card-icon-wrapper {
              transform: scale(1.2) rotate(10deg);
            }

            .card-title {
              color: #5b9bd5;
            }
          }

          &:active {
            transform: translateY(-10px) scale(1.02);
            transition: all 0.1s ease;
          }
        }

        .card-icon-wrapper {
          margin-bottom: 15px;
          transition: all 0.4s cubic-bezier(0.68, -0.55, 0.265, 1.55);
        }

        .el-icon {
          transition: all 0.3s ease;
        }

        .card-title {
          font-size: 18px;
          font-weight: 600;
          color: #333;
          margin-bottom: 8px;
          transition: all 0.3s ease;
        }

        .card-subtitle {
          font-size: 13px;
          color: #999;
          font-weight: 400;
        }
      }
    }
  }
}

// 动画定义
@keyframes rainbowMove {
  0% {
    background-position: 0% 50%;
  }
  100% {
    background-position: 200% 50%;
  }
}

@keyframes shineMove {
  0% {
    left: -100%;
  }
  100% {
    left: 200%;
  }
}

@keyframes cardSlideIn {
  from {
    opacity: 0;
    transform: translateY(40px) scale(0.9);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

@keyframes circleRotate {
  from {
    transform: translate(-50%, -50%) rotate(0deg);
  }
  to {
    transform: translate(-50%, -50%) rotate(360deg);
  }
}

@keyframes starTwinkle {
  0%, 100% {
    opacity: 0.6;
    transform: scale(1) rotate(0deg);
  }
  50% {
    opacity: 1;
    transform: scale(1.3) rotate(180deg);
  }
}

@keyframes emojiFloat {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-8px);
  }
}

@keyframes heartBeat {
  0%, 100% {
    transform: scale(1);
  }
  25% {
    transform: scale(1.2);
  }
  50% {
    transform: scale(1);
  }
  75% {
    transform: scale(1.1);
  }
}

@keyframes starFloat {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
    opacity: 0.6;
  }
  50% {
    transform: translateY(-15px) rotate(180deg);
    opacity: 1;
  }
}

// 标题发光动画
@keyframes titleGlow {
  0%,
  100% {
    filter: drop-shadow(0 0 8px rgba(135, 206, 235, 0.5));
  }
  50% {
    filter: drop-shadow(0 0 20px rgba(221, 160, 221, 0.8));
  }
}

// 闪烁动画
@keyframes sparkle {
  0%,
  100% {
    opacity: 1;
    transform: scale(1) rotate(0deg);
  }
  50% {
    opacity: 0.6;
    transform: scale(1.3) rotate(180deg);
  }
}

// 响应式设计
@media (max-width: 992px) {
  .user-welcome-page {
    .welcome-card {
      .welcome-header {
        flex-direction: column;
        text-align: center;
        padding: 30px 25px;

        .avatar-container {
          margin-bottom: 20px;
        }
      }

      .user-stats {
        flex-direction: column;
        gap: 30px;
      }
    }

    .quick-actions {
      .actions-grid {
        grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
        gap: 20px;
      }
    }
  }
}

@media (max-width: 768px) {
  .user-welcome-page {
    padding: 0 10px;

    .welcome-card {
      .welcome-header {
        padding: 25px 20px;

        .welcome-text {
          .welcome-title {
            font-size: 24px;
          }

          .welcome-subtitle,
          .welcome-greeting {
            font-size: 14px;
          }
        }
      }

      .user-stats {
        padding: 30px 20px;
      }
    }

    .quick-actions {
      .section-title {
        font-size: 22px;
        flex-wrap: wrap;

        .title-line {
          display: none;
        }
      }

      .actions-grid {
        grid-template-columns: 1fr;
        gap: 15px;

        .action-card {
          min-height: 150px;
        }
      }
    }
  }
}
</style>
