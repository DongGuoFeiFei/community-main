<template>
  <div class="points-consumption-list">
    <el-card shadow="never">
      <template #header>
        <div class="flex justify-between items-center">
          <span>积分消费记录</span>
          <div class="flex">
            <el-select
              v-model="searchParams.status"
              placeholder="状态筛选"
              clearable
              style="width: 120px; margin-right: 10px"
              @change="handleSearch"
            >
              <el-option
                v-for="item in statusOptions"
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
        :data="consumptionList"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column prop="consumptionId" label="消费ID" width="100"/>
        <el-table-column label="消费项目" min-width="180">
          <template #default="{ row }">
            <div class="flex items-center">
              <el-avatar
                v-if="row.itemImage"
                :size="40"
                :src="row.itemImage"
                shape="square"
              />
              <div class="ml-2">
                <div>{{ row.itemName }}</div>
                <div class="text-gray-500 text-xs">{{ row.itemType }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="pointsUsed" label="消耗积分" width="120">
          <template #default="{ row }">
            <el-tag type="danger">-{{ row.pointsUsed }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusTag(row.status)">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"/>
        <el-table-column prop="completeTime" label="完成时间" width="180"/>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.status === 0"
              size="small"
              type="danger"
              @click="handleCancel(row.consumptionId)"
            >
              取消
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
import {cancelPointsConsumption, getPointsConsumptions} from '@/api/points';

const props = defineProps({
  accountId: {
    type: Number,
    required: true,
  },
});

const loading = ref(false);
const consumptionList = ref([]);
const dateRange = ref([]);

const statusOptions = [
  {value: 0, label: '处理中'},
  {value: 1, label: '已完成'},
  {value: 2, label: '已取消'},
];

const searchParams = reactive({
  status: null,
  startTime: null,
  endTime: null,
});

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0,
});

// 获取消费记录
const fetchPointsConsumptions = async () => {
  try {
    loading.value = true;
    const params = {
      ...searchParams,
      accountId: props.accountId,
      page: pagination.current,
      size: pagination.size,
    };
    const res = await getPointsConsumptions(params);
    consumptionList.value = res.data.records;
    pagination.total = res.data.total;
  } catch (error) {
    console.error('获取积分消费记录失败:', error);
  } finally {
    loading.value = false;
  }
};

// 搜索
const handleSearch = () => {
  pagination.current = 1;
  fetchPointsConsumptions();
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
  fetchPointsConsumptions();
};

// 当前页变化
const handleCurrentChange = (current) => {
  pagination.current = current;
  fetchPointsConsumptions();
};

// 取消消费
const handleCancel = async (consumptionId) => {
  try {
    await cancelPointsConsumption(consumptionId);
    ElMessage.success('取消成功');
    fetchPointsConsumptions();
  } catch (error) {
    console.error('取消积分消费失败:', error);
    ElMessage.error('取消失败');
  }
};

// 获取状态名称
const getStatusName = (status) => {
  const item = statusOptions.find((item) => item.value === status);
  return item ? item.label : '未知';
};

// 获取状态标签样式
const getStatusTag = (status) => {
  const map = {
    0: 'warning',
    1: 'success',
    2: 'danger',
  };
  return map[status] || '';
};

// 初始化加载
fetchPointsConsumptions();
</script>

<style lang="scss" scoped>
.points-consumption-list {
  .el-table {
    margin-top: 20px;
  }
}
</style>
