<script setup lang="ts">
interface Post {
  id: number
  title: string
  author: string
  summary: string
  date: string
  coverUrl: string | null
}

defineProps<{
  posts: Post[]
  loading: boolean
  pageSize: number
}>()

const formatDate = (dateString: string): string => {
  return dateString?.split(' ')[0] || ''
}
</script>

<template>
  <div class="post-list-container">
    <!-- todo 添加 骨架屏 -->
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
@use "sass:math";

@use '@/styles/skeleton.scss';

.post-list-container {
  .post-list {
    display: flex;
    flex-direction: column;
    gap: 24px;
  }

  .post-card {
    cursor: pointer;
    border-radius: 28px !important; // 大圆角
    background: rgba(255, 255, 255, 0.75);
    backdrop-filter: blur(20px);
    border: 3px solid rgba(255, 255, 255, 0.5);
    box-shadow: 
      0 10px 30px rgba(135, 206, 235, 0.15),
      inset 0 2px 10px rgba(255, 255, 255, 0.8),
      0 0 0 1px rgba(147, 112, 219, 0.1);
    transition: all 0.5s cubic-bezier(0.34, 1.56, 0.64, 1);
    position: relative;
    overflow: hidden;
    animation: cardSlideIn 0.6s cubic-bezier(0.34, 1.56, 0.64, 1) backwards;
    transform: rotate(-0.5deg); // 轻微倾斜

    // 为每个卡片添加延迟动画
    @for $i from 1 through 10 {
      &:nth-child(#{$i}) {
        animation-delay: #{$i * 0.08}s;
        transform: rotate(#{math.random(2) - 1}deg); // 随机轻微倾斜
      }
    }

    // 漫画气泡装饰
    &::before {
      content: '';
      position: absolute;
      top: -50px;
      right: -50px;
      width: 150px;
      height: 150px;
      background: radial-gradient(circle, rgba(255, 182, 193, 0.2), transparent 70%);
      border-radius: 50%;
      pointer-events: none;
    }

    &::after {
      content: '💖';
      position: absolute;
      top: 15px;
      right: 15px;
      font-size: 0;
      opacity: 0;
      transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
    }

    &:hover {
      transform: translateY(-8px) rotate(0deg) scale(1.02); // 弹跳效果
      box-shadow: 
        0 20px 50px rgba(135, 206, 235, 0.3),
        inset 0 2px 10px rgba(255, 255, 255, 0.9),
        0 0 40px rgba(135, 206, 235, 0.4), // 外发光
        0 0 0 3px rgba(147, 112, 219, 0.3);
      border-color: rgba(135, 206, 235, 0.7);

      &::after {
        font-size: 24px;
        opacity: 1;
        animation: heartBeat 0.8s infinite;
      }
    }

    :deep(.el-card__body) {
      padding: 20px;
    }

    .card-content {
      display: flex;
      gap: 20px;
      align-items: flex-start;
      position: relative;

      .cover {
        width: 180px;
        height: 135px;
        border-radius: 20px; // 大圆角
        flex-shrink: 0;
        object-fit: cover;
        box-shadow: 
          0 8px 20px rgba(0, 0, 0, 0.1),
          inset 0 1px 3px rgba(255, 255, 255, 0.5);
        border: 2px solid rgba(255, 255, 255, 0.8);
        transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);

        &:hover {
          transform: scale(1.05) rotate(2deg);
          box-shadow: 0 12px 30px rgba(135, 206, 235, 0.3);
        }
      }

      .no-cover {
        width: 180px;
        height: 135px;
        background: linear-gradient(135deg, #FFE5F0, #E8F5FF);
        border-radius: 20px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #C9A0DC;
        font-size: 14px;
        font-weight: 500;
        flex-shrink: 0;
        border: 2px dashed rgba(201, 160, 220, 0.4);
        box-shadow: inset 0 2px 8px rgba(201, 160, 220, 0.2);
        position: relative;

        &::before {
          content: '🌸';
          position: absolute;
          font-size: 40px;
          opacity: 0.3;
        }
      }

      .text-content {
        flex: 1;
        display: flex;
        flex-direction: column;
        gap: 12px;
        padding: 5px 0;

        h3 {
          margin: 0;
          font-size: 20px;
          font-weight: 600;
          background: linear-gradient(135deg, #5B9BD5, #9370DB, #84C7D0);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          background-clip: text;
          line-height: 1.4;
          transition: all 0.3s ease;
          position: relative;
          display: inline-block;

          &::after {
            content: '';
            position: absolute;
            bottom: -3px;
            left: 0;
            width: 0;
            height: 3px;
            background: linear-gradient(90deg, #87CEEB, #9370DB);
            border-radius: 2px;
            transition: width 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
          }

          &:hover::after {
            width: 100%;
          }
        }

        .summary {
          color: #666;
          margin: 0;
          font-size: 14px;
          line-height: 1.7;
          display: -webkit-box;
          -webkit-line-clamp: 3;
          -webkit-box-orient: vertical;
          overflow: hidden;
          text-overflow: ellipsis;
          padding: 8px 12px;
          background: rgba(224, 240, 255, 0.5);
          border-radius: 15px;
          border-left: 3px solid #87CEEB;
        }

        .meta {
          font-size: 13px;
          color: #999;
          display: flex;
          gap: 16px;
          align-items: center;

          span {
            color: #5B9BD5;
            font-weight: 500;
            padding: 4px 12px;
            background: rgba(135, 206, 235, 0.15);
            border-radius: 12px;
            transition: all 0.3s ease;
            display: inline-flex;
            align-items: center;
            gap: 5px;

            &::before {
              content: '👤';
              font-size: 14px;
            }

            &:last-child::before {
              content: '📅';
            }

            &:hover {
              background: rgba(135, 206, 235, 0.3);
              transform: translateY(-2px);
            }
          }
        }
      }
    }
  }

  // 骨架屏样式美化
  .skeleton-container {
    display: flex;
    flex-direction: column;
    gap: 24px;

    .skeleton-card {
      display: flex;
      gap: 20px;
      padding: 20px;
      border-radius: 28px;
      background: rgba(255, 255, 255, 0.6);
      backdrop-filter: blur(10px);
      border: 2px solid rgba(255, 255, 255, 0.5);
      animation: skeletonPulse 1.5s infinite ease-in-out;

      .skeleton-cover {
        width: 180px;
        height: 135px;
        border-radius: 20px;
        background: linear-gradient(
          90deg,
          rgba(224, 240, 255, 0.5) 25%,
          rgba(232, 245, 255, 0.5) 50%,
          rgba(230, 230, 250, 0.5) 75%
        );
        background-size: 200% 100%;
        animation: skeletonShimmer 1.5s infinite;
      }

      .skeleton-content {
        flex: 1;
        display: flex;
        flex-direction: column;
        gap: 12px;
        justify-content: center;

        .skeleton-title,
        .skeleton-text,
        .skeleton-meta {
          border-radius: 15px;
          background: linear-gradient(
            90deg,
            rgba(224, 240, 255, 0.5) 25%,
            rgba(232, 245, 255, 0.5) 50%,
            rgba(230, 230, 250, 0.5) 75%
          );
          background-size: 200% 100%;
          animation: skeletonShimmer 1.5s infinite;
        }

        .skeleton-title {
          height: 24px;
          width: 70%;
        }

        .skeleton-text {
          height: 16px;
          width: 90%;

          &:last-of-type {
            width: 60%;
          }
        }

        .skeleton-meta {
          height: 20px;
          width: 40%;
          margin-top: 8px;
        }
      }
    }
  }
}

// 动画关键帧
@keyframes cardSlideIn {
  from {
    opacity: 0;
    transform: translateX(-50px) rotate(-5deg);
  }
  to {
    opacity: 1;
    transform: translateX(0) rotate(-0.5deg);
  }
}

@keyframes heartBeat {
  0%, 100% {
    transform: scale(1);
  }
  25% {
    transform: scale(1.2) rotate(-10deg);
  }
  75% {
    transform: scale(1.2) rotate(10deg);
  }
}

@keyframes skeletonPulse {
  0%, 100% {
    opacity: 0.6;
  }
  50% {
    opacity: 0.8;
  }
}

@keyframes skeletonShimmer {
  0% {
    background-position: 200% 0;
  }
  100% {
    background-position: -200% 0;
  }
}
</style>
