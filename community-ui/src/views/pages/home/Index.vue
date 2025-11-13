<template>
  <Live2DViewer />
  <FloatingParticles />

  <div class="main-container">
    <div class="common-layout">
      <el-container>
        <el-header>
          <HomeHeader @category-change="handleCategoryChange" />
        </el-header>
        <el-container direction="horizontal" class="content-container">
          <!-- 左侧装饰区 -->
          <el-aside width="280px" class="left-aside">
            <HomeSidebar />
          </el-aside>

          <!-- 主内容区 -->
          <el-main>
            <!-- 公告栏 -->
            <Announcement />
            <div class="card-container">
              <!-- 欢迎横幅 -->
              <div class="welcome-banner">
                <div class="banner-content">
                  <h1 class="banner-title">
                    <span class="title-emoji">🌸</span>
                    欢迎来到采芙蓉社区
                    <span class="title-emoji">✨</span>
                  </h1>
                  <p class="banner-subtitle">
                    探索有趣的内容，结识志同道合的朋友
                  </p>
                </div>
                <div class="banner-decoration">
                  <span class="deco-item">💖</span>
                  <span class="deco-item">🎀</span>
                  <span class="deco-item">🌺</span>
                </div>
              </div>

              <!-- 搜索组件 -->
              <PostSearch
                v-model:title="searchParam.title"
                @search="loadPosts"
              />

              <!-- 文章列表 -->
              <PostList
                :posts="posts"
                :loading="loading"
                :page-size="searchParam.pageSize"
              />

              <!-- 分页组件 -->
              <PostPagination
                v-model:current-page="searchParam.pageNum"
                :page-size="searchParam.pageSize"
                :total="total"
                :loading="loading"
                @page-change="loadPosts"
              />
            </div>
          </el-main>

          <!-- 右侧装饰区 -->
          <el-aside width="280px" class="right-aside">
            <!-- 可以添加额外的侧边栏内容 -->
            <div class="right-decoration">
              <div class="decoration-item">
                <span class="deco-emoji">🎨</span>
              </div>
              <div class="decoration-item">
                <span class="deco-emoji">🎵</span>
              </div>
              <div class="decoration-item">
                <span class="deco-emoji">🎮</span>
              </div>
            </div>
          </el-aside>
        </el-container>
        <el-footer>
          <Footer />
        </el-footer>
      </el-container>
    </div>
  </div>
</template>

<script setup lang="ts">
import Footer from "@/components/Footer.vue";
import Announcement from "@/components/Announcement.vue";
import { onMounted, reactive, ref, watch } from "vue";
import PostSearch from "@/views/pages/home/components/PostSearch.vue";
import PostList from "@/views/pages/home/components/PostList.vue";
import PostPagination from "@/views/pages/home/components/PostPagination.vue";
import HomeSidebar from "@/views/pages/home/components/HomeSidebar.vue";
import FloatingParticles from "@/views/pages/home/components/FloatingParticles.vue";
import { fetchPosts } from "@/api/index.js";
import { localStores } from "@/stores/localStores.js";
import HomeHeader from "@/components/Header.vue";
import { useRoute } from "vue-router";
import Live2DViewer from "@/components/Live2D/Live2DViewer.vue";
import env from "@/utils/env.js";

interface Post {
  id: number;
  title: string;
  author: string;
  summary: string;
  date: string;
  coverUrl: string | null;
}

interface SearchParam {
  title: string;
  pageSize: number;
  pageNum: number;
  sortType: string;
  categoryId: number | null;
}

// 共享状态
const posts = ref<Post[]>([]);
const total = ref<number>(0);
const loading = ref<boolean>(true);
const route = useRoute();

// 搜索参数
const searchParam = reactive<SearchParam>({
  title: "",
  pageSize: 8,
  pageNum: 1,
  sortType: "",
  categoryId: null,
});

const handleCategoryChange = (categoryId: number | null): void => {
  searchParam.categoryId = categoryId;
  searchParam.pageNum = 1;
  console.log("Selected category ID:", searchParam.categoryId);
  loadPosts();
};

const lStore = localStores();

// 滚动到顶部的方法
const scrollToTop = (): void => {
  // 优先使用平滑滚动
  try {
    // 检查浏览器是否支持 smooth behavior
    if ("scrollBehavior" in document.documentElement.style) {
      window.scrollTo({
        top: 0,
        left: 0,
        behavior: "smooth",
      });
    } else {
      // 不支持平滑滚动时的降级方案
      document.documentElement.scrollTop = 0;
      document.body.scrollTop = 0;
    }
  } catch (error) {
    // 降级方案：直接设置 scrollTop
    console.warn("滚动失败，使用降级方案", error);
    try {
      document.documentElement.scrollTop = 0;
      document.body.scrollTop = 0;
    } catch (e) {
      console.error("滚动到顶部失败", e);
    }
  }
};

// 加载文章方法
const loadPosts = async (): Promise<void> => {
  loading.value = true;
  scrollToTop();

  try {
    const res = await fetchPosts(searchParam);
    posts.value = res.rows.map((item: any) => ({
      id: item.id,
      title: item.title,
      author: item.author,
      summary: item.summary || "",
      date: item.date,
      coverUrl: item.coverUrl ? lStore.baseURL + item.coverUrl : null,
    }));
    console.log(lStore.baseURL);
    console.log(env.apiBaseUrl);
    total.value = res.total;
  } catch (error) {
    console.error("加载文章失败:", error);
  } finally {
    loading.value = false;
  }
};

// 初始化加载
onMounted(() => {
  if (route.params.id) {
    searchParam.categoryId = Number(route.params.id);
    console.log(route.params.id);
  }
  loadPosts();
});
// 监听路由变化
watch(
  () => route.params.id,
  (newId) => {
    if (newId) {
      console.log(newId);
      searchParam.categoryId = Number(newId);
      loadPosts();
    }
  }
);
</script>

<style scoped lang="scss">
.main-container {
  min-height: 100vh;
  width: 100%;
  padding-top: 80px;
  background: linear-gradient(
    135deg,
    #e0f7fa 0%,
    /* 薄荷蓝 */ #e8f5ff 20%,
    /* 天空蓝 */ #e6f3ff 40%,
    #d4f1f4 60%,
    /* 淡青 */ #e0e6ff 80%,
    /* 淡紫 */ #e6e6fa 100% /* 薰衣草 */
  );
  position: relative;
  overflow-x: hidden;
  overflow-y: visible;
  // 确保不会产生内部滚动条
  height: auto !important;
  max-height: none !important;

  // 添加浮动气泡装饰
  &::before,
  &::after {
    content: "";
    position: fixed;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.2);
    filter: blur(60px);
    pointer-events: none;
    z-index: 0;
  }

  &::before {
    width: 400px;
    height: 400px;
    top: -100px;
    right: -100px;
    animation: float 20s infinite ease-in-out;
  }

  &::after {
    width: 300px;
    height: 300px;
    bottom: -80px;
    left: -80px;
    animation: float 15s infinite ease-in-out reverse;
  }

  .common-layout {
    position: relative;
    z-index: 1;
    width: 100%;
    height: auto;

    .el-container {
      min-height: 100vh;
      height: auto !important;
      max-height: none !important;
      display: flex;
      flex-direction: column;
    }

    // 侧边栏样式
    .left-aside,
    .right-aside {
      background: transparent;
      height: auto !important;
    }

    .right-decoration {
      position: sticky;
      top: 100px;
      display: flex;
      flex-direction: column;
      gap: 30px;
      padding: 20px;

      .decoration-item {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 80px;
        height: 80px;
        margin: 0 auto;
        background: rgba(255, 255, 255, 0.6);
        backdrop-filter: blur(15px);
        border-radius: 50%;
        border: 3px solid rgba(255, 255, 255, 0.8);
        box-shadow: 0 8px 25px rgba(135, 206, 235, 0.2),
          inset 0 2px 8px rgba(255, 255, 255, 0.6);
        transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
        animation: decoItemFloat 3s ease-in-out infinite;

        &:nth-child(1) {
          animation-delay: 0s;
        }

        &:nth-child(2) {
          animation-delay: 1s;
        }

        &:nth-child(3) {
          animation-delay: 2s;
        }

        &:hover {
          transform: scale(1.2) rotate(360deg);
          box-shadow: 0 15px 40px rgba(135, 206, 235, 0.4);
        }

        .deco-emoji {
          font-size: 40px;
          filter: drop-shadow(0 4px 15px rgba(135, 206, 235, 0.4));
        }
      }
    }

    .card-container {
      padding: 30px;
      display: flex;
      flex-direction: column;
      gap: 24px;
      animation: slideInUp 0.8s cubic-bezier(0.34, 1.56, 0.64, 1);
      overflow-x: hidden;

      // 欢迎横幅
      .welcome-banner {
        position: relative;
        padding: 40px;
        background: rgba(255, 255, 255, 0.8);
        backdrop-filter: blur(20px);
        border-radius: 30px;
        border: 3px solid rgba(255, 255, 255, 0.6);
        box-shadow: 0 10px 40px rgba(135, 206, 235, 0.2),
          inset 0 2px 10px rgba(255, 255, 255, 0.7);
        overflow: hidden;
        animation: bannerSlideIn 1s cubic-bezier(0.34, 1.56, 0.64, 1);

        &::before {
          content: "";
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          height: 4px;
          background: linear-gradient(
            90deg,
            #87ceeb,
            #9370db,
            #84c7d0,
            #87ceeb
          );
          background-size: 200% 100%;
          animation: gradientMove 3s linear infinite;
        }

        .banner-content {
          text-align: center;
          position: relative;
          z-index: 1;

          .banner-title {
            margin: 0 0 15px 0;
            font-size: 32px;
            font-weight: 800;
            background: linear-gradient(135deg, #5b9bd5, #9370db, #84c7d0);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            animation: titleGlow 2s ease-in-out infinite;

            .title-emoji {
              display: inline-block;
              animation: emojiSpin 3s linear infinite;
              margin: 0 10px;
            }
          }

          .banner-subtitle {
            margin: 0;
            font-size: 16px;
            color: #5b9bd5;
            font-weight: 500;
            letter-spacing: 1px;
          }
        }

        .banner-decoration {
          position: absolute;
          top: 20px;
          right: 30px;
          display: flex;
          gap: 15px;

          .deco-item {
            font-size: 24px;
            animation: decoFloat 2s ease-in-out infinite;

            &:nth-child(1) {
              animation-delay: 0s;
            }

            &:nth-child(2) {
              animation-delay: 0.5s;
            }

            &:nth-child(3) {
              animation-delay: 1s;
            }
          }
        }
      }
    }
  }
}

// 动画关键帧
@keyframes float {
  0%,
  100% {
    transform: translate(0, 0) scale(1);
  }
  25% {
    transform: translate(30px, -30px) scale(1.1);
  }
  50% {
    transform: translate(-20px, 20px) scale(0.9);
  }
  75% {
    transform: translate(20px, 30px) scale(1.05);
  }
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(50px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes bannerSlideIn {
  from {
    opacity: 0;
    transform: translateY(-30px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

@keyframes titleGlow {
  0%,
  100% {
    filter: drop-shadow(0 0 10px rgba(135, 206, 235, 0.5));
  }
  50% {
    filter: drop-shadow(0 0 20px rgba(135, 206, 235, 0.8));
  }
}

@keyframes emojiSpin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

@keyframes decoFloat {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-15px);
  }
}

@keyframes decoItemFloat {
  0%,
  100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-15px) rotate(10deg);
  }
}

@keyframes gradientMove {
  0% {
    background-position: 0% 0%;
  }
  100% {
    background-position: 200% 0%;
  }
}

// 覆盖 Element Plus 默认样式
:deep(.el-header) {
  background: transparent;
  height: auto !important;
  padding: 0;
  overflow: visible !important;
}

:deep(.el-main) {
  background: transparent;
  padding: 20px 10px;
  overflow: visible !important;
  overflow-x: hidden !important;
  overflow-y: visible !important;
  flex: 1;
  height: auto !important;
  max-height: none !important;
}

:deep(.el-aside) {
  background: transparent;
  overflow: visible !important;
  height: auto !important;
  max-height: none !important;
}

:deep(.el-footer) {
  background: transparent;
  height: auto !important;
  padding: 0;
  overflow: visible !important;
}

:deep(.el-container) {
  overflow: visible !important;
  overflow-x: hidden !important;
  overflow-y: visible !important;
  height: auto !important;
  max-height: none !important;

  &:not(.el-container--vertical) {
    flex: 1;
    display: flex;
    flex-direction: row;
  }
}

// 强制内容区域水平排列
:deep(.content-container) {
  display: flex !important;
  flex-direction: row !important;
  flex: 1 !important;
  align-items: stretch !important;
  overflow: visible !important;
  overflow-x: hidden !important;
  overflow-y: visible !important;
  height: auto !important;
  max-height: none !important;
}

// 响应式设计
@media (max-width: 1200px) {
  .left-aside,
  .right-aside {
    display: none !important;
  }

  .card-container {
    padding: 20px !important;
  }
}

@media (max-width: 768px) {
  .welcome-banner {
    padding: 30px 20px !important;

    .banner-title {
      font-size: 24px !important;
    }

    .banner-subtitle {
      font-size: 14px !important;
    }

    .banner-decoration {
      position: static !important;
      justify-content: center;
      margin-top: 20px;
    }
  }
}
</style>
