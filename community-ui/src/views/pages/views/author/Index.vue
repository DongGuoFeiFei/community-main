<script setup>

import AuthorProfile from "@/views/pages/views/author/components/AuthorProfile.vue";
import Header from "@/views/pages/components/Header.vue";
import Footer from "@/views/pages/components/Footer.vue";
import HomeHeader from "@/views/pages/views/home/components/HomeHeader.vue";
import Live2DViewer from "@/views/pages/components/Live2D/Live2DViewer.vue";
</script>

<template>
  <Live2DViewer/>
  <el-container>
    <el-header>
      <HomeHeader/>
    </el-header>
    <el-main>
      <AuthorProfile/>
    </el-main>
    <el-footer>
      <Footer/>
    </el-footer>
  </el-container>

  <!-- todo 隐私设置，是否展示页面数据 -->
</template>

<style scoped lang="scss">

</style>