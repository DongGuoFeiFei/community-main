<template>
  <div class="notification-item like-notification" :class="{ unread: !notification.is_read }">
    <div class="notification-avatar">
      <el-avatar :src="notification.fromUser?.avatar" />
    </div>

    <div class="notification-content">
      <div class="content-text">
        <span class="username">{{ notification.fromUser?.username }}</span>
        点赞了你的
        <router-link
            :to="`/article/${notification.parentSourceId}`"
            class="content-link"
        >
          {{ notification.contentType }}
        </router-link>
      </div>

      <div class="content-time">
        {{ formatTime(notification.createdAt) }}
      </div>
    </div>

    <div class="notification-actions">
      <el-button
          v-if="!notification.isRead"
          link
          size="small"
          @click.stop="$emit('read')"
      >
        标记已读
      </el-button>
      <el-button
          link
          size="small"
          @click.stop="$emit('delete')"
      >
        删除
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { formatTime } from '@/utils/date';

defineProps({
  notification: {
    type: Object,
    required: true
  }
});

defineEmits(['read', 'delete']);
</script>

<style lang="scss" scoped>
.like-notification {
  display: flex;
  padding: 12px 16px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: all 0.2s;

  &.unread {
    background-color: #f6ffed;
  }

  &:hover {
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }

  .notification-avatar {
    margin-right: 16px;

    .el-avatar {
      width: 40px;
      height: 40px;
    }
  }

  .notification-content {
    flex-grow: 1;

    .content-text {
      font-size: 14px;
      color: #333;
      margin-bottom: 4px;

      .username {
        font-weight: 500;
        color: #1890ff;
      }

      .content-link {
        color: #1890ff;
        text-decoration: none;

        &:hover {
          text-decoration: underline;
        }
      }
    }

    .content-time {
      font-size: 12px;
      color: #999;
    }
  }

  .notification-actions {
    display: flex;
    align-items: center;

    .el-button {
      padding: 0 8px;
      color: #666;

      &:hover {
        color: #1890ff;
      }
    }
  }
}
</style>