<!-- /views/admin/user/components/UserSearch.vue -->
<template>
  <div class="user-search">
    <el-form :inline="true" :model="form" class="search-form">
      <el-form-item label="用户名">
        <el-input v-model="form.username" placeholder="请输入用户名" clearable />
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" placeholder="请输入邮箱" clearable />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="form.status" placeholder="请选择状态" clearable>
          <el-option label="正常" value="1" />
          <el-option label="禁用" value="0" />
        </el-select>
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

const form = ref({
  username: '',
  email: '',
  status: '',
});

const emit = defineEmits(['search']);

const handleSearch = () => {
  emit('search', form.value);
};

const handleReset = () => {
  form.value = {
    username: '',
    email: '',
    status: '',
  };
  emit('search', form.value);
};
</script>

<style lang="scss" scoped>
.user-search {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);

  .search-form {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
  }
}
</style>