<!--
  示例：新组件使用 TypeScript
  展示如何在 Vue 3 组件中使用 TypeScript
-->

<template>
  <div class="example-component">
    <h2>{{ title }}</h2>

    <!-- 用户信息 -->
    <div v-if="user" class="user-info">
      <img :src="user.avatar" :alt="user.username" />
      <span>{{ user.username }}</span>
    </div>

    <!-- 文章列表 -->
    <div class="article-list">
      <div
        v-for="article in articles"
        :key="article.id"
        class="article-item"
        @click="handleArticleClick(article)"
      >
        <h3>{{ article.title }}</h3>
        <p>{{ article.summary }}</p>
        <div class="stats">
          <span>👁️ {{ article.viewCount }}</span>
          <span>❤️ {{ article.likeCount }}</span>
          <span>💬 {{ article.commentCount }}</span>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="pagination.page"
      v-model:page-size="pagination.pageSize"
      :total="pagination.total"
      @current-change="handlePageChange"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import type { Article, User, ArticleListParams } from 'src/api/types'
import { getArticleListTS } from '@/api/example'

// ============= Props 定义（带类型） =============
interface Props {
  userId?: string
  categoryId?: string
  showAuthor?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  showAuthor: true
})

// ============= Emits 定义（带类型） =============
interface Emits {
  articleClick: [article: Article]
  pageChange: [page: number]
}

const emit = defineEmits<Emits>()

// ============= 响应式数据（带类型） =============
const title = ref<string>('文章列表')
const user = ref<User | null>(null)
const articles = ref<Article[]>([])
const loading = ref<boolean>(false)

// 分页数据
const pagination = ref({
  page: 1,
  pageSize: 10,
  total: 0
})

// ============= 计算属性（带类型） =============
const hasArticles = computed<boolean>(() => articles.value.length > 0)

const totalPages = computed<number>(() => {
  return Math.ceil(pagination.value.total / pagination.value.pageSize)
})

// ============= 方法（带类型） =============

/**
 * 加载文章列表
 */
const loadArticles = async (): Promise<void> => {
  loading.value = true

  try {
    const params: ArticleListParams = {
      page: pagination.value.page,
      pageSize: pagination.value.pageSize,
      categoryId: props.categoryId,
      authorId: props.userId
    }

    const response = await getArticleListTS(params)

    if (response.data.code === 200) {
      const result = response.data.data
      articles.value = result.list
      pagination.value.total = result.total
    } else {
      ElMessage.error(response.data.message)
    }
  } catch (error) {
    console.error('加载文章失败:', error)
    ElMessage.error('加载失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

/**
 * 处理文章点击
 */
const handleArticleClick = (article: Article): void => {
  emit('articleClick', article)
}

/**
 * 处理翻页
 */
const handlePageChange = (page: number): void => {
  pagination.value.page = page
  emit('pageChange', page)
  loadArticles()
}

/**
 * 格式化日期（类型安全的工具函数）
 */
const formatDate = (dateString: string): string => {
  return new Date(dateString).toLocaleDateString('zh-CN')
}

// ============= 生命周期 =============
onMounted(() => {
  loadArticles()
})

// ============= 暴露给父组件的方法（可选） =============
defineExpose({
  loadArticles,
  refresh: loadArticles
})
</script>

<style scoped lang="scss">
.example-component {
  padding: 20px;

  .user-info {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 20px;

    img {
      width: 40px;
      height: 40px;
      border-radius: 50%;
    }
  }

  .article-list {
    .article-item {
      padding: 15px;
      margin-bottom: 10px;
      border: 1px solid #eee;
      border-radius: 8px;
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
        transform: translateY(-2px);
      }

      h3 {
        margin: 0 0 8px 0;
        font-size: 18px;
      }

      p {
        color: #666;
        margin: 0 0 10px 0;
      }

      .stats {
        display: flex;
        gap: 15px;
        font-size: 14px;
        color: #999;
      }
    }
  }
}
</style>

