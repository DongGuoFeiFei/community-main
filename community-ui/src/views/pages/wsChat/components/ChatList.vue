<template>
  <div class="chat-list">
    <!-- 搜索框 -->
    <div class="search-wrapper">
      <el-input
        v-model="searchQuery"
        placeholder="搜索会话..."
        clearable
        @clear="handleSearchClear"
        class="search-input"
      >
        <template #prefix>
          <el-icon class="search-icon"><search /></el-icon>
        </template>
      </el-input>
    </div>

    <!-- 会话列表 -->
    <div class="session-list">
      <div
        v-for="session in filteredSessions"
        :key="session.id"
        class="session-item"
        :class="{ active: activeSessionId === session.id }"
        @click="handleSessionClick(session)"
      >
        <div class="session-avatar">
          <el-avatar :src="session.avatar" :size="48" />
          <div class="online-indicator" v-if="session.isOnline"></div>
        </div>
        <div class="session-info">
          <div class="session-name">
            {{ session.name }}
            <span class="name-decoration" v-if="session.isOnline">✨</span>
          </div>
          <div class="session-preview">
            {{ session.lastMsgContent || "暂无消息" }}
          </div>
        </div>
        <div class="session-meta">
          <div class="session-time">
            {{ formatTime(session.lastMsgTime) }}
          </div>
          <div class="badge-wrapper" v-if="session.unreadCount > 0">
            <el-badge
              :value="session.unreadCount"
              :max="99"
              class="unread-badge"
            />
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-if="filteredSessions.length === 0" class="empty-sessions">
        <div class="empty-icon">👻</div>
        <p>没有找到会话</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import { Search } from "@element-plus/icons-vue";
import dayjs from "dayjs";
import { getSessions } from "@/api/session";
import type { ChatSessionItem } from "@/types/chat";

defineProps<{
  activeSessionId: number | null;
}>();

const emit = defineEmits<{
  (e: "session-change", sessionId: number): void;
}>();

const sessions = ref<ChatSessionItem[]>([]);
const searchQuery = ref("");
const loading = ref(false);

const fetchSessions = async () => {
  try {
    loading.value = true;
    const res = await getSessions();
    sessions.value = res.data || [];
  } catch (error) {
    console.error("获取会话列表失败:", error);
    sessions.value = [];
  } finally {
    loading.value = false;
  }
};

const filteredSessions = computed(() => {
  const keyword = searchQuery.value.trim().toLowerCase();
  if (!keyword) return sessions.value;
  return sessions.value.filter((session) =>
    (session.name || "").toLowerCase().includes(keyword)
  );
});

const formatTime = (time?: string) => {
  if (!time) return "";
  const msgTime = dayjs(time);
  const now = dayjs();
  
  // 如果是今天，显示时间
  if (msgTime.isSame(now, 'day')) {
    return msgTime.format("HH:mm");
  }
  
  // 如果是昨天
  if (msgTime.isSame(now.subtract(1, 'day'), 'day')) {
    return "昨天";
  }
  
  // 如果是今年，显示月日
  if (msgTime.isSame(now, 'year')) {
    return msgTime.format("MM-DD");
  }
  
  // 否则显示年月日
  return msgTime.format("YYYY-MM-DD");
};

const handleSessionClick = (session: ChatSessionItem) => {
  emit("session-change", session.id);
};

const handleSearchClear = () => {
  searchQuery.value = "";
};

onMounted(() => {
  fetchSessions();
});

// 暴露刷新方法供父组件调用
defineExpose({
  refresh: fetchSessions
});
</script>

<style lang="scss" scoped>
.chat-list {
  width: 100%;
  box-sizing: border-box;
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 0 12px;
  overflow-x: hidden;

  // 搜索框区域
  .search-wrapper {
    padding: 10px 0;

    .search-input {
      border-radius: 20px;
      overflow: hidden;

      :deep(.el-input__wrapper) {
        border-radius: 20px;
        background: linear-gradient(
          135deg,
          rgba(179, 157, 219, 0.1) 0%,
          rgba(159, 168, 218, 0.1) 100%
        );
        border: 2px solid transparent;
        transition: all 0.3s ease;
        box-shadow: 0 2px 8px rgba(179, 157, 219, 0.1);

        &:hover {
          border-color: rgba(179, 157, 219, 0.3);
          box-shadow: 0 4px 12px rgba(179, 157, 219, 0.2);
        }

        &.is-focus {
          border-color: #b39ddb;
          box-shadow: 0 4px 16px rgba(179, 157, 219, 0.3);
        }
      }

      .search-icon {
        color: #b39ddb;
      }
    }
  }

  // 会话列表
  .session-list {
    flex: 1;
    width: 100%;
    box-sizing: border-box;
    overflow-y: auto;
    overflow-x: hidden;
    padding: 5px 10px 10px;

    // 自定义滚动条
    &::-webkit-scrollbar {
      width: 6px;
    }

    &::-webkit-scrollbar-track {
      background: rgba(179, 157, 219, 0.05);
      border-radius: 3px;
    }

    &::-webkit-scrollbar-thumb {
      background: rgba(179, 157, 219, 0.3);
      border-radius: 3px;
      transition: background 0.3s;

      &:hover {
        background: rgba(179, 157, 219, 0.5);
      }
    }

    .session-item {
      width: 100%;
      margin-bottom: 12px;
      box-sizing: border-box;
      display: flex;
      padding: 14px 12px;
      cursor: pointer;
      border-radius: 16px;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      position: relative;
      background: white;
      border: 2px solid transparent;

      &::before {
        content: "";
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        border-radius: 16px;
        background: linear-gradient(
          135deg,
          rgba(179, 157, 219, 0.1) 0%,
          rgba(159, 168, 218, 0.1) 100%
        );
        opacity: 0;
        transition: opacity 0.3s;
        z-index: -1;
      }

      &:hover {
        transform: translateX(4px);
        box-shadow: 0 4px 12px rgba(179, 157, 219, 0.15);

        &::before {
          opacity: 1;
        }
      }

      &.active {
        background: linear-gradient(
          135deg,
          rgba(179, 157, 219, 0.15) 0%,
          rgba(159, 168, 218, 0.15) 100%
        );
        border-color: rgba(179, 157, 219, 0.3);
        box-shadow: 0 4px 16px rgba(179, 157, 219, 0.2);
        transform: translateX(4px);

        .session-name {
          color: #7e57c2;
          font-weight: 600;
        }
      }

      // 头像区域
      .session-avatar {
        position: relative;
        margin-right: 12px;

        .online-indicator {
          position: absolute;
          bottom: 2px;
          right: 2px;
          width: 12px;
          height: 12px;
          background: linear-gradient(135deg, #81c784 0%, #66bb6a 100%);
          border: 2px solid white;
          border-radius: 50%;
          animation: pulse 2s infinite;
        }
      }

      // 会话信息
      .session-info {
        flex: 1;
        min-width: 0;
        overflow: hidden;
        display: flex;
        flex-direction: column;
        justify-content: center;

        .session-name {
          font-weight: 500;
          font-size: 15px;
          color: #333;
          white-space: nowrap;
          text-overflow: ellipsis;
          overflow: hidden;
          margin-bottom: 4px;
          display: flex;
          align-items: center;
          gap: 4px;
          transition: color 0.3s;

          .name-decoration {
            font-size: 12px;
            animation: twinkle 1.5s infinite;
          }
        }

        .session-preview {
          font-size: 13px;
          color: #999;
          min-width: 0;
          white-space: nowrap;
          text-overflow: ellipsis;
          overflow: hidden;
          line-height: 1.4;
        }
      }

      // 元数据区域
      .session-meta {
        display: flex;
        flex-direction: column;
        align-items: flex-end;
        justify-content: center;
        gap: 6px;

        .session-time {
          font-size: 11px;
          color: #b39ddb;
          font-weight: 500;
        }

        .badge-wrapper {
          .unread-badge {
            :deep(.el-badge__content) {
              background: linear-gradient(135deg, #ff6b9d 0%, #ffa06b 100%);
              border: none;
              font-weight: 600;
              box-shadow: 0 2px 8px rgba(255, 107, 157, 0.4);
              animation: bounce 1s infinite;
            }
          }
        }
      }
    }

    // 空状态
    .empty-sessions {
      text-align: center;
      padding: 40px 20px;
      color: #b39ddb;

      .empty-icon {
        font-size: 60px;
        margin-bottom: 12px;
        animation: float 3s infinite ease-in-out;
      }

      p {
        margin: 0;
        font-size: 14px;
        opacity: 0.8;
      }
    }
  }
}

// 动画定义
@keyframes pulse {
  0%,
  100% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.8;
  }
}

@keyframes twinkle {
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

@keyframes bounce {
  0%,
  100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

@keyframes float {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}
</style>
