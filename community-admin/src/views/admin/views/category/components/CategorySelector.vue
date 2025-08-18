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
const selectedValue = ref(null);
const loading = ref(false);

const cascaderProps = {
  value: 'id',
  label: 'categoryName',
  children: 'categoryList',
  checkStrictly: true,
  emitPath: false
};

// 获取分类树并处理排除项
const fetchCategoryTree = async () => {
  try {
    loading.value = true;
    const { data } = await getCategoryTree();
    options.value = filterCategories(data, props.excludeId);

    // 数据加载完成后设置默认值
    if (props.modelValue) {
      selectedValue.value = props.modelValue;
    }
  } catch (error) {
    console.error('获取分类树失败:', error);
  } finally {
    loading.value = false;
  }
};

// 递归过滤掉排除的分类及其子分类
const filterCategories = (categories, excludeId) => {
  if (!categories) return [];
  if (!excludeId) return categories;

  return categories
    .filter(category => category.categoryId !== excludeId)
    .map(category => ({
      ...category,
      categoryList: category.categoryList ? filterCategories(category.categoryList, excludeId) : []
    }));
};

// 处理选择变化
const handleChange = (value) => {
  emit('update:modelValue', value);
  emit('change', value);
};

// 监听外部值变化
watch(() => props.modelValue, (val) => {
  // 只有当值确实变化时才更新
  if (val !== selectedValue.value) {
    selectedValue.value = val;
  }
}, { immediate: true });

onMounted(() => {
  fetchCategoryTree();
});
</script>
