<template>
  <div class="user-stats">
    <el-card shadow="hover">
      <div class="stats-container">
        <div class="stat-item">
          <div class="stat-value">{{ stats.postCount || 0 }}</div>
          <div class="stat-label">帖子</div>
        </div>

        <div class="stat-item">
          <div class="stat-value">{{ stats.followers || 0 }}</div>
          <div class="stat-label">关注</div>
        </div>

        <div class="stat-item">
          <div class="stat-value">{{ stats.following || 0 }}</div>
          <div class="stat-label">粉丝</div>
        </div>
      </div>

      <div class="follow-button">
        <el-button
            :type="isFollowing ? 'default' : 'primary'"
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
      props.stats.followers = Math.max(0, props.stats.followers - 1)
    } else {
      await addFollowAuthor(props.userId)
      props.stats.followers += 1
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
    padding: 15px 0;
    position: relative;
  }

  .stats-container {
    display: flex;
    justify-content: space-around;
  }

  .stat-item {
    display: inline-block;
    width: 33.3%;
    text-align: center;

    .stat-value {
      font-size: 20px;
      font-weight: bold;
      margin-bottom: 5px;
    }

    .stat-label {
      font-size: 14px;
      color: var(--el-text-color-secondary);
    }
  }

  .follow-button {
    margin-top: 15px;
    text-align: center;

    .el-button {
      width: 100px;
    }
  }
}
</style>