<template>
  <el-dialog
      v-model="visible"
      :title="title"
      width="80%"
      top="5vh"
      destroy-on-close
  >
    <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        label-position="top"
    >
      <el-row :gutter="20">
        <el-col :span="16">
          <el-form-item label="标题" prop="title">
            <el-input v-model="form.title" placeholder="请输入标题" />
          </el-form-item>

          <el-form-item label="内容" prop="content">
            <MarkdownEditor
                v-model="form.content"
                height="500px"
            />
          </el-form-item>
        </el-col>

        <el-col :span="8">
          <el-form-item label="分类" prop="categoryId">
            <el-select
                v-model="form.categoryId"
                placeholder="请选择分类"
                style="width: 100%"
            >
              <el-option
                  v-for="item in categories"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="标签" prop="tagIds">
            <el-select
                v-model="form.tagIds"
                multiple
                placeholder="请选择标签"
                style="width: 100%"
            >
              <el-option
                  v-for="item in tags"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="封面" prop="cover">
            <ImageUpload v-model="form.cover" />
          </el-form-item>

          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio :label="0">草稿</el-radio>
              <el-radio :label="1">发布</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="摘要" prop="summary">
            <el-input
                v-model="form.summary"
                type="textarea"
                :rows="4"
                placeholder="请输入摘要"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="handleSubmit">保存</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { createArticle, updateArticle } from '@/api/article'
import { getCategories } from '@/api/article/category'
import { getTags } from '@/api/article/tag'
import MarkdownEditor from '@/components/MarkdownEditor.vue'
import ImageUpload from '@/components/ImageUpload.vue'
import { useArticleStore } from '@/stores/articleStore'

const props = defineProps({
  modelValue: Boolean,
  article: Object
})

const emit = defineEmits(['update:modelValue', 'success'])

const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const title = computed(() => (props.article ? '编辑文章' : '新增文章'))

const formRef = ref(null)
const form = reactive({
  id: null,
  title: '',
  content: '',
  summary: '',
  cover: '',
  categoryId: null,
  tagIds: [],
  status: 0
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }]
}

const categories = ref([])
const tags = ref([])

const articleStore = useArticleStore()

const fetchCategories = async () => {
  const res = await getCategories()
  categories.value = res.data
}

const fetchTags = async () => {
  const res = await getTags()
  tags.value = res.data
}

watch(visible, (val) => {
  if (val) {
    fetchCategories()
    fetchTags()

    if (props.article) {
      Object.assign(form, {
        id: props.article.id,
        title: props.article.title,
        content: props.article.content,
        summary: props.article.summary,
        cover: props.article.cover,
        categoryId: props.article.category?.id,
        tagIds: props.article.tags?.map(tag => tag.id) || [],
        status: props.article.status
      })
    } else {
      formRef.value?.resetFields()
    }
  }
})

const handleSubmit = async () => {
  try {
    await formRef.value.validate()

    const params = {
      ...form,
      tagIds: form.tagIds.join(',')
    }

    if (form.id) {
      await updateArticle(params)
      ElMessage.success('更新成功')
    } else {
      await createArticle(params)
      ElMessage.success('创建成功')
    }

    emit('success')
    visible.value = false
  } catch (error) {
    console.error(error)
  }
}
</script>

<style lang="scss" scoped>
:deep(.el-dialog__body) {
  padding-top: 10px;
}
</style>