<template>
  <el-badge :value="unreadCount" :max="99" class="notification-badge">
    <el-icon :size="20">
      <Bell/>
    </el-icon>
  </el-badge>
</template>

<script setup>
import {Bell} from '@element-plus/icons-vue';
import {computed} from 'vue';
import {sessionStore} from "@/stores/sessionStores.js";


const sStore = sessionStore();
const unreadCount = computed(() => sStore.unreadNotificationCount);
</script>

<style scoped>
.notification-badge {
  cursor: pointer;
  margin-right: 20px;

  :deep(.el-badge__content) {
    top: 5px;
    right: 5px;
  }
}
</style>