<template>
  <div class="chat-view">
    <!-- 动漫风格背景装饰 -->
    <div class="bg-decoration">
      <div class="cloud cloud-1"></div>
      <div class="cloud cloud-2"></div>
      <div class="cloud cloud-3"></div>
      <div class="floating-hearts">
        <span
          class="heart"
          v-for="i in 3"
          :key="i"
          :style="{ animationDelay: `${i * 1}s` }"
          >♡</span
        >
      </div>
    </div>

    <div class="chat-container">
      <!-- 侧边栏 - 会话列表 -->
      <div class="sidebar">
        <div class="sidebar-header">
          <h2 class="sidebar-title">
            <span class="title-icon">💬</span>
            <span>聊天室</span>
          </h2>
        </div>
        <ChatList
          ref="chatListRef"
          :active-session-id="Number(activeSessionId)"
          @session-change="handleSessionChange"
        />
      </div>

      <!-- 主内容区 -->
      <div class="main-content">
        <div
          v-if="activeSessionId && currentSessionDetail"
          class="chat-room-wrapper"
        >
          <ChatRoom
            :session-id="Number(activeSessionId)"
            :session-detail="currentSessionDetail"
          />
        </div>

        <div v-else class="empty-state">
          <div class="empty-content">
            <div class="empty-icon">✨</div>
            <p class="empty-text">选择一个会话开始聊天吧~</p>
            <div class="empty-decoration">( •̀ ω •́ )✧</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import ChatList from "./components/ChatList.vue";
import ChatRoom from "./components/ChatRoom.vue";
import { getSessionDetail } from "@/api/session";

const activeSessionId = ref(null);
const currentSessionDetail = ref(null);
const chatListRef = ref(null);

// 处理会话切换
const handleSessionChange = async (sessionId) => {
  activeSessionId.value = sessionId;
  try {
    const res = await getSessionDetail(sessionId);
    currentSessionDetail.value = res.data;
  } catch (error) {
    console.error("获取会话详情失败:", error);
    currentSessionDetail.value = null;
  }
};

// 刷新会话列表（当发送新消息后）
const refreshSessions = () => {
  if (chatListRef.value) {
    chatListRef.value.refresh();
  }
};
</script>

<style lang="scss" scoped>
@use "@/styles/animations.scss";

.chat-view {
  height: 100vh;
  display: flex;
  position: relative;
  overflow: hidden;
  background: linear-gradient(135deg, #e3f2fd 0%, #f3e5f5 50%, #e1f5fe 100%);

  // 背景装饰
  .bg-decoration {
    position: absolute;
    width: 100%;
    height: 100%;
    pointer-events: none;
    z-index: 0;

    // 云朵装饰
    .cloud {
      position: absolute;
      background: rgba(255, 255, 255, 0.6);
      border-radius: 100px;
      animation: float 20s infinite ease-in-out;

      &::before,
      &::after {
        content: "";
        position: absolute;
        background: rgba(255, 255, 255, 0.6);
        border-radius: 100px;
      }

      &.cloud-1 {
        width: 100px;
        height: 40px;
        top: 10%;
        left: 10%;

        &::before {
          width: 50px;
          height: 50px;
          top: -25px;
          left: 10px;
        }

        &::after {
          width: 60px;
          height: 40px;
          top: -15px;
          right: 10px;
        }
      }

      &.cloud-2 {
        width: 120px;
        height: 45px;
        top: 60%;
        right: 15%;
        animation-delay: -5s;

        &::before {
          width: 55px;
          height: 55px;
          top: -28px;
          left: 15px;
        }

        &::after {
          width: 65px;
          height: 45px;
          top: -18px;
          right: 15px;
        }
      }

      &.cloud-3 {
        width: 90px;
        height: 35px;
        bottom: 15%;
        left: 20%;
        animation-delay: -10s;

        &::before {
          width: 45px;
          height: 45px;
          top: -22px;
          left: 8px;
        }

        &::after {
          width: 50px;
          height: 35px;
          top: -12px;
          right: 8px;
        }
      }
    }

    // 漂浮的心形
    .floating-hearts {
      position: absolute;
      width: 100%;
      height: 100%;

      .heart {
        position: absolute;
        font-size: 20px;
        color: rgba(255, 182, 193, 0.4);
        animation: floatHeart 8s infinite ease-in-out;

        &:nth-child(1) {
          left: 15%;
          top: 20%;
        }
        &:nth-child(2) {
          right: 20%;
          top: 15%;
        }
        &:nth-child(3) {
          left: 45%;
          bottom: 25%;
        }
      }
    }
  }

  .chat-container {
    width: 1100px;
    max-width: 90vw;
    height: 700px;
    max-height: 85vh;
    display: flex;
    position: relative;
    z-index: 1;
    margin: auto;
    border-radius: 24px;
    overflow: hidden;
    background: rgba(255, 255, 255, 0.95);
    box-shadow: 0 12px 48px rgba(179, 157, 219, 0.25),
      0 0 0 1px rgba(179, 157, 219, 0.1);
    backdrop-filter: blur(10px);

    .sidebar {
      width: 280px;
      border-right: 2px solid rgba(159, 168, 218, 0.2);
      height: 100%;
      display: flex;
      flex-direction: column;
      background: linear-gradient(
        180deg,
        rgba(232, 234, 246, 0.5) 0%,
        rgba(255, 255, 255, 0.3) 100%
      );

      .sidebar-header {
        padding: 20px;
        background: linear-gradient(135deg, #b39ddb 0%, #9fa8da 100%);
        border-radius: 0 0 20px 20px;
        margin-bottom: 10px;
        box-shadow: 0 4px 12px rgba(179, 157, 219, 0.3);

        .sidebar-title {
          margin: 0;
          font-size: 22px;
          font-weight: 600;
          color: white;
          display: flex;
          align-items: center;
          gap: 10px;
          text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

          .title-icon {
            font-size: 26px;
            animation: bounce 2s infinite;
          }
        }
      }
    }

    .main-content {
      flex: 1;
      height: 100%;
      position: relative;

      .chat-room-wrapper {
        height: 100%;
      }

      .empty-state {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100%;
        background: linear-gradient(
          135deg,
          rgba(227, 242, 253, 0.3) 0%,
          rgba(243, 229, 245, 0.3) 100%
        );

        .empty-content {
          text-align: center;
          animation: fadeInUp 0.6s ease-out;

          .empty-icon {
            font-size: 80px;
            margin-bottom: 20px;
            animation: sparkle 2s infinite;
          }

          .empty-text {
            font-size: 18px;
            color: #7e57c2;
            margin: 0 0 15px 0;
            font-weight: 500;
          }

          .empty-decoration {
            font-size: 24px;
            color: #b39ddb;
            animation: wave 1.5s infinite;
          }
        }
      }
    }
  }
}
</style>
