<template>
  <el-table :data="data" v-loading="loading" border>
    <el-table-column prop="title" label="活动名称" width="180" />
    <el-table-column prop="startTime" label="开始时间" width="180">
      <template #default="{ row }">
        {{ formatDate(row.startTime) }}
      </template>
    </el-table-column>
    <el-table-column prop="endTime" label="结束时间" width="180">
      <template #default="{ row }">
        {{ formatDate(row.endTime) }}
      </template>
    </el-table-column>
    <el-table-column prop="location" label="活动地点" width="180" />
    <el-table-column prop="currentParticipants" label="参与人数" width="100" />
    <el-table-column prop="maxParticipants" label="最大人数" width="100" />
    <el-table-column prop="status" label="状态" width="100">
      <template #default="{ row }">
        <el-tag :type="row.status === 1 ? 'success' : 'danger'">
          {{ row.status === 1 ? '进行中' : '已结束' }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column label="操作" width="220" fixed="right">
      <template #default="{ row }">
        <el-button size="small" @click="$emit('detail', row)">详情</el-button>
        <el-button size="small" type="primary" @click="$emit('edit', row)">
          编辑
        </el-button>
        <el-button
            size="small"
            :type="row.status === 1 ? 'danger' : 'success'"
            @click="$emit('status-change', row.id, row.status === 1 ? 0 : 1)"
        >
          {{ row.status === 1 ? '结束' : '开启' }}
        </el-button>
        <el-button
            size="small"
            type="danger"
            @click="$emit('delete', row.id)"
        >
          删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup>

import dayjs from 'dayjs';

defineProps({
  data: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  }
});

defineEmits(['edit', 'detail', 'delete', 'status-change']);

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm');
};
</script>

<style lang="scss" scoped>
.el-table {
  width: 100%;

  :deep(.el-table__cell) {
    padding: 12px 0;
  }
}
</style>