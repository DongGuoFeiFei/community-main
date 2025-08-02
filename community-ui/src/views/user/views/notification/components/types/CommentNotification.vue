<template>
  <div class="comment-notification" :class="{ unread: !notification.isRead }">
    <div class="notification-avatar">
      <el-avatar :src="notification.senderAvatar"/>
    </div>

    <div class="notification-main">
      <div class="notification-header">
        <span class="username">{{ notification.senderName }}</span>
        <span class="action-text">{{ getActionText() }}</span>
        <router-link
            target="_blank"
            :to="getSourceLink()"
            class="source-title"
        >
          《{{ notification.sourceTitle }}》
        </router-link>
      </div>

      <div class="comment-content" v-if="notification.relatedContent">
        {{ notification.relatedContent }}
      </div>

      <div class="notification-footer">
        <span class="time">{{ formatTime(notification.createdAt) }}</span>
        <span class="divider">·</span>
        <span class="reply-trigger" @click="toggleReply">回复</span>
      </div>

      <div v-if="showReply" class="reply-box">
        <el-input
            v-model="replyContent"
            type="textarea"
            :rows="3"
            placeholder="写下你的回复..."
            resize="none"
            maxlength="300"
            show-word-limit
        />
        <div class="reply-actions">
          <el-button size="small" @click="cancelReply">取消</el-button>
          <el-button
              type="primary"
              size="small"
              @click="handleReply"
              :loading="isSubmitting"
              :disabled="!replyContent.trim()"
          >
            发送回复
          </el-button>
        </div>
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
import {ref} from 'vue';
import {formatTime} from '@/utils/date';
import {ElMessage} from 'element-plus';
import {submitCommentToPost} from "@/api/index.js";

const props = defineProps({
  notification: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['read', 'delete', 'reply']);

const showReply = ref(false);
const replyContent = ref('');
const isSubmitting = ref(false);

const getActionText = () => {
  return props.notification.type === 'COMMENT' ? '评论了你的' : '回复了你的';
};

const getSourceLink = () => {
  return `/article/${props.notification.sourceId}` +
      (props.notification.relatedId ? `#comment-${props.notification.relatedId}` : '');
};

const toggleReply = () => {
  showReply.value = !showReply.value;
  if (!showReply.value) {
    replyContent.value = '';
  }
};

const cancelReply = () => {
  showReply.value = false;
  replyContent.value = '';
};

const handleRead = () => {
  emit('read', props.notification.notificationId);
};

const handleDelete = () => {
  emit('delete', props.notification.notificationId);
};

const handleReply = async () => {
  if (!replyContent.value.trim()) {
    ElMessage.warning('请输入回复内容');
    return;
  }

  isSubmitting.value = true;
  try {
    // await emit('reply', {
    //   notificationId: props.notification.notificationId,
    //   articleId: props.notification.sourceId,
    //   parentId: props.notification.relatedId,
    //   content: replyContent.value
    // });
    const parentId = props.notification.relatedId
    await submitCommentToPost(props.notification.sourceId, {
      replyContent,
      parentId,
      parentId
    })


    ElMessage.success('回复成功');
    showReply.value = false;
    replyContent.value = '';
  } catch (error) {
    ElMessage.error(error.message || '回复失败');
  } finally {
    isSubmitting.value = false;
  }
};
</script>

<style lang="scss" scoped>
.comment-notification {
  display: flex;
  padding: 16px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  margin-bottom: 12px;
  transition: all 0.2s ease;

  &.unread {
    background-color: rgba(103, 194, 58, 0.05);
    border-left: 3px solid #67c23a;
  }

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .notification-avatar {
    margin-right: 16px;
    flex-shrink: 0;

    .el-avatar {
      width: 44px;
      height: 44px;
      background-color: #f0f9eb;
    }
  }

  .notification-main {
    flex: 1;
    min-width: 0;
  }

  .notification-header {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    margin-bottom: 8px;
    line-height: 1.5;

    .username {
      font-weight: 600;
      color: #67c23a;
      margin-right: 4px;
    }

    .action-text {
      color: #666;
      margin-right: 4px;
    }

    .source-title {
      color: #333;
      font-weight: 500;
      text-decoration: none;
      max-width: 200px;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      display: inline-block;

      &:hover {
        color: #67c23a;
        text-decoration: underline;
      }
    }
  }

  .comment-content {
    padding: 8px 12px;
    margin: 8px 0;
    background-color: #f8f8f8;
    border-radius: 4px;
    color: #333;
    font-size: 14px;
    line-height: 1.6;
    word-break: break-word;
  }

  .notification-footer {
    display: flex;
    align-items: center;
    font-size: 12px;
    color: #999;
    margin-top: 4px;

    .divider {
      margin: 0 6px;
    }

    .reply-trigger {
      color: #67c23a;
      cursor: pointer;

      &:hover {
        text-decoration: underline;
      }
    }
  }

  .reply-box {
    margin-top: 12px;
    border: 1px solid #eee;
    border-radius: 4px;
    padding: 8px;
    background-color: #fafafa;

    .el-textarea {
      margin-bottom: 8px;
    }

    .reply-actions {
      display: flex;
      justify-content: flex-end;
      gap: 8px;
    }
  }

  .notification-actions {
    display: flex;
    align-items: flex-start;
    margin-left: 12px;

    .el-button {
      padding: 0 8px;
      color: #999;
      font-size: 12px;

      &:hover {
        color: #67c23a;
      }
    }
  }
}
</style>