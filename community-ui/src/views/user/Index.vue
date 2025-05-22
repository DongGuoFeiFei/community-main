<script setup>
import UserSidebar from './UserSidebar.vue'
import UserHeader from '@/components/user/UserHeader.vue'
import Footer from "@/components/Footer.vue";
import {ref} from "vue";

const isCollapse = ref(false)
const sidebarWidth = ref("230px")

const handleCollapse = () => {
  // 不进行收放处理
  // isCollapse.value = !isCollapse.value
  // isCollapse.value ? sidebarWidth.value = "70px" : sidebarWidth.value = "230px"
}

</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-aside :width=sidebarWidth>
        <UserSidebar
            :isCollapse="isCollapse"
            @changeCollapse="handleCollapse"
        />
      </el-aside>
      <el-container>
        <el-header>
          <UserHeader/>
        </el-header>
        <el-main class="main">
          <router-view/>
        </el-main>
<!--        <el-footer>-->
<!--          <Footer/>-->
<!--        </el-footer>-->
      </el-container>
    </el-container>
  </div>
</template>

<style scoped lang="less">

:deep(.el-footer){
  --el-footer-padding: 0 20px 0 0;
}

.common-layout {
  height: 100%;
  background-color: #f5f5f5;

  .el-container {
    height: 100%;
    .main{
      height: 100%;
    }
  }
}
</style>
