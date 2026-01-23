<template>
  <div class="user-follow">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="关注" name="following">
        <follow-list :list="followingList" type="following" />
      </el-tab-pane>

      <el-tab-pane label="粉丝" name="followers">
        <follow-list :list="followerList" type="followers" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import FollowList from './FollowList.vue'
import { getFollowingList, getFollowerList } from '@/api/follow.js'

const props = defineProps({
  userId: {
    type: [String, Number],
    required: true
  }
})

const activeTab = ref('following')
const followingList = ref([])
const followerList = ref([])

const fetchFollowing = async () => {
  try {
    const response = await getFollowingList(props.userId)
    followingList.value = response.data
  } catch (error) {
    console.error('获取关注列表失败:', error)
  }
}

const fetchFollowers = async () => {
  try {
    const response = await getFollowerList(props.userId)
    followerList.value = response.data
  } catch (error) {
    console.error('获取粉丝列表失败:', error)
  }
}

onMounted(() => {
  fetchFollowing()
  fetchFollowers()
})
</script>

<style lang="scss" scoped>
.user-follow {
  .el-tabs {
    background: rgba(255, 255, 255, 0.9);
    border-radius: 16px;
    padding: 16px;
    box-shadow: 0 6px 24px rgba(99, 102, 241, 0.12);
    border: 2px solid rgba(147, 197, 253, 0.3);
    transition: all 0.3s ease;

    &:hover {
      box-shadow: 0 8px 32px rgba(99, 102, 241, 0.18);
    }

    :deep(.el-tabs__header) {
      margin-bottom: 16px;
    }

    :deep(.el-tabs__nav-wrap::after) {
      background: linear-gradient(90deg, transparent, #e0e7ff, transparent);
    }

    :deep(.el-tabs__item) {
      font-size: 15px;
      font-weight: 600;
      color: #64748b;
      transition: all 0.3s ease;
      padding: 0 24px;

      &:hover {
        color: #6366f1;
      }

      &.is-active {
        color: #6366f1;
      }
    }

    :deep(.el-tabs__active-bar) {
      height: 3px;
      background: linear-gradient(90deg, #6366f1, #a855f7);
      border-radius: 2px;
    }

    :deep(.el-tabs__content) {
      padding: 0;
    }
  }
}
</style>