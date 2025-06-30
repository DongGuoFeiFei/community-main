<template>
  <div class="notification-panel">
    <div class="panel-header">
      <span>通知中心</span>
      <el-button
        link
          @click="markAllAsRead"
          v-if="notifications.length > 0 && unreadCount > 0"
      >
        全部已读
      </el-button>
    </div>

    <div class="panel-content">
      <el-scrollbar max-height="300px">
        <div v-if="notifications.length === 0" class="empty">
          <el-empty description="暂无通知"/>
        </div>

        <div v-else>
          <div
              v-for="item in notifications"
              :key="item.id"
              class="notification-item"
              :class="{ 'unread': !item.read }"
              @click="handleClick(item)"
          >
            <div class="notification-icon">
              <el-icon :color="getIconColor(item.type)">
                <component :is="getNotificationIcon(item.type)"/>
              </el-icon>
            </div>
            <div class="notification-content">
              <div class="title">{{ item.title }}</div>
              <div class="time">{{ formatTime(item.createTime) }}</div>
            </div>
            <div class="notification-action">
              <el-button
                  type="text"
                  size="small"
                  @click.stop="deleteNotification(item.id)"
              >
                删除
              </el-button>
            </div>
          </div>
        </div>
      </el-scrollbar>
    </div>

    <div class="panel-footer">
      <el-button link @click="viewAll">查看全部</el-button>
    </div>
  </div>
</template>

<script setup>
import {ref} from 'vue'
import {useRouter} from 'vue-router'
import {CircleCheck, InfoFilled, Warning} from '@element-plus/icons-vue'
import {deleteNotifications as deleteNotificationApi, getNotifications, markAsRead} from '../../../community-admin/src/api/notification'

const props = defineProps({
  unreadCount: {
    type: Number,
    default: 0
  }
})

const emit = defineEmits(['read'])

const router = useRouter()
const notifications = ref([])

// 获取通知列表
const fetchNotifications = async () => {
  try {
    const res = await getNotifications({pageSize: 5})
    notifications.value = res.list
  } catch (error) {
    console.error('获取通知列表失败:', error)
  }
}

// 标记所有为已读
const markAllAsRead = async () => {
  const unreadIds = notifications.value
      .filter(item => !item.read)
      .map(item => item.id)

  if (unreadIds.length === 0) return

  try {
    await markAsRead(unreadIds)
    emit('read', props.unreadCount - unreadIds.length)
    fetchNotifications()
  } catch (error) {
    console.error('标记已读失败:', error)
  }
}

// 删除通知
const deleteNotification = async (id) => {
  try {
    await deleteNotificationApi(id)
    const index = notifications.value.findIndex(item => item.id === id)
    if (index !== -1) {
      const wasUnread = !notifications.value[index].read
      notifications.value.splice(index, 1)
      if (wasUnread) {
        emit('read', props.unreadCount - 1)
      }
    }
  } catch (error) {
    console.error('删除通知失败:', error)
  }
}

// 点击通知项
const handleClick = (item) => {
  if (!item.read) {
    markAsRead([item.id]).then(() => {
      emit('read', props.unreadCount - 1)
      item.read = true
    })
  }
  if (item.link) {
    router.push(item.link)
  }
}

// 查看全部
const viewAll = () => {
  router.push('/notifications')
}

// 辅助方法
const getNotificationIcon = (type) => {
  const icons = {
    success: CircleCheck,
    warning: Warning,
    info: InfoFilled
  }
  return icons[type] || InfoFilled
}

const getIconColor = (type) => {
  const colors = {
    success: '#67C23A',
    warning: '#E6A23C',
    info: '#909399'
  }
  return colors[type] || '#909399'
}

const formatTime = (time) => {
  // 这里可以使用dayjs或其他时间格式化工具
  return new Date(time).toLocaleString()
}

// 初始化获取通知
fetchNotifications()
</script>

<style lang="scss" scoped>
.notification-panel {
  .panel-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 15px;
    border-bottom: 1px solid var(--el-border-color-light);

    span {
      font-weight: bold;
    }
  }

  .panel-content {
    .notification-item {
      display: flex;
      padding: 12px 15px;
      cursor: pointer;
      transition: background-color 0.3s;

      &:hover {
        background-color: var(--el-fill-color-light);
      }

      &.unread {
        background-color: var(--el-color-primary-light-9);
      }

      .notification-icon {
        margin-right: 12px;
        display: flex;
        align-items: center;
      }

      .notification-content {
        flex: 1;

        .title {
          margin-bottom: 4px;
          font-size: 14px;
        }

        .time {
          font-size: 12px;
          color: var(--el-text-color-secondary);
        }
      }

      .notification-action {
        display: flex;
        align-items: center;
        margin-left: 10px;
      }
    }
  }

  .panel-footer {
    text-align: center;
    padding: 10px;
    border-top: 1px solid var(--el-border-color-light);
  }
}
</style>