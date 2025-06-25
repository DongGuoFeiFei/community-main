<template>
  <div class="header-wrapper">
    <div class="header-content">
      <div class="logo">🧩 Vue社区</div>

      <el-menu
          mode="horizontal"
          :default-active="activeMenu"
          @select="handleMenuClick"
          class="nav-menu"
          background-color="transparent"
          text-color="#fff"
          active-text-color="#ffd04b"
      >
        <el-menu-item index="card">发现</el-menu-item>
        <el-menu-item index="messages">消息</el-menu-item>
        <el-menu-item index="game">游戏</el-menu-item>
        <el-menu-item index="anime">动漫</el-menu-item>
      </el-menu>

      <div class="user-section">
        <el-dropdown
            @command="handleDropdownClick"
            trigger="click">
          <span class="el-dropdown-link">
            <el-avatar :size="32"
                       :src="avatarUrl"
            />
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="user">个人中心</el-dropdown-item>
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
import {localStore} from "@/stores/localStores.js";
import {sessionStore} from "@/stores/sessionStores.js";

const router = useRouter()
const activeMenu = ref('home')
const lStore = localStore()
const sStore = sessionStore()
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
    case 'user':
      router.push('/user/profile'); // 跳转到个人中心
      break;
    case 'settings':
      router.push('/settings'); // 跳转到设置
      break;
    case 'logout':
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
  font-size: 22px;
  font-weight: 600;
  background: linear-gradient(to right, #ff9a9e, #fad0c4);
  -webkit-background-clip: text;
  color: transparent;
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