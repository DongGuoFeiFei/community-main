<template>
  <div class="common-layout">

    <el-container>
      <!--菜单-->
      <el-aside width="auto">
        <AdminMenu :isCollapse="isCollapse"/>
      </el-aside>
      <el-container>
        <!--头部-->
        <el-header>
          <AdminHeader @toggle-collapse="toggleCollapse"/>
        </el-header>
        <!--主体-->
        <el-main>
          <RouterView/>
        </el-main>
        <!--脚部-->
        <el-footer class="footer">
          <AdminFooter/>
        </el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>

import AdminHeader from "@/views/admin/components/AdminHeader.vue";
import AdminMenu from "@/views/admin/components/AdminMenu.vue";
import AdminFooter from "@/views/admin/components/AdminFooter.vue";
import {ref} from "vue";

const isCollapse = ref(false);

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value;
};
</script>
<style lang="scss" scoped>
.common-layout {
  height: 100vh; // 整个布局占满视口高度
  width: 100vw; // 整个布局占满视口宽度
  overflow: hidden; // 防止全局滚动条

  .el-container {
    height: 100%; // 容器占满父元素高度

    .el-aside {
      height: 100%;
      background-color: #304156;
      overflow-y: auto;
      transition: width 0.3s ease;
    }

    .el-container {
      display: flex;
      flex-direction: column;

      .el-header {
        height: 70px; // 固定高度
        padding: 0;
        background-color: #fff;
        box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
      }

      .el-main {
        flex: 1; // 占据剩余空间
        padding: 20px;
        overflow-y: auto; // 允许内容滚动
        background-color: #f0f2f5;
      }

      .el-footer {
        padding: 0;
        background-color: #fff;
        border-top: 1px solid #e8e8e8;

      }
    }
  }
}
</style>