<script setup>
import {onMounted, ref} from 'vue';
import {getHotTags, getRelatedPosts} from '@/api/article';
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
    // 获取相关文章
    const postsResponse = await getRelatedPosts(props.articleId);
    relatedPosts.value = postsResponse.data.slice(0, 5); // 只显示5篇

    // 获取热门标签
    const tagsResponse = await getHotTags();
    hotTags.value = tagsResponse.data.slice(0, 10); // 只显示10个标签
  } catch (error) {
    console.error('获取推荐内容失败:', error);
  } finally {
    loading.value = false;
  }
};

const navigateToPost = (postId) => {
  router.push(`/article/${postId}`);
};

const navigateToTag = (tagName) => {
  router.push(`/tag/${tagName}`);
};

onMounted(() => {
  if (props.articleId) {
    fetchRecommendations();
  }
});
</script>

<template>
  <div class="recommend-sidebar">
    <!-- 相关文章推荐 -->
    <el-card class="recommend-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span>相关文章推荐</span>
        </div>
      </template>
      <div v-loading="loading">
        <div
            v-for="post in relatedPosts"
            :key="post.id"
            class="recommend-item"
            @click="navigateToPost(post.id)"
        >
          <div class="post-title">{{ post.title }}</div>
          <div class="post-meta">
            <span class="post-views">
              <el-icon><View/></el-icon> {{ post.viewCount }}
            </span>
            <span class="post-likes">
              <el-icon><Star/></el-icon> {{ post.likeCount }}
            </span>
          </div>
        </div>
        <el-empty v-if="!loading && relatedPosts.length === 0" description="暂无相关文章"/>
      </div>
    </el-card>
    <!-- todo 添加标签推荐，再文章下面（通过标签搜索页面，参考知乎）文章粘贴标签，文章查询区分标签区 -->
    <!--    &lt;!&ndash; 热门标签 &ndash;&gt;-->
    <!--    <el-card class="recommend-card" shadow="hover">-->
    <!--      <template #header>-->
    <!--        <div class="card-header">-->
    <!--          <span>热门标签</span>-->
    <!--        </div>-->
    <!--      </template>-->
    <!--      <div v-loading="loading" class="tags-container">-->
    <!--        <el-tag-->
    <!--            v-for="tag in hotTags"-->
    <!--            :key="tag.name"-->
    <!--            class="tag-item"-->
    <!--            effect="plain"-->
    <!--            @click="navigateToTag(tag.name)"-->
    <!--        >-->
    <!--          {{ tag.name }} ({{ tag.count }})-->
    <!--        </el-tag>-->
    <!--        <el-empty v-if="!loading && hotTags.length === 0" description="暂无标签数据" />-->
    <!--      </div>-->
    <!--    </el-card>-->
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
      padding: 10px 0;
      cursor: pointer;
      transition: all 0.3s;
      border-bottom: 1px solid var(--el-border-color-light);

      &:hover {
        background-color: var(--el-color-primary-light-9);
        transform: translateX(5px);
      }

      .post-title {
        font-size: 14px;
        margin-bottom: 5px;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
      }

      .post-meta {
        display: flex;
        font-size: 12px;
        color: var(--el-text-color-secondary);

        .post-views, .post-likes {
          display: flex;
          align-items: center;
          margin-right: 10px;

          .el-icon {
            margin-right: 3px;
          }
        }
      }
    }

    .tags-container {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;

      .tag-item {
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          background-color: var(--el-color-primary-light-9);
          color: var(--el-color-primary);
          transform: scale(1.05);
        }
      }
    }
  }
}
</style>