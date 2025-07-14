<template>
  <header class="header-wrapper">
    <div class="header-content">
      <div class="logo" @click="goHome">
        <el-image src="/芙蓉花.png" style="width: 32px; height: 32px; margin-right: 8px;"/>
        <span>采芙蓉</span>
      </div>

      <el-menu
          mode="horizontal"
          :default-active="activeMenu"
          @select="handleMenuClick"
          class="nav-menu"
          background-color="transparent"
          text-color="#666"
          active-text-color="#ffd04b"
      >
        <el-menu-item index="profile">
          <span>个人信息</span>
        </el-menu-item>
        <el-menu-item index="articles">
          <span>我的文章</span>
        </el-menu-item>
        <el-menu-item index="collections">
          <span>我的收藏</span>
        </el-menu-item>
        <el-menu-item index="notifications">
          <span>消息通知</span>
        </el-menu-item>
        <el-menu-item index="follow&fans">
          <span>关注&粉丝</span>
        </el-menu-item>
      </el-menu>

      <div class="user-section">
        <el-dropdown @command="handleDropdownClick" trigger="click">
          <div class="user-avatar">
            <el-avatar :size="32" :src="avatarUrl"/>
            <span class="username">{{ user.userInfo.nickname || '用户' }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="home">
                <el-icon>
                  <User/>
                </el-icon>
                <span>首页</span>
              </el-dropdown-item>
              <el-dropdown-item command="settings">
                <el-icon>
                  <Setting/>
                </el-icon>
                <span>账户设置</span>
              </el-dropdown-item>
              <el-dropdown-item command="logout" divided>
                <el-icon>
                  <SwitchButton/>
                </el-icon>
                <span>退出登录</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </header>
</template>

<script setup>
import {computed, ref} from 'vue'
import {useRouter} from 'vue-router'
import {Setting, SwitchButton, User} from '@element-plus/icons-vue'
import {localStores} from '@/stores/localStores.js'
import {sessionStores} from '@/stores/sessionStores.js'
import {logout} from '@/api/auth.js'

const router = useRouter()
const lStore = localStores()
const sStore = sessionStores()
const activeMenu = ref(router.currentRoute.value.path.split('/')[2])

const user = computed(() => lStore.userInfo || {})
const avatarUrl = computed(() => {
  return user.value.avatarUrl
      ? lStore.baseURL + user.value.avatarUrl
      : 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
})

const goHome = () => {
  router.push('/')
}

const handleMenuClick = (index) => {
  activeMenu.value = index
  router.push(`/myself/${index}`)
}

const handleDropdownClick = async (command) => {
  switch (command) {
    case 'home':
      router.push('/index')
      break
    case 'settings':
      router.push('/settings')
      break
    case 'logout':
      try {
        await logout()
        lStore.clearStorage()
        sStore.clearStorage()
        router.push('/login')
      } catch (error) {
        console.error('退出登录失败:', error)
      }
      break
  }
}
</script>

<style scoped lang="scss">
.header-wrapper {
  position: sticky;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 0.85);
}

.header-content {
  max-width: 1200px;
  height: 64px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
}

.logo {
  display: flex;
  align-items: center; // 垂直居中
  font-size: 22px;
  font-weight: 600;
  background: #000000;
  -webkit-background-clip: text;
  color: transparent;
  cursor: pointer;

  .el-image {
    width: 32px;
    height: 32px;
    margin-right: 8px;
  }
}

.nav-menu {
  flex-grow: 1;
  margin-left: 40px;
  border-bottom: none;
  font-size: 16px;

  .el-menu-item {
    display: flex;
    align-items: center;

    .el-icon {
      margin-right: 6px;
    }
  }
}

.user-section {
  display: flex;
  align-items: center;

  .user-avatar {
    display: flex;
    align-items: center;
    cursor: pointer;
    transition: all 0.3s;

    &:hover {
      opacity: 0.8;
    }

    .username {
      margin-left: 8px;
      font-size: 14px;
      color: #666;
      max-width: 100px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }

  .el-avatar {
    cursor: pointer;
    border: 2px solid #000000;
    box-shadow: 0 0 4px rgba(255, 255, 255, 0.6);
  }
}

.el-dropdown-menu__item {
  display: flex;
  align-items: center;

  .el-icon {
    margin-right: 8px;
  }
}

@media (max-width: 768px) {
  .header-content {
    padding: 0 16px;
  }

  .nav-menu {
    margin-left: 20px;
  }

  .logo {
    font-size: 18px;
  }

  .username {
    display: none;
  }
}
</style>