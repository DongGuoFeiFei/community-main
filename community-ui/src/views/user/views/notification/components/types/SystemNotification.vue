<template>
  <div ref="rootStyle"  class="system-notification" :class="{ unread: !notification.isRead }">
    <div class="notification-icon">
      <el-icon :size="24" :color="iconColor">
        <component :is="getNotificationIcon(notification.type)" />
      </el-icon>
    </div>

    <div class="notification-main">
      <div class="notification-content">
        <h4 class="notification-title">{{ notification.title }}</h4>
        <div class="notification-message" v-html="notification.content"></div>
      </div>

      <div class="notification-meta">
        <span class="time">{{ formatTime(notification.createdAt) }}</span>
        <span class="divider">·</span>
        <span class="from">系统消息</span>
      </div>
    </div>

    <div class="notification-actions">
      <el-button
          v-if="!notification.isRead"
          link
          size="small"
          @click.stop="handleRead"
      >
        标记已读
      </el-button>
      <el-button
          link
          size="small"
          @click.stop="handleDelete"
      >
        删除
      </el-button>
      <el-button
          v-if="notification.extraData?.action"
          type="primary"
          size="small"
          @click.stop="handleAction"
      >
        {{ notification.extraData.actionText || '查看详情' }}
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { formatTime } from '@/utils/date';
import {
  Bell,
  InfoFilled,
  WarningFilled,
  CircleCheckFilled,
  CloseBold
} from '@element-plus/icons-vue';
import {computed, onMounted, watch} from 'vue';

const props = defineProps({
  notification: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['read', 'delete', 'action']);

const iconColor = computed(() => {
  switch (props.notification.type) {
    case 'warning':
      return '#E6A23C';
    case 'error':
      return '#F56C6C';
    case 'success':
      return '#67C23A';
    default:
      return '#409EFF';
  }
});

const getNotificationIcon = (type) => {
  switch (type) {
    case 'warning':
      return WarningFilled;
    case 'error':
      return CloseBold;
    case 'success':
      return CircleCheckFilled;
    default:
      return InfoFilled;
  }
};

const handleRead = () => {
  emit('read', props.notification.notificationId);
};

const handleDelete = () => {
  emit('delete', props.notification.notificationId);
};

const handleAction = () => {
//   if (props.notification.extraData?.actionUrl) {
//     window.open(props.notification.extraData.actionUrl, '_blank');
//   }
//   emit('action', props.notification.notificationId);
};

const rootStyle = ref(null);

const setCssVariables = () => {
  if (rootStyle.value) {
    rootStyle.value.style.setProperty('--icon-color', iconColor.value);

    // 将颜色转换为 RGB 格式用于 rgba
    const hexToRgb = (hex) => {
      const r = parseInt(hex.slice(1, 3), 16);
      const g = parseInt(hex.slice(3, 5), 16);
      const b = parseInt(hex.slice(5, 7), 16);
      return `${r}, ${g}, ${b}`;
    };

    rootStyle.value.style.setProperty('--icon-color-rgb', hexToRgb(iconColor.value));
  }
};

onMounted(() => {
  rootStyle.value = document.querySelector('.system-notification');
  setCssVariables();
});

watch(iconColor, () => {
  setCssVariables();
});

</script>

<style lang="scss" scoped>
.system-notification {
  display: flex;
  padding: 16px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  margin-bottom: 12px;
  transition: all 0.2s ease;

  &.unread {
    background-color: #f5f7fa;
    border-left: 3px solid var(--icon-color);
  }

  &:hover {
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    transform: translateY(-1px);
  }

  .notification-icon {
    margin-right: 16px;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 40px;
    height: 40px;
    background-color: rgba(var(--icon-color-rgb), 0.1);
    border-radius: 50%;
  }

  .notification-message {
    :deep(a) {
      color: var(--icon-color);
      text-decoration: none;

      &:hover {
        text-decoration: underline;
      }
    }
  }

  .notification-actions {
    .el-button {
      &[type="primary"] {
        color: white;
        background-color: var(--icon-color);
        border-color: var(--icon-color);
      }

      &[link] {
        color: #999;

        &:hover {
          color: var(--icon-color);
        }
      }
    }
  }
}
</style>
