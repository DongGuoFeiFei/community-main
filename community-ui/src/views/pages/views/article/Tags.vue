<script setup>
import {computed, onMounted, ref} from 'vue'
import {getPostTags} from '@/api/article'
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

// watch(tags, (newTags) => {
//   activeTags.value = newTags.filter(tag => tag.status === 1)
// }, {immediate: true, deep: true})
const navigateToTag = (tagName) => {
  router.push({
    name: 'tag-search',
    query: {tag: tagName}
  })
}

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
  padding: 15px;
  background-color: var(--el-bg-color-page);
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .tags-title {
    margin: 0 0 15px 0;
    font-size: 16px;
    color: var(--el-text-color-primary);
    border-bottom: 1px solid var(--el-border-color-light);
    padding-bottom: 10px;
  }

  .tags-list {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;

    .el-tag {
      cursor: pointer;
      user-select: none;
      color: #303133;
    }

    .tag-item {
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      }
    }
  }

  .tags-loading {
    padding: 8px 0;
  }
}
</style>