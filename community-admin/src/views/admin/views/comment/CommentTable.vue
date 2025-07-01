<template>
  <div class="comment-table">
    <el-table
        :data="data"
        v-loading="loading"
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="content" label="评论内容" min-width="200">
        <template #default="{ row }">
          <div class="comment-content">
            {{ row.content }}
            <div v-if="row.replyContent" class="reply-content">
              <span class="reply-label">回复：</span>{{ row.replyContent }}
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="articleTitle" label="所属文章" width="180" />
      <el-table-column prop="username" label="评论人" width="120" />
      <el-table-column prop="createTime" label="评论时间" width="160" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusTagType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button
              v-if="row.status === 0"
              type="success"
              size="small"
              @click="handleApprove(row.id)"
          >
            通过
          </el-button>
          <el-button
              type="primary"
              size="small"
              @click="handleReply(row.id)"
          >
            回复
          </el-button>
          <el-button
              type="danger"
              size="small"
              @click="handleDelete(row.id)"
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
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

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

const emit = defineEmits(['delete', 'approve', 'reply', 'batch-delete']);

const selectedIds = ref([]);

const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id);
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
  emit('batch-delete', selectedIds.value);
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
</script>

<style lang="scss" scoped>
.comment-table {
  margin-bottom: 20px;

  .comment-content {
    line-height: 1.5;

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
    }
  }

  .batch-actions {
    margin-top: 20px;
    padding: 10px;
    background-color: #f5f7fa;
    border-radius: 4px;
  }
}
</style>