<script setup>
import PostDetail from "@/views/pages/views/article/components/PostDetail.vue";
import Footer from "@/views/pages/components/Footer.vue";
import AuthorInfo from "@/views/pages/views/article/components/AuthorInfo.vue";
import RecommendSidebar from "@/views/pages/views/article/components/RecommendSidebar.vue";
import {ref} from "vue";
import CommentSection from "@/views/pages/views/article/components/CommentSection.vue";
import Tags from "@/views/pages/views/article/components/Tags.vue";
import HomeHeader from "@/views/pages/views/home/components/HomeHeader.vue";
import Advertising from "@/components/Advertising.vue";
import Live2DViewer from "@/components/Live2D/Live2DViewer.vue";

const articleId = ref(null)
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
            <AuthorInfo v-if="articleId" :articleId="Number(articleId)"/>
            <RecommendSidebar :articleId="Number(articleId)"/>
            <Advertising/>
          </el-aside>
          <el-main>
            <post-detail v-model="articleId"/>
            <tags v-if="Number(articleId)" :postId="Number(articleId)"/>
            <CommentSection v-if="Number(articleId)" :postId="Number(articleId)"/>
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