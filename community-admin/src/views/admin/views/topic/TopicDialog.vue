<template>
  <el-dialog
      v-model="visible"
      :title="dialogTitle"
      width="50%"
      :before-close="handleClose"
  >
    <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
      <el-form-item label="话题标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入话题标题" />
      </el-form-item>
      <el-form-item label="话题描述" prop="description">
        <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入话题描述"
        />
      </el-form-item>
      <el-form-item label="封面图片" prop="cover">
        <el-upload
            class="avatar-uploader"
            action="/api/upload"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
        >
          <img v-if="form.cover" :src="form.cover" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio label="published">已发布</el-radio>
          <el-radio label="draft">草稿</el-radio>
          <el-radio label="offline">已下架</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue';
import { ElMessage } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';

const props = defineProps({
  modelValue: Boolean,
  dialogTitle: String,
  topicData: Object
});

const emit = defineEmits(['update:modelValue', 'confirm']);

const visible = ref(props.modelValue);
const formRef = ref(null);
const form = ref({
  id: '',
  title: '',
  description: '',
  cover: '',
  status: 'published'
});

const rules = {
  title: [
    { required: true, message: '请输入话题标题', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入话题描述', trigger: 'blur' },
    { min: 10, max: 200, message: '长度在 10 到 200 个字符', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
};

watch(() => props.modelValue, (val) => {
  visible.value = val;
});

watch(() => props.topicData, (val) => {
  if (val) {
    form.value = { ...val };
  } else {
    form.value = {
      id: '',
      title: '',
      description: '',
      cover: '',
      status: 'published'
    };
  }
}, { immediate: true });

watch(visible, (val) => {
  emit('update:modelValue', val);
});

const handleClose = () => {
  visible.value = false;
};

const handleSubmit = async () => {
  try {
    await formRef.value.validate();
    emit('confirm', form.value);
    visible.value = false;
  } catch (error) {
    console.log('表单验证失败');
  }
};

const beforeUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isJPG) {
    ElMessage.error('上传图片只能是 JPG/PNG 格式!');
  }
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB!');
  }
  return isJPG && isLt2M;
};

const handleUploadSuccess = (res) => {
  if (res.code === 200) {
    form.value.cover = res.data.url;
    ElMessage.success('上传成功');
  } else {
    ElMessage.error(res.msg || '上传失败');
  }
};
</script>

<style lang="scss" scoped>
.avatar-uploader {
  :deep(.el-upload) {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
  }

  :deep(.el-upload:hover) {
    border-color: var(--el-color-primary);
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