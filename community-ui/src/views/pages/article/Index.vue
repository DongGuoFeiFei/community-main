<script setup>
import Header from "@/components/Header.vue";
import PostDetail from "@/views/pages/article/PostDetail.vue";
import Footer from "@/components/Footer.vue";
</script>

<template>
  <div class="main-container">
    <div class="common-layout">
      <el-container>
        <el-header>
          <Header/>
        </el-header>
        <el-container>
          <el-aside width="200px"></el-aside>
          <el-main>
            <post-detail/>
          </el-main>
          <el-aside width="200px"></el-aside>
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
    }
  }
}
</style>