<template>
  <el-dialog
      v-model="visible"
      :title="mode === 'create' ? '新增活动' : '编辑活动'"
      width="50%"
  >
    <el-form
        ref="formRef"
        :model="formData"
        label-width="100px"
        :rules="rules"
    >
      <el-form-item label="活动名称" prop="title">
        <el-input v-model="formData.title" placeholder="请输入活动名称" />
      </el-form-item>

      <el-form-item label="活动封面" prop="coverImage">
        <el-upload
            class="avatar-uploader"
            action="/api/upload"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
        >
          <img v-if="formData.coverImage" :src="formData.coverImage" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>

      <el-form-item label="活动内容" prop="content">
        <el-input
            v-model="formData.content"
            type="textarea"
            :rows="4"
            placeholder="请输入活动内容"
        />
      </el-form-item>

      <el-form-item label="开始时间" prop="startTime">
        <el-date-picker
            v-model="formData.startTime"
            type="datetime"
            placeholder="选择开始时间"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>

      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker
            v-model="formData.endTime"
            type="datetime"
            placeholder="选择结束时间"
            value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>

      <el-form-item label="活动地点" prop="location">
        <el-input v-model="formData.location" placeholder="请输入活动地点" />
      </el-form-item>

      <el-form-item label="最大人数" prop="maxParticipants">
        <el-input-number
            v-model="formData.maxParticipants"
            :min="1"
            :max="1000"
        />
      </el-form-item>

      <el-form-item label="活动状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio :value="1">进行中</el-radio>
          <el-radio :value="0">已结束</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue';
import { Plus } from '@element-plus/icons-vue';
import { createActivity, updateActivity } from '@/api/activity';
import { ElMessage } from 'element-plus';

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  formData: {
    type: Object,
    default: () => ({})
  },
  mode: {
    type: String,
    default: 'create'
  }
});

const emit = defineEmits(['update:modelValue', 'submit']);

const visible = ref(false);
const formRef = ref(null);

watch(() => props.modelValue, (val) => {
  visible.value = val;
});

watch(visible, (val) => {
  emit('update:modelValue', val);
});

const rules = {
  title: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
  content: [{ required: true, message: '请输入活动内容', trigger: 'blur' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  location: [{ required: true, message: '请输入活动地点', trigger: 'blur' }]
};

const handleUploadSuccess = (response) => {
  props.formData.coverImage = response.data.url;
};

const handleSubmit = async () => {
  try {
    await formRef.value.validate();

    if (props.mode === 'create') {
      await createActivity(props.formData);
      ElMessage.success('创建成功');
    } else {
      await updateActivity(props.formData);
      ElMessage.success('更新成功');
    }

    emit('submit');
    visible.value = false;
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(props.mode === 'create' ? '创建失败' : '更新失败');
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
    text-align: center;
    line-height: 178px;
  }

  .avatar {
    width: 178px;
    height: 178px;
    display: block;
    object-fit: cover;
  }
}
</style>
