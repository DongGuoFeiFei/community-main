<template>
  <div class="user-notification">
    <div class="notification-header">
      <h3>{{ currentTypeLabel }}通知</h3>
      <div class="header-actions">
        <el-button
            v-if="hasUnread"
            link
            @click="markAllCurrentAsRead"
        >
          全部已读
        </el-button>
      </div>
    </div>

    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="5" animated/>
    </div>

    <div v-else-if="notifications.length === 0" class="empty-container">
      <el-empty description="暂无通知"/>
    </div>

    <div v-else class="notification-list">
      <component
          :is="getNotificationComponent(currentType)"
          v-for="notification in notifications"
          :key="notification.notificationId"
          :notification="notification"
          @read="$emit('read', notification.notificationId)"
          @delete="$emit('delete', notification.notificationId)"
      />

      <div class="pagination-container">
        <el-pagination
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :total="total"
            layout="prev, pager, next"
            @current-change="fetchNotifications"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, ref, watch} from 'vue';
import {getNotifications} from '@/api/notification.js';
import LikeNotification from './types/LikeNotification.vue';
import CommentNotification from './types/CommentNotification.vue';

const props = defineProps({
  type: {
    type: String,
    required: true
  }
});

const emit = defineEmits(['read', 'delete']);

const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);
const loading = ref(false);
const notifications = ref([]);

const notificationComponents = {
  like: LikeNotification,
  comment: CommentNotification,
  // article: ArticleNotification,
  // follow: FollowNotification,
  // system: SystemNotification,
  // favorite: FavoriteNotification,
  // reply: ReplyNotification,
  // favoriteArticle: FavoriteArticleNotification
};

const currentType = computed(() => props.type);
const currentTypeLabel = computed(() => {
  const type = notificationTypes.find(t => t.value === currentType.value);
  return type ? type.label : '';
});

const hasUnread = computed(() => {
  return notifications.value.some(n => !n.isRead);
});

const notificationTypes = [
  {value: 'like', label: '点赞'},
  {value: 'comment', label: '评论'},
  {value: 'article', label: '文章回复'},
  {value: 'follow', label: '关注'},
  {value: 'system', label: '系统'},
  {value: 'favorite', label: '收藏'},
  {value: 'reply', label: '回复'},
  {value: 'favoriteArticle', label: '收藏文章'}
];

const getNotificationComponent = (type) => {
  return notificationComponents[type] || SystemNotification;
};

const fetchNotifications = async () => {
  notifications.value = []
  try {
    loading.value = true;
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      type: currentType.value
    };

    const response = await getNotifications(params);
    notifications.value = response.data.rows;
    console.log(notifications.value)
    total.value = response.data.total;
  } catch (error) {
    console.error('获取通知列表失败:', error);
  } finally {
    loading.value = false;
  }
};

const markAllCurrentAsRead = () => {
  const unreadIds = notifications.value
      .filter(n => !n.isRead)
      .map(n => n.notificationId);
  if (unreadIds.length > 0) {
    emit('read', unreadIds);
  }
};

watch(currentType, () => {
  currentPage.value = 1;
  fetchNotifications();
}, {immediate: true});


defineExpose({
  fetchNotifications
});
</script>

<style lang="scss" scoped>
.user-notification {
  .notification-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding-bottom: 10px;
    border-bottom: 1px solid #e6e6e6;

    h3 {
      margin: 0;
      font-size: 18px;
      color: #333;
    }
  }

  .loading-container {
    padding: 20px 0;
  }

  .empty-container {
    padding: 60px 0;
  }

  .notification-list {
    .notification-item {
      margin-bottom: 16px;
    }
  }

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: center;
  }
}
</style>