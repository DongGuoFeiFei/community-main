<template>
  <el-card>
    <el-avatar :src="user.avatar" size="large" />
    <el-descriptions title="用户信息" :column="1" border>
      <el-descriptions-item label="用户名">{{ user.username }}</el-descriptions-item>
      <el-descriptions-item label="邮箱">{{ user.email }}</el-descriptions-item>
      <el-descriptions-item label="注册时间">{{ user.registerTime }}</el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const user = ref({})
onMounted(() => {
  // 请求后端获取用户信息
})
</script>
