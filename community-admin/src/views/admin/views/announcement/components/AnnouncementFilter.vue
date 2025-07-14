<script setup>
import { ref } from 'vue';

const emit = defineEmits(['filter']);

const form = ref({
  title: '',
  publisher: '',
  status: null,
  startTime: null,
  endTime: null
});

const handleFilter = () => {
  emit('filter', form.value);
};

const handleReset = () => {
  form.value = {
    title: '',
    publisher: '',
    status: null,
    startTime: null,
    endTime: null
  };
  emit('filter', form.value);
};
</script>

<template>
  <el-form :model="form" label-width="80px" class="filter-form">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入标题" clearable />
        </el-form-item>
      </el-col>
      <el-col :span="6">
        <el-form-item label="发布人">
          <el-input v-model="form.publisher" placeholder="请输入发布人" clearable />
        </el-form-item>
      </el-col>
      <el-col :span="6">
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="请选择状态" clearable>
            <el-option label="上线" :value="1" />
            <el-option label="下线" :value="0" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="6">
        <el-form-item label="发布时间">
          <el-date-picker
              v-model="form.startTime"
              type="datetime"
              placeholder="开始时间"
              value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
      </el-col>
      <el-col :span="6">
        <el-form-item label="至">
          <el-date-picker
              v-model="form.endTime"
              type="datetime"
              placeholder="结束时间"
              value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
      </el-col>
      <el-col :span="6" style="display: flex; align-items: center;">
        <el-button type="primary" @click="handleFilter">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-col>
    </el-row>
  </el-form>
</template>

<style lang="scss" scoped>
.filter-form {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}
</style>