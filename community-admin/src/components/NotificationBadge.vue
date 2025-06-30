<template>
  <!--  <div-->
  <!--      class="notification-container"-->
  <!--      @mouseenter="showPanel = true"-->
  <!--      @mouseleave="showPanel = false"-->
  <!--      v-if="unreadCount > 0"-->
  <!--  >-->
  <!-- todo 优化通知显示 -->
  <div
      class="notification-container"
      @mouseenter="showPanel = true"
      @mouseleave="showPanel = false"
  >
    <el-popover
        placement="bottom"
        :width="300"
        trigger="hover"
        v-model:visible="showPanel"
    >
      <template #reference>
        <el-badge :value="unreadCount" :max="99" class="notification-badge" :hidden="unreadCount === 0">
          <el-icon :size="20">
            <Bell/>
          </el-icon>
        </el-badge>
      </template>
      <NotificationPanel
          :unread-count="unreadCount"
          @read="handleRead"
          @close="showPanel = false"
      />
    </el-popover>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {Bell} from '@element-plus/icons-vue'
import NotificationPanel from './NotificationPanel.vue'
import {sessionStores} from "@/stores/sessionStores.js"

const unreadCount = ref(0)
const showPanel = ref(false)
const sStore = sessionStores()

const fetchUnreadCount = async () => {
  try {
    unreadCount.value = sStore.unreadCount
  } catch (error) {
    console.error('获取未读消息失败:', error)
  }
}

const handleRead = (count) => {
  unreadCount.value = count
}

onMounted(() => {
  fetchUnreadCount()
})

// 方法导出到父组件中
defineExpose({
  refresh: fetchUnreadCount
})
</script>

<style lang="scss" scoped>
.notification-container {
  display: inline-block;
  margin-right: 20px;
}

.notification-badge {
  cursor: pointer;
  padding: 5px;
  border-radius: 50%;
  transition: all 0.3s;

  &:hover {
    background-color: var(--el-fill-color-light);
    color: var(--el-color-primary);
  }

  :deep(.el-badge__content) {
    top: 5px;
    right: 5px;
  }
}
</style>