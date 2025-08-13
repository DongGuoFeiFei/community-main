<template>
  <div class="comment-table">
    <el-table
      :data="safeData"
      v-loading="loading"
      border
      style="width: 100%"
      @selection-change="handleSelectionChange"
      :row-key="row => row.commentId"
      :fit="true"
    >
<!--      <el-table-column type="selection" width="55"/>-->
      <el-table-column prop="commentId" label="ID" width="80"/>
      <el-table-column prop="content" label="评论内容" min-width="200">
        <template #default="{ row }">
          <div class="comment-content">
            <div class="content-text" v-html="sanitizeContent(row.content)"></div>
            <div v-if="row.parentComment" class="reply-content">
              <span class="reply-label">回复：</span>
              <span class="reply-user">@{{ getSafeValue(row.parentComment.user?.nickname, '已删除用户') }}</span>
              <div class="reply-text" v-html="sanitizeContent(row.parentComment.content)"></div>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="所属文章" width="180">
        <template #default="{ row }">
          <el-tooltip :content="getSafeValue(row.article?.title, '未知文章')" placement="top">
            <span class="article-title">{{ truncate(getSafeValue(row.article?.title, '未知文章'), 15) }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="评论人" width="120">
        <template #default="{ row }">
          <div class="user-info">
            <el-avatar
              :size="24"
              :src="row.user?.avatar ? env.apiBaseUrl + row.user.avatar : defaultAvatar"
            />
            <span class="nickname">{{ getSafeValue(row.user?.nickname, '已删除用户') }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="评论时间" width="160">
        <template #default="{ row }">
          {{ formatTime(row.createdAt) }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusTagType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="230" fixed="right">
        <template #default="{ row }">
          <el-button
            v-if="row.status === 0"
            type="success"
            size="small"
            @click="handleApprove(row.commentId)"
          >
            通过
          </el-button>
          <el-button
            type="primary"
            size="small"
            @click="handleReply(row.commentId)"
          >
            回复
          </el-button>
          <el-button
            type="danger"
            size="small"
            @click="handleDelete(row.commentId)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="batch-actions" v-if="selectedIds.length > 0">
      <el-button type="danger" size="small" @click="handleBatchDelete">
        批量删除 ({{ selectedIds.length }})
      </el-button>
      <el-button
        v-if="showBatchApprove"
        type="success"
        size="small"
        @click="handleBatchApprove"
      >
        批量通过 ({{ selectedIds.length }})
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue';
import { ElMessage } from 'element-plus';
import dayjs from 'dayjs';
import DOMPurify from 'dompurify';
import env from "@/env.js";

// 默认头像路径
const defaultAvatar = '/default-avatar.png';

const props = defineProps({
  data: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['delete', 'approve', 'reply', 'batch-delete', 'batch-approve']);

const selectedIds = ref([]);

// 安全数据处理
const safeData = computed(() => {
  return props.data.map(item => ({
    commentId: item.commentId || 0,
    content: item.content || '',
    status: item.status ?? 0,
    createdAt: item.createdAt || new Date(),
    article: item.article ? {
      articleId: item.article.articleId || 0,
      title: item.article.title || '未知文章'
    } : { articleId: 0, title: '未知文章' },
    user: item.user ? {
      userId: item.user.userId || 0,
      username: item.user.username || 'unknown',
      nickname: item.user.nickname || '已删除用户',
      avatar: item.user.avatar || ''
    } : { userId: 0, username: 'unknown', nickname: '已删除用户', avatar: '' },
    parentComment: item.parentComment ? {
      commentId: item.parentComment.commentId || 0,
      content: item.parentComment.content || '',
      user: item.parentComment.user ? {
        userId: item.parentComment.user.userId || 0,
        username: item.parentComment.user.username || 'unknown',
        nickname: item.parentComment.user.nickname || '已删除用户',
        avatar: item.parentComment.user.avatar || ''
      } : { userId: 0, username: 'unknown', nickname: '已删除用户', avatar: '' }
    } : null
  }));
});

const showBatchApprove = computed(() => {
  return safeData.value.some(item => selectedIds.value.includes(item.commentId) && item.status === 0);
});

const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.commentId);
};

const handleDelete = (id) => {
  emit('delete', id);
};

const handleApprove = (id) => {
  emit('approve', id);
};

const handleReply = (id) => {
  emit('reply', id);
};

const handleBatchDelete = () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请至少选择一条评论');
    return;
  }
  emit('batch-delete', selectedIds.value);
};

const handleBatchApprove = () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请至少选择一条评论');
    return;
  }
  emit('batch-approve', selectedIds.value);
};

const getStatusText = (status) => {
  const statusMap = {
    0: '待审核',
    1: '已通过',
    2: '已拒绝'
  };
  return statusMap[status] || '未知';
};

const getStatusTagType = (status) => {
  const typeMap = {
    0: 'warning',
    1: 'success',
    2: 'danger'
  };
  return typeMap[status] || '';
};

const formatTime = (time) => {
  try {
    return dayjs(time).isValid() ? dayjs(time).format('YYYY-MM-DD HH:mm') : '未知时间';
  } catch {
    return '未知时间';
  }
};

const truncate = (str, length) => {
  if (!str) return '';
  return str.length > length ? str.substring(0, length) + '...' : str;
};

const sanitizeContent = (content) => {
  if (!content) return '';
  return DOMPurify.sanitize(content);
};

const getSafeValue = (value, defaultValue = '') => {
  return value ?? defaultValue;
};
</script>

<style lang="scss" scoped>
.comment-table {
  margin-bottom: 20px;

  .comment-content {
    line-height: 1.5;
    word-break: break-word;

    .content-text {
      :deep(p) {
        margin: 0;
      }
    }

    .reply-content {
      margin-top: 8px;
      padding: 8px;
      background-color: #f5f7fa;
      border-radius: 4px;
      color: #666;

      .reply-label {
        font-weight: bold;
        color: #409eff;
      }

      .reply-user {
        color: #409eff;
        margin: 0 4px;
      }

      .reply-text {
        margin-top: 4px;
        color: #666;
      }
    }
  }

  .user-info {
    display: flex;
    align-items: center;
    gap: 8px;

    .nickname {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }

  .article-title {
    cursor: pointer;
    color: var(--el-color-primary);

    &:hover {
      text-decoration: underline;
    }
  }

  .batch-actions {
    margin-top: 20px;
    padding: 10px;
    background-color: #f5f7fa;
    border-radius: 4px;
    display: flex;
    gap: 12px;
  }
}
</style>
