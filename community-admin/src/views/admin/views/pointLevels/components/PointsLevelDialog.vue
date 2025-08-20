<template>
  <el-dialog
    :model-value="modelValue"
    :title="dialogType === 'add' ? '新增等级' : '编辑等级'"
    width="700px"
    @update:modelValue="$emit('update:modelValue', $event)"
    @close="handleClose"
  >
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="等级名称" prop="levelName">
        <el-input v-model="form.levelName" placeholder="请输入等级名称"/>
      </el-form-item>
      <el-form-item label="等级图标" prop="levelIcon">
        <el-upload
          class="avatar-uploader"
          action="/api/upload"
          :show-file-list="false"
          :on-success="handleUploadSuccess"
          :before-upload="beforeUpload"
        >
          <img v-if="form.levelIcon" :src="form.levelIcon" class="avatar"/>
          <el-icon v-else class="avatar-uploader-icon">
            <Plus/>
          </el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item label="最小经验值" prop="minExperience">
        <el-input-number
          v-model="form.minExperience"
          :min="0"
          :max="1000000"
          controls-position="right"
        />
      </el-form-item>
      <el-form-item label="最大经验值" prop="maxExperience">
        <el-input-number
          v-model="form.maxExperience"
          :min="0"
          :max="1000000"
          controls-position="right"
        />
      </el-form-item>
      <el-form-item label="等级特权" prop="privileges">
        <el-button type="primary" @click="showPrivilegeDialog">添加特权</el-button>
        <div v-if="form.privileges && form.privileges.length > 0" class="privilege-list">
          <div v-for="(item, index) in form.privileges" :key="index" class="privilege-item">
            <span>{{ item.name }}: {{ item.description }}</span>
            <el-icon class="delete-icon" @click="removePrivilege(index)">
              <Close/>
            </el-icon>
          </div>
        </div>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </template>

    <PrivilegeDialog
      :model-value="privilegeDialogVisible"
      @update:modelValue="privilegeDialogVisible = $event"
      @add-privilege="handleAddPrivilege"
    />
  </el-dialog>
</template>

<script setup>
import {ref, watch} from 'vue'
import {Close, Plus} from '@element-plus/icons-vue'

import PrivilegeDialog from './PrivilegeDialog.vue'
import {addPointsLevel, updatePointsLevel} from "@/api/levels.js";
import {ElMessage} from "element-plus";

const props = defineProps({
  modelValue: Boolean,
  currentLevel: Object,
  dialogType: String
})

const emit = defineEmits(['update:modelValue', 'refresh'])

const formRef = ref(null)
const privilegeDialogVisible = ref(false)
const form = ref({
  levelName: '',
  levelIcon: '',
  minExperience: 0,
  maxExperience: 0,
  privileges: []
})

const rules = ref({
  levelName: [
    {required: true, message: '请输入等级名称', trigger: 'blur'}
  ],
  minExperience: [
    {required: true, message: '请输入最小经验值', trigger: 'blur'}
  ],
  maxExperience: [
    {required: true, message: '请输入最大经验值', trigger: 'blur'},
    {
      validator: (rule, value, callback) => {
        if (value <= form.value.minExperience) {
          callback(new Error('最大经验值必须大于最小经验值'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
})

watch(() => props.currentLevel, (val) => {
  if (val && Object.keys(val).length > 0) {
    form.value = {
      ...val,
      privileges: val.privileges ? JSON.parse(val.privileges) : []
    }
  }
}, {immediate: true})

const handleClose = () => {
  emit('update:modelValue', false)
  formRef.value?.resetFields()
}

const handleAddPrivilege = (param) => {
  console.log(param)
}
const handleSubmit = async () => {
  try {
    await formRef.value.validate()

    const submitData = {
      ...form.value,
      privileges: JSON.stringify(form.value.privileges)
    }

    if (props.dialogType === 'add') {
      await addPointsLevel(submitData)
      ElMessage.success('新增成功')
    } else {
      await updatePointsLevel(submitData)
      ElMessage.success('更新成功')
    }

    emit('refresh')
    handleClose()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('提交失败:', error)
    }
  }
}

const handleUploadSuccess = (res) => {
  if (res.code === 200) {
    form.value.levelIcon = res.data.url
    ElMessage.success('上传成功')
  } else {
    ElMessage.error('上传失败')
  }
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过2MB')
    return false
  }
  return true
}

const showPrivilegeDialog = () => {
  privilegeDialogVisible.value = true
}

const addPrivilege = (privilege) => {
  form.value.privileges.push(privilege)
}

const removePrivilege = (index) => {
  form.value.privileges.splice(index, 1)
}
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
    width: 120px;
    height: 120px;
    line-height: 120px;
    text-align: center;
  }

  .avatar {
    width: 120px;
    height: 120px;
    display: block;
  }
}

.privilege-list {
  margin-top: 10px;
  max-height: 200px;
  overflow-y: auto;

  .privilege-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 8px;
    margin-bottom: 8px;
    background-color: #f5f7fa;
    border-radius: 4px;

    .delete-icon {
      cursor: pointer;
      color: #f56c6c;

      &:hover {
        color: #f78989;
      }
    }
  }
}
</style>
