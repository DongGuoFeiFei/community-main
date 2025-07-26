<script setup>
import UserHeader from "@/views/user/components/UserHeader.vue";
import UserFooter from "@/views/user/components/UserFooter.vue";
import WelcomePage from "@/views/user/components/WelcomePage.vue";
import Live2DViewer from "@/components/Live2D/Live2DViewer.vue";
</script>

<template>
  <Live2DViewer/>
  <div class="main-container">
    <div class="common-layout">
      <el-container class="layout-container">
        <el-header class="header">
          <user-header/>
        </el-header>
        <el-container class="content-container">
          <el-main class="main-content">
            <router-view v-slot="{Component}">
              <component :is="Component" v-if="Component"></component>
              <div v-else>
                <WelcomePage/>
              </div>
            </router-view>
          </el-main>
        </el-container>
        <el-footer class="footer">
          <UserFooter/>
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