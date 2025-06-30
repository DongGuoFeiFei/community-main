<template>
  <div class="admin-header">
    <div class="left">
      <el-icon @click="toggleCollapse">
        <Fold v-if="!isCollapse"/>
        <Expand v-else/>
      </el-icon>
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/">
        <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path">
          {{ item.meta.title }}
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="right">
      <!-- 全屏切换 -->
      <el-tooltip content="全屏" placement="bottom">
        <el-icon class="header-icon" @click="toggleFullscreen">
          <FullScreen/>
        </el-icon>
      </el-tooltip>

      <!-- todo 主题切换 -->
      <!-- 用户菜单 -->
      <el-dropdown>
        <div class="user-info">
          <el-avatar :size="30" :src="user.avatar"/>
          <span class="username">{{ user.name }}</span>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="goProfile">
              <el-icon>
                <User/>
              </el-icon>
              个人中心
            </el-dropdown-item>
            <el-dropdown-item @click="goSettings">
              <el-icon>
                <Setting/>
              </el-icon>
              个人设置
            </el-dropdown-item>
            <el-dropdown-item divided @click="logout">
              <el-icon>
                <SwitchButton/>
              </el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import {computed, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {useFullscreen} from '@vueuse/core'
import {Expand, Fold, FullScreen, Setting, SwitchButton, User} from '@element-plus/icons-vue'
import {localStores} from "@/stores/localStores.js";

const props = defineProps({
  isCollapse: Boolean
})

const emit = defineEmits(['toggle-collapse'])

const lStore = localStores()

// 路由相关
const route = useRoute()
const router = useRouter()
const breadcrumbs = computed(() => {
  return route.matched.filter(item => item.meta && item.meta.title)
})

// 全屏切换
const {toggle: toggleFullscreen} = useFullscreen()

// 用户信息
const user = ref({
  name: lStore.userInfo.userInfo.nickname,
  avatar: lStore.baseURL + lStore.userInfo.avatarUrl
})
// 用户操作
const goProfile = () => router.push('/user/profile')
const goSettings = () => router.push('/settings')
const logout = () => {
  console.log('退出登录')
  router.push('/login')
}

const toggleCollapse = () => {
  emit('toggle-collapse')
}
</script>

<style lang="scss" scoped>
.admin-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 20px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .left {
    display: flex;
    align-items: center;

    .el-icon {
      font-size: 20px;
      margin-right: 15px;
      cursor: pointer;

      &:hover {
        color: var(--el-color-primary);
      }
    }

    .el-breadcrumb {
      margin-left: 10px;
    }
  }

  .right {
    display: flex;
    align-items: center;

    .header-icon {
      font-size: 18px;
      margin-right: 20px;
      cursor: pointer;

      &:hover {
        color: var(--el-color-primary);
      }
    }

    .user-info {
      display: flex;
      align-items: center;
      cursor: pointer;

      .el-avatar {
        border: 2px solid #000000;
        box-shadow: 0 0 4px rgba(255, 255, 255, 0.6);
      }

      .username {
        margin-left: 8px;
        font-size: 14px;
      }
    }
  }
}
</style>