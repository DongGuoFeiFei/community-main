<template>
  <el-table
      :data="data"
      v-loading="loading"
      style="width: 100%"
      @selection-change="$emit('selection-change', $event)"
  >
    <el-table-column type="selection" width="55" />
    <el-table-column prop="id" label="ID" width="80" />
    <el-table-column prop="title" label="话题标题" min-width="150" />
    <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
    <el-table-column prop="cover" label="封面" width="120">
      <template #default="{ row }">
        <el-image
            v-if="row.cover"
            :src="row.cover"
            :preview-src-list="[row.cover]"
            fit="cover"
            style="width: 60px; height: 40px; border-radius: 4px;"
        />
        <span v-else>无封面</span>
      </template>
    </el-table-column>
    <el-table-column prop="status" label="状态" width="100">
      <template #default="{ row }">
        <el-tag :type="statusMap[row.status]?.type">
          {{ statusMap[row.status]?.label }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="viewCount" label="浏览数" width="100" sortable />
    <el-table-column prop="createdAt" label="创建时间" width="180">
      <template #default="{ row }">
        {{ dayjs(row.createdAt).format("YY-MM-DD HH:mm:ss") }}
      </template>
    </el-table-column>
    <el-table-column label="操作" width="180" fixed="right">
      <template #default="{ row }">
        <el-button size="small" @click="$emit('edit', row)">编辑</el-button>
        <el-button size="small" type="danger" @click="$emit('delete', row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup>
import { ElTag } from 'element-plus';
import dayjs from "dayjs";

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

defineEmits(['edit', 'delete', 'selection-change']);

const statusMap = {
  published: { label: '已发布', type: 'success' },
  draft: { label: '草稿', type: 'warning' },
  offline: { label: '已下架', type: 'danger' }
};
</script>