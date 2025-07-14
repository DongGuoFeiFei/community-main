<template>
  <div class="user-posts">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="帖子" name="posts">
        <post-list :list="postList" />
      </el-tab-pane>

      <el-tab-pane label="收藏" name="favorites">
        <post-list :list="favoriteList" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import PostList from './PostList.vue'
import { getUserPosts, getUserFavorites } from '@/api/author'

const props = defineProps({
  userId: {
    type: [String, Number],
    required: true
  }
})

const activeTab = ref('posts')
const postList = ref([])
const favoriteList = ref([])

const fetchPosts = async () => {
  try {
    const response = await getUserPosts(props.userId)
    postList.value = response.data
  } catch (error) {
    console.error('获取用户帖子失败:', error)
  }
}

const fetchFavorites = async () => {
  try {
    const response = await getUserFavorites(props.userId)
    favoriteList.value = response.data
  } catch (error) {
    console.error('获取用户收藏失败:', error)
  }
}

onMounted(() => {
  fetchPosts()
  fetchFavorites()
})
</script>

<style lang="scss" scoped>
.user-posts {
  :deep(.el-tabs__content) {
    padding: 0;
  }
}
</style>