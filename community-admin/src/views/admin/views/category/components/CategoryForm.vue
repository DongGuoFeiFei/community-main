<template>
  <el-dialog
    v-model="visible"
    :title="mode === 'add' ? '添加分类' : '编辑分类'"
    width="600px"
    :before-close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="100px"

    >
      <el-form-item label="分类名称" prop="category_name">
        <el-input v-model="formData.categoryName" placeholder="请输入分类名称" />
      </el-form-item>

      <el-form-item label="URL标识" prop="category_slug">
        <el-input v-model="formData.categorySlug" placeholder="请输入URL标识" />
      </el-form-item>

      <el-form-item label="父级分类" prop="parent_id">
        <category-selector
          v-model="formData.parentId"
          :exclude-id="formData.categoryId"
          placeholder="请选择父级分类"
        />
      </el-form-item>

      <el-form-item label="排序" prop="sort_order">
        <el-input-number v-model="formData.sortOrder" :min="0" />
      </el-form-item>

      <el-form-item label="状态" prop="status">
        <el-switch
          v-model="formData.status"
          :active-value="1"
          :inactive-value="0"
        />
      </el-form-item>

      <el-form-item label="描述" prop="description">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="3"
          placeholder="请输入分类描述"
        />
      </el-form-item>

      <el-form-item label="封面图" prop="cover_url">
        <el-upload
          class="avatar-uploader"
          action="/api/upload"
          :show-file-list="false"
          :on-success="handleUploadSuccess"
          :before-upload="beforeUpload"
        >
          <img v-if="formData.coverUrl" :src="formData.coverUrl" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue';
import { ElMessage } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import { addCategory, updateCategory } from '@/api/category';
import CategorySelector from './CategorySelector.vue';

const props = defineProps({
  modelValue: Boolean,
  formData: {
    type: Object,
    required: true
  },
  mode: {
    type: String,
    default: 'add',
    validator: (value) => ['add', 'edit'].includes(value)
  }
});

const emit = defineEmits(['update:modelValue', 'success']);

const visible = ref(false);
const formRef = ref(null);

const rules = {
  categoryName: [
    { required: true, message: '请输入分类名称', trigger: 'blur' },
    { max: 100, message: '长度不能超过100个字符', trigger: 'blur' }
  ],
  categorySlug: [
    { required: true, message: '请输入URL标识', trigger: 'blur' },
    { pattern: /^[a-z0-9-]+$/, message: '只能包含小写字母、数字和连字符', trigger: 'blur' }
  ]
};

// 处理对话框显示/隐藏
watch(() => props.modelValue, (val) => {
  visible.value = val;
});

watch(visible, (val) => {
  emit('update:modelValue', val);
});

// 关闭对话框
const handleClose = () => {
  visible.value = false;
};

// 上传成功处理
const handleUploadSuccess = (response) => {
  if (response.code === 200) {
    formData.value.coverUrl = response.data.url;
  } else {
    ElMessage.error(response.msg || '上传失败');
  }
};

// 上传前校验
const beforeUpload = (file) => {
  const isImage = ['image/jpeg', 'image/png', 'image/gif'].includes(file.type);
  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isImage) {
    ElMessage.error('只能上传图片文件!');
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过2MB!');
  }

  return isImage && isLt2M;
};

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate();

    if (props.mode === 'add') {
      await addCategory(props.formData);
      ElMessage.success('添加成功');
    } else {
      await updateCategory(props.formData);
      ElMessage.success('更新成功');
    }

    emit('success');
    handleClose();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '操作失败');
    }
  }
};
</script>

<style lang="scss" scoped>
.avatar-uploader {
  :deep(.el-upload) {
    border: 1px dashed var(--el-border-color);
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);

    &:hover {
      border-color: var(--el-color-primary);
    }
  }

  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }

  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
}
</style>
