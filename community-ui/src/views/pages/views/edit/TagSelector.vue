<template>
  <div class="tag-selector">
    <!-- 标签输入框 -->
    <div class="tag-input-container">
      <el-input
          v-model="searchQuery"
          placeholder="搜索或添加标签"
          clearable
          @keyup.enter="handleAddTag"
          @clear="filterTags"
      >
        <template #prefix>
          <el-icon>
            <Search/>
          </el-icon>
        </template>
      </el-input>
    </div>

    <!-- 标签展示区域 -->
    <div class="tag-display-area">
      <!-- 已选标签 -->
      <div class="selected-tags" v-if="selectedTags.length > 0">
        <div class="section-title">已选标签</div>
        <div class="tag-list">
          <el-tag
              v-for="tag in selectedTags"
              :key="tag.id"
              :color="tag.color"
              closable
              @close="removeTag(tag)"
          >
            #{{ tag.name }}
          </el-tag>
        </div>
      </div>

      <!-- 热门标签 -->
      <div class="popular-tags" v-if="popularTags.length > 0">
        <div class="section-title">热门标签</div>
        <div class="tag-list">
          <el-tag
              v-for="tag in popularTags"
              :key="tag.id"
              :color="tag.color"
              @click="toggleTag(tag)"
              :effect="isTagSelected(tag) ? 'dark' : 'plain'"
          >
            #{{ tag.name }}
          </el-tag>
        </div>
      </div>

      <!-- 搜索结果 -->
      <div class="search-results" v-if="searchResults.length > 0">
        <div class="section-title">搜索结果</div>
        <div class="tag-list">
          <el-tag
              v-for="tag in searchResults"
              :key="tag.id"
              :color="tag.color"
              @click="toggleTag(tag)"
              :effect="isTagSelected(tag) ? 'dark' : 'plain'"
          >
            #{{ tag.name }}
          </el-tag>
        </div>
      </div>

      <!-- 创建新标签 -->
      <div class="create-new-tag" v-if="showCreateNewTag">
        <el-button
            type="primary"
            size="small"
            @click="createNewTag"
            :loading="isCreatingTag"
        >
          创建新标签: "{{ searchQuery }}"
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import {computed, onMounted, ref, watch} from 'vue'
import {Search} from '@element-plus/icons-vue'
import {ElMessage} from 'element-plus'
import {createTag, getAllTags, getPopularTags} from "@/api/tag.js";

// 组件属性
const props = defineProps({
  modelValue: {
    type: Array,
    default: () => []
  },
  maxTags: {
    type: Number,
    default: 5
  }
})

// 组件事件
const emit = defineEmits(['update:modelValue'])

// 状态管理
const searchQuery = ref('')
const allTags = ref([])
const popularTags = ref([])
const isCreatingTag = ref(false)

// 计算属性
const selectedTags = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const searchResults = computed(() => {
  if (!searchQuery.value) return []
  return allTags.value.filter(tag =>
      tag.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      tag.slug.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
})

const showCreateNewTag = computed(() => {
  return searchQuery.value &&
      !allTags.value.some(tag =>
          tag.name.toLowerCase() === searchQuery.value.toLowerCase()
      ) &&
      !isTagSelected({name: searchQuery.value})
})

// 初始化加载标签
onMounted(async () => {
  await loadAllTags()
  await loadPopularTags()
})

// 加载所有标签
const loadAllTags = async () => {
  try {
    // 这里替换为实际的API调用
    const response = await getAllTags()
    allTags.value = response.data

    // 模拟数据
    // allTags.value = [
    //   {id: 1, name: '技术', slug: 'tech', color: '#409EFF', description: '技术相关文章'},
    //   {id: 2, name: '编程', slug: 'programming', color: '#67C23A', description: '编程相关文章'},
    //   {id: 3, name: '前端', slug: 'frontend', color: '#E6A23C', description: '前端开发相关'},
    //   {id: 4, name: '后端', slug: 'backend', color: '#F56C6C', description: '后端开发相关'},
    //   {id: 5, name: '数据库', slug: 'database', color: '#909399', description: '数据库相关'}
    // ]
  } catch (error) {
    ElMessage.error('加载标签失败')
    console.error('加载标签失败:', error)
  }
}

// 加载热门标签
const loadPopularTags = async () => {
  try {
    // 这里替换为实际的API调用
    const response = await getPopularTags()
    popularTags.value = response.data

    // 模拟数据
    // popularTags.value = [
    //   {id: 1, name: '技术', slug: 'tech', color: '#409EFF', description: '技术相关文章'},
    //   {id: 2, name: '编程', slug: 'programming', color: '#67C23A', description: '编程相关文章'},
    //   {id: 3, name: 'Vue', slug: 'vue', color: '#42b983', description: 'Vue.js相关'}
    // ]
  } catch (error) {
    ElMessage.error('加载热门标签失败')
    console.error('加载热门标签失败:', error)
  }
}

// 过滤标签
const filterTags = () => {
  // 搜索框清空时重置
  searchQuery.value = ''
}

// 添加标签
const handleAddTag = () => {
  if (!searchQuery.value) return

  // 如果搜索到的标签存在，直接选中
  if (searchResults.value.length > 0) {
    toggleTag(searchResults.value[0])
    return
  }

  // 否则显示创建新标签按钮
}

// 创建新标签
const createNewTag = async () => {
  if (!searchQuery.value) return
  if (selectedTags.value.length >= props.maxTags) {
    ElMessage.warning(`最多只能选择${props.maxTags}个标签`)
    return
  }

  isCreatingTag.value = true
  try {
    // 这里替换为实际的API调用
    const response = await createTag({
      name: searchQuery.value,
      slug: generateSlug(searchQuery.value),
      color: getRandomColor()
    })
    const newTag = response.data

    // 模拟创建新标签
    // const newTag = {
    //   id: Date.now(), // 临时ID
    //   name: searchQuery.value,
    //   slug: generateSlug(searchQuery.value),
    //   color: getRandomColor(),
    //   description: ''
    // }

    allTags.value.push(newTag)
    toggleTag(newTag)
    searchQuery.value = ''
    ElMessage.success('创建标签提交审核')
  } catch (error) {
    ElMessage.error('创建标签失败')
    console.error('创建标签失败:', error)
  } finally {
    isCreatingTag.value = false
  }
}

// 切换标签选择状态
const toggleTag = (tag) => {
  if (isTagSelected(tag)) {
    removeTag(tag)
  } else {
    addTag(tag)
  }
}

// 添加标签
const addTag = (tag) => {
  if (selectedTags.value.length >= props.maxTags) {
    ElMessage.warning(`最多只能选择${props.maxTags}个标签`)
    return
  }

  if (!isTagSelected(tag)) {
    selectedTags.value = [...selectedTags.value, tag]
  }
}

// 移除标签
const removeTag = (tag) => {
  selectedTags.value = selectedTags.value.filter(t => t.id !== tag.id)
}

// 检查标签是否已选
const isTagSelected = (tag) => {
  return selectedTags.value.some(t => t.id === tag.id)
}

// 生成标签slug
const generateSlug = (name) => {
  return name.toLowerCase().replace(/\s+/g, '-').replace(/[^\w-]+/g, '')
}

// 获取随机颜色
const getRandomColor = () => {
  const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#8E44AD', '#3498DB', '#E74C3C', '#1ABC9C']
  return colors[Math.floor(Math.random() * colors.length)]
}

// 监听搜索查询变化
watch(searchQuery, (newVal) => {
  if (!newVal) {
    filterTags()
  }
})
</script>

<style scoped lang="scss">
.tag-selector {
  width: 100%;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 12px;
  box-sizing: border-box;

  .tag-input-container {
    margin-bottom: 12px;
  }

  .tag-display-area {
    max-height: 300px;
    overflow-y: auto;

    .section-title {
      font-size: 14px;
      color: #909399;
      margin-bottom: 8px;
    }

    .tag-list {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;
      margin-bottom: 16px;


      .el-tag {
        cursor: pointer;
        user-select: none;
        color: #303133;
      }
    }

    .create-new-tag {
      margin-top: 12px;
    }
  }
}
</style>