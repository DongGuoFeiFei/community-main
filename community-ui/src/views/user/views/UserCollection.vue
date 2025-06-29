<template>
  <div class="favorites-container">
    <div class="header">
      <h2>我的收藏</h2>
      <el-button type="primary" @click="showCreateFolderDialog">新建收藏夹</el-button>
    </div>

    <!-- 收藏夹列表 - 横向滑动 -->
    <div class="folders-section">
      <div class="section-header">
        <h3>收藏夹</h3>
      </div>
      <div class="folder-scroll-container">
        <div class="folder-list">
          <el-card
              v-for="folder in folders"
              :key="folder.id"
              class="folder-card"
              :class="{ 'active-folder': activeFolderId === folder.id }"
              @click="selectFolder(folder.id)"
          >
            <div class="folder-info">
              <h4>{{ folder.name }}</h4>
              <span>{{ folder.articleCount }}篇文章</span>
              <span class="create-time">{{ formatDate(folder.createTime) }}</span>
            </div>
            <div class="folder-actions">
              <el-button
                  link
                  size="small"
                  @click.stop="showEditFolderDialog(folder)"
                  :icon="Edit"
              />
              <el-button
                  link
                  size="small"
                  @click.stop="confirmDeleteFolder(folder.id)"
                  :icon="Delete"
                  class="delete-btn"
              />
            </div>
          </el-card>
        </div>
      </div>
    </div>

    <!-- 收藏的文章列表 -->
    <div class="articles-section">
      <div class="filter-section">
        <el-input
            v-model="searchQuery"
            placeholder="搜索收藏的文章"
            clearable
            style="width: 300px"
            @clear="handleSearchClear"
            @keyup.enter="fetchFavoriteArticlesList"
        >
          <template #prefix>
            <el-icon>
              <Search/>
            </el-icon>
          </template>
        </el-input>
      </div>

      <div class="article-list">
        <el-table
            :data="favoriteArticles"
            style="width: 100%"
            v-loading="loading"
            empty-text="暂无收藏文章"
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
            </template>
          </el-table-column>

          <el-table-column prop="author" label="作者" width="120">
            <template #default="{ row }">
              <router-link :to="`/user/${row.authorId}`" class="author-link">
                {{ row.author }}
              </router-link>
            </template>
          </el-table-column>

          <el-table-column prop="viewCount" label="浏览" width="100">
            <template #default="{ row }">
              {{ row.viewCount || 0 }}
            </template>
          </el-table-column>

          <el-table-column prop="likeCount" label="点赞" width="100">
            <template #default="{ row }">
              {{ row.likeCount || 0 }}
            </template>
          </el-table-column>

          <el-table-column prop="collectTime" label="收藏时间" width="180">
            <template #default="{ row }">
              {{ formatDateTime(row.collectTime) }}
            </template>
          </el-table-column>

          <el-table-column label="操作" width="180" fixed="right">
            <template #default="{ row }">
              <el-dropdown @command="handleCommand($event, row.id,activeFolderId)">
                <el-button type="primary" size="small" plain>
                  移动到
                  <el-icon class="el-icon--right">
                    <ArrowDown/>
                  </el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item
                        v-for="folder in folders"
                        :key="folder.id"
                        :command="folder.id"
                        :disabled="folder.id === activeFolderId"
                    >
                      {{ folder.name }}
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
              <el-button
                  size="small"
                  type="danger"
                  @click="cancelCollection(row.id)"
                  :icon="Delete"
                  circle
              />
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination">
          <el-pagination
              background
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

    <!-- 创建/编辑收藏夹对话框 -->
    <el-dialog
        v-model="folderDialogVisible"
        :title="isEditFolder ? '编辑收藏夹' : '新建收藏夹'"
        width="500px"
    >
      <el-form :model="folderForm" :rules="folderRules" ref="folderFormRef">
        <el-form-item label="收藏夹名称" prop="name">
          <el-input
              v-model="folderForm.name"
              placeholder="请输入收藏夹名称(2-20个字符)"
              maxlength="20"
              show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="folderDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitFolderForm">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {ArrowDown, Delete, Edit, Search} from '@element-plus/icons-vue'
import dayjs from 'dayjs'
import {
  cancelCollect,
  createFolder,
  deleteFolder,
  fetchFavoriteArticles,
  fetchFolders,
  moveFavorite,
  updateFolder
} from '../../../../../community-admin/src/api/collectApi.js'
import {localStore} from '@/stores/localStores.js'
import {useRouter} from "vue-router";

const router = useRouter()
const lStore = localStore()

// 基础URL，用于拼接图片路径
const baseUrl = lStore.baseURL

// 收藏夹数据
const folders = ref([])
const activeFolderId = ref(null)
const folderDialogVisible = ref(false)
const isEditFolder = ref(false)
const currentFolderId = ref(null) // 当前编辑的收藏夹ID

// 收藏夹表单
const folderForm = reactive({
  name: ''
})
const folderFormRef = ref(null)

// 收藏夹表单验证规则
const folderRules = {
  name: [
    {required: true, message: '请输入收藏夹名称', trigger: 'blur'},
    {min: 2, max: 20, message: '长度在2到20个字符', trigger: 'blur'}
  ]
}

// 收藏的文章数据
const favoriteArticles = ref([])
const searchQuery = ref('')

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const loading = ref(false)

// 格式化日期
const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD')
}

// 格式化日期时间
const formatDateTime = (datetime) => {
  return dayjs(datetime).format('YYYY-MM-DD HH:mm:ss')
}

// 获取收藏夹列表
const fetchFoldersList = async () => {
  try {
    const res = await fetchFolders()
    if (res.code === 200) {
      folders.value = res.data
      // 默认选中第一个收藏夹
      if (folders.value.length > 0 && !activeFolderId.value) {
        activeFolderId.value = folders.value[0].id
        await fetchFavoriteArticlesList()
      }
    } else {
      throw new Error(res.msg || '获取收藏夹列表失败')
    }
  } catch (error) {
    ElMessage.error('获取收藏夹列表失败: ' + error.message)
  }
}

// 获取收藏的文章列表
const fetchFavoriteArticlesList = async () => {
  if (!activeFolderId.value) return

  loading.value = true
  try {
    const params = {
      page: pagination.current,
      size: pagination.size,
      keyword: searchQuery.value,
      folderId: activeFolderId.value
    }
    const res = await fetchFavoriteArticles(params)
    if (res.code === 200) {
      favoriteArticles.value = res.data.rows
      pagination.total = res.data.total
    } else {
      throw new Error(res.msg || '获取收藏文章失败')
    }
  } catch (error) {
    ElMessage.error('获取收藏文章失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

// 选择收藏夹
const selectFolder = (folderId) => {
  activeFolderId.value = folderId
  pagination.current = 1 // 重置到第一页
  fetchFavoriteArticlesList()
}

// 显示创建收藏夹对话框
const showCreateFolderDialog = () => {
  isEditFolder.value = false
  folderForm.name = ''
  folderDialogVisible.value = true
  currentFolderId.value = null
}

// 显示编辑收藏夹对话框
const showEditFolderDialog = (folder) => {
  isEditFolder.value = true
  folderForm.name = folder.name
  currentFolderId.value = folder.id
  folderDialogVisible.value = true
}

// 提交收藏夹表单
const submitFolderForm = async () => {
  await folderFormRef.value.validate()

  try {
    if (isEditFolder.value) {
      // 编辑收藏夹
      const res = await updateFolder(currentFolderId.value, {name: folderForm.name})
      if (res.code === 200) {
        ElMessage.success('收藏夹更新成功')
        await fetchFoldersList()
      } else {
        throw new Error(res.msg || '更新收藏夹失败')
      }
    } else {
      // 创建收藏夹
      const res = await createFolder({name: folderForm.name})
      if (res.code === 200) {
        ElMessage.success('收藏夹创建成功')
        await fetchFoldersList()
      } else {
        throw new Error(res.msg || '创建收藏夹失败')
      }
    }
    folderDialogVisible.value = false
  } catch (error) {
    ElMessage.error('操作失败: ' + error.message)
  }
}

// 确认删除收藏夹
const confirmDeleteFolder = (folderId) => {
  ElMessageBox.confirm('确定要删除这个收藏夹吗？收藏夹内的文章将一并删除。', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
      .then(() => {
        deleteFolder(folderId)
            .then((res) => {
              if (res.code === 200) {
                ElMessage.success('收藏夹删除成功')
                // 如果删除的是当前选中的收藏夹，则选中第一个收藏夹
                if (activeFolderId.value === folderId) {
                  activeFolderId.value = folders.value.length > 0 ? folders.value[0].id : null
                }
                fetchFoldersList()
              } else {
                throw new Error(res.msg || '删除收藏夹失败')
              }
            })
            .catch((error) => {
              ElMessage.error('删除收藏夹失败: ' + error.message)
            })
      })
      .catch(() => {
      })
}

// 取消收藏
const cancelCollection = (articleId) => {
  ElMessageBox.confirm('确定要取消收藏这篇文章吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
      .then(() => {
        cancelCollect(articleId)
            .then((res) => {
              if (res.code === 200) {
                ElMessage.success('已取消收藏')
                fetchFavoriteArticlesList()
              } else {
                throw new Error(res.msg || '取消收藏失败')
              }
            })
            .catch((error) => {
              ElMessage.error('取消收藏失败: ' + error.message)
            })
      })
      .catch(() => {
      })
}

// 处理移动收藏命令
const handleCommand = (folderId, articleId, activeFolderId) => {
  moveFavorite(articleId, folderId, activeFolderId)
      .then((res) => {
        if (res.code === 200) {
          ElMessage.success('文章移动成功')
          fetchFavoriteArticlesList()
          fetchFoldersList()
        } else {
          throw new Error(res.msg || '移动收藏失败')
        }
      })
      .catch((error) => {
        ElMessage.error('移动收藏失败: ' + error.message)
      })
}

// 处理分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1 // 重置到第一页
  fetchFavoriteArticlesList()
}

// 处理页码变化
const handlePageChange = (page) => {
  pagination.current = page
  fetchFavoriteArticlesList()
}

// 搜索框清空
const handleSearchClear = () => {
  searchQuery.value = ''
  fetchFavoriteArticlesList()
}

onMounted(() => {
  fetchFoldersList()
})
</script>

<style scoped lang="scss">
.favorites-container {
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

/* 收藏夹部分样式调整 */
.folders-section {
  margin-bottom: 30px;

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;

    h3 {
      margin: 0;
      font-size: 16px;
      color: #666;
    }
  }
}

.folder-scroll-container {
  width: 100%;
  overflow-x: auto;
  padding-bottom: 10px; // 为滚动条留出空间

  &::-webkit-scrollbar {
    height: 6px;
  }

  &::-webkit-scrollbar-thumb {
    background-color: #c1c1c1;
    border-radius: 3px;
  }
}

.folder-list {
  display: inline-flex;
  gap: 15px;
  padding-bottom: 5px; // 防止阴影被截断
}

.folder-card {
  width: 220px;
  min-width: 220px;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;

  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  &.active-folder {
    border-color: #409eff;
    background-color: #f5f7fa;
  }
}

.folder-info {
  h4 {
    margin: 0 0 8px 0;
    font-size: 15px;
    color: #333;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  span {
    display: block;
    font-size: 12px;
    color: #999;

    &.create-time {
      margin-top: 5px;
    }
  }
}

.folder-actions {
  position: absolute;
  right: 10px;
  top: 10px;

  .delete-btn {
    color: #f56c6c;
  }
}

/* 文章列表部分样式 */
.articles-section {
  .filter-section {
    margin-bottom: 20px;
  }

  .article-list {
    margin-top: 20px;
  }
}

.article-title {
  color: #333;
  text-decoration: none;
  transition: color 0.2s;

  &:hover {
    color: #1890ff;
  }
}

.author-link {
  color: #666;
  text-decoration: none;
  transition: color 0.2s;

  &:hover {
    color: #1890ff;
  }
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
</style>