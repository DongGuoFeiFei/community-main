<template>
  <div class="comment-item" :class="{ 'is-reply': isReply }">
    <div class="comment-header">
      <el-avatar
        :size="isReply ? 28 : 32"
        :src="avatarUrl"
        class="comment-avatar"
      />

      <div class="comment-meta">
        <router-link
          :to="{ path: `/author/${comment.userId}` }"
          target="_blank"
          class="comment-author"
        >
          {{ comment.nickname || "匿名用户" }}
        </router-link>

        <span v-if="comment.repliedNickname && isReply" class="reply-to">
          回复
          <span class="replied-author">@{{ comment.repliedNickname }}</span>
        </span>

        <span class="comment-time">{{ formattedTime }}</span>
      </div>

      <div class="comment-actions-top">
        <el-button
          v-if="canDelete"
          link
          type="danger"
          size="small"
          class="delete-btn"
          @click="handleDelete"
        >
          <el-icon><Delete /></el-icon>
          删除
        </el-button>

        <el-button
          v-if="hasReplies && !isReply"
          link
          type="primary"
          size="small"
          class="toggle-btn"
          @click="handleToggleReplies"
        >
          <el-icon>
            <component :is="isExpanded ? ArrowUp : ArrowDown" />
          </el-icon>
          {{ isExpanded ? "收起" : `展开` }}({{ replyCount }})
        </el-button>
      </div>
    </div>

    <div class="comment-content">
      <p>{{ comment.content }}</p>
    </div>

    <div class="comment-footer">
      <el-button
        link
        type="primary"
        size="small"
        @click="handleReply"
        class="reply-btn"
      >
        <el-icon><ChatDotRound /></el-icon>
        回复
      </el-button>
    </div>

    <!-- 回复表单 -->
    <div v-if="showReplyForm" class="reply-form-wrapper">
      <CommentForm
        :placeholder="`回复 @${comment.nickname}...`"
        :max-length="500"
        submit-text="发送"
        @submit="handleSubmitReply"
        @cancel="showReplyForm = false"
        show-cancel
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from "vue";
import {
  Delete,
  ArrowUp,
  ArrowDown,
  ChatDotRound,
} from "@element-plus/icons-vue";
import { localStores } from "@/stores/localStores";
import type { Comment } from "@/types/comment";
import CommentForm from "./CommentForm.vue";

interface Props {
  comment: Comment;
  isReply?: boolean;
  canDelete?: boolean;
  hasReplies?: boolean;
  replyCount?: number;
  isExpanded?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  isReply: false,
  canDelete: false,
  hasReplies: false,
  replyCount: 0,
  isExpanded: false,
});

const emit = defineEmits<{
  delete: [commentId: string | number];
  reply: [commentId: string | number, content: string];
  toggleReplies: [commentId: string | number];
}>();

const store = localStores();
const showReplyForm = ref(false);

/**
 * 计算头像完整URL
 */
const avatarUrl = computed(() => {
  return props.comment.accessUrl
    ? `${store.baseURL}${props.comment.accessUrl}`
    : "";
});

/**
 * 格式化时间显示
 */
const formattedTime = computed(() => {
  const date = new Date(props.comment.createdAt);
  const now = new Date();
  const diff = now.getTime() - date.getTime();

  // 1分钟内
  if (diff < 60000) {
    return "刚刚";
  }
  // 1小时内
  if (diff < 3600000) {
    return `${Math.floor(diff / 60000)}分钟前`;
  }
  // 24小时内
  if (diff < 86400000) {
    return `${Math.floor(diff / 3600000)}小时前`;
  }
  // 7天内
  if (diff < 604800000) {
    return `${Math.floor(diff / 86400000)}天前`;
  }

  // 超过7天显示具体日期
  return date.toLocaleDateString("zh-CN", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  });
});

/**
 * 处理删除
 */
const handleDelete = () => {
  emit("delete", props.comment.commentId);
};

/**
 * 处理回复
 */
const handleReply = () => {
  showReplyForm.value = !showReplyForm.value;
};

/**
 * 处理提交回复
 */
const handleSubmitReply = async (content: string) => {
  emit("reply", props.comment.commentId, content);
  showReplyForm.value = false;
};

/**
 * 处理切换回复列表
 */
const handleToggleReplies = () => {
  emit("toggleReplies", props.comment.commentId);
};
</script>

<style scoped lang="scss">
.comment-item {
  padding: 20px;
  margin-bottom: 12px;
  background: rgba(255, 255, 255, 0.65);
  border-radius: 20px;
  border: 2px solid rgba(135, 206, 235, 0.15);
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  position: relative;
  overflow: hidden;

  &::before {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 3px;
    background: linear-gradient(
      90deg,
      transparent,
      rgba(135, 206, 235, 0.5),
      transparent
    );
    opacity: 0;
    transition: opacity 0.3s ease;
  }

  &:hover {
    background: rgba(224, 247, 250, 0.75);
    border-color: rgba(135, 206, 235, 0.35);
    transform: translateX(3px);
    box-shadow: 0 8px 20px rgba(135, 206, 235, 0.2),
      0 0 0 1px rgba(135, 206, 235, 0.1);

    &::before {
      opacity: 1;
    }
  }

  &.is-reply {
    padding: 15px;
    margin-bottom: 8px;
    background: rgba(255, 255, 255, 0.5);
    border-radius: 15px;

    &:hover {
      background: rgba(230, 245, 250, 0.7);
    }
  }
}

.comment-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  gap: 10px;
}

.comment-avatar {
  flex-shrink: 0;
  border: 2px solid rgba(135, 206, 235, 0.3);
  box-shadow: 0 4px 10px rgba(135, 206, 235, 0.2);
  transition: all 0.3s ease;

  &:hover {
    transform: scale(1.1) rotate(5deg);
    border-color: rgba(135, 206, 235, 0.5);
  }
}

.comment-meta {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.comment-author {
  font-weight: 700;
  font-size: 14px;
  background: linear-gradient(135deg, #5b9bd5, #9370db);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-decoration: none;
  transition: all 0.3s ease;
  position: relative;

  &::after {
    content: "";
    position: absolute;
    bottom: -2px;
    left: 0;
    width: 0;
    height: 2px;
    background: linear-gradient(90deg, #5b9bd5, #9370db);
    transition: width 0.3s ease;
  }

  &:hover::after {
    width: 100%;
  }
}

.reply-to {
  color: #999;
  font-size: 12px;

  .replied-author {
    color: #5b9bd5;
    font-weight: 600;
  }
}

.comment-time {
  color: #999;
  font-size: 12px;
  white-space: nowrap;
}

.comment-actions-top {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-left: auto;
}

.delete-btn,
.toggle-btn {
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 12px;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
  }
}

.comment-content {
  margin: 12px 0;
  padding: 12px 15px;
  background: rgba(224, 247, 250, 0.6);
  border-radius: 12px;
  border-left: 3px solid rgba(135, 206, 235, 0.5);
  line-height: 1.7;
  color: #555;
  font-size: 14px;
  word-wrap: break-word;
  word-break: break-word;

  p {
    margin: 0;
  }

  .is-reply & {
    margin: 8px 0;
    padding: 10px 12px;
    font-size: 13px;
  }
}

.comment-footer {
  display: flex;
  align-items: center;
  gap: 12px;
}

.reply-btn {
  padding: 4px 8px;
  font-size: 12px;
  border-radius: 12px;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);

  &:hover {
    transform: translateY(-2px) scale(1.05);
  }

  .el-icon {
    margin-right: 4px;
  }
}

.reply-form-wrapper {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px dashed rgba(135, 206, 235, 0.2);
  animation: slideDown 0.3s ease;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
