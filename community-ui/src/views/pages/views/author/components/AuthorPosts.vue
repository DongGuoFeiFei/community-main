<template>
  <div class="user-posts">
    <el-tabs v-model="activeTab" class="custom-tabs">
      <el-tab-pane label="帖子" name="posts">
        <PostList :list="postLists"/>
      </el-tab-pane>

      <el-tab-pane label="收藏" name="favorites">
        <PostList :list="favoriteLists"/>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<!--todo 添加一个分页-->
<script setup>
import {onMounted, ref} from 'vue'
import PostList from './PostList.vue'
import {getUserFavorites, getUserPosts} from '@/api/author'

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
  :deep(.el-tabs__content) {
    padding: 0;
  }
}
</style>