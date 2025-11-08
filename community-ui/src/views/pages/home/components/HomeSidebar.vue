<script setup lang="ts">
import {ref} from "vue";
import {useRouter} from "vue-router";

const router = useRouter();
// todo 数据后续后台获取，并页面的跳转
// 热门标签
const hotTags = ref([
  {name: "动漫", icon: "🎨", color: "#5B9BD5"},
  {name: "游戏", icon: "🎮", color: "#84C7D0"},
  {name: "音乐", icon: "🎵", color: "#9370DB"},
  {name: "二次元", icon: "✨", color: "#87CEEB"},
  {name: "日常", icon: "☀️", color: "#DDA0DD"},
]);

// 今日热榜
const hotPosts = ref([
  {id: 1, title: "最新动漫推荐", views: 1234},
  {id: 2, title: "精彩游戏攻略", views: 987},
  {id: 3, title: "音乐分享时刻", views: 876},
]);

const handleTagClick = (tag: string) => {
  console.log("点击标签:", tag);
  // 可以添加标签搜索功能
};

const handlePostClick = (postId: number) => {
  router.push(`/article/${postId}`);
};
</script>

<template>
  <div class="home-sidebar">
    <!-- 可爱的吉祥物 -->
    <div class="mascot-section">
      <div class="mascot-container">
        <div class="mascot-character">
          <span class="mascot-emoji">🌸</span>
          <div class="mascot-speech-bubble">
            <p>欢迎来到采芙蓉~</p>
            <p class="mascot-greeting">今天也要开心哦！</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 热门标签 -->
    <div class="sidebar-card hot-tags-card">
      <div class="card-header">
        <span class="header-icon">🔥</span>
        <h3 class="card-title">热门标签</h3>
      </div>
      <div class="hot-tags-list">
        <button
            v-for="tag in hotTags"
            :key="tag.name"
            class="tag-btn"
            :style="{ '--tag-color': tag.color }"
            @click="handleTagClick(tag.name)"
        >
          <span class="tag-icon">{{ tag.icon }}</span>
          <span class="tag-name">{{ tag.name }}</span>
        </button>
      </div>
    </div>

    <!-- 今日热榜 -->
    <div class="sidebar-card hot-posts-card">
      <div class="card-header">
        <span class="header-icon">📊</span>
        <h3 class="card-title">今日热榜</h3>
      </div>
      <div class="hot-posts-list">
        <div
            v-for="(post, index) in hotPosts"
            :key="post.id"
            class="hot-post-item"
            @click="handlePostClick(post.id)"
        >
          <span class="post-rank" :class="`rank-${index + 1}`">{{
              index + 1
            }}</span>
          <div class="post-info">
            <p class="post-title">{{ post.title }}</p>
            <span class="post-views">👁️ {{ post.views }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 装饰元素 -->
    <div class="sidebar-decorations">
      <span class="deco-heart">💕</span>
      <span class="deco-star">⭐</span>
      <span class="deco-flower">🌺</span>
    </div>
  </div>
</template>

<style scoped lang="scss">
.home-sidebar {
  position: sticky;
  top: 100px;
  display: flex;
  flex-direction: column;
  gap: 24px;
  padding: 20px;

  // 吉祥物部分
  .mascot-section {
    margin-bottom: 10px;

    .mascot-container {
      position: relative;
      padding: 20px;
      background: rgba(255, 255, 255, 0.7);
      backdrop-filter: blur(15px);
      border-radius: 25px;
      border: 3px solid rgba(135, 206, 235, 0.3);
      box-shadow: 0 8px 25px rgba(135, 206, 235, 0.2),
      inset 0 2px 8px rgba(255, 255, 255, 0.6);
      animation: mascotFloat 3s ease-in-out infinite;

      .mascot-character {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 15px;

        .mascot-emoji {
          font-size: 60px;
          animation: mascotSpin 10s linear infinite;
          filter: drop-shadow(0 4px 15px rgba(135, 206, 235, 0.4));
        }

        .mascot-speech-bubble {
          position: relative;
          background: rgba(224, 240, 255, 0.9);
          padding: 15px 20px;
          border-radius: 20px;
          border: 2px solid rgba(135, 206, 235, 0.4);
          box-shadow: 0 4px 15px rgba(135, 206, 235, 0.2);

          &::before {
            content: "";
            position: absolute;
            top: -10px;
            left: 50%;
            transform: translateX(-50%);
            width: 0;
            height: 0;
            border-left: 10px solid transparent;
            border-right: 10px solid transparent;
            border-bottom: 10px solid rgba(135, 206, 235, 0.4);
          }

          p {
            margin: 0;
            font-size: 14px;
            font-weight: 600;
            color: #5b9bd5;
            text-align: center;
            line-height: 1.6;
          }

          .mascot-greeting {
            font-size: 12px;
            color: #9370db;
            margin-top: 5px;
          }
        }
      }
    }
  }

  // 卡片通用样式
  .sidebar-card {
    background: rgba(255, 255, 255, 0.75);
    backdrop-filter: blur(15px);
    border-radius: 25px;
    border: 3px solid rgba(255, 255, 255, 0.5);
    box-shadow: 0 8px 25px rgba(135, 206, 235, 0.15),
    inset 0 2px 8px rgba(255, 255, 255, 0.6);
    padding: 20px;
    transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
    animation: cardSlideIn 0.6s cubic-bezier(0.34, 1.56, 0.64, 1) backwards;

    &:hover {
      transform: translateY(-5px) scale(1.02);
      box-shadow: 0 15px 35px rgba(135, 206, 235, 0.25),
      inset 0 2px 8px rgba(255, 255, 255, 0.7);
    }

    .card-header {
      display: flex;
      align-items: center;
      gap: 10px;
      margin-bottom: 15px;

      .header-icon {
        font-size: 24px;
        animation: iconBounce 2s ease-in-out infinite;
      }

      .card-title {
        margin: 0;
        font-size: 18px;
        font-weight: 700;
        background: linear-gradient(135deg, #5b9bd5, #9370db);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
      }
    }
  }

  // 热门标签卡片
  .hot-tags-card {
    animation-delay: 0.1s;

    .hot-tags-list {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;

      .tag-btn {
        display: flex;
        align-items: center;
        gap: 6px;
        padding: 8px 16px;
        border: none;
        border-radius: 20px;
        background: rgba(255, 255, 255, 0.6);
        border: 2px solid rgba(255, 255, 255, 0.8);
        box-shadow: 0 4px 12px rgba(135, 206, 235, 0.15);
        cursor: pointer;
        transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
        position: relative;
        overflow: hidden;

        &::before {
          content: "";
          position: absolute;
          top: 50%;
          left: 50%;
          width: 0;
          height: 0;
          border-radius: 50%;
          background: var(--tag-color, #87ceeb);
          opacity: 0.2;
          transform: translate(-50%, -50%);
          transition: width 0.6s, height 0.6s;
        }

        &:hover {
          transform: translateY(-3px) scale(1.1);
          box-shadow: 0 8px 20px rgba(135, 206, 235, 0.3);
          background: rgba(255, 255, 255, 0.9);

          &::before {
            width: 150px;
            height: 150px;
          }

          .tag-icon {
            transform: scale(1.3) rotate(15deg);
          }
        }

        .tag-icon {
          font-size: 18px;
          transition: all 0.3s;
          position: relative;
          z-index: 1;
        }

        .tag-name {
          font-size: 13px;
          font-weight: 600;
          color: var(--tag-color, #ff69b4);
          position: relative;
          z-index: 1;
        }
      }
    }
  }

  // 热榜卡片
  .hot-posts-card {
    animation-delay: 0.2s;

    .hot-posts-list {
      display: flex;
      flex-direction: column;
      gap: 12px;

      .hot-post-item {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 12px;
        background: rgba(224, 240, 255, 0.5);
        border-radius: 15px;
        border: 2px solid rgba(135, 206, 235, 0.2);
        cursor: pointer;
        transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);

        &:hover {
          transform: translateX(8px) scale(1.03);
          background: rgba(224, 240, 255, 0.8);
          box-shadow: 0 6px 20px rgba(135, 206, 235, 0.2);
          border-color: rgba(135, 206, 235, 0.4);
        }

        .post-rank {
          display: flex;
          align-items: center;
          justify-content: center;
          width: 28px;
          height: 28px;
          border-radius: 50%;
          background: linear-gradient(135deg, #ffe5f0, #e8f5ff);
          font-size: 14px;
          font-weight: 700;
          color: #5b9bd5;
          flex-shrink: 0;

          &.rank-1 {
            background: linear-gradient(135deg, #ffd700, #ffa500);
            color: #f0f8ff;
            box-shadow: 0 4px 15px rgba(255, 215, 0, 0.4);
          }

          &.rank-2 {
            background: linear-gradient(135deg, #c0c0c0, #a8a8a8);
            color: #f0f8ff;
            box-shadow: 0 4px 15px rgba(192, 192, 192, 0.4);
          }

          &.rank-3 {
            background: linear-gradient(135deg, #cd7f32, #b8860b);
            color: #f0f8ff;
            box-shadow: 0 4px 15px rgba(205, 127, 50, 0.4);
          }
        }

        .post-info {
          flex: 1;
          display: flex;
          flex-direction: column;
          gap: 4px;

          .post-title {
            margin: 0;
            font-size: 13px;
            font-weight: 600;
            color: #555;
            line-height: 1.4;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }

          .post-views {
            font-size: 11px;
            color: #999;
            font-weight: 500;
          }
        }
      }
    }
  }

  // 装饰元素
  .sidebar-decorations {
    position: absolute;
    pointer-events: none;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;

    .deco-heart,
    .deco-star,
    .deco-flower {
      position: absolute;
      font-size: 20px;
      opacity: 0.4;
      animation: decoFloat 4s ease-in-out infinite;
    }

    .deco-heart {
      top: 10%;
      right: 10%;
      animation-delay: 0s;
    }

    .deco-star {
      top: 50%;
      left: 5%;
      animation-delay: 1.5s;
    }

    .deco-flower {
      bottom: 20%;
      right: 5%;
      animation-delay: 3s;
    }
  }
}

// 动画关键帧
@keyframes cardSlideIn {
  from {
    opacity: 0;
    transform: translateX(50px) scale(0.9);
  }
  to {
    opacity: 1;
    transform: translateX(0) scale(1);
  }
}

@keyframes mascotFloat {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

@keyframes mascotSpin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

@keyframes iconBounce {
  0%,
  100% {
    transform: translateY(0) scale(1);
  }
  50% {
    transform: translateY(-5px) scale(1.1);
  }
}

@keyframes decoFloat {
  0%,
  100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-15px) rotate(180deg);
  }
}
</style>
