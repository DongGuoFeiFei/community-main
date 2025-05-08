<template>
  <div class="post-container">
    <!-- 返回按钮 -->
    <div class="back-button" @click="goBack">
      <el-icon>
        <ArrowLeft/>
      </el-icon>
      返回上一页
    </div>

    <!-- 单个帖子卡片 -->
    <el-card class="post-card" v-if="post">
      <div class="card-content">
        <div class="post-details">
          <h2 class="post-title">{{ post.title }}</h2>

          <div class="post-meta">
            <span class="author">作者：{{ post.nickname }}</span>
            <span class="date">发布时间：{{ formatDate(post.createdAt) }}</span>
            <span class="update-date" v-if="post.updatedAt !== post.createdAt">
              最后更新：{{ formatDate(post.updatedAt) }}
            </span>
          </div>
          <div v-html="post.content"></div>
        </div>
      </div>
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

  <CommentSection v-if="post" :postId="post.articleId"/>
</template>


<script setup>
import {onMounted, ref, watch} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {ElAlert, ElCard, ElIcon} from 'element-plus'
import {ArrowLeft} from '@element-plus/icons-vue'
import {fetchPostDetail} from '@/api/index.js'
import MarkdownIt from 'markdown-it'
import DOMPurify from 'dompurify'
import {sessionStore} from "@/stores/sessionStores.js";
import CommentSection from "@/components/CommentSection.vue";

const md = new MarkdownIt(
    {
      html: true,        // 允许HTML标签
      linkify: true,     // 自动转换URL为链接
      typographer: true,  // 美化排版
      breaks: true  // 换行转换为 <br>

    }
)
const store = sessionStore()

const SANITIZE_CONFIG = {
  ALLOWED_TAGS: [
    'h1', 'h2', 'h3', 'h4', 'h5', 'h6',
    'p', 'br', 'strong', 'em', 'blockquote',
    'ul', 'ol', 'li', 'code', 'pre', 'a',
    'img', 'table', 'thead', 'tbody', 'tr', 'th', 'td'
  ],
  ALLOWED_ATTR: ['href', 'src', 'alt', 'title', 'class', 'target', 'rel']
}
const route = useRoute()
const router = useRouter()
const post = ref(null)
const loading = ref(true)
const error = ref(null)

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '未知时间'
  const date = new Date(dateString)
  return date.toLocaleString()
}

// 返回上一页
const goBack = () => {
  router.go(-1);
}

// 获取帖子详情
const fetchPostData = async (id) => {
  try {
    loading.value = true
    error.value = null
    const response = await fetchPostDetail(id)

    // 正确获取数据
    const postData = response.rows?.[0] || response
    // 处理帖子数据
    post.value = {
      ...postData,
      imageUrl: store.baseURL + postData.imageUrl,
      content: DOMPurify.sanitize(
          md.render(postData.content || ''),
          SANITIZE_CONFIG
      )
    }
  } catch (err) {
    error.value = err.message || '获取帖子详情失败'
    console.error('Error fetching post:', err)
  } finally {
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
</script>

<style scoped lang="less">

.post-container {
  max-width: 900px;
  margin: 20px auto;
  padding: 0 20px;
}

.back-button {
  display: inline-flex;
  align-items: center;
  cursor: pointer;
  color: #fff; /* 默认白色，适配背景 */
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

.post-container {
  max-width: 900px;
  margin: 20px auto;
  padding: 0 20px;
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