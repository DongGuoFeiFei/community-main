<template>
  <div class="like-notification" :class="{ unread: !notification.isRead }">
    <div class="notification-avatar">
      <el-avatar :src="store.baseURL + notification.senderAvatar"/>
    </div>

    <div class="notification-main">
      <div class="notification-content">
        <span class="username">{{ notification.senderName }}</span>
        <span class="action-text">点赞了你的文章</span>
        <router-link
            :to="`/article/${notification.sourceId}`"
            target="_blank"
            class="article-title"
        >
          《{{ notification.sourceTitle }}》
        </router-link>
      </div>

      <div class="notification-meta">
        <span class="time">{{ formatTime(notification.createdAt) }}</span>
        <span class="divider">·</span>
        <span class="from">来自{{ notification.extraData?.from || '社区' }}</span>
      </div>
    </div>

    <div class="notification-actions">
      <el-button
          v-if="!notification.isRead"
          link
          size="small"
          @click.stop="handleRead"
      >
        标记已读
      </el-button>
      <el-button
          link
          size="small"
          @click.stop="handleDelete"
      >
        删除
      </el-button>
    </div>
  </div>
</template>

<script setup>
import {formatTime} from '@/utils/date.js';
import {localStores} from "@/stores/localStores.js";

const props = defineProps({
  notification: {
    type: Object,
    required: true
  }
});

const store = localStores()
const emit = defineEmits(['read', 'delete']);

const handleRead = () => {
  emit('read', props.notification.notificationId);
};

const handleDelete = () => {
  emit('delete', props.notification.notificationId);
};
</script>

<style lang="scss" scoped>
.like-notification {
  display: flex;
  padding: 16px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  margin-bottom: 12px;
  transition: all 0.2s ease;

  &.unread {
    background-color: #fffaf6;
    border-left: 3px solid #ff7d47;
  }

  &:hover {
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    transform: translateY(-1px);
  }

  .notification-avatar {
    margin-right: 16px;

    .el-avatar {
      width: 40px;
      height: 40px;
      background-color: #f8f8f8;
    }
  }

  .notification-main {
    flex: 1;
    min-width: 0;
  }

  .notification-content {
    font-size: 14px;
    line-height: 1.5;
    margin-bottom: 4px;

    .username {
      font-weight: 600;
      color: #ff7d47;
    }

    .action-text {
      color: #666;
      margin: 0 4px;
    }

    .article-title {
      color: #333;
      font-weight: 500;
      text-decoration: none;

      &:hover {
        color: #ff7d47;
        text-decoration: underline;
      }
    }
  }

  .notification-meta {
    font-size: 12px;
    color: #999;

    .divider {
      margin: 0 4px;
    }

    .from {
      color: #666;
    }
  }

  .notification-actions {
    display: flex;
    align-items: center;

    .el-button {
      padding: 0 8px;
      color: #999;
      font-size: 12px;

      &:hover {
        color: #ff7d47;
      }
    }
  }
}
</style>
