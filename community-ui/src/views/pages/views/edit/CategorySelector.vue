<template>
  <div class="category-selector">
    <!-- 分类选择器头部 -->
    <div class="category-header">
      <el-input
          v-model="searchQuery"
          placeholder="搜索分类..."
          clearable
          @clear="handleSearchClear"
          @input="handleSearch"
      >
        <template #prefix>
          <el-icon>
            <Search/>
          </el-icon>
        </template>
      </el-input>
    </div>

    <!-- 已选分类展示区 -->
    <div class="selected-categories" v-if="selectedCategories.length > 0">
      <div class="selected-title">已选分类 ({{ selectedCategories.length }}/3):</div>
      <div class="selected-tags">
        <el-tag
            v-for="category in selectedCategories"
            :key="category.id"
            closable
            @close="removeSelectedCategory(category.id)"
        >
          {{ category.categoryName }}
        </el-tag>
      </div>
    </div>

    <!-- 分类树 -->
    <div class="category-tree">
      <el-tree
          ref="treeRef"
          :data="filteredCategories"
          :props="treeProps"
          node-key="id"
          :accordion="true"
          :highlight-current="true"
          :expand-on-click-node="false"
          :filter-node-method="filterNode"
          @node-click="handleNodeClick"
      >
        <template #default="{ node, data }">
          <div class="tree-node">
            <span class="category-name">{{ node.label }}</span>
            <el-tag
                v-if="data.status === 0"
                size="small"
                type="danger"
                effect="plain"
            >
              禁用
            </el-tag>
            <el-tag
                v-if="data.articleCount > 0"
                size="small"
                type="info"
                effect="plain"
            >
              {{ data.articleCount }}
            </el-tag>
          </div>
        </template>
      </el-tree>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, ref, watch} from 'vue'
import {Search} from '@element-plus/icons-vue'
import {getCategories} from '@/api/category'
import {localStores} from '@/stores/localStores'
import {ElMessage} from "element-plus"

const lStore = localStores()
const baseUrl = lStore.baseURL

// 树形结构配置
const treeProps = {
  label: 'categoryName',
  children: 'children'
}

// 分类数据
const categories = ref([])
const searchQuery = ref('')
const treeRef = ref(null)
const selectedCategories = ref([])

// 接收props
const props = defineProps({
  initialSelected: {
    type: Array,
    default: () => []
  }
})

// 检查是否已选中
const isSelected = (id) => {
  return selectedCategories.value.some(c => c.id === id)
}

// 过滤后的分类数据
const filteredCategories = computed(() => {
  if (!searchQuery.value) return categories.value
  return filterTree(categories.value, searchQuery.value.toLowerCase())
})

// 获取分类数据
const fetchCategories = async () => {
  try {
    const res = await getCategories()
    categories.value = buildTree(res.data)
    // 初始化选中的分类
    initSelectedCategories()
  } catch (error) {
    console.error('获取分类失败:', error)
    ElMessage.error('获取分类失败')
  }
}

// 构建树形结构
const buildTree = (items, parentId = null) => {
  return items
      .filter(item => item.parentId === parentId)
      .map(item => ({
        ...item,
        children: buildTree(items, item.id)
      }))
}

// 初始化选中的分类
const initSelectedCategories = () => {
  if (props.initialSelected && props.initialSelected.length > 0) {
    // 扁平化树结构以便查找
    const flatCategories = flattenTree(categories.value)
    selectedCategories.value = props.initialSelected
        .map(id => {
          const category = flatCategories.find(c => c.id === id)
          return category ? {id: category.id, categoryName: category.categoryName} : null
        })
        .filter(Boolean)
  }
}

// 扁平化树结构
const flattenTree = (tree) => {
  return tree.reduce((acc, node) => {
    acc.push(node)
    if (node.children && node.children.length > 0) {
      acc.push(...flattenTree(node.children))
    }
    return acc
  }, [])
}

// 过滤树节点
const filterNode = (value, data) => {
  if (!value) return true
  return data.categoryName.toLowerCase().includes(value.toLowerCase())
}

// 过滤整棵树
const filterTree = (tree, query) => {
  return tree
      .map(node => ({...node}))
      .filter(node => {
        const matches = filterNode(query, node)
        if (node.children) {
          node.children = filterTree(node.children, query)
          return matches || node.children.length > 0
        }
        return matches
      })
}

// 处理搜索
const handleSearch = () => {
  treeRef.value?.filter(searchQuery.value)
}

// 处理搜索清除
const handleSearchClear = () => {
  searchQuery.value = ''
  treeRef.value?.filter('')
}

// 移除已选分类
const removeSelectedCategory = (id) => {
  selectedCategories.value = selectedCategories.value.filter(c => c.id !== id)
  emit('update:selected', selectedCategories.value.map(c => c.id))
}

// 处理节点点击
const emit = defineEmits(['select', 'update:selected'])
const handleNodeClick = (data) => {
  if (data.status !== 1) {
    ElMessage.warning('该分类已禁用，无法选择')
    return
  }

  if (isSelected(data.id)) {
    removeSelectedCategory(data.id)
    return
  }

  if (selectedCategories.value.length >= 3) {
    ElMessage.warning('最多只能选择3个分类')
    return
  }

  selectedCategories.value.push({
    id: data.id,
    categoryName: data.categoryName
  })

  emit('select', data.id)
  emit('update:selected', selectedCategories.value.map(c => c.id))
}

// 监听初始选中的变化
watch(() => props.initialSelected, (newVal) => {
  if (categories.value.length > 0) {
    initSelectedCategories()
  }
}, {deep: true})

onMounted(() => {
  fetchCategories()
})
</script>

<style scoped lang="scss">
.category-selector {
  border: 1px solid var(--el-border-color);
  border-radius: var(--el-border-radius-base);
  padding: 12px;
  background-color: var(--el-bg-color);

  .category-header {
    margin-bottom: 12px;
  }

  .selected-categories {
    margin-bottom: 16px;
    padding: 8px;
    background-color: var(--el-fill-color-light);
    border-radius: var(--el-border-radius-base);

    .selected-title {
      font-size: 14px;
      color: var(--el-text-color-secondary);
      margin-bottom: 8px;
    }

    .selected-tags {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;

      .el-tag {
        margin-right: 8px;
      }
    }
  }

  .category-tree {
    max-height: 400px;
    overflow-y: auto;

    .tree-node {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 2px 0;

      .category-name {
        flex: 1;
      }
    }

    :deep(.el-tree-node__content) {
      height: 32px;
    }
  }
}
</style>