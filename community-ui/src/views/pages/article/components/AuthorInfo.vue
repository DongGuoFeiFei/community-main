<template>
  <div class="author-card">
    <div class="author-header">
      <el-avatar :size="80" :src="store.baseURL + props.authorInfo.avatar" />
      <router-link
        :to="{ path: `/author/${props.authorInfo.id}` }"
        target="_blank"
        ><h3>{{ props.authorInfo.nickname }}</h3>
      </router-link>
    </div>

    <div class="author-bio">
      <p>{{ props.authorInfo.bio || "这个作者很懒，什么都没留下~" }}</p>
    </div>

    <div class="author-stats">
      <div class="stat-item">
        <span class="stat-number">{{ props.authorInfo.postCount }}</span>
        <span class="stat-label">文章</span>
      </div>
      <div class="stat-item">
        <span class="stat-number">{{ props.authorInfo.followingCount }}</span>
        <span class="stat-label">粉丝</span>
      </div>
      <div class="stat-item">
        <span class="stat-number">{{ props.authorInfo.followerCount }}</span>
        <span class="stat-label">关注</span>
      </div>
    </div>

    <div class="author-actions">
      <el-button
        :type="props.authorInfo.isFollowing ? 'default' : 'primary'"
        size="small"
        @click="toggleFollow"
        :disabled="
          Number(props.authorInfo.id) === store.userInfo.userInfo.userId
        "
      >
        {{ props.authorInfo.isFollowing ? "已关注" : "关注" }}
      </el-button>
    </div>

    <div class="author-meta">
      <el-tag size="small" type="info">
        <i class="el-icon-time"></i> 加入于 {{ props.authorInfo.joinDate }}
      </el-tag>
    </div>
  </div>
</template>

<script setup>
import { ElAvatar, ElButton, ElTag } from "element-plus";
import { localStores } from "@/stores/localStores.js";
import { addFollowAuthor, delFollowAuthor } from "@/api/follow.js";

const props = defineProps({
  authorInfo: {
    type: Object,
    required: true,
  },
});
const store = localStores();

// 关注/取消关注
const toggleFollow = () => {
  if (props.authorInfo.isFollowing) {
    delFollowAuthor(props.authorInfo.id);
    props.authorInfo.isFollowing = !props.authorInfo.isFollowing;
    props.authorInfo.followingCount += props.authorInfo.isFollowing ? 1 : -1;
  } else {
    addFollowAuthor(props.authorInfo.id);
    props.authorInfo.isFollowing = !props.authorInfo.isFollowing;
    props.authorInfo.followingCount += props.authorInfo.isFollowing ? 1 : -1;
  }
};
</script>

<style scoped lang="scss">
.author-card {
  max-width: 100%;
  margin: 40px auto;
  padding: 25px;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  border-radius: 28px;
  border: 3px solid rgba(255, 255, 255, 0.6);
  box-shadow: 0 15px 40px rgba(135, 206, 235, 0.25),
    inset 0 2px 10px rgba(255, 255, 255, 0.9),
    0 0 0 1px rgba(147, 112, 219, 0.15);
  text-align: center;
  position: sticky;
  top: 20px;
  transition: all 0.5s cubic-bezier(0.34, 1.56, 0.64, 1);
  animation: bounceIn 0.8s cubic-bezier(0.34, 1.56, 0.64, 1);
  overflow: hidden;

  // 装饰气泡
  &::before {
    content: "✨";
    position: absolute;
    top: 15px;
    right: 15px;
    font-size: 20px;
    animation: twinkle 2s infinite ease-in-out;
  }

  &::after {
    content: "";
    position: absolute;
    bottom: -30px;
    left: -30px;
    width: 100px;
    height: 100px;
    background: radial-gradient(
      circle,
      rgba(135, 206, 235, 0.2),
      transparent 60%
    );
    border-radius: 50%;
    pointer-events: none;
  }

  &:hover {
    transform: translateY(-5px) scale(1.02);
    box-shadow: 0 25px 60px rgba(135, 206, 235, 0.35),
      inset 0 2px 10px rgba(255, 255, 255, 1), 0 0 50px rgba(135, 206, 235, 0.4);
    border-color: rgba(135, 206, 235, 0.8);
  }

  .author-header {
    margin-bottom: 20px;
    position: relative;

    :deep(.el-avatar) {
      border: 4px solid rgba(135, 206, 235, 0.3);
      box-shadow: 0 8px 20px rgba(135, 206, 235, 0.3),
        0 0 30px rgba(135, 206, 235, 0.2);
      transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);

      &:hover {
        transform: scale(1.1) rotate(5deg);
        box-shadow: 0 12px 30px rgba(135, 206, 235, 0.4),
          0 0 40px rgba(135, 206, 235, 0.4);
        border-color: rgba(135, 206, 235, 0.6);
      }
    }

    h3 {
      margin: 15px 0 5px;
      font-size: 20px;
      font-weight: 700;
      background: linear-gradient(135deg, #5b9bd5, #9370db);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      transition: all 0.3s ease;

      &:hover {
        transform: scale(1.05);
      }
    }

    .username {
      margin: 0;
      font-size: 14px;
      color: #9370db;
      font-weight: 500;
    }
  }

  .author-bio {
    margin: 20px 0;
    padding: 15px;
    border-top: 2px dashed rgba(135, 206, 235, 0.3);
    border-bottom: 2px dashed rgba(135, 206, 235, 0.3);
    font-size: 14px;
    color: #666;
    line-height: 1.7;
    background: rgba(224, 247, 250, 0.4);
    border-radius: 15px;
    position: relative;

    &::before {
      // content: '💬';
      position: absolute;
      top: -15px;
      left: 50%;
      transform: translateX(-50%);
      font-size: 24px;
      background: rgba(255, 255, 255, 0.9);
      padding: 4px 8px;
      border-radius: 50%;
    }
  }

  .author-stats {
    display: flex;
    justify-content: space-around;
    margin: 25px 0;
    padding: 15px;
    background: rgba(224, 247, 250, 0.3);
    border-radius: 20px;
    gap: 10px;

    .stat-item {
      display: flex;
      flex-direction: column;
      padding: 10px;
      border-radius: 15px;
      transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
      cursor: pointer;

      &:hover {
        background: rgba(135, 206, 235, 0.2);
        transform: translateY(-5px) scale(1.1);
      }

      .stat-number {
        font-weight: bold;
        font-size: 20px;
        background: linear-gradient(135deg, #5b9bd5, #9370db);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
      }

      .stat-label {
        font-size: 12px;
        color: #9370db;
        margin-top: 4px;
        font-weight: 500;
      }
    }
  }

  .author-actions {
    margin: 20px 0;

    :deep(.el-button) {
      border-radius: 20px !important;
      padding: 10px 24px;
      font-weight: 600;
      transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
      border: 2px solid transparent;

      &.el-button--primary {
        background: linear-gradient(135deg, #87ceeb, #9370db);
        border: none;
        box-shadow: 0 8px 20px rgba(135, 206, 235, 0.3);

        &:hover {
          transform: translateY(-3px) scale(1.05);
          box-shadow: 0 12px 30px rgba(135, 206, 235, 0.4),
            0 0 30px rgba(135, 206, 235, 0.5);
        }
      }

      &.el-button--default {
        background: rgba(255, 255, 255, 0.8);
        color: #5b9bd5;
        border-color: rgba(135, 206, 235, 0.5);

        &:hover {
          background: rgba(224, 247, 250, 0.9);
          border-color: #87ceeb;
          transform: translateY(-3px) scale(1.05);
        }
      }

      &:disabled {
        opacity: 0.5;
        cursor: not-allowed;
      }
    }
  }

  .author-meta {
    margin-top: 15px;
    font-size: 12px;
    color: #9370db;

    :deep(.el-tag) {
      border-radius: 15px;
      padding: 6px 14px;
      background: rgba(147, 112, 219, 0.15);
      border: 2px solid rgba(147, 112, 219, 0.3);
      color: #9370db;
      font-weight: 500;

      &::before {
        content: "🎂";
        margin-right: 5px;
      }
    }
  }
}

@keyframes bounceIn {
  0% {
    opacity: 0;
    transform: scale(0.3);
  }
  50% {
    opacity: 1;
    transform: scale(1.05);
  }
  70% {
    transform: scale(0.95);
  }
  100% {
    transform: scale(1);
  }
}

@keyframes twinkle {
  0%,
  100% {
    opacity: 0.6;
    transform: scale(1);
  }
  50% {
    opacity: 1;
    transform: scale(1.3);
  }
}
</style>
