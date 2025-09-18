<template>
  <div class="message-item" :class="{ 'self-message': isSelf }">
    <div class="message-avatar">
      <el-avatar :src="message.senderAvatar" :size="36" />
    </div>
    <div class="message-content">
      <div class="message-info">
        <span class="sender-name">{{ message.senderName }}</span>
        <span class="message-time">{{ formatTime(message.sendTime) }}</span>
      </div>
      <div class="message-body">
        <div class="message-text">{{ message.content }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, computed } from 'vue';
import dayjs from 'dayjs';

const props = defineProps({
  message: {
    type: Object,
    required: true
  },
  isSelf: {
    type: Boolean,
    default: false
  }
});

const formatTime = (time) => {
  return dayjs(time).format('HH:mm');
};
</script>

<style lang="scss" scoped>
.message-item {
  display: flex;
  margin-bottom: 16px;

  .message-avatar {
    margin-right: 12px;
  }

  .message-content {
    max-width: 70%;

    .message-info {
      margin-bottom: 4px;

      .sender-name {
        font-size: 14px;
        font-weight: 500;
        margin-right: 8px;
      }

      .message-time {
        font-size: 12px;
        color: #999;
      }
    }

    .message-body {
      .message-text {
        padding: 8px 12px;
        background: #f5f5f5;
        border-radius: 4px;
        word-break: break-word;
      }
    }
  }

  &.self-message {
    flex-direction: row-reverse;

    .message-avatar {
      margin-right: 0;
      margin-left: 12px;
    }

    .message-content {
      text-align: right;

      .message-body {
        .message-text {
          background: #409eff;
          color: white;
        }
      }
    }
  }
}
</style>
