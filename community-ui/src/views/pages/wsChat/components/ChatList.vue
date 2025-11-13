<template>
  <div class="chat-list">
    <el-input
        v-model="searchQuery"
        placeholder="搜索会话"
        clearable
        @clear="handleSearchClear"
    >
      <template #prefix>
        <el-icon><search /></el-icon>
      </template>
    </el-input>

    <div class="session-list">
      <div
          v-for="session in filteredSessions"
          :key="session.id"
          class="session-item"
          :class="{ active: activeSessionId === session.id }"
          @click="handleSessionClick(session)"
      >
        <el-avatar :src="session.avatar" :size="40" />
        <div class="session-info">
          <div class="session-name">{{ session.name }}</div>
          <div class="session-preview">{{ session.lastMessageContent }}</div>
        </div>
        <div class="session-meta">
          <div class="session-time">{{ formatTime(session.lastMessageTime) }}</div>
          <el-badge
              v-if="session.unreadCount > 0"
              :value="session.unreadCount"
              class="unread-badge"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue';
import { Search } from '@element-plus/icons-vue';
import dayjs from 'dayjs';
import { getSessions } from '@/api/session.js';

const props = defineProps({
  activeSessionId: {
    type: Number,
    default: null
  }
});

const emit = defineEmits(['session-change']);

const sessions = ref([]);
const searchQuery = ref('');

// 获取会话列表
const fetchSessions = async () => {
  try {
    const res = await getSessions();
    sessions.value = res.data;
  } catch (error) {
    console.error('获取会话列表失败:', error);
  }
};

// 过滤会话
const filteredSessions = computed(() => {
  if (!searchQuery.value) return sessions.value;
  return sessions.value.filter(session =>
      session.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

// 格式化时间
const formatTime = (time) => {
  return dayjs(time).format('HH:mm');
};

// 点击会话
const handleSessionClick = (session) => {
  emit('session-change', session.id);
};

// 清空搜索
const handleSearchClear = () => {
  searchQuery.value = '';
};

// 初始化获取数据
fetchSessions();
</script>

<style lang="scss" scoped>
.chat-list {
  height: 100%;
  display: flex;
  flex-direction: column;

  .session-list {
    flex: 1;
    overflow-y: auto;
    margin-top: 10px;

    .session-item {
      display: flex;
      padding: 12px;
      cursor: pointer;
      transition: background-color 0.2s;

      &:hover {
        background-color: #f5f5f5;
      }

      &.active {
        background-color: #e6f7ff;
      }

      .session-info {
        flex: 1;
        margin-left: 12px;
        overflow: hidden;

        .session-name {
          font-weight: 500;
          white-space: nowrap;
          text-overflow: ellipsis;
          overflow: hidden;
        }

        .session-preview {
          font-size: 12px;
          color: #999;
          white-space: nowrap;
          text-overflow: ellipsis;
          overflow: hidden;
        }
      }

      .session-meta {
        display: flex;
        flex-direction: column;
        align-items: flex-end;

        .session-time {
          font-size: 12px;
          color: #999;
          margin-bottom: 4px;
        }
      }
    }
  }
}
</style>
