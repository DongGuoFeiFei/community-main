<template>
  <el-cascader
    v-model="selectedValue"
    :options="options"
    :props="cascaderProps"
    :placeholder="placeholder"
    clearable
    filterable
    :show-all-levels="false"
    @change="handleChange"
  />
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { getCategoryTree } from '@/api/category';

const props = defineProps({
  modelValue: [Number, String],
  placeholder: {
    type: String,
    default: '请选择分类'
  },
  excludeId: {
    type: Number,
    default: null
  }
});

const emit = defineEmits(['update:modelValue', 'change']);

const options = ref([]);
const selectedValue = ref([]);

const cascaderProps = {
  value: 'category_id',
  label: 'category_name',
  children: 'children',
  checkStrictly: true,
  emitPath: false
};

// 获取分类树并处理排除项
const fetchCategoryTree = async () => {
  try {
    const { data } = await getCategoryTree();
    options.value = filterCategories(data, props.excludeId);
  } catch (error) {
    console.error('获取分类树失败:', error);
  }
};

// 递归过滤掉排除的分类及其子分类
const filterCategories = (categories, excludeId) => {
  if (!excludeId) return categories;

  return categories
    .filter(category => category.category_id !== excludeId)
    .map(category => ({
      ...category,
      children: category.children ? filterCategories(category.children, excludeId) : []
    }));
};

// 处理选择变化
const handleChange = (value) => {
  emit('update:modelValue', value);
  emit('change', value);
};

// 监听外部值变化
watch(() => props.modelValue, (val) => {
  selectedValue.value = val;
}, { immediate: true });

onMounted(() => {
  fetchCategoryTree();
});
</script>
