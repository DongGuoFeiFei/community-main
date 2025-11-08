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
          ref="userNotificationRef"
          @read="handleMarkAsRead"
          @delete="handleDelete"
      />
    </div>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import NotificationTypeNav from './components/NotificationTypeNav.vue';
import UserNotification from './components/UserNotification.vue';
import {deleteNotifications, getUnreadCountByType, markAllAsRead, markAsRead} from '@/api/notification.js';
import {ElMessage} from "element-plus";

const activeType = ref('');
const unreadCounts = ref({
  like: 0,
  comment: 0,
  article: 0,
  follow: 0,
  system: 0,
  favorite: 0,
  reply: 0,
  favoriteArticle: 0,
  privateMessages: 0 // 后续添加聊天室
});

const userNotificationRef = ref()
// 获取未读通知数量
const fetchUnreadCounts = async () => {
  try {
    const response = await getUnreadCountByType();
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

// 标记已读处理
const handleMarkAsRead = async (ids) => {
  try {
    if (!ids || (Array.isArray(ids) && ids.length === 0)) {
      throw new Error('请选择要标记的通知');
    }

    await markAsRead({
      ids: Array.isArray(ids) ? ids : [ids],
      type: activeType.value
    });

    await fetchUnreadCounts();
    await userNotificationRef.value.fetchNotifications()
    ElMessage.success(`已成功标记 ${ids.length} 条通知为已读`);
  } catch (error) {
    console.error('标记为已读失败:', error);
    ElMessage.error(`标记已读失败: ${error.message}`);
  }
};

const handleDelete = async (ids) => {
  try {
    if (!ids || (Array.isArray(ids) && ids.length === 0)) {
      throw new Error('请选择要删除的通知');
    }

    await deleteNotifications({
      ids: Array.isArray(ids) ? ids : [ids],
      type: activeType.value
    });

    await fetchUnreadCounts(); // 刷新未读数量
    await userNotificationRef.value.fetchNotifications()
    ElMessage.success(`成功删除 ${Array.isArray(ids) ? ids.length : 1} 条${getTypeLabel(activeType.value)}通知`);
  } catch (error) {
    console.error('删除通知失败:', error);
    ElMessage.error(`删除失败: ${error.message}`);
  }
};

// 辅助函数：获取类型对应的中文标签
const getTypeLabel = (type) => {
  const typeMap = {
    LIKE: '点赞',
    comment: '评论',
    follow: '关注',
    system: '系统',
    favorite: '收藏',
    reply: '回复',
    favoriteArticle: '文章收藏',
    privateMessages: '私信'
  };
  return typeMap[type] || '';
};

const handleMarkAllRead = async () => {
  try {
    await markAllAsRead();
    await fetchUnreadCounts();
    await userNotificationRef.value.fetchNotifications()
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
