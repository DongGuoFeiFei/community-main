<template>
  <div class="admin-header">
    <div class="left">
      <el-icon @click="toggleCollapse">
        <Fold v-if="!isCollapse" />
        <Expand v-else />
      </el-icon>
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/">
        <el-breadcrumb-item v-for="item in breadcrumbs" :key="item.path">
          {{ item.meta.title }}
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="right">
      <!-- 全局搜索 -->
      <el-popover placement="bottom" :width="300" trigger="click">
        <template #reference>
          <el-icon class="header-icon"><Search /></el-icon>
        </template>
        <el-input
            v-model="searchQuery"
            placeholder="搜索内容..."
            clearable
            @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </el-popover>

      <!-- 全屏切换 -->
      <el-tooltip content="全屏" placement="bottom">
        <el-icon class="header-icon" @click="toggleFullscreen">
          <FullScreen />
        </el-icon>
      </el-tooltip>

      <!-- todo 主题切换 -->
<!--      <el-tooltip :content="theme === 'light' ? '深色模式' : '浅色模式'" placement="bottom">-->
<!--        <el-icon class="header-icon" @click="toggleTheme">-->
<!--          <Sunny v-if="theme === 'light'" />-->
<!--          <Moon v-else />-->
<!--        </el-icon>-->
<!--      </el-tooltip>-->

      <!-- 通知中心 -->
      <el-popover placement="bottom" :width="300" trigger="click">
        <template #reference>
          <el-badge :value="unreadCount" class="header-icon">
            <el-icon><Bell /></el-icon>
          </el-badge>
        </template>
        <NotificationPanel @read="handleNotificationRead" />
      </el-popover>

      <!-- 用户菜单 -->
      <el-dropdown>
        <div class="user-info">
          <el-avatar :size="30" :src="user.avatar" />
          <span class="username">{{ user.name }}</span>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="goProfile">
              <el-icon><User /></el-icon>个人中心
            </el-dropdown-item>
            <el-dropdown-item @click="goSettings">
              <el-icon><Setting /></el-icon>个人设置
            </el-dropdown-item>
            <el-dropdown-item divided @click="logout">
              <el-icon><SwitchButton /></el-icon>退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useFullscreen } from '@vueuse/core'
import {
  Fold,
  Expand,
  Search,
  FullScreen,
  Sunny,
  Moon,
  Bell,
  User,
  Setting,
  SwitchButton
} from '@element-plus/icons-vue'

const props = defineProps({
  isCollapse: Boolean
})

const emit = defineEmits(['toggle-collapse'])

// 路由相关
const route = useRoute()
const router = useRouter()
const breadcrumbs = computed(() => {
  const matched = route.matched.filter(item => item.meta && item.meta.title)
  return matched
})

// 搜索功能
const searchQuery = ref('')
const handleSearch = () => {
  console.log('搜索:', searchQuery.value)
  // 实际项目中这里调用搜索API
}

// 全屏切换
const { toggle: toggleFullscreen } = useFullscreen()

// 主题切换
const theme = ref('light')
const toggleTheme = () => {
  theme.value = theme.value === 'light' ? 'dark' : 'light'
  document.documentElement.setAttribute('data-theme', theme.value)
}

// 通知中心
const unreadCount = ref(5)
const handleNotificationRead = () => {
  unreadCount.value = 0
}

// 用户信息
const user = ref({
  name: '管理员',
  avatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
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

      .username {
        margin-left: 8px;
        font-size: 14px;
      }
    }
  }
}
</style>