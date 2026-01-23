<template>
  <div class="user-posts">
    <el-tabs v-model="activeTab" class="custom-tabs">
      <el-tab-pane label="投稿" name="posts">
        <PostList :list="postLists"/>
      </el-tab-pane>

      <el-tab-pane label="收藏" name="favorites">
        <PostList :list="favoriteLists"/>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import PostList from './PostList.vue'
import {getUserFavorites, getUserPosts} from '@/api/author.js'

const props = defineProps({
  userId: {
    type: [String, Number],
    required: true
  }
})

const activeTab = ref('posts')
const postLists = ref([])
const favoriteLists = ref([])

const fetchPosts = async () => {
  try {
    const response = await getUserPosts(props.userId)
    postLists.value = response.data
  } catch (error) {
    console.error('获取用户帖子失败:', error)
  }
}

const fetchFavorites = async () => {
  try {
    const response = await getUserFavorites(props.userId)
    favoriteLists.value = response.data
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
  background: rgba(255, 255, 255, 0.8);
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 6px 24px rgba(99, 102, 241, 0.12);
  border: 2px solid rgba(147, 197, 253, 0.3);
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 8px 32px rgba(99, 102, 241, 0.18);
  }

  :deep(.el-tabs__header) {
    margin-bottom: 20px;
  }

  :deep(.el-tabs__nav-wrap::after) {
    background: linear-gradient(90deg, transparent, #e0e7ff, transparent);
  }

  :deep(.el-tabs__item) {
    font-size: 16px;
    font-weight: 600;
    color: #64748b;
    transition: all 0.3s ease;
    padding: 0 28px;

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
</style>
