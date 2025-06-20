<template>
  <div class="card-page">
    <div class="toolbar">
      <el-input
          v-model="searchParam.title"
          placeholder="搜索文章标题"
          prefix-icon="ep:search"
          @keyup.enter="loadPosts"
          clearable
          @clear="loadPosts"
          class="search-input"
      />
    </div>

    <div class="post-list">
      <el-card
          v-for="post in posts"
          :key="post.id"
          class="post-card"
          @click="goToDetail(post.id)"
      >
        <div class="card-content">
          <el-image
              v-if="post.cover"
              :src="post.cover"
              fit="cover"
              class="cover"
          />
          <div v-else class="no-cover">无封面</div>
          <div class="text-content">
            <h3>{{ post.title }}</h3>
            <p class="summary">{{ truncateSummary(post.summary) }}</p>
            <div class="meta">
              <span>作者：{{ post.author }}</span>
              <span>{{ formatDate(post.date) }}</span>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <el-pagination
        v-model:current-page="searchParam.pageNum"
        :page-size="searchParam.pageSize"
        :total="total"
        layout="prev, pager, next"
        background
        @current-change="loadPosts"
        class="pagination"
    />
  </div>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {fetchPosts} from '@/api'
import MarkdownIt from "markdown-it";
import {localStore} from "@/stores/localStores.js";


const lStore = localStore()
const md = new MarkdownIt({
  html: true,        // 允许HTML标签
  linkify: true,     // 自动转换URL为链接
  typographer: true,  // 美化排版
  breaks: true  // 换行转换为 <br>

})
const SANITIZE_CONFIG = {
  ALLOWED_TAGS: [
    'h1', 'h2', 'h3', 'h4', 'h5', 'h6',
    'p', 'br', 'strong', 'em', 'blockquote',
    'ul', 'ol', 'li', 'code', 'pre', 'a',
    'img', 'table', 'thead', 'tbody', 'tr', 'th', 'td'
  ],
  ALLOWED_ATTR: ['href', 'src', 'alt', 'title', 'class', 'target', 'rel']
}
const router = useRouter()
const posts = ref(null)
const total = ref(0)
const route = useRoute()
const searchParam = reactive({
  title: '',
  pageSize: 5,
  pageNum: 1,
})

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  return dateString.split(' ')[0] // 只显示日期部分
}

// 截断摘要
const truncateSummary = (summary) => {
  if (!summary) return ''
  return summary.length > 100 ? summary.substring(0, 100) + '...' : summary
}

// 跳转到详情
const goToDetail = (id) => {
  router.push(`/index/article/${id}`)
}

// 加载文章
const loadPosts = async () => {
  const res = await fetchPosts(searchParam)
  posts.value = res.rows.map(item => ({
    id: item.id,
    title: item.title,
    author: item.author,
    summary: "",
    date: item.date,
    cover: item.cover ? lStore.baseURL + item.cover : null
  }))

  total.value = res.total
  // 更新URL但不触发重新加载
  await router.replace({
    query: {
      page: searchParam.pageNum,
      keyword: searchParam.title
    }
  })
}

// 初始化时从路由读取页码
onMounted(() => {
  searchParam.pageNum = Number(route.query.page) || 1
  searchParam.title = route.query.keyword || ''
  loadPosts()
})
</script>

<style scoped lang="less">
.card-page {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.toolbar {
  display: flex;
  justify-content: flex-start;
  margin-bottom: 12px;

  .search-input {
    width: 100%;
    height: 3vw;
  }
}

.post-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.post-card {
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .card-content {
    display: flex;
    gap: 16px;
    align-items: flex-start;
  }

  .cover {
    width: 160px;
    height: 120px;
    border-radius: 4px;
    flex-shrink: 0;
    object-fit: cover;
  }

  .no-cover {
    width: 160px;
    height: 120px;
    background-color: #f5f5f5;
    border-radius: 4px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #999;
    font-size: 12px;
    flex-shrink: 0;
  }

  .text-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 8px;
  }

  h3 {
    margin: 0;
    font-size: 18px;
    color: #333;
  }

  .summary {
    color: #666;
    margin: 0;
    font-size: 14px;
    line-height: 1.5;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .meta {
    font-size: 12px;
    color: #999;
    display: flex;
    gap: 12px;
  }
}

.pagination {
  align-self: center;
  margin-top: 16px;
}
</style>