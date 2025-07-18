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
import {followUser} from '@/api/author'

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
    padding: 20px 0;
  }

  .list-container {
    max-height: 500px;
    overflow-y: auto;
  }

  .follow-item {
    display: flex;
    align-items: center;
    padding: 15px;
    border-bottom: 1px solid var(--el-border-color-light);

    .el-avatar {
      margin-right: 15px;
    }

    .item-info {
      flex-grow: 1;

      .username {
        color: black;
        font-weight: bold;
        //color: var(--el-color-primary);
        text-decoration: none;

        &:hover {
          text-decoration: underline;
        }
      }

      .bio {
        margin: 5px 0 0 0;
        font-size: 13px;
        color: var(--el-text-color-secondary);
      }
    }

    .el-button {
      margin-left: 10px;
    }
  }
}
</style>