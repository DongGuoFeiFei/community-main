<template>
  <div id="app">
    <RouterView/>
    <!-- 登录弹窗   -->
    <GlobalLoginDialog ref="loginDialog" @login-success="handleLoginSuccess"/>
  </div>
</template>

<script setup lang="js">
import GlobalLoginDialog from "@/components/GlobalLoginDialog.vue";
import {setLoginDialog} from '@/utils/request'
import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";

const router = useRouter()
const loginDialog = ref(null)

const handleLoginSuccess = () => {
  console.log('全局登录成功')
}

onMounted(() => {
  if (loginDialog.value) {
    setLoginDialog(loginDialog.value)
  }
})
</script>
