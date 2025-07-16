<template>
  <div class="post-list">
    <div v-if="list.length === 0" class="empty-tip">
      <el-empty description="暂无数据"/>
    </div>

    <div v-else class="list-container">
      <div v-for="item in list" :key="item.id" class="post-item">
        <div class="post-container">
          <!-- 封面图片部分 - 左侧 -->
          <div class="post-cover" v-if="item.coverUrl && !item.coverUrl.includes('null')">
            <router-link :to="`/article/${item.id}`" target="_blank">
              <el-image
                  :src="item.coverUrl"
                  fit="cover"
                  class="cover-image"
              />
            </router-link>
          </div>

          <!-- 内容部分 - 右侧 -->
          <div class="post-content-wrapper">
            <div class="post-header">
              <router-link :to="`/article/${item.id}`" target="_blank" class="post-title">{{ item.title }}</router-link>
              <div class="post-meta">
                <span class="post-date">{{ formatDate(item.createdAt) }}</span>
                <span class="post-views">
                  <el-icon><View/></el-icon> {{ item.viewCount || 0 }}
                </span>
                <span class="post-likes">
                  <el-icon><Star/></el-icon> {{ item.likeCount || 0 }}
                </span>
              </div>
            </div>

            <div class="post-content" v-html="item.summary"></div>

            <div class="post-footer">
              <el-tag v-for="tag in item.tags" :key="tag" size="small" effect="plain"
                      :color="tag.color"
              >
                {{ tag.name }}
              </el-tag>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.post-list {
  .post-item {
    margin-bottom: 20px;
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .post-container {
      display: flex;
      gap: 20px;

      .post-cover {
        flex: 0 0 240px;
        border-radius: 6px;
        overflow: hidden;

        .cover-image {
          width: 100%;
          height: 160px;
          transition: transform 0.3s;

          &:hover {
            transform: scale(1.02);
          }
        }
      }

      .post-content-wrapper {
        flex: 1;
        display: flex;
        flex-direction: column;

        .post-header {
          margin-bottom: 12px;

          .post-title {
            font-size: 18px;
            font-weight: bold;
            color: #333;
            text-decoration: none;
            display: block;
            margin-bottom: 8px;

            &:hover {
              color: var(--el-color-primary);
            }
          }

          .post-meta {
            font-size: 12px;
            color: #999;

            span {
              margin-right: 12px;

              .el-icon {
                margin-right: 4px;
              }
            }
          }
        }

        .post-content {
          margin-bottom: 16px;
          font-size: 14px;
          color: #666;
          line-height: 1.6;
          flex: 1;
        }

        .post-footer {
          .el-tag {
            color: black;
            margin-right: 8px;
          }
        }
      }
    }
  }
}
</style>

<script setup>
import {Star, View} from '@element-plus/icons-vue'
import {formatDate} from '@/utils/date'

const props = defineProps({
  list: {
    type: Array,
    required: true
  }
})
</script>