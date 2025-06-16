<script setup lang="js">
import Footer from "@/components/Footer.vue";
import Header from "@/components/Header.vue";
// import {onBeforeUnmount, onMounted, ref} from 'vue';
//
// let cloudInterval = null;
// const isActive = ref(true); // 页面是否处于激活状态
//
// function createClouds() {
//   if (!isActive.value) return; // 如果页面不是激活状态，不生成
//
//   const cloud = document.createElement("div");
//   cloud.className = 'clouds';
//
//   // 随机位置、大小、速度
//   const size = 20 + Math.random() * 30; // 20px - 50px
//   const speed = 10 + Math.random() * 20; // 10s - 30s
//
//   cloud.style.left = `-${Math.random() * 50}px`;
//   cloud.style.top = `${Math.random() * 20}vh`;
//   cloud.style.width = `${size}px`;
//   cloud.style.height = `${size}px`;
//   cloud.style.animationDuration = `${speed}s`;
//
//   // 动画结束后自动销毁
//   cloud.addEventListener('animationend', () => {
//     cloud.remove();
//   });
//
//   document.querySelector('.cloud-container').appendChild(cloud);
// }
//
// function startClouds() {
//   if (cloudInterval) clearInterval(cloudInterval);
//   cloudInterval = setInterval(createClouds, 6000);
// }
//
// function stopClouds() {
//   if (cloudInterval) {
//     clearInterval(cloudInterval);
//     cloudInterval = null;
//   }
// }
//
// function handleVisibilityChange() {
//   if (document.visibilityState === 'visible') {
//     isActive.value = true;
//     startClouds();
//   } else {
//     isActive.value = false;
//     stopClouds();
//   }
// }
//
// onMounted(() => {
//   // 初始生成几朵
//   for (let i = 0; i < 5; i++) {
//     createClouds();
//   }
//   startClouds();
//   // 监听页面是否可见
//   document.addEventListener('visibilitychange', handleVisibilityChange);
// });
//
// onBeforeUnmount(() => {
//   stopClouds();
//   document.removeEventListener('visibilitychange', handleVisibilityChange);
// });
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
            <RouterView/>
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
