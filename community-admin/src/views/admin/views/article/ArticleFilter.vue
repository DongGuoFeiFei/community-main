<template>
  <el-form :model="form" inline>
    <el-form-item label="标题">
      <el-input
        v-model="form.title"
        placeholder="请输入标题"
        clearable
        @keyup.enter="handleSearch"
      />
    </el-form-item>

    <el-form-item label="分类">
      <el-select
        v-model="form.categoryId"
        placeholder="请选择分类"
        clearable
      >
        <el-option
          v-for="item in categories"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        />
      </el-select>
    </el-form-item>

    <el-form-item label="标签">
      <el-select
        v-model="form.tagId"
        placeholder="请选择标签"
        clearable
      >
        <el-option
          v-for="item in tags"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        />
      </el-select>
    </el-form-item>

    <el-form-item label="状态">
      <el-select
        v-model="form.status"
        placeholder="请选择状态"
        clearable
      >
        <el-option label="草稿" :value="0"/>
        <el-option label="已发布" :value="1"/>
        <el-option label="已下架" :value="2"/>
      </el-select>
    </el-form-item>

    <el-form-item label="发布时间">
      <el-date-picker
        v-model="form.dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        value-format="YYYY-MM-DD"
      />
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button @click="handleReset">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import {onMounted, ref} from 'vue'
import {getCategories} from '@/api/article/category'
import {getTags} from '@/api/article/tag'

const emit = defineEmits(['search'])

const form = ref({
  title: '',
  categoryId: null,
  tagId: null,
  status: null,
  dateRange: []
})

const categories = ref([])
const tags = ref([])

onMounted(() => {
  fetchCategories()
  fetchTags()
})

const fetchCategories = async () => {
  const res = await getCategories()
  categories.value = res.data
}

const fetchTags = async () => {
  const res = await getTags()
  tags.value = res.data
}

const handleSearch = () => {
  const params = {
    ...form.value,
    startTime: form.value.dateRange?.[0] || null,
    endTime: form.value.dateRange?.[1] || null
  }
  delete params.dateRange
  emit('search', params)
}

const handleReset = () => {
  form.value = {
    title: '',
    categoryId: null,
    tagId: null,
    status: null,
    dateRange: []
  }
  emit('search', {})
}
</script>

<style lang="scss" scoped>
.el-form-item {
  margin-bottom: 0;
}
</style>
