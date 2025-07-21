<script setup>
defineProps({
  title: {
    type: String,
    required: true
  }
})

const emit = defineEmits(['update:title', 'search'])

const handleSearch = () => {
  emit('search')
}

const handleClear = () => {
  emit('update:title', '')
  emit('search')
}
</script>

<template>
  <div class="post-search">
    <el-input
        :model-value="title"
        @update:model-value="val => $emit('update:title', val)"
        placeholder="搜索文章标题"
        prefix-icon="ep:search"
        @keyup.enter="handleSearch"
        clearable
        @clear="handleClear"
        class="search-input"
    />
  </div>
</template>

<style scoped lang="scss">
.post-search {
  margin-bottom: 12px;

  .search-input {
    width: 100%;
    height: 40px;
  }
}
</style>