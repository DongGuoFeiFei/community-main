<script setup lang="js">
import Header from "@/views/pages/components/Header.vue";
import Card from "@/views/pages/views/home/Card.vue";
import Footer from "@/views/pages/components/Footer.vue";
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
            <Card/>
          </el-main>
          <el-aside width="200px"></el-aside>
        </el-container>
        <el-footer>
          <Footer/>
        </el-footer>
      </el-container>
    </div>
    <div class="cloud-container"></div>
  </div>
</template>

<style scoped lang="less">
.main-container {
  min-height: 100vh;
  .common-layout {
    .el-container {
      height: 100%;
    }
  }
}

</style>
