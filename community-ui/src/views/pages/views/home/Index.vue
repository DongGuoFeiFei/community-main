<script setup lang="js">
import Footer from "@/views/pages/components/Footer.vue";
import Announcement from "@/components/Announcement.vue";
import {onMounted, reactive, ref, watch} from "vue";
import PostSearch from "@/views/pages/views/home/components/PostSearch.vue";
import PostList from "@/views/pages/views/home/components/PostList.vue";
import PostPagination from "@/views/pages/views/home/components/PostPagination.vue";
import {fetchPosts} from "@/api/index.js";
import {localStores} from "@/stores/localStores.js";
import HomeHeader from "@/views/pages/views/home/components/HomeHeader.vue";
import {useRoute} from "vue-router";
import Live2DViewer from "@/views/pages/components/Live2D/Live2DViewer.vue";


// 共享状态
const posts = ref([])
const total = ref(0)
const loading = ref(true)
const route = useRoute()
const routeCategoryId = ref(route.params.id)

// 搜索参数
const searchParam = reactive({
  title: '',
  pageSize: 8,
  pageNum: 1,
  sortType: '',
  categoryId: null
})

const handleCategoryChange = (categoryId) => {
  searchParam.categoryId = categoryId
  searchParam.pageNum = 1
  console.log('Selected category ID:', searchParam.categoryId)
  loadPosts()
}

const lStore = localStores()
// 加载文章方法
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
  } catch (error) {
    console.error('加载文章失败:', error)
  } finally {
    loading.value = false
  }
}

// 初始化加载
onMounted(() => {
  if (route.params.id) {
    searchParam.categoryId = route.params.id
    console.log(route.params.id)
  }
  loadPosts()
})
// 监听路由变化
watch(() => route.params.id, (newId) => {
  if (newId) {
    console.log(newId)
    searchParam.categoryId = newId
    loadPosts()
  }
})

</script>

<template>
  <div class="main-container">
    <div class="common-layout">
      <el-container>
        <el-header>
          <HomeHeader @category-change="handleCategoryChange"/>
        </el-header>
        <el-container>
          <el-aside width="200px">
            <Live2DViewer
                :scale="0.15"
                :position="{ x: 50, y: 350 }"
            />
          </el-aside>
          <el-main>
            <Announcement/>
            <div class="card-container">
              <!-- 搜索组件 -->
              <PostSearch
                  v-model:title="searchParam.title"
                  @search="loadPosts"
              />

              <!-- 文章列表 -->
              <PostList
                  :posts="posts"
                  :loading="loading"
                  :page-size="searchParam.pageSize"
              />

              <!-- 分页组件 -->
              <PostPagination
                  v-model:current-page="searchParam.pageNum"
                  :page-size="searchParam.pageSize"
                  :total="total"
                  :loading="loading"
                  @page-change="loadPosts"
              />
            </div>
          </el-main>
          <el-aside width="200px">

          </el-aside>
        </el-container>
        <el-footer>
          <Footer/>
        </el-footer>
      </el-container>
    </div>
  </div>
</template>

<style scoped lang="less">
.main-container {
  min-height: 100vh;

  .common-layout {
    .el-container {
      height: 100%;
    }

    .card-container {
      padding: 20px;
      display: flex;
      flex-direction: column;
      gap: 16px;
    }
  }
}

</style>
