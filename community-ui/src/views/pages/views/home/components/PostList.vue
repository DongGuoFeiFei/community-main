<script setup>

defineProps({
  posts: {
    type: Array,
    required: true
  },
  loading: {
    type: Boolean,
    required: true
  },
  pageSize: {
    type: Number,
    required: true
  }
})


const formatDate = (dateString) => {
  return dateString?.split(' ')[0] || ''
}
</script>

<template>
  <div class="post-list-container">
    <!-- 骨架屏 -->
    <div v-if="loading" class="skeleton-container">
      <div v-for="i in pageSize" :key="`skeleton-${i}`" class="skeleton-card">
        <div class="skeleton-cover"></div>
        <div class="skeleton-content">
          <div class="skeleton-title"></div>
          <div class="skeleton-text"></div>
          <div class="skeleton-text"></div>
          <div class="skeleton-meta"></div>
        </div>
      </div>
    </div>

    <!-- 文章列表 -->
    <div v-else class="post-list">
      <el-card
          v-for="post in posts"
          :key="post.id"
          class="post-card"
      >
        <router-link :to="`/article/${post.id}`" target="_blank">
          <div class="card-content">
            <el-image
                v-if="post.coverUrl"
                :src="post.coverUrl"
                fit="cover"
                class="cover"
                loading="lazy"
            />
            <div v-else class="no-cover">无封面</div>
            <div class="text-content">
              <h3>{{ post.title }}</h3>
              <p class="summary">{{ post.summary }}</p>
              <div class="meta">
                <span>作者：{{ post.author }}</span>
                <span>{{ formatDate(post.date) }}</span>
              </div>
            </div>
          </div>
        </router-link>
      </el-card>
    </div>
  </div>
</template>

<style scoped lang="scss">
//@import '@/styles/skeleton.scss';

.post-list-container {
  .post-list {
    display: flex;
    flex-direction: column;
    gap: 16px;
  }

  .post-card {
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-3px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }

    .card-content {
      display: flex;
      gap: 16px;
      align-items: flex-start;

      .cover {
        width: 160px;
        height: 120px;
        border-radius: 4px;
        flex-shrink: 0;
        object-fit: cover;
      }

      .no-cover {
        width: 160px;
        height: 120px;
        background-color: #f5f5f5;
        border-radius: 4px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #999;
        font-size: 12px;
        flex-shrink: 0;
      }

      .text-content {
        flex: 1;
        display: flex;
        flex-direction: column;
        gap: 8px;

        h3 {
          margin: 0;
          font-size: 18px;
          color: #333;
        }

        .summary {
          color: #666;
          margin: 0;
          font-size: 14px;
          line-height: 1.5;
          display: -webkit-box;
          -webkit-line-clamp: 3;
          -webkit-box-orient: vertical;
          overflow: hidden;
          text-overflow: ellipsis;
        }

        .meta {
          font-size: 12px;
          color: #999;
          display: flex;
          gap: 12px;

          span {
            color: black;
          }
        }
      }
    }
  }
}
</style>