<script setup>
defineProps({
  currentPage: {
    type: Number,
    required: true
  },
  pageSize: {
    type: Number,
    required: true
  },
  total: {
    type: Number,
    required: true
  },
  loading: {
    type: Boolean,
    required: true
  }
})

const emit = defineEmits(['update:currentPage', 'page-change'])

const handlePageChange = (page) => {
  emit('update:currentPage', page)
  emit('page-change')
}
</script>

<template>
  <div class="post-pagination">
    <el-pagination
        :current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        background
        @current-change="handlePageChange"
        :disabled="loading"
    />
  </div>
</template>

<style scoped lang="scss">
.post-pagination {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}
</style>