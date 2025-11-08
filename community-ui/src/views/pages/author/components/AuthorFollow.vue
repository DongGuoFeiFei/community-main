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
    :deep(.el-tabs__content) {
      padding: 0;
    }
  }
}
</style>