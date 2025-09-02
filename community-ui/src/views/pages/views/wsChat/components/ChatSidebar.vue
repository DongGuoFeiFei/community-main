<template>
  <div class="chat-sidebar">
    <div class="sidebar-header">
      <h2>聊天</h2>
      <el-button type="primary" size="small" circle @click="showCreateDialog">
        <el-icon>
          <Plus/>
        </el-icon>
      </el-button>
    </div>

    <div class="search-box">
      <el-input
          v-model="searchText"
          placeholder="搜索会话"
          prefix-icon="Search"
          size="large"
      />
    </div>

    <div class="session-list">
      <div
          v-for="session in filteredSessions"
          :key="session.id"
          :class="['session-item', { active: session.id === currentSession?.id }]"
          @click="$emit('select-session', session)"
      >
        <div class="session-avatar">
          <el-avatar :size="48" :src="session.avatar">
            <span v-if="!session.avatar">{{ session.name.charAt(0) }}</span>
          </el-avatar>
          <div v-if="session.online" class="online-indicator"></div>
        </div>

        <div class="session-info">
          <div class="session-name">{{ session.name }}</div>
          <div class="last-message">{{ session.lastMessageContent || '暂无消息' }}</div>
        </div>

        <div class="session-meta">
          <div class="time">{{ formatTime(session.lastMessageTime) }}</div>
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
import {computed, ref} from 'vue'
import {ElMessageBox} from 'element-plus'
import {Plus} from '@element-plus/icons-vue'
import dayjs from 'dayjs'

const props = defineProps({
  sessions: {
    type: Array,
    default: () => []
  },
  currentSession: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['select-session', 'create-session'])

const searchText = ref('')

// 过滤会话
const filteredSessions = computed(() => {
  console.log(props.sessions)
  if (!searchText.value) return props.sessions
  return props.sessions.filter(session =>
      session.name.toLowerCase().includes(searchText.value.toLowerCase())
  )
})

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const now = dayjs()
  const messageTime = dayjs(time)

  if (now.isSame(messageTime, 'day')) {
    return messageTime.format('HH:mm')
  } else if (now.subtract(1, 'day').isSame(messageTime, 'day')) {
    return '昨天'
  } else {
    return messageTime.format('MM/DD')
  }
}

// 显示创建会话对话框
const showCreateDialog = async () => {
  try {
    const {value: userId} = await ElMessageBox.prompt('请输入用户ID', '新建会话', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPattern: /^\d+$/,
      inputErrorMessage: '用户ID必须是数字'
    })

    emit('create-session', parseInt(userId))
  } catch (error) {
    if (error !== 'cancel') {
      console.error('创建会话出错:', error)
    }
  }
}
</script>

<style lang="scss" scoped>
.chat-sidebar {
  width: 320px;
  height: 100%;
  background: white;
  display: flex;
  flex-direction: column;
  border-right: 1px solid #e9ecef;

  .sidebar-header {
    padding: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #e9ecef;

    h2 {
      margin: 0;
      font-size: 20px;
      font-weight: 600;
      color: #2c3e50;
    }
  }

  .search-box {
    padding: 16px 20px;
    border-bottom: 1px solid #e9ecef;
  }

  .session-list {
    flex: 1;
    overflow-y: auto;

    .session-item {
      display: flex;
      padding: 16px 20px;
      cursor: pointer;
      transition: background-color 0.2s ease;
      border-bottom: 1px solid #f8f9fa;

      &:hover {
        background-color: #f8f9fa;
      }

      &.active {
        background-color: #e3f2fd;
        border-right: 3px solid var(--el-color-primary);
      }

      .session-avatar {
        position: relative;
        margin-right: 16px;

        .online-indicator {
          position: absolute;
          bottom: 0;
          right: 0;
          width: 12px;
          height: 12px;
          background-color: #4caf50;
          border: 2px solid white;
          border-radius: 50%;
        }
      }

      .session-info {
        flex: 1;
        overflow: hidden;

        .session-name {
          font-weight: 500;
          margin-bottom: 4px;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          color: #2c3e50;
        }

        .last-message {
          font-size: 13px;
          color: #6c757d;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }

      .session-meta {
        display: flex;
        flex-direction: column;
        align-items: flex-end;

        .time {
          font-size: 12px;
          color: #adb5bd;
          margin-bottom: 8px;
        }

        .unread-badge {
          :deep(.el-badge__content) {
            background-color: var(--el-color-primary);
          }
        }
      }
    }
  }
}
</style>
