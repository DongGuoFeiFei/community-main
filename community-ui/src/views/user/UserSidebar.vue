<template>
  <div class="user-header">
    <div class="nav-menu">
      <el-menu
          mode="horizontal"
          :default-active="activeMenu"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
      >
        <el-menu-item
            v-for="item in menuData"
            :key="item.path"
            :index="item.path"
            @click="handleMenuSelect(item)"
        >
          <el-icon>
            <component :is="item.meta.iconComponent"/>
          </el-icon>
          <span>{{ item.meta.title }}</span>
        </el-menu-item>
      </el-menu>
    </div>

    <div class="user-actions">
      <el-dropdown trigger="click" @command="handleDropdownClick">
        <div class="user-dropdown-trigger">
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

<script setup>
import { localStore } from '@/stores/localStores'
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  User as ElIconUser,
  Document as ElIconDocument,
  Star as ElIconStar,
  Bell as ElIconBell
} from '@element-plus/icons-vue'

const lStore = localStore()
const router = useRouter()
const route = useRoute()

const activeMenu = ref('/user/profile')
watch(() => route.path, (newPath) => {
  activeMenu.value = newPath
}, { immediate: true })

const menuData = ref([
  {
    path: '/user/profile',
    meta: {
      title: '个人信息',
      iconComponent: ElIconUser
    }
  },
  {
    path: '/user/articles',
    meta: {
      title: '我的文章',
      iconComponent: ElIconDocument
    }
  },
  {
    path: '/user/collections',
    meta: {
      title: '我的收藏',
      iconComponent: ElIconStar
    }
  },
  {
    path: '/user/notifications',
    meta: {
      title: '消息通知',
      iconComponent: ElIconBell
    }
  }
])

const handleMenuSelect = (menuItem) => {
  router.push(menuItem.path)
}

const handleDropdownClick = (command) => {
  if (command === 'index') {
    router.push('/index')
  } else if (command === "logout") {
    router.push("/login")
  }
}
</script>

<style scoped lang="scss">
.user-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
  padding: 0 20px;
  background-color: #545c64;
  color: #fff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);

  .nav-menu {
    flex: 1;

    .el-menu {
      border-bottom: none;
    }

    .el-menu-item {
      height: 60px;
      line-height: 60px;
    }
  }

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