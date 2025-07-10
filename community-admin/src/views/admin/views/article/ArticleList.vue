<template>
  <div class="article-list-container">
    <el-card shadow="never">
      <div class="filter-container">
        <ArticleFilter @search="handleSearch"/>
        <el-button type="primary" @click="handleCreate" class="add-btn">
          <el-icon>
            <Plus/>
          </el-icon>
          新增文章
        </el-button>
      </div>

      <el-table
          :data="articleList"
          v-loading="loading"
          border
          style="width: 100%"
      >
        <el-table-column prop="articleId" label="ID" width="80"/>
        <el-table-column prop="title" label="标题" min-width="200"/>
        <el-table-column prop="shareCount" label="分享" width="120"/>
        <!--        <el-table-column label="标签" width="180">-->
        <!--          <template #default="{ row }">-->
        <!--            <el-tag-->
        <!--                v-for="tag in row.tags"-->
        <!--                :key="tag.id"-->
        <!--                size="small"-->
        <!--                class="mr-2"-->
        <!--            >-->
        <!--              {{ tag.name }}-->
        <!--            </el-tag>-->
        <!--          </template>-->
        <!--        </el-table-column>-->
        <el-table-column prop="viewCount" label="浏览" width="80"/>
        <el-table-column prop="commentCount" label="评论" width="80"/>
        <el-table-column prop="isDrafts" label="状态" width="100"/>
        <el-table-column prop="createdAt" label="发布时间" width="180"/>
        <el-table-column prop="updatedAt" label="修改时间" width="180"/>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button
                size="small"
                type="danger"
                @click="handleDelete(row)"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
            v-model:current-page="pagination.current"
            v-model:page-size="pagination.size"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <ArticleEditor
        v-model="editorVisible"
        :article="currentArticle"
        @success="handleEditorSuccess"
    />
  </div>
</template>
<!--todo 编辑器还是会有问题，待修复 编辑器实例化，拉出提前实例化-->
<script setup>
import {onMounted, reactive, ref} from 'vue'
import {Plus} from '@element-plus/icons-vue'
import {deleteArticle, getArticleList} from '@/api/article/index.js'
import ArticleFilter from './ArticleFilter.vue'
import {useArticleStore} from '@/stores/articleStore'
import {ElMessage, ElMessageBox} from "element-plus";
import ArticleEditor from "@/views/admin/views/article/ArticleEditor.vue";

const articleStore = useArticleStore()

const loading = ref(false)
const editorVisible = ref(false)
const currentArticle = ref(null)

const articleList = ref([])

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const statusMap = {
  0: {label: '草稿', type: 'info'},
  1: {label: '已发布', type: 'success'},
  2: {label: '已下架', type: 'warning'}
}

const searchParams = reactive({
  title: '',
  categoryId: '',
  tagId: '',
  status: '',
  startTime: '',
  endTime: ''
})

onMounted(() => {
  fetchData()
})

const fetchData = async () => {
  try {
    loading.value = true
    const params = {
      ...searchParams,
      pageNum: pagination.current,
      pageSize: pagination.size
    }

    const res = await getArticleList(params)
    articleList.value = res.data.rows
    pagination.total = res.data.total

    console.log(articleList.value)
  } finally {
    loading.value = false
  }
}

const handleSearch = (params) => {
  Object.assign(searchParams, params)
  pagination.current = 1
  fetchData()
}

const handleSizeChange = (size) => {
  pagination.size = size
  fetchData()
}

const handleCurrentChange = (current) => {
  pagination.current = current
  fetchData()
}

const handleCreate = () => {
  currentArticle.value = null
  editorVisible.value = true
}

const handleEdit = (article) => {

  currentArticle.value = {...article}
  console.log(currentArticle.value)
  editorVisible.value = true
}

const handleDelete = async (article) => {
  try {
    await ElMessageBox.confirm(`确定删除文章 "${article.title}" 吗?`, '提示', {
      type: 'warning'
    })
    await deleteArticle(article.articleId)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {
    console.error(error)
  }
}

const handleEditorSuccess = () => {
  editorVisible.value = false
  fetchData()
}
</script>

<style lang="scss" scoped>
.article-list-container {
  padding: 20px;

  .filter-container {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;

    .add-btn {
      margin-left: 10px;
    }
  }

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }

  .mr-2 {
    margin-right: 8px;
  }
}
</style>