<template>
  <div class="notifications-container">
    <div class="header">
      <h2>我的通知</h2>
      <div class="actions">
        <el-button
            type="text"
            :disabled="selectedIds.length === 0"
            @click="handleMarkSelectedAsRead"
        >
          标记已读
        </el-button>
        <el-button
            type="text"
            :disabled="selectedIds.length === 0"
            @click="handleDeleteSelected"
        >
          删除
        </el-button>
        <el-button
            type="text"
            @click="handleMarkAllAsRead"
        >
          全部已读
        </el-button>
      </div>
    </div>

    <div class="filter-section">
      <el-select
          v-model="filterType"
          placeholder="全部类型"
          clearable
          style="width: 150px"
          @change="fetchNotifications"
      >
        <el-option label="点赞" value="like" />
        <el-option label="评论" value="comment" />
        <el-option label="回复" value="reply" />
        <el-option label="关注" value="follow" />
        <el-option label="系统" value="system" />
      </el-select>

      <el-radio-group
          v-model="filterReadStatus"
          style="margin-left: 15px"
          @change="fetchNotifications"
      >
        <el-radio-button label="all">全部</el-radio-button>
        <el-radio-button label="unread">未读</el-radio-button>
        <el-radio-button label="read">已读</el-radio-button>
      </el-radio-group>
    </div>

    <div class="notification-list">
      <el-table
          :data="notifications"
          style="width: 100%"
          v-loading="loading"
          empty-text="暂无通知"
          @selection-change="handleSelectionChange"
          @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="50" />

        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getTagType(row.type)" size="small">
              {{ getTypeText(row.type) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="content" label="内容" min-width="300">
          <template #default="{ row }">
            <div class="notification-content" :class="{ 'unread': !row.is_read }">
              <div class="content-main">
                <span v-html="renderContent(row)"></span>
              </div>
              <div class="content-time">
                {{ formatDateTime(row.created_at) }}
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
                size="small"
                type="text"
                @click.stop="handleMarkAsRead(row.notification_id)"
            >
              标记已读
            </el-button>
            <el-button
                size="small"
                type="text"
                @click.stop="handleDelete(row.notification_id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            v-model:current-page="pagination.current"
            v-model:page-size="pagination.size"
            :page-sizes="[10, 20, 30, 50]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import dayjs from 'dayjs';
import { getNotifications, markAsRead, deleteNotifications } from '@/api/notification';
import { sessionStore } from '@/stores/sessionStores';
import {useRouter} from "vue-router";

const router = useRouter();
const sStore = sessionStore();

// 通知数据
const notifications = ref([]);
const selectedIds = ref([]);
const loading = ref(false);

// 筛选条件
const filterType = ref(null);
const filterReadStatus = ref('all');

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
});

// 格式化日期时间
const formatDateTime = (datetime) => {
  return dayjs(datetime).format('YYYY-MM-DD HH:mm');
};

// 获取通知类型文本
const getTypeText = (type) => {
  const types = {
    like: '点赞',
    comment: '评论',
    reply: '回复',
    follow: '关注',
    system: '系统'
  };
  return types[type] || type;
};

// 获取标签类型
const getTagType = (type) => {
  const types = {
    like: 'success',
    comment: 'primary',
    reply: 'warning',
    follow: 'danger',
    system: 'info'
  };
  return types[type] || '';
};

// 渲染通知内容
const renderContent = (notification) => {
  const { type, son_source_id: sourceId } = notification;
  const userLink = `<a href="/user/${sourceId}" class="user-link">用户${sourceId}</a>`;

  const contents = {
    like: `${userLink} 点赞了你的内容`,
    comment: `${userLink} 评论了你的内容`,
    reply: `${userLink} 回复了你的评论`,
    follow: `${userLink} 关注了你`,
    system: `系统消息: ${notification.content || '请查看详情'}`
  };

  return contents[type] || '新通知';
};

// 获取通知列表
const fetchNotifications = async () => {
  loading.value = true;
  try {
    const params = {
      page: pagination.current,
      size: pagination.size,
      type: filterType.value,
      isRead: filterReadStatus.value === 'all' ? null : filterReadStatus.value === 'read'
    };

    const res = await getNotifications(params);
    if (res.code === 200) {
      notifications.value = res.rows;
      pagination.total = res.total;

      // 更新未读数量
      sStore.updateUnreadCount();
    } else {
      throw new Error(res.msg || '获取通知列表失败');
    }
  } catch (error) {
    ElMessage.error('获取通知列表失败: ' + error.message);
  } finally {
    loading.value = false;
  }
};

// 处理选择变化
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.notification_id);
};

// 处理行点击
const handleRowClick = (row) => {
  // 标记为已读
  if (!row.is_read) {
    handleMarkAsRead(row.notification_id);
  }

  // 根据类型跳转到相应页面
  switch (row.type) {
    case 'like':
    case 'comment':
    case 'reply':
      router.push(`/article/${row.parent_source_id}`);
      break;
    case 'follow':
      router.push(`/user/${row.son_source_id}`);
      break;
    default:
      // 系统消息或其他类型不做跳转
      break;
  }
};

// 标记为已读
const handleMarkAsRead = async (id) => {
  try {
    await markAsRead(id);
    ElMessage.success('标记已读成功');
    await fetchNotifications();
  } catch (error) {
    ElMessage.error('标记已读失败: ' + error.message);
  }
};

// 标记选中为已读
const handleMarkSelectedAsRead = async () => {
  if (selectedIds.value.length === 0) return;

  try {
    await markAsRead(selectedIds.value);
    ElMessage.success(`已成功标记 ${selectedIds.value.length} 条通知为已读`);
    selectedIds.value = [];
    fetchNotifications();
  } catch (error) {
    ElMessage.error('标记已读失败: ' + error.message);
  }
};

// 标记全部为已读
const handleMarkAllAsRead = async () => {
  try {
    await ElMessageBox.confirm('确定要将所有通知标记为已读吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });

    // 这里可以调用API标记所有为已读，或者根据实际情况处理
    await markAsRead('all');
    ElMessage.success('所有通知已标记为已读');
    fetchNotifications();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('标记全部已读失败: ' + error.message);
    }
  }
};

// 删除通知
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这条通知吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });

    await deleteNotifications(id);
    ElMessage.success('删除成功');
    fetchNotifications();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败: ' + error.message);
    }
  }
};

// 删除选中通知
const handleDeleteSelected = async () => {
  if (selectedIds.value.length === 0) return;

  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedIds.value.length} 条通知吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    });

    await deleteNotifications(selectedIds.value);
    ElMessage.success(`已删除 ${selectedIds.value.length} 条通知`);
    selectedIds.value = [];
    fetchNotifications();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败: ' + error.message);
    }
  }
};

// 处理分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size;
  pagination.current = 1;
  fetchNotifications();
};

// 处理页码变化
const handlePageChange = (page) => {
  pagination.current = page;
  fetchNotifications();
};

onMounted(() => {
  fetchNotifications();
});
</script>

<style scoped lang="scss">
.notifications-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;

  .actions {
    display: flex;
    gap: 10px;
  }
}

.filter-section {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.notification-list {
  margin-top: 20px;

  .notification-content {
    padding: 8px 0;

    &.unread {
      font-weight: 500;
    }

    .content-main {
      margin-bottom: 4px;
    }

    .content-time {
      font-size: 12px;
      color: #999;
    }
  }
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.user-link {
  color: var(--el-color-primary);
  text-decoration: none;

  &:hover {
    text-decoration: underline;
  }
}

:deep(.el-table__row) {
  cursor: pointer;

  &:hover {
    background-color: #f5f7fa;
  }
}
</style>