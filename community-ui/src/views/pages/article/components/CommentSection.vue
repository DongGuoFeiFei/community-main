<template>
  <div class="comment-section">
    <!-- 标题区域 -->
    <div class="section-header">
      <h3 class="comment-title">
        <span class="title-icon">💬</span>
        评论区
        <span class="comment-count"
          >({{ commentState.totalComments.value }})</span
        >
      </h3>
    </div>

    <!-- 评论输入框 -->
    <div v-if="commentState.isOpen.value" class="comment-form-container">
      <CommentForm
        title="发表评论"
        placeholder="写下你的精彩评论..."
        :max-length="500"
        :rows="4"
        submit-text="发表评论"
        show-header
        @submit="handleSubmitMainComment"
      />
    </div>

    <!-- 评论关闭提示 -->
    <div v-else class="comment-closed">
      <el-alert
        title="评论功能已关闭"
        type="info"
        description="当前文章的评论功能已关闭，无法发表新的评论。"
        :closable="false"
        show-icon
      />
    </div>

    <!-- 评论列表 -->
    <div v-if="commentState.isOpen.value" class="comment-list-container">
      <!-- 加载状态 -->
      <div v-if="commentState.loading.value" class="loading-state">
        <el-skeleton :rows="3" animated />
        <el-skeleton :rows="3" animated style="margin-top: 20px" />
      </div>

      <!-- 空状态 -->
      <div
        v-else-if="commentState.comments.value.length === 0"
        class="empty-state"
      >
        <div class="empty-icon">💭</div>
        <p class="empty-text">暂无评论</p>
        <p class="empty-hint">快来发表你的看法吧~</p>
      </div>

      <!-- 评论列表 -->
      <template v-else>
        <div
          v-for="comment in commentState.comments.value"
          :key="comment.commentId"
          class="main-comment-wrapper"
        >
          <!-- 主评论 -->
          <CommentItem
            :comment="comment"
            :can-delete="commentState.canDeleteComment(comment)"
            :has-replies="comment.voList && comment.voList.length > 0"
            :reply-count="commentState.countReplies(comment)"
            :is-expanded="
              commentState.expandedComments.value[comment.commentId]
            "
            @delete="commentState.handleDeleteComment"
            @reply="handleReplyToComment"
            @toggle-replies="commentState.toggleReplies"
          />

          <!-- 回复列表（展开时显示） -->
          <transition name="replies-expand">
            <div
              v-if="
                comment.voList &&
                comment.voList.length > 0 &&
                commentState.expandedComments.value[comment.commentId]
              "
              class="reply-list"
            >
              <CommentItem
                v-for="reply in commentState.flattenReplies(comment.voList)"
                :key="reply.commentId"
                :comment="reply"
                :is-reply="true"
                :can-delete="commentState.canDeleteComment(reply)"
                @delete="commentState.handleDeleteComment"
                @reply="handleReplyToComment"
              />
            </div>
          </transition>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, watch } from "vue";
import { ElMessage } from "element-plus";
import { useComments } from "@/composables/useComments";
import { localStores } from "@/stores/localStores";
import CommentItem from "./CommentItem.vue";
import CommentForm from "./CommentForm.vue";

interface Props {
  postId: string | number;
  authorId: string | number;
}

const props = defineProps<Props>();

const store = localStores();
const currentUserId = store.userInfo.userInfo.userId;

/**
 * 使用评论组合式函数
 */
const commentState = useComments({
  postId: props.postId,
  authorId: props.authorId,
  currentUserId: currentUserId,
});

/**
 * 处理提交主评论，一级评论
 */
const handleSubmitMainComment = async (content: string) => {
  const newComment = await commentState.submitComment(content);
  if (newComment) {
    commentState.addCommentToList(newComment);
    ElMessage.success("评论发表成功！");
  }
};

/**
 * 处理回复主评论，二级评论
 */
const handleReplyToComment = async (
  commentId: string | number,
  content: string
) => {
  const newReply = await commentState.submitComment(
    content,
    commentId,
    commentId
  );

  if (newReply) {
    commentState.addCommentToList(newReply, commentId);
    ElMessage.success("回复发表成功！");
  }
};


/**
 * 初始化加载评论
 */
onMounted(() => {
  commentState.loadComments();
});

/**
 * 监听 postId 变化重新加载
 */
watch(
  () => props.postId,
  () => {
    commentState.loadComments();
  }
);
</script>

<style scoped lang="scss">
.comment-section {
  margin-top: 40px;
  padding: 35px;
  background: linear-gradient(
    135deg,
    rgba(255, 255, 255, 0.9) 0%,
    rgba(224, 247, 250, 0.85) 100%
  );
  backdrop-filter: blur(20px);
  border-radius: 28px;
  border: 3px solid rgba(255, 255, 255, 0.6);
  box-shadow: 0 15px 50px rgba(135, 206, 235, 0.25),
    inset 0 2px 15px rgba(255, 255, 255, 0.9),
    0 0 0 1px rgba(147, 112, 219, 0.1);
  animation: sectionFadeIn 0.8s cubic-bezier(0.34, 1.56, 0.64, 1);
  position: relative;
  overflow: hidden;

  // 背景装饰
  &::before {
    content: "";
    position: absolute;
    top: -50%;
    right: -50%;
    width: 200%;
    height: 200%;
    background: radial-gradient(
      circle,
      rgba(135, 206, 235, 0.05) 0%,
      transparent 70%
    );
    animation: rotate 30s linear infinite;
    pointer-events: none;
  }

  // 顶部波浪装饰
  &::after {
    content: "";
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(90deg, #87ceeb 0%, #9370db 50%, #87ceeb 100%);
    background-size: 200% 100%;
    animation: shimmer 3s linear infinite;
  }
}

.section-header {
  margin-bottom: 25px;
  position: relative;
  z-index: 1;
}

.comment-title {
  margin: 0;
  font-size: 24px;
  font-weight: 700;
  display: flex;
  align-items: center;
  gap: 10px;

  .title-icon {
    font-size: 28px;
    animation: float 3s infinite ease-in-out;
  }

  background: linear-gradient(135deg, #5b9bd5, #9370db);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;

  .comment-count {
    font-size: 18px;
    opacity: 0.7;
  }
}

.comment-form-container {
  margin-bottom: 30px;
  position: relative;
  z-index: 1;
}

.comment-closed {
  margin-bottom: 30px;
  position: relative;
  z-index: 1;

  :deep(.el-alert) {
    border-radius: 20px;
    border: 2px solid rgba(100, 150, 200, 0.2);
  }
}

.comment-list-container {
  position: relative;
  z-index: 1;
}

.loading-state {
  padding: 20px;

  :deep(.el-skeleton) {
    .el-skeleton__item {
      background: linear-gradient(
        90deg,
        rgba(135, 206, 235, 0.1) 25%,
        rgba(135, 206, 235, 0.2) 50%,
        rgba(135, 206, 235, 0.1) 75%
      );
      background-size: 200% 100%;
      animation: loading 1.5s ease infinite;
      border-radius: 12px;
    }
  }
}

.empty-state {
  padding: 60px 20px;
  text-align: center;

  .empty-icon {
    font-size: 64px;
    margin-bottom: 20px;
    animation: float 3s infinite ease-in-out;
  }

  .empty-text {
    font-size: 18px;
    font-weight: 600;
    color: #999;
    margin: 0 0 10px 0;
  }

  .empty-hint {
    font-size: 14px;
    color: #bbb;
    margin: 0;
  }
}

.main-comment-wrapper {
  margin-bottom: 20px;
  animation: commentSlideIn 0.5s ease;

  &:last-child {
    margin-bottom: 0;
  }
}

.reply-list {
  margin-top: 15px;
  margin-left: 40px;
  padding-left: 20px;
  border-left: 3px solid rgba(135, 206, 235, 0.3);
  position: relative;

  &::before {
    content: "";
    position: absolute;
    left: -3px;
    top: 0;
    width: 3px;
    height: 0;
    background: linear-gradient(180deg, #87ceeb, #9370db);
    animation: replyLineGrow 0.5s ease forwards;
  }
}

// 回复展开动画
.replies-expand-enter-active,
.replies-expand-leave-active {
  transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
  overflow: hidden;
}

.replies-expand-enter-from,
.replies-expand-leave-to {
  opacity: 0;
  max-height: 0;
  transform: translateY(-20px);
}

.replies-expand-enter-to,
.replies-expand-leave-from {
  opacity: 1;
  max-height: 2000px;
  transform: translateY(0);
}

// 动画定义
@keyframes sectionFadeIn {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
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

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

@keyframes shimmer {
  0% {
    background-position: 200% center;
  }
  100% {
    background-position: -200% center;
  }
}

@keyframes commentSlideIn {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes loading {
  0% {
    background-position: 200% center;
  }
  100% {
    background-position: -200% center;
  }
}

@keyframes replyLineGrow {
  from {
    height: 0;
  }
  to {
    height: 100%;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .comment-section {
    padding: 20px;
    margin-top: 30px;
  }

  .comment-title {
    font-size: 20px;

    .title-icon {
      font-size: 24px;
    }

    .comment-count {
      font-size: 16px;
    }
  }

  .reply-list {
    margin-left: 20px;
    padding-left: 15px;
  }
}
</style>
