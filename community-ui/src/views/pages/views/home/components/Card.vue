<template>
  <div class="card-page">
    <!-- 骨架屏 -->
    <div v-if="loading" class="skeleton-container">
      <div v-for="i in skeletonCount" :key="`skeleton-${i}`" class="skeleton-card">
        <div class="skeleton-cover"></div>
        <div class="skeleton-content">
          <div class="skeleton-title"></div>
          <div class="skeleton-text"></div>
          <div class="skeleton-text"></div>
          <div class="skeleton-meta"></div>
        </div>
      </div>
    </div>

    <!-- 实际内容 -->
    <div v-else>

      <!-- todo  综合排序 最新发布 最新发布  最热内容  最多浏览 最多点赞
      <template>
        <el-select v-model="sortType" @change="handleSortChange" placeholder="排序方式">
          <el-option label="综合排序" value="default"></el-option>
          <el-option label="最新发布" value="newest"></el-option>
          <el-option label="最热内容" value="hottest"></el-option>
          <el-option label="最多浏览" value="most_viewed"></el-option>
          <el-option label="最多点赞" value="most_liked"></el-option>
        </el-select>
      </template>    -->

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
            <!-- todo 降低图片质量-->
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
        </el-card>
      </div>

      <div class="pagination-container">
        <el-pagination
            v-model:current-page="searchParam.pageNum"
            :page-size="searchParam.pageSize"
            :total="total"
            layout="prev, pager, next"
            background
            @current-change="loadPosts"
            class="pagination"
            :disabled="loading"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, reactive, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {fetchPosts} from '@/api/index.js'
import {localStores} from "@/stores/localStores.js"

const lStore = localStores()
const router = useRouter()
const route = useRoute()

// 数据状态
const posts = ref([])
const total = ref(0)
const loading = ref(true)

// 搜索参数
const searchParam = reactive({
  title: '',
  pageSize: 8,
  pageNum: 1,
})

// 计算骨架屏数量，根据当前页面大小动态变化
const skeletonCount = computed(() => {
  return searchParam.pageSize
})

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  return dateString.split(' ')[0] // 只显示日期部分
}

// 跳转到详情
const goToDetail = (id) => {
  window.open(`/article/${id}`, '_blank');
}

// 加载文章
const loadPosts = async () => {
  loading.value = true
  try {
    const res = await fetchPosts(searchParam)
    posts.value = res.rows.map(item => ({
      id: item.id,
      title: item.title,
      author: item.author,
      summary: item.summary || "",
      date: item.date,
      coverUrl: item.coverUrl ? lStore.baseURL + item.coverUrl : null
    }))
    total.value = res.total

    console.log("posts.value ", posts.value)
    // 更新URL但不触发重新加载
    await router.replace({
      query: {
        page: searchParam.pageNum,
        keyword: searchParam.title
      }
    })
  } catch (error) {
    console.error('加载文章失败:', error)
    // 可以在这里添加错误处理，如显示错误提示
  } finally {
    loading.value = false
  }
}

// 初始化时从路由读取页码
onMounted(() => {
  searchParam.pageNum = Number(route.query.page) || 1
  searchParam.title = route.query.keyword || ''
  loadPosts()
})
</script>

<style scoped lang="scss">
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
    min-height: 40px;
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

    span {
      color: black;
    }
  }
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 16px;
  width: 100%;
}

/* 骨架屏样式 */
.skeleton-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.skeleton-card {
  display: flex;
  gap: 16px;
  padding: 16px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.skeleton-cover {
  width: 160px;
  height: 120px;
  background: linear-gradient(90deg, #f2f2f2 25%, #e6e6e6 37%, #f2f2f2 63%);
  background-size: 400% 100%;
  animation: skeleton-loading 1.4s ease infinite;
  border-radius: 4px;
}

.skeleton-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.skeleton-title {
  height: 24px;
  width: 60%;
  background: linear-gradient(90deg, #f2f2f2 25%, #e6e6e6 37%, #f2f2f2 63%);
  background-size: 400% 100%;
  animation: skeleton-loading 1.4s ease infinite;
}

.skeleton-text {
  height: 16px;
  width: 100%;
  background: linear-gradient(90deg, #f2f2f2 25%, #e6e6e6 37%, #f2f2f2 63%);
  background-size: 400% 100%;
  animation: skeleton-loading 1.4s ease infinite;

  &:nth-child(3) {
    width: 80%;
  }
}

.skeleton-meta {
  height: 14px;
  width: 40%;
  background: linear-gradient(90deg, #f2f2f2 25%, #e6e6e6 37%, #f2f2f2 63%);
  background-size: 400% 100%;
  animation: skeleton-loading 1.4s ease infinite;
}

@keyframes skeleton-loading {
  0% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0 50%;
  }
}
</style>