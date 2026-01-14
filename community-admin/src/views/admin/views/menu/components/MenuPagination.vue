<template>
  <div class="menu-pagination-container">
    <el-pagination
      v-model:current-page="pagination.currentPage"
      v-model:page-size="pagination.pageSize"
      :page-sizes="[10, 20, 50, 100]"
      :total="pagination.total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  pagination: {
    type: Object,
    required: true,
    default: () => ({
      currentPage: 1,
      pageSize: 10,
      total: 0
    })
  }
})

const emit = defineEmits(['page-change'])

const handleSizeChange = (val) => {
  emit('page-change', {
    currentPage: 1,
    pageSize: val
  })
}

const handleCurrentChange = (val) => {
  emit('page-change', {
    currentPage: val,
    pageSize: props.pagination.pageSize
  })
}
</script>

<style lang="scss" scoped>
.menu-pagination-container {
  padding: 20px 0;
  display: flex;
  justify-content: flex-end;
  background-color: #fff;
  margin-top: 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}
</style>
