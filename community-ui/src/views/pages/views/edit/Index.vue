<script setup>

import {computed, onMounted, reactive, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {ElLoading, ElMessage, ElMessageBox} from 'element-plus'
import {addArticle, delFileById, getArticleById, updateArticle, uploadFile} from '@/api/index.js'
import {localStores} from '@/stores/localStores.js'
import {sessionStores} from '@/stores/sessionStores.js'
import CoverSection from "@/views/pages/views/edit/CoverSection.vue";
import EditorHeader from "@/views/pages/views/edit/EditorHeader.vue";
import TagSelector from "@/views/pages/views/edit/TagSelector.vue";
import {getPostTags} from "@/api/article.js";
import TiptapEditor from "@/views/pages/views/edit/components/TiptapEditor.vue";
import {getArticleCategories} from "@/api/category.js";
import CategorySelector from "@/views/pages/views/edit/CategorySelector.vue";
import HomeHeader from "@/views/pages/views/home/components/HomeHeader.vue";
import Live2DViewer from "@/views/pages/components/Live2D/Live2DViewer.vue";

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
  content: '<div style="font-family: SimSun, 宋体, serif; font-size: 18px; text-align: center; margin-bottom: 10px;">\n' +
      '  涉江<span style="color: #8b4513; font-weight: bold;">采芙蓉</span>，兰泽多<span style="color: #8b4513; font-weight: bold;">芳草</span>。\n' +
      '</div>\n' +
      '<div style="font-family: SimSun, 宋体, serif; font-size: 18px; text-align: center; margin-bottom: 10px;">\n' +
      '  采之欲<span style="color: #8b4513; font-weight: bold;">遗谁</span>？所思在<span style="color: #8b4513; font-weight: bold;">远道</span>。\n' +
      '</div>',
  status: 0, // 0: 发布, 1: 草稿
  tagIds: computed(() => tags.value.map(tag => tag.id)),
  categoryIds: []
})

const content = ref('')

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

/**
 *  保存文章
 *  status：保存还是更新
 * @param status
 */
const saveArticle = (status) => {
  if (!articleData.title.trim()) {
    ElMessage.warning('请输入文章标题')
    return
  }
  if (articleData.fileId === null) {
    ElMessage.warning('请添加封面')
    return
  }
  if (!articleData.content.trim()) {
    ElMessage.warning('文章内容不能为空')
    return
  }
  if (articleData.tagIds === []) {
    ElMessage.warning('请添加标签')
    return
  }
  if (articleData.categoryIds === []) {
    ElMessage.warning('请添加分类')
    return
  }
  articleData.status = status

  const loading = ElLoading.service({
    lock: true,
    text: status === 0 ? '正在发布文章...' : '正在保存草稿...'
  })
  console.log(articleData)
  if (sStore.isEditMode) {
    updateArticle(sStore.editorArticleId, articleData)
        .then(res => {
          ElMessage.success(status === 0 ? '文章重发布成功' : '草稿修改成功')
          sStore.isEditMode = false
          router.back()
        })
        .catch(err => {
          ElMessage.warning('操作失败，稍后重试。')
        })
        .finally(() => {
          loading.close()
        })
  } else {
    addArticle(articleData)
        .then(res => {
          ElMessage.success(status === 0 ? '文章发布成功' : '草稿保存成功')
          sStore.isEditMode = false
          router.back()
        })
        .catch(err => {
          ElMessage.warning('操作失败，稍后重试。')
        })
        .finally(() => {
          loading.close()
        })
  }
}

// 设置是否是旧值
const isEditor = async () => {
  if (route.path === '/editor') {
    isEditMode.value = false
    return
  }

  if (route.path === '/editor-edit') {
    isEditMode.value = true
  }

  if (isEditMode.value) {
    try {
      const [articleRes, tagsRes, categoriesRes] = await Promise.all([
        getArticleById(sStore.editorArticleId),
        getPostTags(sStore.editorArticleId),
        getArticleCategories(sStore.editorArticleId)
      ])

      // 一次性赋值，减少响应式更新次数
      Object.assign(articleData, {
        title: articleRes.title,
        content: articleRes.content,
        fileId: articleRes.fileId,
        status: articleRes.status
      })

      coverImageData.fileId = articleRes.fileId
      coverImageData.accessUrl = sStore.baseURL + articleRes.coverUrl
      tags.value = tagsRes.data
      articleData.categoryIds = categoriesRes.data.map(category => category.categoryId)
      console.log(articleData)
    } catch (error) {
      console.error('获取文章数据失败:', error)
      ElMessage.error('加载文章失败')
    }
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

// watch(() => articleData.categoryIds,
//     (newDate) => {
//       console.log(newDate)
//       console.log(articleData)
//     }
// )

</script>

<template>
  <Live2DViewer/>
  <el-container>
    <el-header>
      <HomeHeader/>
    </el-header>
    <el-main>
      <div class="editor-container">
        <!-- 编辑器头部 -->
        <EditorHeader
            v-model:title="articleData.title"
            :is-edit-mode="isEditMode"
            @back="goBack"
            @save="saveArticle"
        />

        <!-- 编辑器主体 -->
        <tiptap-editor v-model:html="articleData.content"/>
        <!--类别选择器-->
        <CategorySelector
            v-model:selected="articleData.categoryIds"
            class="category-selector-container"
        />
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
    </el-main>
  </el-container>
</template>

<style scoped>
.editor-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;

  .tag-selector-container {
    margin-bottom: 20px;
  }

  .category-selector-container {
    margin-bottom: 20px;
  }
}
</style>