<template>
  <div class="post-container">
    <!-- 单个帖子卡片 -->
    <el-card class="post-card" v-if="post">
      <div class="card-content">
        <div class="post-details">
          <h2 class="post-title">{{ post.title }}</h2>

          <el-divider border-style="dashed" />
          <div v-html="post.content"></div>
          <el-divider border-style="dashed" />
          <div class="post-meta">
            <router-link :to="`/author/${post.userId}`" target="_blank">
              <span class="author">作者：{{ post.nickname }}</span>
            </router-link>
            <span class="date">发布时间：{{ formatDate(post.createdAt) }}</span>
            <span class="update-date" v-if="post.updatedAt !== post.createdAt">
              最后更新：{{ formatDate(post.updatedAt) }}
            </span>
          </div>
        </div>
      </div>
      <LikeCollect
        :item-id="article.id"
        :initial-like-count="article.likeCount"
        :initial-collected-count="article.collectCount"
        :initial-is-liked="article.isLiked"
        :initial-is-collected="article.isCollected"
        :share-title="article.title"
        :share-url="article.url"
        @like="handleLike"
        @collect="handleCollect"
        @collect-success="handleCollectSuccess"
      />
      <!--  todo 添加转发按钮，完善点赞和收藏    -->
      <!--      <ShareButton :post-id="article.id"/>-->
    </el-card>

    <!-- 加载状态 -->
    <div v-else-if="loading" class="loading">
      <el-icon class="is-loading">
        <Loading />
      </el-icon>
      加载中...
    </div>

    <!-- 错误提示 -->
    <el-alert
      v-if="error"
      :title="error"
      type="error"
      show-icon
      class="error-alert"
    />
  </div>
</template>

<script setup>
import { computed, onMounted, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { ElAlert, ElCard, ElIcon, ElMessage } from "element-plus";
import { fetchPostDetail } from "@/api/index.js";
import MarkdownIt from "markdown-it";
import NProgress from "nprogress";
import { localStores } from "@/stores/localStores.js";
import LikeCollect from "@/components/LikeCollect.vue";
import { addLike, delLike } from "@/api/likeApi.js";
import { cancelCollect } from "@/api/collectApi.js";
import dayjs from "dayjs";

defineProps({
  modelValue: {
    type: [String, Number],
    default: undefined,
  },
});

const emit = defineEmits(["update:modelValue"]);
const route = useRoute();
const router = useRouter();
const post = ref(null);
const loading = ref(true);
const error = ref(null);
const md = new MarkdownIt({
  html: true, // 允许HTML标签
  linkify: true, // 自动转换URL为链接
  typographer: true, // 美化排版
  breaks: true, // 换行转换为 <br>
});

const lStore = localStores();
const SANITIZE_CONFIG = {
  ALLOWED_TAGS: [
    "h1",
    "h2",
    "h3",
    "h4",
    "h5",
    "h6",
    "p",
    "br",
    "strong",
    "em",
    "blockquote",
    "ul",
    "ol",
    "li",
    "code",
    "pre",
    "a",
    "img",
    "table",
    "thead",
    "tbody",
    "tr",
    "th",
    "td",
  ],
  ALLOWED_ATTR: ["href", "src", "alt", "title", "class", "target", "rel"],
};
// 点赞组件的相关数据

const article = computed(() => {
  if (!post.value)
    return {
      id: 0,
      likeCount: 0,
      isLiked: 0,
      collectCount: 0,
      isCollected: 0,
      title: "",
      url: "",
    };

  return {
    id: post.value.articleId,
    likeCount: post.value.likeCount,
    isLiked: post.value.isLiked,
    collectCount: post.value.collectCount,
    isCollected: post.value.isCollected,
    title: post.value.title,
    url: post.value.url,
  };
});

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return "未知时间";
  const date = dayjs().format("YYYY-MM-DD");
  return date.toLocaleString();
};
// 获取帖子详情
const fetchPostData = async (id) => {
  try {
    NProgress.start();
    loading.value = true;
    error.value = null;
    const response = await fetchPostDetail(id);
    emit("update:modelValue", id);
    // 正确获取数据
    const postData = response.data;
    console.log(postData);
    // 处理帖子数据
    post.value = {
      ...postData,
      imageUrl: lStore.baseURL + postData.imageUrl,
      // content: DOMPurify.sanitize(
      //     md.render(postData.content || ''),
      //     SANITIZE_CONFIG
      // ),
      content: postData.content || "",
      url: window.location.href,
    };
    document.title = post.value.title;
  } catch (err) {
    emit("update:modelValue", null);
    error.value = err.message || "帖子已被下架，请移步其他文章";
    console.error("Error fetching post:", err);
  } finally {
    NProgress.done();
    loading.value = false;
  }
};
// 组件挂载时和路由参数变化时获取数据
onMounted(() => {
  const postId = route.params.id;
  if (postId) {
    fetchPostData(postId);
  }
});

// 监听路由参数变化
watch(
  () => route.params.id,
  (newId) => {
    if (newId) {
      fetchPostData(newId);
    }
  }
);

const handleLike = async ({ itemId, isLiked }) => {
  try {
    if (isLiked) {
      await addLike(itemId);
    } else {
      await delLike(itemId);
    }

    // 更新本地状态
    post.value.isLiked = isLiked;
    post.value.likeCount += isLiked ? 1 : -1;
  } catch (error) {
    console.error("点赞操作失败:", error);
    ElMessage.error(error.message || "点赞操作失败");
    throw error;
  }
};

// 收藏处理
const handleCollect = async ({ itemId, action }) => {
  try {
    if (action === "uncollect") {
      await cancelCollect(itemId);
      post.value.isCollected = false;
      post.value.collectCount -= 1;
    }
  } catch (error) {
    console.error("取消收藏失败:", error);
    ElMessage.error(error.message || "取消收藏失败");
    throw error;
  }
};
// 收藏成功回调
const handleCollectSuccess = () => {
  post.value.isCollected = 1;
  post.value.collectCount += 1;
};
</script>

<style scoped lang="scss">
.post-container {
  max-width: 100%;
  margin: 20px auto;
  animation: slideInUp 0.8s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.back-button {
  display: inline-flex;
  align-items: center;
  cursor: pointer;
  color: #5b9bd5;
  font-size: 16px;
  margin-bottom: 20px;
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 20px;
  transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
  box-shadow: 0 4px 12px rgba(135, 206, 235, 0.2);

  .el-icon {
    margin-right: 6px;
    font-size: 20px;
  }

  &:hover {
    color: #f0f8ff;
    background: linear-gradient(135deg, #87ceeb, #9370db);
    transform: translateY(-3px) scale(1.05);
    box-shadow: 0 8px 20px rgba(135, 206, 235, 0.4);
  }
}

.post-card {
  margin-top: 20px;
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

  // 清新气泡装饰
  &::before {
    content: "";
    position: absolute;
    top: -80px;
    right: -80px;
    width: 200px;
    height: 200px;
    background: radial-gradient(
      circle,
      rgba(135, 206, 235, 0.15),
      transparent 70%
    );
    border-radius: 50%;
    pointer-events: none;
  }

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 25px 60px rgba(135, 206, 235, 0.3),
      inset 0 2px 10px rgba(255, 255, 255, 1), 0 0 50px rgba(135, 206, 235, 0.4);
    border-color: rgba(135, 206, 235, 0.8);
  }

  :deep(.el-card__body) {
    padding: 30px;
  }
}

.card-content {
  display: flex;
  gap: 20px;
  padding: 20px;

  @media (max-width: 768px) {
    flex-direction: column;
  }
}

.post-cover {
  width: 300px;
  height: 200px;
  border-radius: 20px;
  flex-shrink: 0;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  overflow: hidden;

  @media (max-width: 768px) {
    width: 100%;
    height: auto;
    max-height: 300px;
  }
}

.post-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.post-title {
  margin: 0 0 15px 0;
  font-size: 28px;
  font-weight: 700;
  background: linear-gradient(135deg, #5b9bd5, #9370db, #84c7d0);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  line-height: 1.4;
  position: relative;
  display: inline-block;

  &::after {
    content: "";
    position: absolute;
    bottom: -8px;
    left: 0;
    width: 60px;
    height: 4px;
    background: linear-gradient(90deg, #87ceeb, #9370db);
    border-radius: 2px;
  }
}

.post-content {
  margin: 0;
  color: #555;
  font-size: 16px;
  line-height: 1.8;
  padding: 20px;
  background: rgba(224, 247, 250, 0.5);
  border-radius: 20px;
  border-left: 4px solid #87ceeb;
}

.post-meta {
  margin-top: auto;
  font-size: 14px;
  color: #999;
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  padding-top: 15px;
  border-top: 2px dashed rgba(135, 206, 235, 0.3);

  span {
    padding: 6px 14px;
    background: rgba(135, 206, 235, 0.15);
    border-radius: 15px;
    color: #5b9bd5;
    font-weight: 500;
    transition: all 0.3s ease;
    display: inline-flex;
    align-items: center;
    gap: 5px;

    &:hover {
      background: rgba(135, 206, 235, 0.3);
      transform: translateY(-2px);
    }
  }

  .author {
    color: #5b9bd5;
    font-weight: 600;

    &::before {
      content: "👤";
      margin-right: 4px;
    }
  }

  .date::before {
    content: "📅";
    margin-right: 4px;
  }

  .update-date::before {
    content: "🔄";
    margin-right: 4px;
  }
}

.loading {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: #5b9bd5;
  font-size: 18px;
  font-weight: 500;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 28px;
  backdrop-filter: blur(10px);

  .el-icon {
    margin-right: 12px;
    font-size: 24px;
  }
}

.error-alert {
  margin-top: 20px;
  border-radius: 20px !important;
  border: 2px solid #87ceeb;
}

/* 控制 Markdown 内容中的图片 */
.post-card :deep(img) {
  max-width: 100%;
  height: auto;
  max-height: 400px;
  object-fit: contain;
  border-radius: 20px;
  display: block;
  margin: 15px auto;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  border: 3px solid rgba(255, 255, 255, 0.8);
}

// Markdown 内容样式美化
.post-card :deep(h1),
.post-card :deep(h2),
.post-card :deep(h3) {
  background: linear-gradient(135deg, #5b9bd5, #9370db);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-top: 24px;
  margin-bottom: 12px;
}

.post-card :deep(blockquote) {
  border-left: 4px solid #87ceeb;
  background: rgba(224, 247, 250, 0.6);
  padding: 12px 16px;
  border-radius: 12px;
  margin: 16px 0;
}

.post-card :deep(code) {
  background: rgba(147, 112, 219, 0.15);
  padding: 2px 8px;
  border-radius: 8px;
  color: #9370db;
  font-weight: 500;
}

.post-card :deep(pre) {
  background: rgba(255, 255, 255, 0.8);
  border-radius: 15px;
  padding: 16px;
  border: 2px solid rgba(147, 112, 219, 0.2);
  overflow-x: auto;
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(40px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
