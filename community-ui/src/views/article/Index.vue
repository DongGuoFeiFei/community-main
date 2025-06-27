<script setup>

import Header from "@/components/Header.vue";
import PostDetail from "@/views/article/PostDetail.vue";
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
    </div>
    <div class="cloud-container"></div>
  </div>
</template>

<style scoped lang="less">
.main-container {
  position: relative;
  min-height: 100vh;

  .common-layout {
    position: relative;
    z-index: 2; /* 确保内容在云朵上方 */

    .el-container {
      height: 100%;
    }
  }
}

.cloud-container {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 20vh;
  overflow: hidden;
  z-index: 1;
}
</style>