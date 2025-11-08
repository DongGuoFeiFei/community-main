<script setup>
import PostDetail from "@/views/pages/article/components/PostDetail.vue";
import Footer from "@/components/Footer.vue";
import RecommendSidebar from "@/views/pages/article/components/RecommendSidebar.vue";
import { onMounted, ref, watch } from "vue";
import CommentSection from "@/views/pages/article/components/CommentSection.vue";
import Tags from "@/views/pages/article/components/Tags.vue";
import HomeHeader from "@/components/Header.vue";
import Live2DViewer from "@/components/Live2D/Live2DViewer.vue";
import { getUserInfo } from "@/api/user.js";
import { useRoute } from "vue-router";
import AuthorInfo from "@/views/pages/article/components/AuthorInfo.vue";

const route = useRoute();
const articleId = ref(null);

const author = ref({
  id: null,
  username: "",
  nickname: "",
  avatar: "",
  bio: "",
  joinDate: "",
  postCount: 0,
  followerCount: 0,
  followingCount: 0,
  isFollowing: false,
});

// 获取作者信息
const fetchAuthorInfo = async () => {
  try {
    if (route.params.id) {
      const res = await getUserInfo(route.params.id);
      author.value = res.data;
      console.log(author.value);
    }
  } catch (error) {
    console.error("获取作者信息失败:", error);
  }
};

onMounted(() => {
  fetchAuthorInfo();
});

// 监听路由参数变化
watch(
  () => route.params.id,
  (newId) => {
    if (newId) {
      fetchAuthorInfo();
    }
  }
);
</script>

<template>
  <Live2DViewer />
  <div class="main-container">
    <div class="common-layout">
      <el-container>
        <el-header>
          <HomeHeader />
        </el-header>
        <el-container direction="horizontal" class="content-container">
          <el-aside width="350px" class="left-aside">
            <AuthorInfo v-if="articleId" :author-info="author" />
            <RecommendSidebar :articleId="Number(articleId)" />
          </el-aside>
          <el-main>
            <post-detail v-model="articleId" />
            <tags v-if="Number(articleId)" :postId="Number(articleId)" />
            <CommentSection
              v-if="Number(articleId)"
              :postId="Number(articleId)"
              :author-id="Number(author.id)"
            />
            <!--    todo 付费内容    -->
          </el-main>
          <el-aside width="100px" class="right-aside"> </el-aside>
        </el-container>
        <el-footer>
          <Footer />
        </el-footer>
      </el-container>
    </div>
  </div>
</template>

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

      .el-aside {
        padding: 20px;
        background: transparent;
      }

      .el-main {
        padding: 30px 20px;
        max-width: 900px;
        margin: 0 auto;
      }
    }
  }
}

// 浮动动画
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

// 覆盖 Element Plus 默认样式
:deep(.el-header) {
  background: transparent;
  height: auto !important;
  padding: 0;
  overflow: visible !important;
}

:deep(.el-main) {
  background: transparent;
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
  .left-aside {
    width: 300px !important;
  }

  .right-aside {
    display: none !important;
  }
}

@media (max-width: 768px) {
  .main-container {
    padding-top: 70px;
  }

  .left-aside {
    display: none !important;
  }

  :deep(.el-main) {
    padding: 20px 10px !important;
  }
}
</style>
