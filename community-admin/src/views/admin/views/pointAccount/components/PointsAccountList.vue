<template>
  <div class="points-account-list">
    <el-card shadow="never">
      <template #header>
        <div class="flex justify-between items-center">
          <span>积分账户管理</span>
          <div>
            <el-input
              v-model="searchParams.keyword"
              placeholder="搜索用户ID/昵称"
              style="width: 240px"
              clearable
              @clear="handleSearch"
              @keyup.enter="handleSearch"
            >
              <template #append>
                <el-button :icon="Search" @click="handleSearch" />
              </template>
            </el-input>
          </div>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="accountList"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column prop="accountId" label="账户ID" width="100" />
        <el-table-column prop="userId" label="用户ID" width="100" />
        <el-table-column label="用户信息" min-width="180">
          <template #default="{ row }">
            <div class="flex items-center">
              <el-avatar :size="40" :src="row.user.avatar" />
              <div class="ml-2">
                <div>{{ row.user.nickname }}</div>
                <div class="text-gray-500 text-xs">{{ row.user.username }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="availablePoints" label="可用积分" width="120">
          <template #default="{ row }">
            <el-tag type="success">{{ row.availablePoints }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="frozenPoints" label="冻结积分" width="120">
          <template #default="{ row }">
            <el-tag type="warning">{{ row.frozenPoints }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="levelId" label="等级" width="120">
          <template #default="{ row }">
            <el-tag>{{ row.level.levelName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastUpdateTime" label="最后更新时间" width="180" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button
              size="small"
              type="primary"
              @click="handleDetail(row.accountId)"
            >
              详情
            </el-button>
            <el-button
              size="small"
              type="warning"
              @click="handleAdjust(row.accountId)"
            >
              调整
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="mt-4 flex justify-end">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <PointsAccountDetail
      v-if="showDetail"
      :account-id="currentAccountId"
      @close="showDetail = false"
    />
    <PointsAdjustDialog
      v-if="showAdjustDialog"
      :account-id="currentAccountId"
      @close="showAdjustDialog = false"
      @success="handleAdjustSuccess"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { Search } from '@element-plus/icons-vue';
import { getPointsAccounts } from '@/api/points';
import PointsAccountDetail from './PointsAccountDetail.vue';
import PointsAdjustDialog from './PointsAdjustDialog.vue';

const loading = ref(false);
const accountList = ref([]);
const showDetail = ref(false);
const showAdjustDialog = ref(false);
const currentAccountId = ref(null);

const searchParams = reactive({
  keyword: '',
});

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0,
});

// 获取积分账户列表
const fetchPointsAccounts = async () => {
  try {
    loading.value = true;
    const params = {
      ...searchParams,
      page: pagination.current,
      size: pagination.size,
    };
    const res = await getPointsAccounts(params);
    accountList.value = res.data.records;
    pagination.total = res.data.total;
  } catch (error) {
    console.error('获取积分账户列表失败:', error);
  } finally {
    loading.value = false;
  }
};

// 搜索
const handleSearch = () => {
  pagination.current = 1;
  fetchPointsAccounts();
};

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size;
  fetchPointsAccounts();
};

// 当前页变化
const handleCurrentChange = (current) => {
  pagination.current = current;
  fetchPointsAccounts();
};

// 查看详情
const handleDetail = (accountId) => {
  currentAccountId.value = accountId;
  showDetail.value = true;
};

// 调整积分
const handleAdjust = (accountId) => {
  currentAccountId.value = accountId;
  showAdjustDialog.value = true;
};

// 调整成功回调
const handleAdjustSuccess = () => {
  fetchPointsAccounts();
};

onMounted(() => {
  fetchPointsAccounts();
});
</script>

<style lang="scss" scoped>
.points-account-list {
  .el-table {
    margin-top: 20px;
  }
}
</style>
