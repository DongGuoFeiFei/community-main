<!-- components/GlobalLoginDialog.vue -->
<template>
  <el-dialog
      v-model="dialogVisible"
      title="登录"
      width="500px"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
      destroy-on-close
  >
    <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" placeholder="请输入用户名"/>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="form.password" placeholder="请输入密码" type="password" show-password/>
      </el-form-item>
      <el-form-item label="验证码" prop="captchaCode">
        <div class="captcha-container">
          <el-input
              v-model="form.captchaCode"
              placeholder="请输入验证码"
              style="width: 150px; margin-right: 10px;"
          />
          <div class="captcha-image" @click="refreshCaptcha">
            <img :src="captchaImage" v-if="captchaImage" alt="验证码"/>
            <span v-else>点击刷新验证码</span>
          </div>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="loading" @click="handleLogin">登录</el-button>
        <el-button @click="goToRegister">注册</el-button>
        <el-button link @click="goToForgotPassword">找回密码</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script setup>
import {reactive, ref} from 'vue'
import {useRouter} from 'vue-router'
import {ElMessage} from 'element-plus'
import {getCaptcha, login} from '@/api/auth.js'
import {localStores} from "@/stores/localStores.js"

const emit = defineEmits(['login-success'])
const dialogVisible = ref(false)
const loading = ref(false)
const formRef = ref(null)
const captchaImage = ref('')
const lStore = localStores()
const router = useRouter()

const form = reactive({
  username: '',
  password: '',
  captchaCode: '',
  captchaKey: ''
})

const rules = {
  username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
  password: [{required: true, message: '请输入密码', trigger: 'blur'}],
  captchaCode: [{required: true, message: '请输入验证码', trigger: 'blur'}]
}

// 打开弹窗
const openDialog = () => {
  dialogVisible.value = true
  refreshCaptcha()
}

// 获取验证码
const refreshCaptcha = async () => {
  try {
    const res = await getCaptcha()
    captchaImage.value = res.data.image
    form.captchaKey = res.data.code
  } catch (error) {
    console.error('获取验证码失败:', error)
    ElMessage.error('获取验证码失败')
  }
}

const handleLogin = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      const res = await login(form)
      if (res?.token) {
        // 将数据存入lStore.userInfo
        lStore.userInfo = {
          ...res
        }
        // 将token的相关信息放入lStore.tokenInfo
        lStore.tokenInfo.token = res.token
        lStore.tokenInfo.refreshTime = res.userInfo.lastLogin
        lStore.tokenInfo.expiresIn = res.expiresIn
        localStorage.setItem('token', res.token)
        ElMessage.success('登录成功')
        dialogVisible.value = false
        emit('login-success') // 通知父组件登录成功
      } else {
        ElMessage.error(res?.msg || '登录失败')
        refreshCaptcha()
      }
    } catch (error) {
      console.error('登录出错:', error)
      ElMessage.error(error.response?.data?.msg || '登录失败，请检查用户名或密码')
      refreshCaptcha()
    } finally {
      loading.value = false
    }
  })
}

const goToRegister = () => {
  dialogVisible.value = false
  router.push('/register')
}

const goToForgotPassword = () => {
  dialogVisible.value = false
  router.push('/forgot-password')
}

// 暴露方法给父组件调用
defineExpose({
  openDialog
})
</script>

<style scoped>
.captcha-container {
  display: flex;
  align-items: center;
}

.captcha-image {
  width: 120px;
  height: 40px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
  background-color: #f5f7fa;
}

.captcha-image img {
  width: 100%;
  height: 100%;
}

.captcha-image span {
  color: #909399;
  font-size: 12px;
}
</style>
