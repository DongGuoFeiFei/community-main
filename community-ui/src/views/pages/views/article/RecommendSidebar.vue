<script setup>
import {onMounted, ref, watch} from 'vue';
import {getHotPosts} from '@/api/article';
import {useRouter} from 'vue-router';

const props = defineProps({
  articleId: {
    type: Number,
    default: null
  }
});

const router = useRouter();
const relatedPosts = ref([]);
const hotTags = ref([]);
const loading = ref(false);

const fetchRecommendations = async () => {
  try {
    loading.value = true;
    const postsResponse = await getHotPosts();
    relatedPosts.value = postsResponse.data;
  } catch (error) {
    console.error('获取推荐内容失败:', error);
  } finally {
    loading.value = false;
  }
};

onMounted(() => {
  fetchRecommendations();
});
watch(
    () => props.articleId,
    () => {
      fetchRecommendations();
    }
)

</script>

<template>
  <div class="recommend-sidebar">
    <!-- 相关文章推荐 -->
    <el-card class="recommend-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span>热门文章推荐</span>
        </div>
      </template>
      <div v-loading="loading">
        <div
            v-for="post in relatedPosts"
            :key="post.articleId"
            class="recommend-item"
        >
          <router-link :to="`/article/${post.articleId}`" target="_blank">
            <div class="post-container">
              <div class="post-cover">
                <el-image
                    :src="post.coverUrl"
                    fit="cover"
                    class="cover-image"
                />
              </div>
              <div class="post-content">
                <div class="post-title">{{ post.title }}</div>
                <div class="post-meta">
                  <span class="post-date">{{ post.createdAt }}</span>
                </div>
                <div class="post-summary" v-if="post.content">
                  {{ post.content.substring(0, 15) }}...
                </div>
              </div>
            </div>
          </router-link>
        </div>
        <el-empty v-if="!loading && relatedPosts.length === 0" description="暂无相关文章"/>
      </div>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.recommend-sidebar {
  position: sticky;
  top: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;

  .recommend-card {
    border-radius: 8px;

    .card-header {
      font-weight: bold;
      font-size: 16px;
    }

    .recommend-item {
      padding: 12px 0;
      cursor: pointer;

      transition: all 0.3s;
      border-bottom: 1px solid var(--el-border-color-light);

      &:hover {
        background-color: var(--el-color-primary-light-9);
      }

      .post-container {
        display: flex;
        gap: 12px;
        color: black;
        align-items: center;

        .post-cover {
          flex: 0 0 80px;
          height: 60px;
          border-radius: 4px;
          overflow: hidden;

          .cover-image {
            width: 100%;
            height: 100%;
            object-fit: cover;
          }
        }

        .post-content {
          flex: 1;
          min-width: 0;

          .post-title {
            font-size: 14px;
            font-weight: 500;
            margin-bottom: 4px;
            display: -webkit-box;
            -webkit-line-clamp: 1;
            -webkit-box-orient: vertical;
            overflow: hidden;
          }

          .post-meta {
            font-size: 12px;
            color: var(--el-text-color-secondary);
            margin-bottom: 4px;
          }

          .post-summary {
            font-size: 12px;
            color: var(--el-text-color-regular);
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
          }
        }
      }
    }
  }
}
</style>