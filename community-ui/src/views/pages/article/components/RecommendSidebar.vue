<script setup>
import { onMounted, ref, watch } from "vue";
import { getHotPosts } from "@/api/article.js";
import { useRouter } from "vue-router";

const props = defineProps({
  articleId: {
    type: Number,
    default: null,
  },
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
    console.error("获取推荐内容失败:", error);
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
);
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
        <el-empty
          v-if="!loading && relatedPosts.length === 0"
          description="暂无相关文章"
        />
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
  animation: slideInRight 0.8s cubic-bezier(0.34, 1.56, 0.64, 1);

  .recommend-card {
    border-radius: 28px !important;
    background: rgba(255, 255, 255, 0.85);
    backdrop-filter: blur(20px);
    border: 3px solid rgba(255, 255, 255, 0.6);
    box-shadow: 0 15px 40px rgba(135, 206, 235, 0.2),
      inset 0 2px 10px rgba(255, 255, 255, 0.9),
      0 0 0 1px rgba(147, 112, 219, 0.1);
    transition: all 0.5s cubic-bezier(0.34, 1.56, 0.64, 1);
    overflow: hidden;
    position: relative;

    // 装饰火焰图标
    &::before {
      content: "🔥";
      position: absolute;
      top: 15px;
      right: 20px;
      font-size: 24px;
      animation: flicker 2s infinite ease-in-out;
    }

    &:hover {
      transform: translateY(-5px) scale(1.02);
      box-shadow: 0 25px 60px rgba(135, 206, 235, 0.3),
        inset 0 2px 10px rgba(255, 255, 255, 1),
        0 0 50px rgba(135, 206, 235, 0.4);
      border-color: rgba(135, 206, 235, 0.8);
    }

    :deep(.el-card__header) {
      background: linear-gradient(
        135deg,
        rgba(224, 247, 250, 0.6),
        rgba(230, 240, 255, 0.6)
      );
      border-bottom: 2px dashed rgba(135, 206, 235, 0.3);
      padding: 20px;
    }

    .card-header {
      font-weight: 700;
      font-size: 18px;
      background: linear-gradient(135deg, #5b9bd5, #9370db);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
    }

    :deep(.el-card__body) {
      padding: 15px 20px;
    }

    .recommend-item {
      padding: 15px;
      margin-bottom: 12px;
      cursor: pointer;
      transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
      border-radius: 20px;
      border: 2px solid transparent;
      position: relative;

      &::after {
        content: "→";
        position: absolute;
        right: 15px;
        top: 50%;
        transform: translateY(-50%);
        font-size: 18px;
        color: #87ceeb;
        opacity: 0;
        transition: all 0.3s ease;
      }

      &:hover {
        background: linear-gradient(
          135deg,
          rgba(224, 247, 250, 0.6),
          rgba(230, 240, 255, 0.6)
        );
        border-color: rgba(135, 206, 235, 0.3);
        transform: translateX(8px);
        box-shadow: 0 8px 20px rgba(135, 206, 235, 0.15);

        &::after {
          opacity: 1;
          transform: translateY(-50%) translateX(5px);
        }
      }

      &:last-child {
        margin-bottom: 0;
      }

      .post-container {
        display: flex;
        gap: 12px;
        color: black;
        align-items: center;

        .post-cover {
          flex: 0 0 80px;
          height: 60px;
          border-radius: 15px;
          overflow: hidden;
          box-shadow: 0 4px 12px rgba(135, 206, 235, 0.2);
          border: 2px solid rgba(255, 255, 255, 0.8);
          transition: all 0.3s ease;

          .cover-image {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.3s ease;
          }

          &:hover .cover-image {
            transform: scale(1.1);
          }
        }

        .post-content {
          flex: 1;
          min-width: 0;

          .post-title {
            font-size: 14px;
            font-weight: 600;
            margin-bottom: 6px;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            color: #333;
            line-height: 1.4;
          }

          .post-meta {
            font-size: 12px;
            color: #9370db;
            margin-bottom: 4px;
            font-weight: 500;

            &::before {
              content: "📅 ";
            }
          }

          .post-summary {
            font-size: 12px;
            color: #999;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            line-height: 1.5;
          }
        }
      }
    }

    :deep(.el-empty) {
      padding: 30px 20px;

      .el-empty__description p {
        color: #9370db;
        font-weight: 500;
      }
    }
  }
}

@keyframes slideInRight {
  from {
    opacity: 0;
    transform: translateX(50px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes flicker {
  0%,
  100% {
    opacity: 0.8;
    transform: scale(1);
  }
  50% {
    opacity: 1;
    transform: scale(1.2);
  }
}
</style>
