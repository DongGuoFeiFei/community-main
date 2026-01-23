<template>
  <div class="follow-list">
    <div v-if="list.length === 0" class="empty-tip">
      <el-empty description="暂无数据"/>
    </div>

    <div v-else class="list-container">
      <div v-for="item in list" :key="item.id" class="follow-item">
        <el-avatar :size="50" :src="item.avatar"/>

        <div class="item-info">
          <router-link :to="`/author/${item.id}`" target="_blank" class="username">{{ item.nickname }}</router-link>
          <p class="bio">{{ item.bio || '暂无简介' }}</p>
        </div>

<!--        <el-button v-if="type === 'following'" size="small" @click="handleFollow(item.id)">-->
<!--          {{ item.isFollowing ? '已关注' : '关注' }}-->
<!--        </el-button>-->
      </div>
    </div>
  </div>
</template>

<script setup>
import {followUser} from '@/api/author.js'

const props = defineProps({
  list: {
    type: Array,
    required: true
  },
  type: {
    type: String,
    required: true,
    validator: value => ['following', 'followers'].includes(value)
  }
})

const handleFollow = async (userId) => {
  try {
    await followUser(userId)
    // 更新本地状态
    const item = props.list.find(item => item.id === userId)
    if (item) {
      item.isFollowing = !item.isFollowing
    }
  } catch (error) {
    console.error('关注操作失败:', error)
  }
}
</script>

<style lang="scss" scoped>
.follow-list {
  .empty-tip {
    padding: 40px 20px;
    text-align: center;
    background: linear-gradient(135deg, rgba(224, 242, 254, 0.3), rgba(243, 232, 255, 0.2));
    border-radius: 12px;
    border: 2px dashed rgba(147, 197, 253, 0.4);
  }

  .list-container {
    max-height: 500px;
    overflow-y: auto;
    padding-right: 8px;

    // 自定义滚动条
    &::-webkit-scrollbar {
      width: 6px;
    }

    &::-webkit-scrollbar-track {
      background: rgba(224, 242, 254, 0.3);
      border-radius: 3px;
    }

    &::-webkit-scrollbar-thumb {
      background: linear-gradient(180deg, #93c5fd, #a78bfa);
      border-radius: 3px;
      transition: background 0.3s ease;

      &:hover {
        background: linear-gradient(180deg, #60a5fa, #8b5cf6);
      }
    }
  }

  .follow-item {
    display: flex;
    align-items: center;
    padding: 16px;
    border-bottom: 1px solid rgba(224, 231, 255, 0.5);
    background: rgba(255, 255, 255, 0.5);
    margin-bottom: 8px;
    border-radius: 12px;
    transition: all 0.3s ease;
    position: relative;
    overflow: hidden;

    &:hover {
      background: rgba(255, 255, 255, 0.9);
      transform: translateX(5px);
      box-shadow: 0 4px 16px rgba(99, 102, 241, 0.12);

      &::before {
        opacity: 1;
      }

      .el-avatar {
        transform: scale(1.1);
      }
    }

    // 悬停装饰条
    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 0;
      bottom: 0;
      width: 4px;
      background: linear-gradient(180deg, #6366f1, #a855f7);
      opacity: 0;
      transition: opacity 0.3s ease;
    }

    .el-avatar {
      margin-right: 16px;
      border: 3px solid #fff;
      box-shadow: 0 4px 12px rgba(99, 102, 241, 0.2);
      transition: all 0.3s ease;
      flex-shrink: 0;
    }

    .item-info {
      flex-grow: 1;
      min-width: 0;

      .username {
        color: #1e293b;
        font-weight: 700;
        font-size: 15px;
        text-decoration: none;
        display: inline-block;
        position: relative;
        transition: all 0.3s ease;

        &::after {
          content: '';
          position: absolute;
          bottom: -2px;
          left: 0;
          width: 0;
          height: 2px;
          background: linear-gradient(90deg, #6366f1, #a855f7);
          transition: width 0.3s ease;
        }

        &:hover {
          color: #6366f1;

          &::after {
            width: 100%;
          }
        }
      }

      .bio {
        margin: 6px 0 0 0;
        font-size: 13px;
        color: #64748b;
        line-height: 1.4;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
      }
    }

    .el-button {
      margin-left: 12px;
      border-radius: 16px;
      font-weight: 600;
      transition: all 0.3s ease;
      flex-shrink: 0;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
      }
    }
  }
}
</style>
