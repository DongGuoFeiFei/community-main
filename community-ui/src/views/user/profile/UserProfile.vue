<template>
  <div class="user-profile">
    <!-- 欢迎横幅 -->
    <div class="welcome-banner">
      <div class="banner-decoration-left">
        <span class="deco-emoji">🌸</span>
        <span class="deco-emoji">✨</span>
      </div>
      <div class="banner-content">
        <h1 class="banner-title">个人中心</h1>
        <p class="banner-subtitle">管理你的个人信息</p>
      </div>
      <div class="banner-decoration-right">
        <span class="deco-emoji">💖</span>
        <span class="deco-emoji">🎀</span>
      </div>
    </div>

    <!-- 主卡片 -->
    <div class="profile-card">
      <!-- 卡片头部 -->
      <div class="card-header">
        <div class="header-left">
          <div class="header-icon">👤</div>
          <h2 class="title">个人信息</h2>
        </div>
        <div class="header-actions">
          <el-button
            type="primary"
            size="default"
            @click="submitForm"
            :loading="loading"
            class="save-btn"
          >
            <el-icon class="btn-icon"><Check /></el-icon>
            保存修改
          </el-button>
        </div>
      </div>

      <!-- 卡片内容 -->
      <div class="profile-content">
        <!-- 左侧头像区域 -->
        <div class="avatar-section">
          <div class="avatar-container">
            <el-upload
              class="avatar-uploader"
              action=""
              :show-file-list="false"
              :auto-upload="false"
              :on-change="handleAvatarChange"
            >
              <div class="avatar-wrapper">
                <el-avatar 
                  :size="140" 
                  :src="avatarPreview || lStore.baseURL + lStore.userInfo.avatarUrl"
                  class="user-avatar"
                />
                <div class="avatar-overlay">
                  <el-icon class="camera-icon"><Camera /></el-icon>
                  <span class="overlay-text">更换头像</span>
                </div>
              </div>
            </el-upload>
            <div class="avatar-decoration">
              <span class="deco-star">⭐</span>
              <span class="deco-star">✨</span>
              <span class="deco-star">⭐</span>
            </div>
          </div>
          <p class="upload-tip">
            <el-icon><Picture /></el-icon>
            支持 JPG/PNG 格式，大小不超过 5MB
          </p>
        </div>

        <!-- 右侧表单区域 -->
        <div class="form-section">
          <el-form
            ref="profileForm"
            :model="formData"
            :rules="formRules"
            label-width="100px"
            label-position="left"
            class="profile-form"
          >
            <el-form-item label="用户名" prop="username">
              <el-input 
                v-model="formData.username" 
                disabled
                prefix-icon="User"
              />
            </el-form-item>

            <el-form-item label="昵称" prop="nickname">
              <el-input 
                v-model="formData.nickname" 
                placeholder="请输入昵称"
                prefix-icon="Edit"
              />
            </el-form-item>

            <el-form-item label="邮箱" prop="email">
              <el-input 
                v-model="formData.email" 
                placeholder="请输入邮箱"
                prefix-icon="Message"
              />
            </el-form-item>

            <el-form-item label="手机号" prop="phone">
              <el-input 
                v-model="formData.phone" 
                placeholder="请输入手机号"
                prefix-icon="Phone"
              />
            </el-form-item>

            <el-form-item label="注册时间">
              <el-input
                :value="formatDate(lStore.userInfo.userInfo.createTime)"
                disabled
                prefix-icon="Calendar"
              />
            </el-form-item>

            <el-form-item label="最后登录">
              <el-input
                :value="formatDate(lStore.userInfo.userInfo.lastLogin)"
                disabled
                prefix-icon="Clock"
              />
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue'
import {localStores} from '@/stores/localStores.js'
import {Camera, Check, Picture} from '@element-plus/icons-vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import dayjs from 'dayjs'
import {uploadFile} from "@/api/index.js";
import {updateUserCoverId, updateUserProfile} from "@/api/user.js";

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
    lStore.userInfo.userInfo.username = formData.username
    lStore.userInfo.userInfo.nickname = formData.nickname
    lStore.userInfo.userInfo.email = formData.email
    lStore.userInfo.userInfo.phone = formData.phone

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

<style lang="scss" scoped>
// 动漫风格配色
$sky-blue: #87ceeb;
$mint-blue: #b0e0e6;
$lavender: #e6e6fa;
$light-purple: #dda0dd;
$mint-green: #98fb98;
$pink-accent: #ffb6c1;

.user-profile {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 30px;
  // 禁止内部滚动
  overflow: visible;
  height: auto;

  // 欢迎横幅
  .welcome-banner {
    position: relative;
    padding: 30px 40px;
    background: rgba(255, 255, 255, 0.85);
    backdrop-filter: blur(20px);
    border-radius: 25px;
    border: 3px solid rgba(255, 255, 255, 0.6);
    box-shadow: 0 10px 40px rgba(135, 206, 235, 0.25),
      inset 0 2px 10px rgba(255, 255, 255, 0.7);
    display: flex;
    align-items: center;
    justify-content: space-between;
    animation: bannerSlideIn 0.8s cubic-bezier(0.34, 1.56, 0.64, 1);
    overflow: visible;

    &::before {
      content: "";
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 4px;
      background: linear-gradient(90deg, $sky-blue, $light-purple, $mint-green, $sky-blue);
      background-size: 200% 100%;
      animation: gradientMove 3s linear infinite;
      border-radius: 25px 25px 0 0;
    }

    .banner-decoration-left,
    .banner-decoration-right {
      display: flex;
      gap: 15px;

      .deco-emoji {
        font-size: 28px;
        animation: decoFloat 2.5s ease-in-out infinite;
        filter: drop-shadow(0 4px 10px rgba(135, 206, 235, 0.3));

        &:nth-child(1) {
          animation-delay: 0s;
        }

        &:nth-child(2) {
          animation-delay: 0.5s;
        }
      }
    }

    .banner-content {
      text-align: center;
      flex: 1;

      .banner-title {
        margin: 0 0 8px 0;
        font-size: 32px;
        font-weight: 800;
        background: linear-gradient(135deg, #5b9bd5, #9370db, #84c7d0);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
        animation: titleGlow 2s ease-in-out infinite;
      }

      .banner-subtitle {
        margin: 0;
        font-size: 15px;
        color: #5b9bd5;
        font-weight: 500;
        letter-spacing: 1px;
      }
    }
  }

  // 主卡片
  .profile-card {
    background: rgba(255, 255, 255, 0.9);
    backdrop-filter: blur(25px);
    border-radius: 30px;
    border: 3px solid rgba(255, 255, 255, 0.7);
    box-shadow: 0 15px 50px rgba(135, 206, 235, 0.2),
      inset 0 2px 15px rgba(255, 255, 255, 0.8);
    animation: cardSlideIn 1s cubic-bezier(0.34, 1.56, 0.64, 1);
    overflow: visible;

    // 卡片头部
    .card-header {
      padding: 25px 35px;
      background: linear-gradient(135deg, 
        rgba(135, 206, 235, 0.15) 0%,
        rgba(230, 230, 250, 0.15) 100%
      );
      border-bottom: 2px solid rgba(135, 206, 235, 0.2);
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-radius: 27px 27px 0 0;

      .header-left {
        display: flex;
        align-items: center;
        gap: 15px;

        .header-icon {
          width: 45px;
          height: 45px;
          background: linear-gradient(135deg, $sky-blue, $light-purple);
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 24px;
          box-shadow: 0 5px 15px rgba(135, 206, 235, 0.3);
          animation: iconPulse 2s ease-in-out infinite;
        }

        .title {
          margin: 0;
          font-size: 24px;
          font-weight: 700;
          background: linear-gradient(135deg, #5b9bd5, #9370db);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          background-clip: text;
        }
      }

      .header-actions {
        .save-btn {
          padding: 12px 28px;
          border-radius: 20px;
          font-size: 15px;
          font-weight: 600;
          background: linear-gradient(135deg, $sky-blue, $mint-blue);
          border: none;
          box-shadow: 0 5px 20px rgba(135, 206, 235, 0.4);
          transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);

          .btn-icon {
            margin-right: 6px;
          }

          &:hover {
            transform: translateY(-3px) scale(1.05);
            box-shadow: 0 8px 30px rgba(135, 206, 235, 0.6);
          }

          &:active {
            transform: translateY(-1px) scale(1.02);
          }
        }
      }
    }

    // 卡片内容
    .profile-content {
      padding: 40px 35px;
      display: flex;
      gap: 50px;
      overflow: visible;

      // 左侧头像区域
      .avatar-section {
        display: flex;
        flex-direction: column;
        align-items: center;
        flex-shrink: 0;
        width: 260px;

        .avatar-container {
          position: relative;
          margin-bottom: 20px;

          .avatar-uploader {
            .avatar-wrapper {
              position: relative;
              cursor: pointer;

              .user-avatar {
                border: 5px solid rgba(255, 255, 255, 0.9);
                box-shadow: 0 10px 35px rgba(135, 206, 235, 0.3),
                  inset 0 2px 10px rgba(255, 255, 255, 0.5);
                transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
              }

              .avatar-overlay {
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background: rgba(135, 206, 235, 0.85);
                backdrop-filter: blur(5px);
                border-radius: 50%;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                opacity: 0;
                transition: all 0.3s ease;
                color: white;

                .camera-icon {
                  font-size: 32px;
                  margin-bottom: 8px;
                }

                .overlay-text {
                  font-size: 14px;
                  font-weight: 600;
                }
              }

              &:hover {
                .user-avatar {
                  transform: scale(1.08) rotate(5deg);
                  box-shadow: 0 15px 50px rgba(135, 206, 235, 0.5);
                }

                .avatar-overlay {
                  opacity: 1;
                }
              }
            }
          }

          .avatar-decoration {
            position: absolute;
            top: -15px;
            right: -15px;
            display: flex;
            gap: 8px;

            .deco-star {
              font-size: 20px;
              animation: starTwinkle 2s ease-in-out infinite;
              filter: drop-shadow(0 2px 8px rgba(255, 215, 0, 0.5));

              &:nth-child(1) {
                animation-delay: 0s;
              }

              &:nth-child(2) {
                animation-delay: 0.5s;
              }

              &:nth-child(3) {
                animation-delay: 1s;
              }
            }
          }
        }

        .upload-tip {
          display: flex;
          align-items: center;
          gap: 8px;
          color: #5b9bd5;
          font-size: 13px;
          padding: 12px 20px;
          background: rgba(135, 206, 235, 0.1);
          border-radius: 15px;
          border: 2px dashed rgba(135, 206, 235, 0.3);
          margin: 0;

          .el-icon {
            font-size: 16px;
          }
        }
      }

      // 右侧表单区域
      .form-section {
        flex: 1;
        overflow: visible;

        .profile-form {
          :deep(.el-form-item) {
            margin-bottom: 28px;

            .el-form-item__label {
              color: #5b9bd5;
              font-weight: 600;
              font-size: 15px;
            }

            .el-input {
              .el-input__wrapper {
                border-radius: 15px;
                padding: 8px 15px;
                background: rgba(255, 255, 255, 0.8);
                border: 2px solid rgba(135, 206, 235, 0.2);
                box-shadow: 0 4px 15px rgba(135, 206, 235, 0.1),
                  inset 0 2px 8px rgba(255, 255, 255, 0.5);
                transition: all 0.3s ease;

                &:hover {
                  border-color: rgba(135, 206, 235, 0.4);
                  box-shadow: 0 6px 20px rgba(135, 206, 235, 0.2);
                }

                &.is-focus {
                  border-color: $sky-blue;
                  box-shadow: 0 8px 25px rgba(135, 206, 235, 0.3);
                }
              }

              &.is-disabled {
                .el-input__wrapper {
                  background: rgba(230, 230, 250, 0.3);
                  border-color: rgba(135, 206, 235, 0.15);
                }
              }
            }
          }
        }
      }
    }
  }
}

// 动画定义
@keyframes bannerSlideIn {
  from {
    opacity: 0;
    transform: translateY(-30px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

@keyframes cardSlideIn {
  from {
    opacity: 0;
    transform: translateY(30px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

@keyframes gradientMove {
  0% {
    background-position: 0% 0%;
  }
  100% {
    background-position: 200% 0%;
  }
}

@keyframes titleGlow {
  0%, 100% {
    filter: drop-shadow(0 0 10px rgba(135, 206, 235, 0.5));
  }
  50% {
    filter: drop-shadow(0 0 20px rgba(135, 206, 235, 0.8));
  }
}

@keyframes decoFloat {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-12px) rotate(10deg);
  }
}

@keyframes iconPulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

@keyframes starTwinkle {
  0%, 100% {
    opacity: 0.6;
    transform: scale(1) rotate(0deg);
  }
  50% {
    opacity: 1;
    transform: scale(1.3) rotate(180deg);
  }
}

// 响应式设计
@media (max-width: 992px) {
  .user-profile {
    .profile-card {
      .profile-content {
        flex-direction: column;
        align-items: center;
        gap: 30px;

        .avatar-section {
          width: 100%;
        }

        .form-section {
          width: 100%;
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .user-profile {
    padding: 0 10px;

    .welcome-banner {
      padding: 20px 25px;
      flex-direction: column;
      gap: 15px;

      .banner-content {
        .banner-title {
          font-size: 24px;
        }

        .banner-subtitle {
          font-size: 13px;
        }
      }
    }

    .profile-card {
      .card-header {
        flex-direction: column;
        gap: 15px;
        padding: 20px 25px;

        .header-left {
          width: 100%;
          justify-content: center;
        }

        .header-actions {
          width: 100%;

          .save-btn {
            width: 100%;
          }
        }
      }

      .profile-content {
        padding: 25px 20px;
      }
    }
  }
}
</style>
