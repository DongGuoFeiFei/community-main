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
  .empty-tip {
    padding: 60px 20px;
    text-align: center;
    background: linear-gradient(135deg, rgba(224, 242, 254, 0.3), rgba(243, 232, 255, 0.2));
    border-radius: 16px;
    border: 2px dashed rgba(147, 197, 253, 0.4);
  }

  .list-container {
    display: flex;
    flex-direction: column;
    gap: 20px;
  }

  .post-item {
    padding: 24px;
    background: linear-gradient(135deg, rgba(255, 255, 255, 0.95) 0%, rgba(248, 250, 252, 0.9) 100%);
    border-radius: 16px;
    box-shadow: 0 4px 20px rgba(99, 102, 241, 0.08);
    border: 2px solid rgba(147, 197, 253, 0.2);
    position: relative;
    overflow: hidden;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 8px 32px rgba(99, 102, 241, 0.15);
      border-color: rgba(147, 197, 253, 0.4);

      &::before {
        opacity: 1;
      }

      .post-cover .cover-image {
        transform: scale(1.05);
      }
    }

    // 悬停背景效果
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 4px;
      background: linear-gradient(90deg, #93c5fd, #a78bfa, #c084fc);
      opacity: 0;
      transition: opacity 0.3s ease;
    }

    // 装饰元素
    &::after {
      content: '✦';
      position: absolute;
      bottom: 15px;
      right: 20px;
      font-size: 50px;
      color: rgba(167, 139, 250, 0.08);
      pointer-events: none;
    }

    .post-container {
      display: flex;
      gap: 24px;
      position: relative;
      z-index: 1;

      .post-cover {
        flex: 0 0 260px;
        border-radius: 12px;
        overflow: hidden;
        box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
        position: relative;

        &::before {
          content: '';
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background: linear-gradient(135deg, rgba(99, 102, 241, 0.1), transparent);
          opacity: 0;
          transition: opacity 0.3s ease;
          z-index: 1;
        }

        &:hover::before {
          opacity: 1;
        }

        .cover-image {
          width: 100%;
          height: 180px;
          transition: transform 0.5s cubic-bezier(0.4, 0, 0.2, 1);
        }
      }

      .post-content-wrapper {
        flex: 1;
        display: flex;
        flex-direction: column;

        .post-header {
          margin-bottom: 14px;

          .post-title {
            font-size: 20px;
            font-weight: 700;
            color: #1e293b;
            text-decoration: none;
            display: block;
            margin-bottom: 10px;
            line-height: 1.4;
            transition: all 0.3s ease;
            position: relative;
            display: inline-block;

            &::after {
              content: '';
              position: absolute;
              bottom: -2px;
              left: 0;
              width: 0;
              height: 2px;
              background: linear-gradient(90deg, #6366f1, #a855f7);
              transition: width 0.3s ease;
            }

            &:hover {
              color: #6366f1;

              &::after {
                width: 100%;
              }
            }
          }

          .post-meta {
            font-size: 13px;
            color: #94a3b8;
            display: flex;
            align-items: center;
            gap: 16px;
            flex-wrap: wrap;

            span {
              display: flex;
              align-items: center;
              gap: 4px;
              padding: 4px 10px;
              background: rgba(147, 197, 253, 0.15);
              border-radius: 12px;
              transition: all 0.3s ease;

              &:hover {
                background: rgba(147, 197, 253, 0.25);
                transform: translateY(-2px);
              }

              .el-icon {
                font-size: 14px;
                color: #6366f1;
              }
            }
          }
        }

        .post-content {
          margin-bottom: 16px;
          font-size: 14px;
          color: #64748b;
          line-height: 1.7;
          flex: 1;
          display: -webkit-box;
          -webkit-line-clamp: 3;
          -webkit-box-orient: vertical;
          overflow: hidden;
        }

        .post-footer {
          display: flex;
          gap: 8px;
          flex-wrap: wrap;

          .el-tag {
            color: #1e293b;
            border: none;
            font-weight: 500;
            padding: 6px 14px;
            border-radius: 16px;
            transition: all 0.3s ease;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

            &:hover {
              transform: translateY(-2px);
              box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            }
          }
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .post-list {
    .post-item {
      padding: 16px;

      .post-container {
        flex-direction: column;

        .post-cover {
          flex: 0 0 auto;
          width: 100%;

          .cover-image {
            height: 200px;
          }
        }
      }
    }
  }
}
</style>

<script setup>
import {Star, View} from '@element-plus/icons-vue'
import {formatDate} from '@/utils/date.js'

const props = defineProps({
  list: {
    type: Array,
    required: true
  }
})
</script>
