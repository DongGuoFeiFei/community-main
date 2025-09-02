<template>
  <div class="chat-header">
    <div class="header-left">
      <div class="session-avatar">
        <el-avatar :size="44" :src="session.avatar">
          <span v-if="!session.avatar">{{ session.name.charAt(0) }}</span>
        </el-avatar>
      </div>

      <div class="session-info">
        <div class="session-name">{{ session.name }}</div>
        <div class="online-status">
          <span v-if="onlineUsers.length > 0">{{ onlineUsers.length }}人在线</span>
          <span v-else>离线</span>
        </div>
      </div>
    </div>

    <div class="header-right">
      <el-tooltip content="语音通话" placement="bottom">
        <el-button link circle>
          <el-icon><Phone /></el-icon>
        </el-button>
      </el-tooltip>

      <el-tooltip content="视频通话" placement="bottom">
        <el-button link circle>
          <el-icon><VideoCamera /></el-icon>
        </el-button>
      </el-tooltip>

      <el-tooltip content="会话信息" placement="bottom">
        <el-button link circle @click="$emit('show-session-info')">
          <el-icon><More /></el-icon>
        </el-button>
      </el-tooltip>
    </div>
  </div>
</template>

<script setup>
import { Phone, VideoCamera, More } from '@element-plus/icons-vue'

defineProps({
  session: {
    type: Object,
    required: true
  },
  onlineUsers: {
    type: Array,
    default: () => []
  }
})

defineEmits(['show-session-info'])
</script>

<style lang="scss" scoped>
.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background: white;
  border-bottom: 1px solid #e9ecef;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.03);

  .header-left {
    display: flex;
    align-items: center;

    .session-info {
      margin-left: 16px;

      .session-name {
        font-weight: 600;
        font-size: 16px;
        color: #2c3e50;
        margin-bottom: 2px;
      }

      .online-status {
        font-size: 13px;
        color: #6c757d;
      }
    }
  }

  .header-right {
    display: flex;
    gap: 8px;

    :deep(.el-button) {
      width: 36px;
      height: 36px;
      color: #6c757d;

      &:hover {
        background-color: #f8f9fa;
        color: var(--el-color-primary);
      }
    }
  }
}
</style>
