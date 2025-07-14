<script setup lang="js">
import {onMounted} from 'vue';
import {useAnnouncementStore} from '@/stores/announcement';
import AnnouncementFilter from './AnnouncementFilter.vue';

const store = useAnnouncementStore();
const emit = defineEmits(['edit', 'view']);

onMounted(() => {
  store.fetchAnnouncementList();
});

const handlePageChange = (page) => {
  store.pagination.page = page;
  store.fetchAnnouncementList();
};

const handleSizeChange = (size) => {
  store.pagination.pageSize = size;
  store.fetchAnnouncementList();
};

const handleFilter = (params) => {
  console.log(params)
  store.filterParams = params;
  store.fetchAnnouncementList();
};

const handleStatusChange = async (id, status) => {
  if (status === 1) {
    await store.publishAnnouncement(id);
  } else {
    await store.offlineAnnouncement(id);
  }
};
</script>

<template>
  <div class="announcement-list">
    <AnnouncementFilter @filter="handleFilter"/>

    <el-table
        :data="store.announcementList"
        v-loading="store.loading"
        border
        style="width: 100%"
    >
      <el-table-column prop="id" label="ID" width="80"/>
      <el-table-column prop="priority" label="优先级" width="100"/>
      <el-table-column prop="title" label="标题" min-width="150"/>
      <el-table-column prop="publisher" label="发布人" width="120"/>
      <el-table-column prop="publishTime" label="发布时间" width="180">
        <template #default="{ row }">
          {{ new Date(row.publishTime).toLocaleString() }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="(val) => handleStatusChange(row.id, val)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="publishTime" label="开始时间" width="180">
        <template #default="{ row }">
          {{ new Date(row.startTime).toLocaleString() }}
        </template>
      </el-table-column>
      <el-table-column prop="publishTime" label="结束时间" width="180">
        <template #default="{ row }">
          {{ new Date(row.endTime).toLocaleString() }}
        </template>
      </el-table-column>

      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="emit('view', row.id)">查看</el-button>
          <el-button size="small" type="primary" @click="emit('edit', row.id)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        v-model:current-page="store.pagination.page"
        v-model:page-size="store.pagination.pageSize"
        :total="store.pagination.total"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top: 20px; justify-content: flex-end;"
    />
  </div>
</template>

<style lang="scss" scoped>
.announcement-list {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>