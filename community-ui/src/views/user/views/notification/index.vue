<template>
  <div class="notification-container">
    <!-- 左侧导航栏 -->
    <div class="notification-sidebar">
      <NotificationTypeNav
          :active-type="activeType"
          :unread-counts="unreadCounts"
          @type-change="handleTypeChange"
          @mark-all-read="handleMarkAllRead"
      />
    </div>

    <!-- 右侧内容区域 -->
    <div class="notification-content">
      <UserNotification
          :type="activeType"
          @read="handleMarkAsRead"
          @delete="handleDelete"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import NotificationTypeNav from './components/NotificationTypeNav.vue';
import UserNotification from './components/UserNotification.vue';
import {deleteNotifications, getUnreadCount, markAllAsRead, markAsRead} from '@/api/notification';

const activeType = ref('');
const unreadCounts = ref({});

// 获取未读通知数量
const fetchUnreadCounts = async () => {
  try {
    const response = await getUnreadCount();
    unreadCounts.value = response.data;
  } catch (error) {
    console.error('获取未读通知数量失败:', error);
  }
};

// 初始化加载第一个通知类型
onMounted(() => {
  fetchUnreadCounts();
  activeType.value = 'like'; // 默认显示点赞通知
});

const handleTypeChange = (type) => {
  activeType.value = type;
};

const handleMarkAsRead = async (ids) => {
  try {
    await markAsRead(ids);
    await fetchUnreadCounts(); // 刷新未读数量
  } catch (error) {
    console.error('标记为已读失败:', error);
  }
};

const handleDelete = async (ids) => {
  try {
    await deleteNotifications(ids);
    await fetchUnreadCounts(); // 刷新未读数量
  } catch (error) {
    console.error('删除通知失败:', error);
  }
};

const handleMarkAllRead = async () => {
  try {
    await markAllAsRead();
    await fetchUnreadCounts(); // 刷新未读数量
  } catch (error) {
    console.error('全部标记为已读失败:', error);
  }
};
</script>

<style lang="scss" scoped>
.notification-container {
  display: flex;
  max-width: 1200px;
  margin: 20px auto;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;

  .notification-sidebar {
    width: 240px;
    flex-shrink: 0;
    border-right: 1px solid #e6e6e6;
    background-color: #f8f9fa;
  }

  .notification-content {
    flex-grow: 1;
    padding: 20px;
    min-height: 600px;
  }
}
</style>