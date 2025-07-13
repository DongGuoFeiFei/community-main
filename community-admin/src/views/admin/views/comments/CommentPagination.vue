<template>
  <div class="comment-pagination">
    <el-pagination
        v-model:current-page="page"
        v-model:page-size="size"
        :page-sizes="[10, 20, 50, 100]"
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
    default: 0
  },
  page: {
    type: Number,
    default: 1
  },
  size: {
    type: Number,
    default: 10
  }
});

const emit = defineEmits(['page-change']);

const page = ref(props.page);
const size = ref(props.size);

watch(() => props.page, (val) => {
  page.value = val;
});

watch(() => props.size, (val) => {
  size.value = val;
});

const handleSizeChange = (val) => {
  size.value = val;
  emit('page-change', 1, val);
};

const handleCurrentChange = (val) => {
  page.value = val;
  emit('page-change', val, size.value);
};
</script>

<style lang="scss" scoped>
.comment-pagination {
  display: flex;
  justify-content: flex-end;
  padding: 20px 0;
}
</style>