<template>
  <div class="comment-management">
    <CommentSearch @search="handleSearch" />
    <CommentTable
        :data="commentList"
        :loading="loading"
        @delete="handleDelete"
        @approve="handleApprove"
        @reply="handleReply"
        @batch-delete="handleBatchDelete"
    />
    <CommentPagination
        :total="total"
        :page="page"
        :size="size"
        @page-change="handlePageChange"
    />
    <CommentReplyDialog
        v-model="replyDialogVisible"
        :comment-id="currentCommentId"
        @submit="handleReplySubmit"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getCommentList, deleteComment, approveComment, replyComment, batchDeleteComments } from '@/api/comment';
import CommentSearch from './CommentSearch.vue';
import CommentTable from './CommentTable.vue';
import CommentPagination from './CommentPagination.vue';
import CommentReplyDialog from './CommentReplyDialog.vue';
import { ElMessage, ElMessageBox } from 'element-plus';

const loading = ref(false);
const commentList = ref([]);
const total = ref(0);
const page = ref(1);
const size = ref(10);
const searchParams = ref({});
const replyDialogVisible = ref(false);
const currentCommentId = ref(null);

// 获取评论列表
const fetchComments = async () => {
  try {
    loading.value = true;
    const params = {
      page: page.value,
      size: size.value,
      ...searchParams.value
    };
    const res = await getCommentList(params);
    commentList.value = res.data.list;
    total.value = res.data.total;
  } catch (error) {
    ElMessage.error(error.message || '获取评论列表失败');
  } finally {
    loading.value = false;
  }
};

// 搜索处理
const handleSearch = (params) => {
  searchParams.value = params;
  page.value = 1;
  fetchComments();
};

// 分页处理
const handlePageChange = (newPage, newSize) => {
  page.value = newPage;
  size.value = newSize;
  fetchComments();
};

// 删除评论
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这条评论吗？', '提示', {
      type: 'warning'
    });
    await deleteComment(id);
    ElMessage.success('删除成功');
    fetchComments();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败');
    }
  }
};

// 批量删除
const handleBatchDelete = async (ids) => {
  try {
    await ElMessageBox.confirm(`确定要删除选中的${ids.length}条评论吗？`, '提示', {
      type: 'warning'
    });
    await batchDeleteComments(ids);
    ElMessage.success('批量删除成功');
    fetchComments();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '批量删除失败');
    }
  }
};

// 审核评论
const handleApprove = async (id) => {
  try {
    await approveComment(id);
    ElMessage.success('审核通过');
    fetchComments();
  } catch (error) {
    ElMessage.error(error.message || '审核失败');
  }
};

// 打开回复对话框
const handleReply = (id) => {
  currentCommentId.value = id;
  replyDialogVisible.value = true;
};

// 提交回复
const handleReplySubmit = async (content) => {
  try {
    await replyComment(currentCommentId.value, content);
    ElMessage.success('回复成功');
    replyDialogVisible.value = false;
    fetchComments();
  } catch (error) {
    ElMessage.error(error.message || '回复失败');
  }
};

onMounted(() => {
  fetchComments();
});
</script>

<style lang="scss" scoped>
.comment-management {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}
</style>