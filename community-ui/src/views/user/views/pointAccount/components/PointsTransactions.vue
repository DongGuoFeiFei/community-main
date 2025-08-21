<template>
  <el-card class="points-transactions">
    <template #header>
      <div class="card-header">
        <span>积分明细</span>
        <div class="header-actions">
          <el-input
              v-model="searchQuery"
              placeholder="搜索交易备注"
              style="width: 200px"
              clearable
              @clear="handleSearch"
              @keyup.enter="handleSearch"
          />
          <el-button type="primary" @click="handleSearch">搜索</el-button>
        </div>
      </div>
    </template>

    <el-table :data="transactions" style="width: 100%" v-loading="loading">
      <el-table-column prop="transactionId" label="交易ID" width="100"/>
      <el-table-column prop="points" label="积分变化" width="120">
        <template #default="{ row }">
          <span :class="row.points > 0 ? 'positive' : 'negative'">
            {{ row.points > 0 ? '+' : '' }}{{ row.points }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="type" label="类型" width="120">
        <template #default="{ row }">
          <el-tag :type="getTagType(row.type)">
            {{ transactionTypes[row.type] || row.type }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述"/>
      <el-table-column prop="createTime" label="时间" width="180">
        <template #default="{ row }">
          {{ formatTime(row.createTime) }}
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
      />
    </div>
  </el-card>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import {getPointsTransactions} from '@/api/points';
import dayjs from 'dayjs';

const props = defineProps({
  userId: {
    type: Number,
    required: true
  }
});

const transactions = ref([]);
const loading = ref(false);
const searchQuery = ref('');

const pagination = ref({
  current: 1,
  size: 10,
  total: 0
});

const transactionTypes = {
  'SIGN_IN': '签到',
  'POST': '发帖',
  'COMMENT': '评论',
  'LIKE': '点赞',
  'CONSUME': '消费',
  'ADMIN_ADJUST': '管理员调整',
  'OTHER': '其他'
};

const loadTransactions = async () => {
  try {
    loading.value = true;
    const params = {
      page: pagination.value.current,
      size: pagination.value.size,
      name: searchQuery.value
    };

    const response = await getPointsTransactions(props.userId, params);
    transactions.value = response.data.records;
    pagination.value.total = response.data.total;
  } catch (error) {
    console.error('Failed to load transactions:', error);
  } finally {
    loading.value = false;
  }
};

const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm:ss');
};

const getTagType = (type) => {
  const positiveTypes = ['SIGN_IN', 'POST', 'COMMENT', 'LIKE', 'ADMIN_ADJUST'];
  return positiveTypes.includes(type) ? 'success' : 'danger';
};

const handleSearch = () => {
  pagination.value.current = 1;
  loadTransactions();
};

const handlePageChange = () => {
  loadTransactions();
};

const handleSizeChange = (size) => {
  pagination.value.size = size;
  loadTransactions();
};

onMounted(() => {
  loadTransactions();
});
</script>

<style scoped lang="scss">
.points-transactions {
  margin-bottom: 20px;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: bold;
    font-size: 16px;

    .header-actions {
      display: flex;
      gap: 10px;
    }
  }

  .positive {
    color: #67c23a;
  }

  .negative {
    color: #f56c6c;
  }

  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
