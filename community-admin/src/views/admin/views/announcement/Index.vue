<script setup>
import { ref } from 'vue';
import AnnouncementList from "@/views/admin/views/announcement/components/AnnouncementList.vue";
import AnnouncementForm from "@/views/admin/views/announcement/components/AnnouncementForm.vue";
import AnnouncementDetail from "@/views/admin/views/announcement/components/AnnouncementDetail.vue";
const showForm = ref(false);
const showDetail = ref(false);
const formType = ref('');
const currentId = ref(null);

const handleAdd = () => {
  formType.value = 'add';
  currentId.value = null;
  showForm.value = true;
};

const handleEdit = (id) => {
  formType.value = 'edit';
  currentId.value = id;
  showForm.value = true;
};

const handleView = (id) => {
  currentId.value = id;
  showDetail.value = true;
};

const handleCloseForm = () => {
  showForm.value = false;
};

const handleCloseDetail = () => {
  showDetail.value = false;
};
</script>

<template>
  <div class="announcement-view">
    <el-button type="primary" @click="handleAdd">新增公告</el-button>

    <AnnouncementList
        @edit="handleEdit"
        @view="handleView"
    />

    <AnnouncementForm
        v-if="showForm"
        :type="formType"
        :id="currentId"
        @close="handleCloseForm"
    />

    <AnnouncementDetail
        v-if="showDetail"
        :id="currentId"
        @close="handleCloseDetail"
    />
  </div>
</template>

<style lang="scss" scoped>
.announcement-view {
  padding: 20px;

  .el-button {
    margin-bottom: 20px;
  }
}
</style>