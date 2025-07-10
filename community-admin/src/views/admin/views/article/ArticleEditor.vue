<!-- /views/admin/views/article/ArticleEditor.vue -->
<template>
  <el-dialog
      v-model="visible"
      :title="title"
      width="90%"
      top="5vh"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="true"
      class="article-editor-dialog"
      @closed="handleClose"
  >
    <!-- 编辑器头部 -->
    <EditorHeader
        v-model:title="articleData.title"
        :is-edit-mode="isEditMode"
        @save="saveArticle"
        @cancel="handleCancel"
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

    <!-- 隐藏的文件上传input -->
    <input
        ref="fileInput"
        type="file"
        accept="image/*"
        style="display: none"
        @change="handleCoverUpload"
    />
  </el-dialog>
</template>

<!--todo 数据接口管理端个人定制-->
<script setup>
import EditorHeader from "@/views/admin/views/article/edit/EditorHeader.vue";
import MarkdownEditor from "@/views/admin/views/article/edit/MarkdownEditor.vue";
import TagSelector from "@/views/admin/views/article/edit/TagSelector.vue";
import CoverSection from "@/views/admin/views/article/edit/CoverSection.vue";
import {computed, reactive, ref, watch} from "vue";
import {localStores} from "@/stores/localStores.js";
import {ElLoading, ElMessage, ElMessageBox} from "element-plus";
import {addArticle, delFileById, getArticleById, updateArticle, uploadFile} from "@/api/index.js";
import {getPostTags} from "@/api/article.js";
import router from "@/router/index.js";

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true
  },
  article: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['update:modelValue', 'success',])


const lStore = localStores()
const baseUrl = lStore.baseURL
const fileInput = ref(null)
const content = ref('朋友，有趣的故事，你来分享🎉️！')
const tags = ref([])

// 文章数据
const articleData = reactive({
  articleId: null,
  title: '',
  fileId: null,
  content: '',
  status: 0, // 0: 发布, 1: 草稿
  tagIds: computed(() => tags.value.map(tag => tag.id))
})

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
  }).then(async () => {
    if (Number(coverImageData.fileId) !== 1) {
      await delFileById(coverImageData.fileId)
    }
    coverImageData.accessUrl = ''
    articleData.fileId = null
    ElMessage.success('封面已移除')
  }).catch(() => {
    // 用户取消操作
  })
}

// 保存文章
const saveArticle = async (status) => {
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

  const loadingInstance = ElLoading.service({
    lock: true,
    text: status === 0 ? '正在发布文章...' : '正在保存草稿...'
  })

  try {
    if (isEditMode.value) {
      await updateArticle(props.article.articleId, articleData)
      ElMessage.success(status === 0 ? '文章重发布成功' : '草稿修改成功')
    } else {
      await addArticle(articleData)
      ElMessage.success(status === 0 ? '文章发布成功' : '草稿保存成功')
    }
    emit('success')
    router.back()
  } catch (err) {
    console.error('保存文章失败:', err)
    ElMessage.warning('操作失败，稍后重试。')
  } finally {
    loadingInstance.close()
  }
}

const handleCancel = () => {
  ElMessageBox.confirm('确定要取消编辑吗？未保存的内容将会丢失', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    visible.value = false
    resetForm()
  }).catch(() => {
    // 用户点击了取消
  })
}

const loadArticleData = async (article) => {
  const loadingInstance = ElLoading.service({
    lock: true,
    text: '正在加载文章数据...'
  })

  try {
    // 加载文章基本信息
    Object.assign(articleData, {
      articleId: article.articleId,
      title: article.title,
      content: article.content,
      fileId: article.fileId,
      status: article.status
    })
    content.value = article.content

    const myArticleData = await getArticleById(article.articleId)

    console.log(myArticleData)
    // 加载封面
    if (article.fileId) {
      coverImageData.fileId = article.fileId
      coverImageData.accessUrl = baseUrl + myArticleData.coverUrl
    }

    // 加载标签
    const tagsRes = await getPostTags(article.articleId)
    tags.value = tagsRes.data

    console.log(tagsRes)
    console.log(coverImageData)
    console.log(articleData)
  } catch (error) {
    console.error('加载文章数据失败:', error)
    ElMessage.error('加载文章数据失败')
  } finally {
    loadingInstance.close()
  }
}

// 重置表单
const resetForm = () => {
  articleData.articleId = null
  articleData.title = ''
  articleData.content = '朋友，有趣的故事，你来分享🎉️！'
  articleData.fileId = null
  articleData.status = 0
  content.value = '朋友，有趣的故事，你来分享🎉️！'
  coverImageData.accessUrl = ''
  tags.value = []
}

const handleClose = () => {
  resetForm()
}

const title = computed(() => {
  return props.article ? '编辑文章' : '新增文章'
})

const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const isEditMode = computed(() => !!props.article)

// 监听article变化，加载数据
watch(() => props.article, (newVal) => {
  if (newVal) {
    loadArticleData(newVal).catch(error => {
      console.error('加载文章数据出错:', error)
      ElMessage.error('加载文章数据出错')
    })
  } else {
    resetForm()
  }
}, {immediate: true})
</script>

<style lang="scss" scoped>
.editor-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;

  .tag-selector-container {
    margin-bottom: 20px;
  }
}
</style>