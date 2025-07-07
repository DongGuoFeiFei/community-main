<template>
  <div class="user-profile">
    <el-card shadow="hover" class="profile-card">
      <!-- 头部标题和按钮 -->
      <template #header>
        <div class="card-header">
          <h2 class="title">个人信息</h2>
          <div class="header-actions">
            <el-button
                type="primary"
                size="small"
                @click="submitForm"
                :loading="loading"
            >
              保存修改
            </el-button>
          </div>
        </div>
      </template>

      <!-- 主要内容 -->
      <div class="profile-content">
        <!-- 左侧头像区域 -->
        <div class="avatar-section">
          <el-upload
              class="avatar-uploader"
              action=""
              :show-file-list="false"
              :auto-upload="false"
              :on-change="handleAvatarChange"
          >
            <el-avatar :size="120" :src="avatarPreview || lStore.baseURL + lStore.userInfo.avatarUrl"/>
            <div class="avatar-edit">
              <el-icon>
                <Camera/>
              </el-icon>
              <span>更换头像</span>
            </div>
          </el-upload>
          <p class="upload-tip">支持 JPG/PNG 格式，大小不超过 5MB</p>
        </div>

        <!-- 右侧表单区域 -->
        <div class="form-section">
          <el-form
              ref="profileForm"
              :model="formData"
              :rules="formRules"
              label-width="100px"
              label-position="left"
          >
            <el-form-item label="用户名" prop="username">
              <el-input v-model="formData.username" disabled/>
            </el-form-item>

            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="formData.nickname" placeholder="请输入昵称"/>
            </el-form-item>

            <el-form-item label="邮箱" prop="email">
              <el-input v-model="formData.email" placeholder="请输入邮箱"/>
            </el-form-item>

            <el-form-item label="手机号" prop="phone">
              <el-input v-model="formData.phone" placeholder="请输入手机号"/>
            </el-form-item>

            <el-form-item label="注册时间">
              <el-input
                  :value="formatDate(lStore.userInfo.userInfo.createTime)"
                  disabled
              />
            </el-form-item>

            <el-form-item label="最后登录">
              <el-input
                  :value="formatDate(lStore.userInfo.userInfo.lastLogin)"
                  disabled
              />
            </el-form-item>
          </el-form>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue'
import {localStores} from '@/stores/localStores.js'
import {Camera} from '@element-plus/icons-vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import dayjs from 'dayjs'
import {updateUserCoverId, updateUserProfile, uploadFile} from "../../../../../../community-admin/src/api/index.js";

const lStore = localStores()
const loading = ref(false)

// 表单数据
const formData = reactive({
  username: '',
  nickname: '',
  email: '',
  phone: ''
})

// 头像预览
const avatarPreview = ref(null)

// 表单验证规则
const formRules = reactive({
  nickname: [
    {required: true, message: '请输入昵称', trigger: 'blur'},
    {min: 2, max: 16, message: '长度在 2 到 16 个字符', trigger: 'blur'}
  ],
  email: [
    {type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}
  ],
  phone: [
    {pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur'}
  ]
})

// 初始化表单数据
const initFormData = () => {
  const {userInfo} = lStore.userInfo
  formData.username = userInfo.username
  formData.nickname = userInfo.nickname
  formData.email = userInfo.email
  formData.phone = userInfo.phone
}

// 头像更改处理
const handleAvatarChange = async (file) => {
  const isImage = file.raw.type.startsWith('image/');
  const isLt5M = file.raw.size / 1024 / 1024 < 5;

  if (!isImage) {
    ElMessage.error('只能上传图片文件!');
    return false;
  }
  if (!isLt5M) {
    ElMessage.error('头像图片大小不能超过 5MB!');
    return false;
  }

  const formData = new FormData();
  formData.append("file", file.raw);

  try {
    const res = await uploadFile(formData);
    lStore.userInfo.avatarUrl = res.data.accessUrl;

    await updateUserCoverId(res.data.fileId);
    ElMessage.success("头像更换成功。");
  } catch (error) {
    ElMessage.error("上传失败：" + error.message);
  }
};

// 日期格式化
const formatDate = (timestamp) => {
  if (!timestamp) return '未知'
  return dayjs(timestamp).format('YYYY-MM-DD HH:mm:ss')
}

// 提交表单
const submitForm = async () => {
  await ElMessageBox.confirm('确定修改吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })

  loading.value = true
  try {
    // 这里添加保存逻辑
    const res = await updateUserProfile(formData)
    lStore.userInfo.userInfo = {
      ...res.data
    }
    ElMessage.success('个人信息已更新')
  } catch (error) {
    ElMessage.error('保存失败: ' + error.message)
  } finally {
    loading.value = false
  }
}

// 组件挂载时初始化数据
onMounted(() => {
  initFormData()
})
</script>

<style lang="less" scoped>
/* 头部布局调整 */
.card-header {
  display: flex;
  justify-content: space-between; /* 标题和按钮左右对齐 */
  align-items: center; /* 垂直居中 */
  width: 100%; /* 确保占满整个头部 */

  .title {
    margin: 0;
    font-size: 18px;
    font-weight: 600;
    color: #303133;
  }

  .header-actions {
    display: flex;
    gap: 10px; /* 如果有多个按钮，增加间距 */
  }
}


.user-profile {
  padding: 20px;
  box-sizing: border-box;
  background-color: #f5f7fa;
  display: flex;
  flex-direction: column;
  min-height: 500px;

  .profile-card {
    flex: 1;
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
    display: flex;
    flex-direction: column;
    overflow: hidden;

    /* 头部样式微调 */

    :deep(.el-card__header) {
      background: linear-gradient(135deg, #f9fafc, #eef2f6);
      border-bottom: none;
      padding: 16px 24px;
    }

    .card-header {
      .title {
        color: #303133;
        font-size: 20px;
        font-weight: 600;
      }

      .el-button {
        border-radius: 6px;
      }
    }

    /* 内容区美化 */

    .profile-content {
      flex: 1;
      display: flex;
      padding: 24px;
      gap: 24px; /* 增加左右间距 */

      /* 头像区域 */

      .avatar-section {
        display: flex;
        flex-direction: column;
        align-items: center;
        flex-shrink: 0;
        width: 240px;

        .avatar-uploader {
          margin-bottom: 16px;

          .el-avatar {
            transition: transform 0.3s;

            &:hover {
              transform: scale(1.05);
            }
          }
        }

        .avatar-edit {
          color: var(--el-color-primary);
          cursor: pointer;

          &:hover {
            opacity: 0.8;
          }
        }

        .upload-tip {
          color: var(--el-text-color-secondary);
          font-size: 12px;
        }
      }

      /* 表单区域 */

      .form-section {
        flex: 1;

        :deep(.el-form-item__label) {
          color: #606266;
        }

        .el-input {
          border-radius: 6px;
        }
      }
    }
  }
}
</style>