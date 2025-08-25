<script setup>
import PostDetail from "@/views/pages/views/article/components/PostDetail.vue";
import Footer from "@/views/pages/components/Footer.vue";
import RecommendSidebar from "@/views/pages/views/article/components/RecommendSidebar.vue";
import {onMounted, ref, watch} from "vue";
import CommentSection from "@/views/pages/views/article/components/CommentSection.vue";
import Tags from "@/views/pages/views/article/components/Tags.vue";
import HomeHeader from "@/views/pages/views/home/components/HomeHeader.vue";
import Live2DViewer from "@/components/Live2D/Live2DViewer.vue";
import {getUserInfo} from "@/api/user.js";
import {useRoute} from "vue-router";
import AuthorInfo from "@/views/pages/views/article/components/AuthorInfo.vue";

const route = useRoute()
const articleId = ref(null)

const author = ref({
  id: null,
  username: '',
  nickname: '',
  avatar: '',
  bio: '',
  joinDate: '',
  postCount: 0,
  followerCount: 0,
  followingCount: 0,
  isFollowing: false
})

// 获取作者信息
const fetchAuthorInfo = async () => {
  try {
    if (route.params.id) {
      const res = await getUserInfo(route.params.id)
      author.value = res.data
      console.log(author.value)
    }
  } catch (error) {
    console.error('获取作者信息失败:', error)
  }
}

onMounted(() => {
  fetchAuthorInfo()
})

// 监听路由参数变化
watch(
    () => route.params.id,
    (newId) => {
      if (newId) {
        fetchAuthorInfo()
      }
    }
)

</script>

<template>
  <Live2DViewer/>
  <div class="main-container">
    <div class="common-layout">
      <el-container>
        <el-header>
          <HomeHeader/>
        </el-header>
        <el-container>
          <el-aside width="400px">
            <AuthorInfo v-if="articleId" :author-info="author"/>
            <RecommendSidebar :articleId="Number(articleId)"/>
          </el-aside>
          <el-main>
            <post-detail v-model="articleId"/>
            <tags v-if="Number(articleId)" :postId="Number(articleId)"/>
            <CommentSection v-if="Number(articleId)" :postId="Number(articleId)" :author-id="Number(author.id)"/>
            <!--    todo 付费内容    -->
          </el-main>
          <el-aside width="100px">

          </el-aside>
        </el-container>
      </el-container>
      <el-footer>
        <Footer/>
      </el-footer>
    </div>
  </div>
</template>

<style scoped lang="less">
.main-container {
  .common-layout {
    .el-container {
      height: 100%;

      .el-aside {
        padding: 20px;
        background-color: var(--el-bg-color-page);
      }

      .el-main {
        padding: 20px;
        max-width: 800px;
        margin: 0 auto;
      }
    }
  }
}
</style>
