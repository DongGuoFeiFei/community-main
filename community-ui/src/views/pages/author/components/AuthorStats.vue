<template>
  <div class="user-stats">
    <el-card shadow="hover">
      <div class="stats-container">
        <div class="stat-item">
          <div class="stat-value">{{ stats.postCount || 0 }}</div>
          <div class="stat-label">帖子</div>
        </div>

        <div class="stat-item">
          <div class="stat-value">{{ stats.following || 0 }}</div>
          <div class="stat-label">粉丝</div>
        </div>

        <div class="stat-item">
          <div class="stat-value">{{ stats.followers || 0 }}</div>
          <div class="stat-label">关注</div>
        </div>
      </div>

      <div class="follow-button">
        <el-button
            :type="isFollowing  ? 'default' : 'primary'"
            :disabled="Number(props.userId) === store.userInfo.userInfo.userId"
            size="small"
            @click="toggleFollow"
        >
          {{ isFollowing ? '已关注' : '关注' }}
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {addFollowAuthor, delFollowAuthor, isFollowingAuthor} from '@/api/follow.js'
import {localStores} from "@/stores/localStores.js";

const props = defineProps({
  stats: {
    type: Object,
    required: true
  },
  userId: {
    type: [String, Number],
    required: true
  }
})

const store = localStores()

const isFollowing = ref(false)

const getIsFollowing = async () => {
  const res = await isFollowingAuthor(props.userId)
  isFollowing.value = res.data
  console.log(res)
}

onMounted(() => {
  getIsFollowing()
})

// 关注/取消关注
const toggleFollow = async () => {
  try {
    if (isFollowing.value) {
      await delFollowAuthor(props.userId)
      props.stats.following = Math.max(0, props.stats.following - 1)
    } else {
      await addFollowAuthor(props.userId)
      props.stats.following += 1
    }
    isFollowing.value = !isFollowing.value
  } catch (error) {
    console.error('操作失败:', error)
  }
}
</script>

<style lang="scss" scoped>
.user-stats {
  margin-bottom: 20px;

  .el-card {
    padding: 20px 0;
    position: relative;
    border-radius: 16px;
    border: 2px solid rgba(147, 197, 253, 0.3);
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.9) 0%, rgba(240, 244, 255, 0.8) 100%);
    box-shadow: 0 6px 24px rgba(99, 102, 241, 0.12);
    overflow: hidden;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 10px 32px rgba(99, 102, 241, 0.2);
    }

    // 顶部装饰条
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 4px;
      background: linear-gradient(90deg, #93c5fd, #a78bfa, #c084fc, #86efac);
      background-size: 200% 100%;
      animation: gradient-slide 3s linear infinite;
    }

    // 装饰图案
    &::after {
      content: '♪';
      position: absolute;
      bottom: 10px;
      right: 15px;
      font-size: 40px;
      color: rgba(167, 139, 250, 0.1);
      animation: bounce-gentle 3s ease-in-out infinite;
    }
  }

  .stats-container {
    display: flex;
    justify-content: space-around;
    position: relative;
    z-index: 1;
  }

  .stat-item {
    display: inline-block;
    width: 33.3%;
    text-align: center;
    position: relative;
    padding: 10px 0;
    transition: all 0.3s ease;
    cursor: pointer;

    &:hover {
      transform: translateY(-5px);

      .stat-value {
        color: #6366f1;
        transform: scale(1.15);
      }

      &::before {
        opacity: 1;
      }
    }

    // 悬停背景效果
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 10%;
      right: 10%;
      bottom: 0;
      background: linear-gradient(135deg, rgba(147, 197, 253, 0.2), rgba(167, 139, 250, 0.2));
      border-radius: 12px;
      opacity: 0;
      transition: opacity 0.3s ease;
      z-index: -1;
    }

    // 分隔线
    &:not(:last-child)::after {
      content: '';
      position: absolute;
      right: 0;
      top: 20%;
      bottom: 20%;
      width: 1px;
      background: linear-gradient(180deg, transparent, #e0e7ff, transparent);
    }

    .stat-value {
      font-size: 26px;
      font-weight: 700;
      margin-bottom: 8px;
      background: linear-gradient(135deg, #6366f1, #a855f7);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      transition: all 0.3s ease;
    }

    .stat-label {
      font-size: 14px;
      color: #64748b;
      font-weight: 500;
    }
  }

  .follow-button {
    margin-top: 20px;
    text-align: center;
    position: relative;
    z-index: 1;

    .el-button {
      width: 120px;
      height: 40px;
      border-radius: 20px;
      font-weight: 600;
      font-size: 15px;
      transition: all 0.3s ease;
      box-shadow: 0 4px 12px rgba(99, 102, 241, 0.2);

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 16px rgba(99, 102, 241, 0.3);
      }

      &:active {
        transform: translateY(0);
      }

      &.el-button--primary {
        background: linear-gradient(135deg, #6366f1, #a855f7);
        border: none;

        &:hover {
          background: linear-gradient(135deg, #4f46e5, #9333ea);
        }
      }

      &.el-button--default {
        background: rgba(255, 255, 255, 0.9);
        border: 2px solid #e0e7ff;
        color: #6366f1;

        &:hover {
          background: #f0f4ff;
          border-color: #c7d2fe;
        }
      }
    }
  }
}

// 动画定义
@keyframes gradient-slide {
  0% {
    background-position: 0% 50%;
  }
  100% {
    background-position: 200% 50%;
  }
}

@keyframes bounce-gentle {
  0%, 100% {
    transform: translateY(0) rotate(-10deg);
  }
  50% {
    transform: translateY(-10px) rotate(10deg);
  }
}
</style>