<template>
  <div class="editor-container">
    <!-- 编辑器头部 -->
    <EditorHeader
        v-model:title="articleData.title"
        :is-edit-mode="isEditMode"
        @back="goBack"
        @save="saveArticle"
    />

    <!-- 编辑器主体 -->
    <MarkdownEditor v-model="content"/>

    <!-- 标签选择器 -->
    <TagSelector
        v-model="tags"
        class="tag-selector-container"
        :max-tags="5"
    />

    <!-- 封面区域 -->
    <CoverSection
        :cover-url="coverImageData.accessUrl"
        @select="openFileDialog"
        @remove="removeCover"
    />

    <!-- 隐藏的文件输入 -->
    <input
        type="file"
        ref="fileInput"
        @change="handleCoverUpload"
        accept="image/*"
        style="display: none;"
    >
  </div>
</template>

<script setup>

import {computed, onMounted, reactive, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {ElLoading, ElMessage, ElMessageBox} from 'element-plus'


import MarkdownEditor from '·/MarkdownEditor.vue'
import {addArticle, delFileById, getArticleById, updateArticle, uploadFile} from '@/api/index.js'
import {localStores} from '@/stores/localStores.js'
import {sessionStores} from '@/stores/sessionStores.js'
import CoverSection from "·/CoverSection.vue";
import EditorHeader from "·/EditorHeader.vue";
import TagSelector from "·/TagSelector.vue";
import {getPostTags} from "@/api/article.js";

const content = ref('朋友，有趣的故事，你来分享🎉️！')
const lStore = localStores()
const baseUrl = lStore.baseURL
const router = useRouter()
const fileInput = ref(null)
const sStore = sessionStores()
const isEditMode = ref(sStore.isEditMode)
const route = useRoute()

// 文章数据
const articleData = reactive({
  title: '',
  fileId: null,
  content: '朋友，有趣的故事，你来分享🎉️！',
  status: 0, // 0: 发布, 1: 草稿
  tagIds: computed(() => tags.value.map(tag => tag.id))
})

const tags = ref([])

// 封面数据
const coverImageData = reactive({
  fileId: '',
  fileOriginalName: '',
  fileAutoName: '',
  storageUrl: '',
  accessUrl: '',
  uploadTime: ''
})

// 打开文件选择对话框
const openFileDialog = () => {
  fileInput.value.click()
}

// 处理封面上传
const handleCoverUpload = async (event) => {
  const file = event.target.files[0]
  if (!file) return

  if (!file.type.startsWith('image/')) {
    ElMessage.error('请选择图片文件')
    return
  }

  if (file.size > 5 * 1024 * 1024) {
    ElMessage.error('图片大小不能超过5MB')
    return
  }

  if (coverImageData.accessUrl?.trim() !== '' && Number(coverImageData.fileId) !== 1) {
    await delFileById(coverImageData.fileId)
    coverImageData.accessUrl = ''
  }

  try {
    const formData = new FormData()
    formData.append('file', file)

    const res = await uploadFile(formData)
    Object.assign(coverImageData, res.data)
    coverImageData.accessUrl = baseUrl + res.data.accessUrl
    articleData.fileId = res.data.fileId

    ElMessage.success('封面上传成功')
  } catch (error) {
    console.error('封面上传失败:', error)
    ElMessage.error('封面上传失败')
  } finally {
    event.target.value = ''
  }
}

// 移除封面
const removeCover = () => {
  ElMessageBox.confirm('确定要移除封面吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    if (Number(coverImageData.fileId) !== 1) {
      delFileById(coverImageData.fileId)
    }
    coverImageData.accessUrl = ''
    articleData.fileId = null
    ElMessage.success('封面已移除')
  }).catch(() => {
  })
}

// 保存文章
const saveArticle = (status) => {
  if (!articleData.title.trim()) {
    ElMessage.warning('请输入文章标题')
    return
  }

  articleData.content = content.value
  articleData.status = status

  if (!articleData.content.trim()) {
    ElMessage.warning('文章内容不能为空')
    return
  }

  const loading = ElLoading.service({
    lock: true,
    text: status === 0 ? '正在发布文章...' : '正在保存草稿...'
  })

  if (sStore.isEditMode) {
    articleData.tags =
        updateArticle(sStore.editorArticleId, articleData)
            .then(res => {
              ElMessage.success(status === 0 ? '文章重发布成功' : '草稿修改成功')
              router.back()
            })
            .catch(err => {
              ElMessage.warning('操作失败，稍后重试。')
            })
            .finally(() => {
              sStore.isEditMode = false
              loading.close()
            })
  } else {
    addArticle(articleData)
        .then(res => {
          ElMessage.success(status === 0 ? '文章发布成功' : '草稿保存成功')
          router.back()
        })
        .catch(err => {
          ElMessage.warning('操作失败，稍后重试。')
        })
        .finally(() => {
          sStore.isEditMode = false
          loading.close()
        })
  }
}

// 设置是否是旧值
const isEditor = () => {
  if (route.path === '/editor') {
    isEditMode.value = false
  }
  if (route.path === '/editor-edit') {
    isEditMode.value = true
  }
  if (isEditMode.value) {
    getArticleById(sStore.editorArticleId).then(res => {
      articleData.title = res.title
      articleData.content = res.content
      articleData.fileId = res.fileId
      articleData.status = res.status
      coverImageData.fileId = res.fileId
      coverImageData.accessUrl = sStore.baseURL + res.coverUrl
      content.value = articleData.content
    })
    getPostTags(sStore.editorArticleId).then(res => {
      tags.value = res.data
    })
  }
}

onMounted(() => {
  isEditor()
})

// 返回上一页
const goBack = async () => {
  try {
    await ElMessageBox.confirm('确定要离开当前页面吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    sStore.isEditMode = false
    router.go(-1)
  } catch {
    console.log('用户取消了返回操作')
  }
}
</script>

<style scoped>
.editor-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;

  .tag-selector-container {
    margin-bottom: 20px;
  }
}
</style>