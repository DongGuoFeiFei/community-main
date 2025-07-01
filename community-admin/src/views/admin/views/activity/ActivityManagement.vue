<template>
  <div class="activity-management">
    <el-card shadow="never">
      <div class="header">
        <el-button type="primary" @click="handleCreate">新增活动</el-button>
        <el-input
            v-model="searchQuery"
            placeholder="搜索活动名称"
            style="width: 300px"
            clearable
            @clear="handleSearch"
            @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch">
              <el-icon><Search /></el-icon>
            </el-button>
          </template>
        </el-input>
      </div>

      <ActivityTable
          :data="activityList"
          :loading="loading"
          @edit="handleEdit"
          @detail="handleDetail"
          @delete="handleDelete"
          @status-change="handleStatusChange"
      />

      <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />
    </el-card>

    <ActivityFormDialog
        v-model="dialogVisible"
        :form-data="currentActivity"
        :mode="dialogMode"
        @submit="handleSubmit"
    />

    <ActivityDetailDialog
        v-model="detailDialogVisible"
        :activity="currentActivity"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { Search } from '@element-plus/icons-vue';
import {
  getActivityList,
  deleteActivity,
  changeActivityStatus
} from '@/api/activity';
import ActivityTable from './components/ActivityTable.vue';
import ActivityFormDialog from './components/ActivityFormDialog.vue';
import ActivityDetailDialog from './components/ActivityDetailDialog.vue';

const activityList = ref([]);
const loading = ref(false);
const searchQuery = ref('');
const dialogVisible = ref(false);
const detailDialogVisible = ref(false);
const dialogMode = ref('create');
const currentActivity = ref({});

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
});

const fetchActivityList = async () => {
  try {
    loading.value = true;
    const params = {
      page: pagination.current,
      size: pagination.size,
      keyword: searchQuery.value
    };
    const res = await getActivityList(params);
    activityList.value = res.data.records;
    pagination.total = res.data.total;
  } finally {
    loading.value = false;
  }
};

const handleSearch = () => {
  pagination.current = 1;
  fetchActivityList();
};

const handleSizeChange = (size) => {
  pagination.size = size;
  fetchActivityList();
};

const handleCurrentChange = (page) => {
  pagination.current = page;
  fetchActivityList();
};

const handleCreate = () => {
  dialogMode.value = 'create';
  currentActivity.value = {
    title: '',
    content: '',
    startTime: '',
    endTime: '',
    location: '',
    maxParticipants: 100,
    coverImage: '',
    status: 1
  };
  dialogVisible.value = true;
};

const handleEdit = (activity) => {
  dialogMode.value = 'edit';
  currentActivity.value = { ...activity };
  dialogVisible.value = true;
};

const handleDetail = (activity) => {
  currentActivity.value = activity;
  detailDialogVisible.value = true;
};

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该活动吗？', '提示', {
      type: 'warning'
    });
    await deleteActivity(id);
    ElMessage.success('删除成功');
    fetchActivityList();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败');
    }
  }
};

const handleStatusChange = async (id, status) => {
  try {
    await changeActivityStatus(id, status);
    ElMessage.success('状态更新成功');
    fetchActivityList();
  } catch (error) {
    ElMessage.error('状态更新失败');
  }
};

const handleSubmit = () => {
  dialogVisible.value = false;
  fetchActivityList();
};

onMounted(() => {
  fetchActivityList();
});
</script>

<style lang="scss" scoped>
.activity-management {
  padding: 20px;

  .header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
  }

  .el-pagination {
    margin-top: 20px;
    justify-content: flex-end;
  }
}
</style>