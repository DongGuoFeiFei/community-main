<script setup>
import UserSidebar from './UserSidebar.vue'
import UserHeader from '@/components/user/UserHeader.vue'
import {ref} from "vue";

const isCollapse = ref(false)
const sidebarWidth = ref("230px")

const handleCollapse = () => {

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
      </el-container>
    </el-container>
  </div>
</template>

<style scoped lang="less">

:deep(.el-footer) {
  --el-footer-padding: 0 20px 0 0;
}

.common-layout {
  height: 100%;
  background-color: #f5f5f5;

  .el-container {
    height: 100%;

    .main {
      height: 100%;
    }
  }
}
</style>
