<script setup>
import {computed, onMounted, ref, watch} from 'vue'
import {getPostTags} from '@/api/article.js'
import {useRouter} from 'vue-router'

const props = defineProps({
  postId: {
    type: Number,
    required: true
  }
})

const router = useRouter()
const tags = ref([])
const loading = ref(false)

const activeTags = computed(() => {
  return tags.value.filter(tag => tag.status === 1)
})
const fetchTags = async () => {
  try {
    loading.value = true
    const response = await getPostTags(props.postId)
    tags.value = response.data || []
    console.log(tags.value)
  } catch (error) {
    console.error('获取标签失败:', error)
  } finally {
    loading.value = false
  }
}

const navigateToTag = (tagName) => {
  router.push({
    name: 'tag-search',
    query: {tag: tagName}
  })
}
watch(
    () => props.postId,
    () => {
      fetchTags()
    }
)
onMounted(() => {
  fetchTags()
})


</script>

<template>
  <div class="post-tags-container">
    <div v-if="loading" class="tags-loading">
      <el-skeleton :rows="1" animated/>
    </div>
    <div v-else class="tags-list">
      <el-tag
          v-for="tag in activeTags"
          :key="tag.id"
          class="tag-item"
          effect="plain"
          :color="tag.color"
          @click="navigateToTag(tag.name)"
      >
        {{ tag.name }}
      </el-tag>
      <el-tag v-if="activeTags.length === 0" type="info" effect="plain">
        暂无标签
      </el-tag>
    </div>
  </div>
</template>

<style scoped lang="scss">
.post-tags-container {
  margin: 20px 0;
  padding: 20px;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px);
  border-radius: 28px;
  border: 3px solid rgba(255, 255, 255, 0.6);
  box-shadow:
    0 15px 40px rgba(255, 182, 193, 0.2),
    inset 0 2px 10px rgba(255, 255, 255, 0.9),
    0 0 0 1px rgba(212, 175, 255, 0.1);
  animation: slideInUp 0.6s cubic-bezier(0.34, 1.56, 0.64, 1);
  position: relative;

  // 装饰标签图标
  &::before {
    content: '🏷️';
    position: absolute;
    top: 15px;
    right: 15px;
    font-size: 24px;
    opacity: 0.4;
  }

  .tags-title {
    margin: 0 0 15px 0;
    font-size: 18px;
    font-weight: 700;
    background: linear-gradient(135deg, #FF6B9D, #C9A0DC);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    border-bottom: 2px dashed rgba(255, 182, 193, 0.3);
    padding-bottom: 10px;
  }

  .tags-list {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;

    :deep(.el-tag) {
      cursor: pointer;
      user-select: none;
      border-radius: 20px !important;
      padding: 8px 16px !important;
      font-weight: 600;
      font-size: 14px;
      transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
      border: 2px solid transparent;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
    }

    .tag-item {
      cursor: pointer;
      transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
      position: relative;

      &::before {
        content: '#';
        font-weight: 700;
        margin-right: 2px;
      }

      &:hover {
        transform: translateY(-5px) scale(1.1);
        box-shadow:
          0 10px 25px rgba(255, 182, 193, 0.3),
          0 0 20px rgba(255, 192, 203, 0.4);
      }

      // 为不同标签添加渐变背景
      &:nth-child(5n+1) {
        background: linear-gradient(135deg, #FFB6C1, #FFD6E8) !important;
        color: #FF6B9D !important;
        border-color: rgba(255, 107, 157, 0.3) !important;
      }

      &:nth-child(5n+2) {
        background: linear-gradient(135deg, #C9A0DC, #E0D4F7) !important;
        color: #9B59B6 !important;
        border-color: rgba(155, 89, 182, 0.3) !important;
      }

      &:nth-child(5n+3) {
        background: linear-gradient(135deg, #84C7D0, #B8E6EC) !important;
        color: #4DB8C4 !important;
        border-color: rgba(77, 184, 196, 0.3) !important;
      }

      &:nth-child(5n+4) {
        background: linear-gradient(135deg, #FFD6E8, #FFF0F5) !important;
        color: #FF6B9D !important;
        border-color: rgba(255, 182, 193, 0.3) !important;
      }

      &:nth-child(5n+5) {
        background: linear-gradient(135deg, #E8F5FF, #F0E6FF) !important;
        color: #C9A0DC !important;
        border-color: rgba(201, 160, 220, 0.3) !important;
      }
    }

    :deep(.el-tag.el-tag--info) {
      background: rgba(240, 240, 240, 0.6) !important;
      color: #C9A0DC !important;
      border-color: rgba(201, 160, 220, 0.2) !important;
    }
  }

  .tags-loading {
    padding: 8px 0;
  }
}

@keyframes slideInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
