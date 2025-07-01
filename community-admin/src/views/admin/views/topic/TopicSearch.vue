<template>
  <el-form :model="form" inline>
    <el-form-item label="话题标题">
      <el-input v-model="form.title" placeholder="请输入话题标题" clearable />
    </el-form-item>
    <el-form-item label="状态">
      <el-select v-model="form.status" placeholder="请选择状态" clearable>
        <el-option label="已发布" value="published" />
        <el-option label="草稿" value="draft" />
        <el-option label="已下架" value="offline" />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button @click="handleReset">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { ref } from 'vue';

const form = ref({
  title: '',
  status: ''
});

const emit = defineEmits(['search', 'reset']);

const handleSearch = () => {
  emit('search', form.value);
};

const handleReset = () => {
  form.value = {
    title: '',
    status: ''
  };
  emit('reset');
};
</script>