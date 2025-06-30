<template>
  <div class="articles-container">
    <div class="header">
      <h2>我的文章</h2>
      <el-button type="primary" @click="goToEditor">写文章</el-button>
    </div>

    <div class="filter-section">
      <el-input
          v-model="searchQuery"
          placeholder="搜索文章标题"
          clearable
          style="width: 300px"
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
          style="width: 150px; margin-left: 10px"
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
                :preview-src-list="[baseUrl + row.coverUrl]"
                hide-on-click-modal
            />
            <div v-else class="no-cover">无封面</div>
          </template>
        </el-table-column>

        <el-table-column prop="title" label="标题" min-width="200">
          <template #default="{ row }">
            <router-link :to="`/index/article/${row.id}`" class="article-title">
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

        <el-table-column prop="createTime" label="发布时间" width="180" sortable="custom">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
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
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue'
import {useRouter} from 'vue-router'
import {Delete, Edit, Search} from '@element-plus/icons-vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import dayjs from 'dayjs'
import {deleteArticle, getMyArticles} from '../../../../../community-admin/src/api/index.js'
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
      page: pagination.current, // 或 page，根据后端API
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

<style scoped>
.articles-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filter-section {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.article-list {
  margin-top: 20px;
}

.article-title {
  color: #333;
  text-decoration: none;
  transition: color 0.2s;
}

.article-title:hover {
  color: #1890ff;
}

.no-cover {
  width: 80px;
  height: 60px;
  background-color: #f5f5f5;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #999;
  font-size: 12px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.el-button + .el-button {
  margin-left: 8px;
}
</style>
