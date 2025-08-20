<template>
  <div class="points-log-list">
    <el-card shadow="never">
      <template #header>
        <div class="flex justify-between items-center">
          <span>积分记录</span>
          <div class="flex">
            <el-select
              v-model="searchParams.operationType"
              placeholder="操作类型"
              clearable
              style="width: 120px; margin-right: 10px"
              @change="handleSearch"
            >
              <el-option
                v-for="item in operationTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              @change="handleDateChange"
            />
          </div>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="logList"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column prop="logId" label="记录ID" width="100"/>
        <el-table-column prop="ruleName" label="规则名称" width="180"/>
        <el-table-column label="积分变动" width="120">
          <template #default="{ row }">
            <el-tag :type="row.changePoints > 0 ? 'success' : 'danger'">
              {{ row.changePoints > 0 ? '+' : '' }}{{ row.changePoints }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="balance" label="变动后余额" width="120"/>
        <el-table-column label="操作类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getOperationTypeTag(row.operationType)">
              {{ getOperationTypeName(row.operationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sourceType" label="来源类型" width="120"/>
        <el-table-column prop="description" label="描述" min-width="180"/>
        <el-table-column prop="createTime" label="创建时间" width="180"/>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '有效' : '已撤销' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.status === 1 && row.operationType === 1"
              size="small"
              type="danger"
              @click="handleRevoke(row.logId)"
            >
              撤销
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
  </div>
</template>

<script setup>
import {reactive, ref} from 'vue';
import {getPointsLogs, revokePointsLog} from '@/api/points';

const props = defineProps({
  accountId: {
    type: Number,
    required: true,
  },
});

const loading = ref(false);
const logList = ref([]);
const dateRange = ref([]);

const operationTypes = [
  {value: 1, label: '获取积分'},
  {value: 2, label: '消费积分'},
  {value: 3, label: '冻结积分'},
  {value: 4, label: '解冻积分'},
  {value: 5, label: '调整积分'},
];

const searchParams = reactive({
  operationType: null,
  startTime: null,
  endTime: null,
});

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0,
});

// 获取积分记录
const fetchPointsLogs = async () => {
  try {
    loading.value = true;
    const params = {
      ...searchParams,
      accountId: props.accountId,
      page: pagination.current,
      size: pagination.size,
    };
    const res = await getPointsLogs(params);
    logList.value = res.data.records;
    pagination.total = res.data.total;
  } catch (error) {
    console.error('获取积分记录失败:', error);
  } finally {
    loading.value = false;
  }
};

// 搜索
const handleSearch = () => {
  pagination.current = 1;
  fetchPointsLogs();
};

// 日期变化
const handleDateChange = (dates) => {
  if (dates && dates.length === 2) {
    searchParams.startTime = dates[0].toISOString();
    searchParams.endTime = dates[1].toISOString();
  } else {
    searchParams.startTime = null;
    searchParams.endTime = null;
  }
  handleSearch();
};

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size;
  fetchPointsLogs();
};

// 当前页变化
const handleCurrentChange = (current) => {
  pagination.current = current;
  fetchPointsLogs();
};

// 撤销记录
const handleRevoke = async (logId) => {
  try {
    await revokePointsLog(logId);
    ElMessage.success('撤销成功');
    fetchPointsLogs();
  } catch (error) {
    console.error('撤销积分记录失败:', error);
    ElMessage.error('撤销失败');
  }
};

// 获取操作类型名称
const getOperationTypeName = (type) => {
  const item = operationTypes.find((item) => item.value === type);
  return item ? item.label : '未知';
};

// 获取操作类型标签样式
const getOperationTypeTag = (type) => {
  const map = {
    1: 'success',
    2: 'danger',
    3: 'warning',
    4: 'info',
    5: '',
  };
  return map[type] || '';
};

// 初始化加载
fetchPointsLogs();
</script>

<style lang="scss" scoped>
.points-log-list {
  .el-table {
    margin-top: 20px;
  }
}
</style>
