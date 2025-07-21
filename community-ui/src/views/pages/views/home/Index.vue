<script setup lang="js">
import Header from "@/views/pages/components/Header.vue";
import Footer from "@/views/pages/components/Footer.vue";
import Announcement from "@/components/Announcement.vue";
import {onMounted, reactive, ref} from "vue";
import PostSearch from "@/views/pages/views/home/components/PostSearch.vue";
import PostList from "@/views/pages/views/home/components/PostList.vue";
import PostPagination from "@/views/pages/views/home/components/PostPagination.vue";
import {fetchPosts} from "@/api/index.js";
import {localStores} from "@/stores/localStores.js";


// 共享状态
const posts = ref([])
const total = ref(0)
const loading = ref(true)

// 搜索参数
const searchParam = reactive({
  title: '',
  pageSize: 8,
  pageNum: 1,
})

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
  loadPosts()
})

</script>

<template>
  <div class="main-container">
    <div class="common-layout">
      <el-container>
        <el-header>
          <Header/>
        </el-header>
        <el-container>
          <el-aside width="200px">
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
          <el-aside width="200px"></el-aside>
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
