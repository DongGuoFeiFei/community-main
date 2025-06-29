<script setup>
import UserHeader from "@/views/user/UserHeader.vue";
import Footer from "@/components/Footer.vue";
</script>

<template>
  <div class="main-container">
    <div class="common-layout">
      <el-container class="layout-container">
        <el-header class="header">
          <user-header />
        </el-header>
        <el-container class="content-container">
          <el-main class="main-content">
            <router-view />
          </el-main>
        </el-container>
        <el-footer class="footer">
          <Footer />
        </el-footer>
      </el-container>
    </div>
  </div>
</template>

<style scoped lang="scss">
.main-container {
  position: relative;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: var(--el-bg-color-page);

  .common-layout {
    flex: 1;
    display: flex;
    min-height: 100%;

    .layout-container {
      display: flex;
      flex-direction: column;
      width: 100%;
      min-height: 100%;

      .header {
        padding: 0;
        height: auto;
      }

      .content-container {
        flex: 1;
        display: flex;
        flex-direction: column;

        .main-content {
          flex: 1;
          padding: 20px 0;
          max-width: 1200px;
          width: 100%;
          margin: 0 auto;
        }
      }

      .footer {
        padding: 0;
      }
    }
  }
}
</style>