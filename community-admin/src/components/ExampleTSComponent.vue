<template>
  <div class="example-ts-component">
    <h2>{{ title }}</h2>
    <p>计数器: {{ count }}</p>
    <p>双倍: {{ doubleCount }}</p>
    <div>
      <el-button type="primary" @click="increment">增加</el-button>
      <el-button @click="decrement">减少</el-button>
      <el-button type="danger" @click="reset">重置</el-button>
    </div>

    <div v-if="loading" style="margin-top: 20px">加载中...</div>

    <div v-if="articles.length > 0" style="margin-top: 20px">
      <h3>文章列表</h3>
      <ul>
        <li v-for="article in articles" :key="article.id">
          {{ article.title }} - {{ article.author }}
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts">
/**
 * 示例：TypeScript Vue 组件
 * 演示如何在 Vue 组件中使用 TypeScript
 */
import { ref, computed, onMounted } from 'vue'
import { getArticleList } from '@/api/example'
import type { Article } from '@/api/example'

// 定义 Props 接口
interface Props {
  title: string
  initialCount?: number
}

// 使用 TypeScript 的 Props
const props = withDefaults(defineProps<Props>(), {
  initialCount: 0,
})

// 定义 Emits 接口
interface Emits {
  (e: 'change', value: number): void
  (e: 'reset'): void
}

const emit = defineEmits<Emits>()

// 响应式数据（带明确类型）
const count = ref<number>(props.initialCount)
const loading = ref<boolean>(false)
const articles = ref<Article[]>([])

// 计算属性（自动推断返回类型）
const doubleCount = computed(() => count.value * 2)

// 方法（带类型注解）
const increment = (): void => {
  count.value++
  emit('change', count.value)
}

const decrement = (): void => {
  count.value--
  emit('change', count.value)
}

const reset = (): void => {
  count.value = props.initialCount
  emit('reset')
}

// 异步方法（演示调用 TS API）
const loadArticles = async (): Promise<void> => {
  try {
    loading.value = true
    const response = await getArticleList({ page: 1, pageSize: 5 })

    if (response.data && response.data.list) {
      articles.value = response.data.list
    }
  } catch (error) {
    console.error('加载文章失败:', error)
  } finally {
    loading.value = false
  }
}

// 生命周期钩子
onMounted(() => {
  // loadArticles()  // 取消注释以加载文章
})
</script>

<style scoped>
.example-ts-component {
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
}

h2 {
  color: #409eff;
  margin-bottom: 16px;
}

ul {
  list-style: none;
  padding: 0;
}

li {
  padding: 8px;
  border-bottom: 1px solid #eee;
}
</style>
