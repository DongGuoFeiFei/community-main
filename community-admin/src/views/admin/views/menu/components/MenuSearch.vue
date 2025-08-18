<template>
  <div class="menu-search-container">
    <el-form :model="searchForm" ref="searchFormRef" inline class="search-form">
      <el-form-item prop="menuName" label="菜单名称">
        <el-input
          v-model="searchForm.menuName"
          placeholder="请输入菜单名称"
          clearable
          @keyup.enter="handleSearch"
        />
      </el-form-item>
      <el-form-item prop="menuType" label="菜单类型">
        <el-select
          v-model="searchForm.menuType"
          placeholder="请选择菜单类型"
          clearable
        >
          <el-option
            v-for="item in menuTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item prop="status" label="状态">
        <el-select
          v-model="searchForm.status"
          placeholder="请选择状态"
          clearable
        >
          <el-option label="启用" :value="1"/>
          <el-option label="禁用" :value="0"/>
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
import {defineEmits, ref} from 'vue'

const searchForm = ref({
  menuName: '',
  menuType: '',
  status: ''
})

const searchFormRef = ref(null)

const menuTypeOptions = [
  {value: 'M', label: '目录'},
  {value: 'C', label: '菜单'},
  {value: 'F', label: '按钮'},
  {value: 'L', label: '链接'}
]

const emit = defineEmits(['search', 'reset'])

const handleSearch = () => {
  emit('search', searchForm.value)
}

const handleReset = () => {
  searchFormRef.value.resetFields()
  emit('reset')
}
</script>

<style lang="scss" scoped>
.menu-search-container {
  padding: 20px;
  background-color: #fff;
  margin-bottom: 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 16px; // 统一设置间距

  .el-form-item {
    margin-right: 0; // 清除默认右边距

    .el-input, .el-select {
      width: 180px; // 可以设置不同的宽度
    }
  }
}
</style>
