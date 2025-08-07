<template>
  <div class="follow-notification" :class="{ unread: !notification.isRead }">
    <div class="notification-avatar">
      <el-avatar :src="store.baseURL + notification.senderAvatar"/>
    </div>

    <div class="notification-main">
      <div class="notification-content">
        <span class="username">{{ notification.senderName }}</span>
        <span class="action-text">关注了你</span>

        <div v-if="notification.relatedContent" class="follow-message">
          {{ notification.relatedContent }}
        </div>
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
<!--      <el-button-->
<!--          v-if="!isFollowing"-->
<!--          type="primary"-->
<!--          size="small"-->
<!--          @click.stop="handleFollowBack"-->
<!--      >-->
<!--        回关-->
<!--      </el-button>-->
    </div>
  </div>
</template>

<script setup>
import { formatTime } from '@/utils/date';
import { localStores } from "@/stores/localStores.js";
import { ref } from 'vue';

const props = defineProps({
  notification: {
    type: Object,
    required: true
  }
});

const store = localStores();
const emit = defineEmits(['read', 'delete', 'follow']);
const isFollowing = ref(props.notification.extraData?.isFollowing || false);

const handleRead = () => {
  emit('read', props.notification.notificationId);
};

const handleDelete = () => {
  emit('delete', props.notification.notificationId);
};

const handleFollowBack = () => {
  emit('follow', props.notification.senderId);
  isFollowing.value = true;
};
</script>

<style lang="scss" scoped>
.follow-notification {
  display: flex;
  padding: 16px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  margin-bottom: 12px;
  transition: all 0.2s ease;

  &.unread {
    background-color: #f6f7ff;
    border-left: 3px solid #9C51B6;
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
      color: #9C51B6;
    }

    .action-text {
      color: #666;
      margin: 0 4px;
    }

    .follow-message {
      margin-top: 6px;
      padding: 6px 8px;
      background-color: #f8f8f8;
      border-radius: 4px;
      color: #666;
      font-size: 13px;
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
    gap: 4px;

    .el-button {
      padding: 0 8px;
      font-size: 12px;

      &[type="primary"] {
        color: white;
        background-color: #9C51B6;
        border-color: #9C51B6;
      }

      &[link] {
        color: #999;

        &:hover {
          color: #9C51B6;
        }
      }
    }
  }
}
</style>
