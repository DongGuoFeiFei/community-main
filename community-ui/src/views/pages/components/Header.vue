<template>
  <div class="header-wrapper">
    <div class="header-content">
      <div class="logo">
        <el-image src="/芙蓉花.png" style="width: 32px; height: 32px; margin-right: 8px;"/>
        <router-link to="/index" class="text">采芙蓉</router-link>
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
        <el-menu-item index="card">
          发现
        </el-menu-item>
        <el-menu-item index="messages">消息</el-menu-item>
        <el-menu-item index="game">游戏</el-menu-item>
        <el-menu-item index="anime">动漫</el-menu-item>
        <el-menu-item index="anime">动漫</el-menu-item>
        <el-menu-item index="anime">杂谈</el-menu-item>
        <el-menu-item index="anime">galgame</el-menu-item>
        <el-menu-item index="anime">动漫</el-menu-item>
      </el-menu>

      <NotificationBadge/>
      <div class="user-section">
        <el-dropdown
            @command="handleDropdownClick"
            trigger="click">
          <span class="user-avatar">
            <el-avatar :size="32"
                       :src="avatarUrl"
            />
            <span class="username">{{ lStore.userInfo.userInfo.nickname || '用户' }}</span>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="myself">个人中心</el-dropdown-item>
              <el-dropdown-item command="settings">设置</el-dropdown-item>
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>

    </div>
  </div>
</template>

<script setup lang="js">
import {ref} from 'vue'
import {useRouter} from 'vue-router'
import {localStores} from "@/stores/localStores.js";
import {sessionStores} from "@/stores/sessionStores.js";
import {logout} from "../../../../../community-admin/src/api/auth.js";
import NotificationBadge from "@/components/NotificationBadge.vue";

const router = useRouter()
const activeMenu = ref('home')
const lStore = localStores()
const sStore = sessionStores()
const avatarUrl = ref(lStore.baseURL + lStore.userInfo.avatarUrl)
const handleMenuClick = (index) => {
  activeMenu.value = index
  if (index === 'card') {
    router.push('/index')
  } else if (index === 'editor') {
    router.push('/editor')
  } else if (index === 'messages') {
    router.push('/messages')
  }
}

const handleDropdownClick = (command) => {
  switch (command) {
    case 'myself':
      router.push('/myself'); // 跳转到个人中心
      break;
    case 'settings':
      router.push('/settings'); // 跳转到设置
      break;
    case 'logout':
      lStore.clearStorage();
      sStore.clearStorage();
      logout();
      router.push("/login"); //
      break;
    default:
      break;
  }
};

</script>

<style scoped lang="less">
.header-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.header-content {
  max-width: 1200px;
  height: 64px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  color: #000000;
}

.logo {
  display: flex;
  align-items: center; // 垂直居中
  font-size: 22px;
  font-weight: 600;
  background: #000000;
  -webkit-background-clip: text;
  color: transparent;
  cursor: default;

  .el-image {
    width: 32px;
    height: 32px;
    margin-right: 8px;
  }

  .text {
    color: inherit; // 继承父元素颜色

    // 如果需要，可以同时覆盖激活状态
    &.router-link-active,
    &.router-link-exact-active {
      color: inherit;
      text-decoration: none;
    }
  }
}

.nav-menu {
  flex-grow: 1;
  margin-left: 40px;
  border-bottom: none;
  font-size: 16px;
  color: #000000;
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

/* 响应式调整 */
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
}
</style>