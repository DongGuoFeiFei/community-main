<template>
  <div class="topic-container">
    <TopicSearch @search="handleSearch" @reset="handleReset" />
    <div class="operation-bar">
      <el-button type="primary" @click="handleCreate">
        <el-icon><Plus /></el-icon>新增话题
      </el-button>
      <el-button type="danger" :disabled="!selectedIds.length" @click="handleBatchDelete">
        <el-icon><Delete /></el-icon>批量删除
      </el-button>
    </div>
    <TopicTable
        :data="topics"
        :loading="loading"
        @edit="handleEdit"
        @delete="handleDelete"
        @selection-change="handleSelectionChange"
    />
    <TopicPagination
        :total="total"
        :page="queryParams.page"
        :limit="queryParams.limit"
        @page-change="handlePageChange"
    />
    <TopicDialog
        v-model="dialogVisible"
        :dialog-title="dialogTitle"
        :topic-data="currentTopic"
        @confirm="handleConfirm"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { Plus, Delete } from '@element-plus/icons-vue';
import { useTopicStore } from '@/stores/useTopicStore.js';
import TopicSearch from './TopicSearch.vue';
import TopicTable from './TopicTable.vue';
import TopicPagination from './TopicPagination.vue';
import TopicDialog from './TopicDialog.vue';

const topicStore = useTopicStore();
const { topics, total, loading, queryParams, fetchTopics } = topicStore;

const dialogVisible = ref(false);
const dialogTitle = ref('');
const currentTopic = ref(null);
const selectedIds = ref([]);

onMounted(() => {
  fetchTopics();
});

const handleSearch = (params) => {
  Object.assign(queryParams, params);
  queryParams.page = 1;
  fetchTopics();
};

const handleReset = () => {
  queryParams.title = '';
  queryParams.status = '';
  fetchTopics();
};

const handlePageChange = (page, limit) => {
  queryParams.page = page;
  queryParams.limit = limit;
  fetchTopics();
};

const handleCreate = () => {
  dialogTitle.value = '新增话题';
  currentTopic.value = null;
  dialogVisible.value = true;
};

const handleEdit = (topic) => {
  dialogTitle.value = '编辑话题';
  currentTopic.value = { ...topic };
  dialogVisible.value = true;
};

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该话题吗？', '提示', {
      type: 'warning'
    });
    await topicStore.removeTopic(id);
    ElMessage.success('删除成功');
  } catch (error) {
    console.log('取消删除');
  }
};

const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定要删除选中的${selectedIds.value.length}个话题吗？`, '提示', {
      type: 'warning'
    });
    await topicStore.removeTopics(selectedIds.value);
    selectedIds.value = [];
    ElMessage.success('批量删除成功');
  } catch (error) {
    console.log('取消删除');
  }
};

const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id);
};

const handleConfirm = async (data) => {
  if (data.id) {
    await topicStore.modifyTopic(data.id, data);
    ElMessage.success('更新成功');
  } else {
    await topicStore.createTopic(data);
    ElMessage.success('创建成功');
  }
  dialogVisible.value = false;
};
</script>

<style lang="scss" scoped>
.topic-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .operation-bar {
    margin-bottom: 20px;
    display: flex;
    justify-content: space-between;
  }
}
</style>