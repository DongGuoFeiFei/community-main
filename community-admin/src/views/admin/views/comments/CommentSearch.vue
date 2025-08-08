<template>
  <div class="comment-search">
    <el-form :inline="true" :model="form" class="search-form">
      <el-form-item label="评论内容">
        <el-input v-model="form.content" placeholder="请输入评论内容" clearable />
      </el-form-item>
      <el-form-item label="文章标题">
        <el-input v-model="form.articleTitle" placeholder="请输入文章标题" clearable />
      </el-form-item>
      <el-form-item label="评论人">
        <el-input v-model="form.username" placeholder="请输入评论人" clearable />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="form.status" placeholder="请选择状态" clearable>
          <el-option label="待审核" value="0" />
          <el-option label="已通过" value="1" />
          <el-option label="已拒绝" value="2" />
          <el-option label="已删除" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="评论时间">
        <el-date-picker
          v-model="form.timeRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYY-MM-DD"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import dayjs from 'dayjs';

const form = ref({
  content: '',
  articleTitle: '',
  username: '',
  status: '',
  timeRange: []
});

const emit = defineEmits(['search']);

const handleSearch = () => {
  const params = {
    ...form.value,
    startTime: form.value.timeRange?.[0] ? dayjs(form.value.timeRange[0]).startOf('day').format('YYYY-MM-DD HH:mm:ss') : undefined,
    endTime: form.value.timeRange?.[1] ? dayjs(form.value.timeRange[1]).endOf('day').format('YYYY-MM-DD HH:mm:ss') : undefined
  };
  // 移除timeRange字段，避免传给后端
  delete params.timeRange;
  emit('search', params);
};

const handleReset = () => {
  form.value = {
    content: '',
    articleTitle: '',
    username: '',
    status: '',
    timeRange: []
  };
  emit('search', {});
};
</script>

<style lang="scss" scoped>
.comment-search {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .search-form {
    display: flex;
    flex-wrap: wrap;
    align-items: center;

    :deep(.el-form-item) {
      margin-right: 20px;
      margin-bottom: 0;

      .el-date-editor {
        width: 240px;
      }
    }
  }
}
</style>
