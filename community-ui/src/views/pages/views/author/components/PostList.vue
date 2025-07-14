<template>
  <div class="post-list">
    <div v-if="list.length === 0" class="empty-tip">
      <el-empty description="暂无数据" />
    </div>

    <div v-else class="list-container">
      <div v-for="item in list" :key="item.id" class="post-item">
        <div class="post-header">
          <router-link :to="`/post/${item.id}`" class="post-title">{{ item.title }}</router-link>
          <div class="post-meta">
            <span class="post-date">{{ formatDate(item.createdAt) }}</span>
            <span class="post-views">
              <el-icon><View /></el-icon> {{ item.viewCount || 0 }}
            </span>
            <span class="post-likes">
              <el-icon><Star /></el-icon> {{ item.likeCount || 0 }}
            </span>
          </div>
        </div>

        <div class="post-content" v-html="item.summary"></div>

        <div class="post-footer">
          <el-tag v-for="tag in item.tags" :key="tag" size="small" effect="plain">
            {{ tag }}
          </el-tag>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { View, Star } from '@element-plus/icons-vue'
import { formatDate } from '@/utils/date'

const props = defineProps({
  list: {
    type: Array,
    required: true
  }
})
</script>

<style lang="scss" scoped>
.post-list {
  .empty-tip {
    padding: 20px 0;
  }

  .list-container {
    max-height: 600px;
    overflow-y: auto;
  }

  .post-item {
    padding: 20px;
    border-bottom: 1px solid var(--el-border-color-light);

    .post-header {
      margin-bottom: 10px;

      .post-title {
        font-size: 18px;
        font-weight: bold;
        color: var(--el-color-primary);
        text-decoration: none;

        &:hover {
          text-decoration: underline;
        }
      }

      .post-meta {
        margin-top: 5px;
        font-size: 13px;
        color: var(--el-text-color-secondary);

        span {
          margin-right: 15px;

          .el-icon {
            margin-right: 3px;
          }
        }
      }
    }

    .post-content {
      margin-bottom: 15px;
      color: var(--el-text-color-regular);
      line-height: 1.6;
      font-size: 14px;
    }

    .post-footer {
      .el-tag {
        margin-right: 8px;
      }
    }
  }
}
</style>