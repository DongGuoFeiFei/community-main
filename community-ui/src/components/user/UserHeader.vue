<script setup>
import {localStore} from '@/stores/localStores'
import {ref} from 'vue'
import {useRouter} from "vue-router";

const lStore = localStore()
const router = useRouter()
const dropdownVisible = ref(false)
const toggleDropdown = () => {
  dropdownVisible.value = !dropdownVisible.value
}
const handleDropdownClick = (comment) => {
  if (comment === 'index') {
    router.push('/index')
  } else if (comment === "logout") {
    router.push("/login")
  }
}
</script>

<template>
  <div class="user-header">
    <div class="user-actions">
      <el-dropdown trigger="click" v-model="dropdownVisible" @command="handleDropdownClick">
        <div class="user-dropdown-trigger" @click="toggleDropdown">
          <span class="username">{{ lStore.userInfo.userInfo.nickname }}</span>
          <el-avatar :size="40" :src="lStore.baseURL + lStore.userInfo.avatarUrl"/>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="index">返回首页</el-dropdown-item>
            <el-dropdown-item>设置</el-dropdown-item>
            <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<style scoped lang="less">
.user-header {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  border-bottom: 1px solid var(--el-border-color-light);

  .user-actions {
    .user-dropdown-trigger {
      display: flex;
      align-items: center;
      cursor: pointer;

      .username {
        margin-right: 10px;
        font-size: 16px;
        font-weight: 500;
      }
    }
  }
}
</style>