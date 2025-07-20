<template>
  <div class="post-container">
    <!-- 单个帖子卡片 -->
    <el-card class="post-card" v-if="post">
      <div class="card-content">
        <div class="post-details">
          <h2 class="post-title">{{ post.title }}</h2>

          <el-divider border-style="dashed"/>
          <div v-html="post.content"></div>
          <el-divider border-style="dashed"/>
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
        <Loading/>
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
import {computed, onMounted, ref, watch} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {ElAlert, ElCard, ElIcon, ElMessage} from 'element-plus'
import {fetchPostDetail} from '@/api/index.js'
import MarkdownIt from 'markdown-it'
import NProgress from "nprogress";
import {localStores} from "@/stores/localStores.js";
import LikeCollect from "@/views/pages/components/LikeCollect.vue";
import {addLike, delLike} from "@/api/likeApi.js";
import {cancelCollect} from "@/api/collectApi.js";
import dayjs from "dayjs";

defineProps({
  modelValue: {
    type: [String, Number],
    default: undefined,
  },
});

const emit = defineEmits(['update:modelValue']);
const route = useRoute()
const router = useRouter()
const post = ref(null);
const loading = ref(true)
const error = ref(null)
const md = new MarkdownIt(
    {
      html: true,        // 允许HTML标签
      linkify: true,     // 自动转换URL为链接
      typographer: true,  // 美化排版
      breaks: true  // 换行转换为 <br>

    }
)

const lStore = localStores()
const SANITIZE_CONFIG = {
  ALLOWED_TAGS: [
    'h1', 'h2', 'h3', 'h4', 'h5', 'h6',
    'p', 'br', 'strong', 'em', 'blockquote',
    'ul', 'ol', 'li', 'code', 'pre', 'a',
    'img', 'table', 'thead', 'tbody', 'tr', 'th', 'td'
  ],
  ALLOWED_ATTR: ['href', 'src', 'alt', 'title', 'class', 'target', 'rel']
}
// 点赞组件的相关数据

const article = computed(() => {
  if (!post.value) return {
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
  if (!dateString) return '未知时间'
  const date = dayjs().format('YYYY-MM-DD')
  return date.toLocaleString()
}

// 返回上一页
const goBack = () => {
  router.go(-1);
}

// 获取帖子详情
const fetchPostData = async (id) => {
  try {
    NProgress.start()
    loading.value = true
    error.value = null
    const response = await fetchPostDetail(id)
    emit("update:modelValue", id)
    // 正确获取数据
    const postData = response.data
    console.log(postData)
    // 处理帖子数据
    post.value = {
      ...postData,
      imageUrl: lStore.baseURL + postData.imageUrl,
      // content: DOMPurify.sanitize(
      //     md.render(postData.content || ''),
      //     SANITIZE_CONFIG
      // ),
      content: postData.content || '',
      url: window.location.href
    }
    document.title = post.value.title
  } catch (err) {
    emit("update:modelValue", null)
    error.value = err.message || '帖子已被下架，请移步其他文章'
    console.error('Error fetching post:', err)
  } finally {
    NProgress.done()
    loading.value = false
  }
}
// 组件挂载时和路由参数变化时获取数据
onMounted(() => {
  const postId = route.params.id
  if (postId) {
    fetchPostData(postId)
  }
})

// 监听路由参数变化
watch(
    () => route.params.id,
    (newId) => {
      if (newId) {
        fetchPostData(newId)
      }
    }
)

const handleLike = async ({itemId, isLiked}) => {
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
    console.error('点赞操作失败:', error);
    ElMessage.error(error.message || '点赞操作失败');
    throw error;
  }
};

// 收藏处理
const handleCollect = async ({itemId, action}) => {
  try {
    if (action === 'uncollect') {
      await cancelCollect(itemId);
      post.value.isCollected = false;
      post.value.collectCount -= 1;
    }
  } catch (error) {
    console.error('取消收藏失败:', error);
    ElMessage.error(error.message || '取消收藏失败');
    throw error;
  }
};
// 收藏成功回调
const handleCollectSuccess = () => {
  post.value.isCollected = 1
  post.value.collectCount += 1
}

</script>

<style scoped lang="less">

.post-container {
  max-width: 100%;
  margin: 20px auto;
}

.back-button {
  display: inline-flex;
  align-items: center;
  cursor: pointer;
  color: #7e7e7e; /* 默认白色，适配背景 */
  font-size: 16px;
  margin-bottom: 20px;
  transition: color 0.3s;

  .el-icon {
    margin-right: 6px;
    font-size: 20px;
  }

  &:hover {
    color: #2c3e50; /* 悬浮时加深，深蓝色 */
  }
}

.post-card {
  margin-top: 20px;
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }
}

.post-card {
  margin-top: 20px;
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
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
  border-radius: 4px;
  flex-shrink: 0;

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
  gap: 12px;
}

.post-title {
  margin: 0;
  color: #333;
  font-size: 24px;
  font-weight: bold;
}

.post-content {
  margin: 0;
  color: #666;
  font-size: 16px;
  line-height: 1.6;
}

.post-meta {
  margin-top: auto;
  font-size: 14px;
  color: #999;
  display: flex;
  flex-wrap: wrap;
  gap: 15px;

  .author {
    color: #409eff;
  }
}

.loading {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: #666;
  font-size: 16px;

  .el-icon {
    margin-right: 8px;
    font-size: 20px;
  }
}

.error-alert {
  margin-top: 20px;
}

/* 控制 Markdown 内容中的图片 */
.post-card :deep(img) {
  max-width: 100%;
  height: auto;
  max-height: 400px;
  object-fit: contain;
  border-radius: 4px;
  display: block;
  margin: 10px auto;
}
</style>