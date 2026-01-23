<template>
  <div class="user-profile-container">
    <div class="user-profile-header">
      <AuthorBasicInfo :user="userData"/>
    </div>

    <div class="user-profile-content">
      <div class="user-profile-left">
        <AuthorStats :stats="userStats" :userId="userId"/>
        <AuthorFollow :userId="userId"/>
      </div>

      <div class="user-profile-right">
        <AuthorPosts :userId="userId"/>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {useRoute} from 'vue-router'
import AuthorBasicInfo from './AuthorBasicInfo.vue'
import AuthorStats from './AuthorStats.vue'
import AuthorFollow from './AuthorFollow.vue'
import AuthorPosts from './AuthorPosts.vue'
import {getUserProfile} from '@/api/author.js'
import {getUserStats} from "@/api/user.js";

const route = useRoute()
// 获取用户ID
const userId = ref(route.params.id)
const userData = ref({})
const userStats = ref({})

const fetchUserData = async () => {
  try {
    const response = await getUserProfile(userId.value)
    userData.value = response.data
    const count = await getUserStats(userId.value)
    console.log(count)
    console.log("userData.value", userData.value)
    userStats.value = count.data
    document.title = userData.value.nickname + "的主页"
  } catch (error) {
    console.error('获取用户数据失败:', error)
  }
}

onMounted(() => {
  fetchUserData()
})
</script>

<style lang="scss" scoped>
.user-profile-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  animation: fade-in-up 0.8s ease-out;

  .user-profile-header {
    margin-bottom: 30px;
    animation: slide-in-down 0.6s ease-out;
  }

  .user-profile-content {
    display: flex;
    gap: 20px;

    .user-profile-left {
      flex: 1;
      max-width: 300px;
      animation: slide-in-left 0.7s ease-out;
    }

    .user-profile-right {
      flex: 3;
      animation: slide-in-right 0.7s ease-out;
    }
  }
}

// 动画定义
@keyframes fade-in-up {
  0% {
    opacity: 0;
    transform: translateY(30px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slide-in-down {
  0% {
    opacity: 0;
    transform: translateY(-30px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slide-in-left {
  0% {
    opacity: 0;
    transform: translateX(-30px);
  }
  100% {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes slide-in-right {
  0% {
    opacity: 0;
    transform: translateX(30px);
  }
  100% {
    opacity: 1;
    transform: translateX(0);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .user-profile-container {
    padding: 10px;

    .user-profile-content {
      flex-direction: column;

      .user-profile-left {
        max-width: 100%;
      }
    }
  }
}
</style>
