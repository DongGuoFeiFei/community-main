<template>
  <div class="articles-container">
    <!-- 欢迎横幅 -->
    <div class="welcome-banner">
      <div class="banner-decoration-left">
        <span class="deco-emoji">📝</span>
        <span class="deco-emoji">✨</span>
      </div>
      <div class="banner-content">
        <h1 class="banner-title">我的文章</h1>
        <p class="banner-subtitle">管理你的创作内容</p>
      </div>
      <div class="banner-decoration-right">
        <span class="deco-emoji">💡</span>
        <span class="deco-emoji">🎨</span>
      </div>
    </div>

    <div class="content-card">
      <div class="card-header">
        <div class="header-left">
          <div class="header-icon">📚</div>
          <h2 class="title">文章列表</h2>
        </div>
        <el-button type="primary" @click="goToEditor" class="create-btn">
          <el-icon class="btn-icon"><Edit /></el-icon>
          写文章
        </el-button>
      </div>

      <div class="filter-section">
        <el-input
            v-model="searchQuery"
            placeholder="搜索文章标题"
            clearable
            class="search-input"
            @clear="handleSearchClear"
            @keyup.enter="fetchArticles"
        >
          <template #prefix>
            <el-icon>
              <Search/>
            </el-icon>
          </template>
        </el-input>

        <el-select
            v-model="filterStatus"
            placeholder="全部状态"
            clearable
            class="status-select"
            @change="fetchArticles"
        >
          <el-option label="已发布" :value="0"/>
          <el-option label="草稿" :value="1"/>
        </el-select>
      </div>

      <div class="article-list">
      <el-table
          :data="articles"
          style="width: 100%"
          v-loading="loading"
          empty-text="暂无文章"
          @sort-change="handleSortChange"
      >
        <el-table-column prop="coverUrl" label="封面" width="120">
          <template #default="{ row }">
            <el-image
                v-if="row.coverUrl"
                :src="baseUrl + row.coverUrl"
                fit="cover"
                style="width: 80px; height: 60px; border-radius: 4px"
                hide-on-click-modal
            />
            <div v-else class="no-cover">无封面</div>
          </template>
        </el-table-column>

        <el-table-column prop="title" label="标题" min-width="200">
          <template #default="{ row }">
            <router-link :to="`/article/${row.id}`" target="_blank" class="article-title">
              {{ row.title }}
            </router-link>
            <el-tag
                v-if="row.status === 0"
                size="small"
                type="warning"
                style="margin-left: 8px"
            >
              草稿
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="viewCount" label="浏览" width="100" sortable="custom">
          <template #default="{ row }">
            {{ row.viewCount || 0 }}
          </template>
        </el-table-column>

        <el-table-column prop="likeCount" label="点赞" width="100" sortable="custom">
          <template #default="{ row }">
            {{ row.likeCount || 0 }}
          </template>
        </el-table-column>

        <el-table-column prop="commentCount" label="评论" width="100" sortable="custom">
          <template #default="{ row }">
            {{ row.commentCount || 0 }}
          </template>
        </el-table-column>

        <!-- 新增评论区状态列 -->
        <el-table-column prop="isOpenComment" label="评论区" width="100" align="center">
          <template #default="{ row }">
            <el-tag
                :type="row.isOpenComment === 1 ? 'success' : 'info'"
                size="small"
            >
              {{ row.isOpenComment === 1 ? '开启' : '关闭' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="createTime" label="发布时间" width="180" sortable="custom">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <!-- 评论区开关 -->
            <el-tooltip
                :content="row.isOpenComment === 1 ? '关闭评论区' : '开启评论区'"
                placement="top"
            >
              <el-switch
                  v-model="row.isOpenComment"
                  :active-value="1"
                  :inactive-value="0"
                  @change="toggleCommentStatus(row)"
                  style="margin-right: 8px"
              />
            </el-tooltip>

            <el-button
                size="small"
                @click="editArticle(row.id)"
                :icon="Edit"
                circle
            />
            <el-button
                size="small"
                type="danger"
                @click="handleDelete(row.id)"
                :icon="Delete"
                circle
            />
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            v-model:current-page="pagination.current"
            v-model:page-size="pagination.size"
            :page-sizes="[10, 20, 30, 50]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</div>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue'
import {useRouter} from 'vue-router'
import {Delete, Edit, Search} from '@element-plus/icons-vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import dayjs from 'dayjs'
import {deleteArticle, getMyArticles, updateArticleCommentStatus} from '@/api/article.js'
import {localStores} from "@/stores/localStores.js";
import {sessionStores} from "@/stores/sessionStores.js";

const router = useRouter()

// 文章数据
const articles = ref([])
const sStore = sessionStores()
// 搜索和筛选
const searchQuery = ref('')
const filterStatus = ref(null)
const sortField = ref('createTime')
const sortOrder = ref('desc') // 'asc' or 'desc'

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const loading = ref(false)
const lStore = localStores()
// 基础URL，用于拼接图片路径
const baseUrl = lStore.baseURL // 根据你的实际后端地址调整

// 格式化日期时间
const formatDateTime = (datetime) => {
  return dayjs(datetime).format('YYYY-MM-DD HH:mm')
}

// 获取文章列表
const fetchArticles = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.current,
      size: pagination.size,
      title: searchQuery.value,
      status: filterStatus.value,
      sortField: sortField.value,
      isAsc: sortOrder.value === 'asc'
    }

    const res = await getMyArticles(params)
    if (res.code === 200) {
      articles.value = res.rows
      pagination.total = res.total
    } else {
      throw new Error(res.msg || '获取文章列表失败')
    }
  } catch (error) {
    ElMessage.error('获取文章列表失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

// 切换评论区状态
const toggleCommentStatus = async (article) => {
  try {
    const originalStatus = article.isOpenComment === 1 ? 0 : 1 // 保存原始状态用于回滚

    const res = await updateArticleCommentStatus(article.id, article.isOpenComment)
    if (res.code === 200) {
      ElMessage.success(article.isOpenComment === 1 ? '已开启评论区' : '已关闭评论区')
    } else {
      // 如果API调用失败，恢复原来的状态
      article.isOpenComment = originalStatus
      throw new Error(res.msg || '操作失败')
    }
  } catch (error) {
    ElMessage.error('操作失败: ' + error.message)
  }
}

// 处理排序变化
const handleSortChange = ({prop, order}) => {
  if (prop) {
    sortField.value = prop
    sortOrder.value = order === 'ascending' ? 'asc' : 'desc'
    fetchArticles()
  }
}

// 处理分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1 // 重置到第一页
  fetchArticles()
}

// 处理页码变化
const handlePageChange = (page) => {
  pagination.current = page
  fetchArticles()
}

// 搜索框清空
const handleSearchClear = () => {
  searchQuery.value = ''
  fetchArticles()
}

// 编辑文章
const editArticle = (id) => {
  sStore.isEditMode = true
  sStore.editorArticleId = id
  router.push(`/editor-edit`)
}

// 前往编辑器
const goToEditor = () => {
  sStore.isEditMode = false
  router.push('/editor')
}

// 删除文章
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这篇文章吗？放入垃圾箱最多30天。', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await deleteArticle(id)
    ElMessage.success('文章删除成功')

    // 重新获取当前页数据
    fetchArticles()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败: ' + error.message)
    }
  }
}

onMounted(() => {
  fetchArticles()
})
</script>

<style scoped lang="scss">
// 动漫风格配色
$sky-blue: #87ceeb;
$mint-blue: #b0e0e6;
$lavender: #e6e6fa;
$light-purple: #dda0dd;
$mint-green: #98fb98;
$pink-accent: #ffb6c1;

.articles-container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 30px;
  overflow: visible;
  height: auto;

  // 欢迎横幅
  .welcome-banner {
    position: relative;
    padding: 30px 40px;
    background: rgba(255, 255, 255, 0.85);
    backdrop-filter: blur(20px);
    border-radius: 25px;
    border: 3px solid rgba(255, 255, 255, 0.6);
    box-shadow: 0 10px 40px rgba(135, 206, 235, 0.25),
      inset 0 2px 10px rgba(255, 255, 255, 0.7);
    display: flex;
    align-items: center;
    justify-content: space-between;
    animation: bannerSlideIn 0.8s cubic-bezier(0.34, 1.56, 0.64, 1);

    &::before {
      content: "";
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 4px;
      background: linear-gradient(90deg, $sky-blue, $light-purple, $mint-green, $sky-blue);
      background-size: 200% 100%;
      animation: gradientMove 3s linear infinite;
      border-radius: 25px 25px 0 0;
    }

    .banner-decoration-left,
    .banner-decoration-right {
      display: flex;
      gap: 15px;

      .deco-emoji {
        font-size: 28px;
        animation: decoFloat 2.5s ease-in-out infinite;
        filter: drop-shadow(0 4px 10px rgba(135, 206, 235, 0.3));

        &:nth-child(1) {
          animation-delay: 0s;
        }

        &:nth-child(2) {
          animation-delay: 0.5s;
        }
      }
    }

    .banner-content {
      text-align: center;
      flex: 1;

      .banner-title {
        margin: 0 0 8px 0;
        font-size: 32px;
        font-weight: 800;
        background: linear-gradient(135deg, #5b9bd5, #9370db, #84c7d0);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
        animation: titleGlow 2s ease-in-out infinite;
      }

      .banner-subtitle {
        margin: 0;
        font-size: 15px;
        color: #5b9bd5;
        font-weight: 500;
        letter-spacing: 1px;
      }
    }
  }

  // 内容卡片
  .content-card {
    background: rgba(255, 255, 255, 0.9);
    backdrop-filter: blur(25px);
    border-radius: 30px;
    border: 3px solid rgba(255, 255, 255, 0.7);
    box-shadow: 0 15px 50px rgba(135, 206, 235, 0.2),
      inset 0 2px 15px rgba(255, 255, 255, 0.8);
    animation: cardSlideIn 1s cubic-bezier(0.34, 1.56, 0.64, 1);
    overflow: visible;

    .card-header {
      padding: 25px 35px;
      background: linear-gradient(135deg, 
        rgba(135, 206, 235, 0.15) 0%,
        rgba(230, 230, 250, 0.15) 100%
      );
      border-bottom: 2px solid rgba(135, 206, 235, 0.2);
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-radius: 27px 27px 0 0;

      .header-left {
        display: flex;
        align-items: center;
        gap: 15px;

        .header-icon {
          width: 45px;
          height: 45px;
          background: linear-gradient(135deg, $sky-blue, $light-purple);
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 24px;
          box-shadow: 0 5px 15px rgba(135, 206, 235, 0.3);
          animation: iconPulse 2s ease-in-out infinite;
        }

        .title {
          margin: 0;
          font-size: 24px;
          font-weight: 700;
          background: linear-gradient(135deg, #5b9bd5, #9370db);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          background-clip: text;
        }
      }

      .create-btn {
        padding: 12px 28px;
        border-radius: 20px;
        font-size: 15px;
        font-weight: 600;
        background: linear-gradient(135deg, $sky-blue, $mint-blue);
        border: none;
        box-shadow: 0 5px 20px rgba(135, 206, 235, 0.4);
        transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);

        .btn-icon {
          margin-right: 6px;
        }

        &:hover {
          transform: translateY(-3px) scale(1.05);
          box-shadow: 0 8px 30px rgba(135, 206, 235, 0.6);
        }
      }
    }

    .filter-section {
      padding: 25px 35px;
      display: flex;
      align-items: center;
      gap: 15px;

      .search-input {
        width: 350px;

        :deep(.el-input__wrapper) {
          border-radius: 20px;
          border: 2px solid rgba(135, 206, 235, 0.3);
          box-shadow: 0 4px 15px rgba(135, 206, 235, 0.1);
          transition: all 0.3s ease;

          &:hover {
            border-color: rgba(135, 206, 235, 0.5);
          }

          &.is-focus {
            border-color: $sky-blue;
            box-shadow: 0 6px 20px rgba(135, 206, 235, 0.3);
          }
        }
      }

      .status-select {
        width: 150px;

        :deep(.el-input__wrapper) {
          border-radius: 20px;
          border: 2px solid rgba(135, 206, 235, 0.3);
          transition: all 0.3s ease;

          &:hover {
            border-color: rgba(135, 206, 235, 0.5);
          }
        }
      }
    }

    .article-list {
      padding: 0 35px 35px;

      :deep(.el-table) {
        border-radius: 15px;
        overflow: hidden;

        th {
          background: linear-gradient(135deg, 
            rgba(135, 206, 235, 0.1) 0%,
            rgba(230, 230, 250, 0.1) 100%
          );
          color: #5b9bd5;
          font-weight: 600;
        }

        tr:hover {
          background: rgba(135, 206, 235, 0.05);
        }
      }

      .article-title {
        color: #5b9bd5;
        text-decoration: none;
        font-weight: 500;
        transition: all 0.3s ease;

        &:hover {
          color: #9370db;
          text-decoration: underline;
        }
      }

      .no-cover {
        width: 80px;
        height: 60px;
        background: linear-gradient(135deg, rgba(135, 206, 235, 0.1), rgba(230, 230, 250, 0.1));
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        color: #5b9bd5;
        font-size: 12px;
        border: 2px dashed rgba(135, 206, 235, 0.3);
      }

      .pagination {
        margin-top: 25px;
        display: flex;
        justify-content: flex-end;

        :deep(.el-pagination) {
          .btn-prev,
          .btn-next,
          .el-pager li {
            border-radius: 8px;
            transition: all 0.3s ease;

            &:hover {
              background: rgba(135, 206, 235, 0.2);
            }

            &.is-active {
              background: linear-gradient(135deg, $sky-blue, $mint-blue);
              color: white;
            }
          }
        }
      }
    }
  }
}

// 动画定义
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

@keyframes cardSlideIn {
  from {
    opacity: 0;
    transform: translateY(30px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
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

@keyframes titleGlow {
  0%, 100% {
    filter: drop-shadow(0 0 10px rgba(135, 206, 235, 0.5));
  }
  50% {
    filter: drop-shadow(0 0 20px rgba(135, 206, 235, 0.8));
  }
}

@keyframes decoFloat {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-12px) rotate(10deg);
  }
}

@keyframes iconPulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .articles-container {
    padding: 0 10px;

    .welcome-banner {
      padding: 20px 25px;
      flex-direction: column;
      gap: 15px;

      .banner-content {
        .banner-title {
          font-size: 24px;
        }

        .banner-subtitle {
          font-size: 13px;
        }
      }
    }

    .content-card {
      .card-header {
        flex-direction: column;
        gap: 15px;
        padding: 20px 25px;

        .header-left {
          width: 100%;
          justify-content: center;
        }

        .create-btn {
          width: 100%;
        }
      }

      .filter-section {
        flex-direction: column;
        padding: 20px 25px;

        .search-input,
        .status-select {
          width: 100%;
        }
      }

      .article-list {
        padding: 0 20px 25px;
      }
    }
  }
}
</style>
