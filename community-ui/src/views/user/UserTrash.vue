<template>
  <div class="trash-container">
    <div class="header">
      <h2>垃圾箱</h2>
      <div class="header-actions">
        <el-button type="danger" :disabled="!selectedArticles.length" @click="batchDelete">
          永久删除
        </el-button>
        <el-button type="warning" :disabled="!selectedArticles.length" @click="batchRestore">
          恢复文章
        </el-button>
      </div>
    </div>

    <div class="filter-section">
      <el-input
          v-model="searchQuery"
          placeholder="搜索已删除文章"
          clearable
          style="width: 300px"
          @clear="handleSearchClear"
          @keyup.enter="fetchTrashArticles"
      >
        <template #prefix>
          <el-icon>
            <Search/>
          </el-icon>
        </template>
      </el-input>

      <el-select
          v-model="filterDays"
          placeholder="删除时间"
          clearable
          style="width: 150px; margin-left: 10px"
          @change="fetchTrashArticles"
      >
        <el-option label="最近一周" :value="7"/>
        <el-option label="最近一月" :value="30"/>
        <el-option label="全部" :value="0"/>
      </el-select>
    </div>

    <div class="trash-list">
      <el-table
          :data="trashArticles"
          style="width: 100%"
          v-loading="loading"
          empty-text="垃圾箱为空"
          @selection-change="handleSelectionChange"
          @sort-change="handleSortChange"
      >
        <el-table-column type="selection" width="55"/>

        <el-table-column prop="title" label="标题" min-width="200">
          <template #default="{ row }">
            <span class="article-title">{{ row.title }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="deleteTime" label="删除时间" width="180" sortable="custom">
          <template #default="{ row }">
            {{ formatDateTime(row.deleteTime) }}
          </template>
        </el-table-column>

        <el-table-column prop="deleteBy" label="操作人" width="120">
          <template #default="{ row }">
            {{ row.deleteBy || '系统' }}
          </template>
        </el-table-column>

        <el-table-column prop="deleteReason" label="删除原因" width="200">
          <template #default="{ row }">
            {{ row.deleteReason || '无说明' }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button
                size="small"
                @click="showDetail(row)"
                :icon="View"
                circle
            />
            <el-button
                size="small"
                type="warning"
                @click="restoreArticle(row.id)"
                :icon="Refresh"
                circle
            />
            <el-button
                size="small"
                type="danger"
                @click="deleteArticle(row.id)"
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

    <!-- 文章详情对话框 -->
    <el-dialog v-model="detailVisible" title="文章详情" width="70%">
      <div v-if="currentArticle" class="article-detail">
        <h3>{{ currentArticle.title }}</h3>
        <div class="meta">
          <span>创建时间: {{ formatDateTime(currentArticle.createTime) }}</span>
          <span style="margin-left: 20px">删除时间: {{ formatDateTime(currentArticle.deleteTime) }}</span>
        </div>
        <div class="content" v-html="currentArticle.content"></div>
      </div>
    </el-dialog>
  </div>
</template>

<style scoped>
.trash-container {
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

.header-actions .el-button + .el-button {
  margin-left: 10px;
}

.filter-section {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.trash-list {
  margin-top: 20px;
}

.article-title {
  color: #666;
}

.article-detail .meta {
  margin: 10px 0;
  color: #999;
  font-size: 14px;
}

.article-detail .content {
  margin-top: 20px;
  line-height: 1.6;
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

<script setup>
import { ref, onMounted } from 'vue'
import { Search, View, Refresh, Delete } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'
// import * as api from '@/api/trash'
import dompurify from 'dompurify'

const router = useRouter()

// 数据状态
const trashArticles = ref([])
const selectedArticles = ref([])
const currentArticle = ref(null)
const loading = ref(false)
const detailVisible = ref(false)

// 搜索和筛选条件
const searchQuery = ref('')
const filterDays = ref(7)

// 分页
const pagination = ref({
  current: 1,
  size: 10,
  total: 0
})

// 排序
const sortParams = ref({
  prop: '',
  order: ''
})

// 初始化加载数据
onMounted(() => {
  fetchTrashArticles()
})

// 获取垃圾箱文章列表
const fetchTrashArticles = async () => {
  try {
    loading.value = true
    const params = {
      pageNum: pagination.value.current,
      pageSize: pagination.value.size,
      keyword: searchQuery.value,
      days: filterDays.value,
      sortField: sortParams.value.prop,
      sortOrder: sortParams.value.order
    }

    // API调用预留位置
    const res = await api.getTrashList(params)

    trashArticles.value = res.data.list
    pagination.value.total = res.data.total
  } catch (error) {
    ElMessage.error(error.message || '获取垃圾箱列表失败')
  } finally {
    loading.value = false
  }
}

// 表格选择变化
const handleSelectionChange = (selection) => {
  selectedArticles.value = selection.map(item => item.id)
}

// 排序变化
const handleSortChange = ({ prop, order }) => {
  sortParams.value = { prop, order }
  fetchTrashArticles()
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.value.size = size
  fetchTrashArticles()
}

// 页码变化
const handlePageChange = (page) => {
  pagination.value.current = page
  fetchTrashArticles()
}

// 清除搜索
const handleSearchClear = () => {
  searchQuery.value = ''
  fetchTrashArticles()
}

// 显示文章详情
const showDetail = (article) => {
  currentArticle.value = {
    ...article,
    content: dompurify.sanitize(article.content)
  }
  detailVisible.value = true
}

// 恢复单个文章
const restoreArticle = async (id) => {
  try {
    await ElMessageBox.confirm('确定要恢复这篇文章吗?', '提示', {
      type: 'warning'
    })

    // API调用预留位置
    await api.restoreArticle(id)

    ElMessage.success('文章恢复成功')
    fetchTrashArticles()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '恢复文章失败')
    }
  }
}

// 永久删除单个文章
const deleteArticle = async (id) => {
  try {
    await ElMessageBox.confirm('此操作将永久删除该文章, 是否继续?', '警告', {
      type: 'error'
    })

    // API调用预留位置
    await api.deleteArticlePermanently(id)

    ElMessage.success('文章已永久删除')
    fetchTrashArticles()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除文章失败')
    }
  }
}

// 批量恢复
const batchRestore = async () => {
  try {
    await ElMessageBox.confirm(`确定要恢复选中的 ${selectedArticles.value.length} 篇文章吗?`, '提示', {
      type: 'warning'
    })

    // API调用预留位置
    await api.batchRestoreArticles(selectedArticles.value)

    ElMessage.success('批量恢复成功')
    selectedArticles.value = []
    fetchTrashArticles()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '批量恢复失败')
    }
  }
}

// 批量永久删除
const batchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定要永久删除选中的 ${selectedArticles.value.length} 篇文章吗?`, '警告', {
      type: 'error'
    })

    // API调用预留位置
    await api.batchDeleteArticles(selectedArticles.value)

    ElMessage.success('批量删除成功')
    selectedArticles.value = []
    fetchTrashArticles()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '批量删除失败')
    }
  }
}

// 日期格式化
const formatDateTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}
</script>