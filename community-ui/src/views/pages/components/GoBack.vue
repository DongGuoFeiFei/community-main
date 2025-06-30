<script setup>

import {ArrowLeft} from "@element-plus/icons-vue";
import {ElIcon} from "element-plus";
import router from "@/router/index.js";

const goBack = () => {
  router.go(-1);
}

</script>

<template>
  <div class="container">
    <!-- 返回按钮 -->
    <div class="back-button" @click="goBack">
      <el-icon>
        <ArrowLeft/>
      </el-icon>
      返回上一页
    </div>
  </div>

</template>

<style scoped lang="less">

.container {
  max-width: 100%;
  margin: 20px auto;
}

.back-button {
  display: inline-flex;
  align-items: center;
  cursor: pointer;
  color: #7e7e7e; /* 默认白色，适配背景 */
  font-size: 16px;
  margin-bottom: 20px;
  transition: color 0.3s;

  .el-icon {
    margin-right: 6px;
    font-size: 20px;
  }

  &:hover {
    color: #2c3e50; /* 悬浮时加深，深蓝色 */
  }
}
</style>