<template>
  <div class="pagination-container">
    <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 30, 50]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
    />
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  total: {
    type: Number,
    required: true
  },
  page: {
    type: Number,
    required: true
  },
  limit: {
    type: Number,
    required: true
  }
});

const emit = defineEmits(['page-change']);

const currentPage = ref(props.page);
const pageSize = ref(props.limit);

watch(() => props.page, (val) => {
  currentPage.value = val;
});

watch(() => props.limit, (val) => {
  pageSize.value = val;
});

const handleSizeChange = (val) => {
  emit('page-change', 1, val);
};

const handleCurrentChange = (val) => {
  emit('page-change', val, pageSize.value);
};
</script>

<style lang="scss" scoped>
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>