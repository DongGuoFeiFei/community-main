<template>
  <div class="header-wrapper">
    <div class="header-content">
      <div class="logo" @click="handleMenuClick('home')">
        <el-image src="/芙蓉花.png" style="width: 32px; height: 32px; margin-right: 8px;"/>
        <div class="text">采芙蓉</div>
      </div>

      <el-menu
          mode="horizontal"
          :default-active="activeMenu"
          @select="handleMenuClick"
          class="nav-menu"
          background-color="transparent"
      >
        <el-menu-item index="home">首页</el-menu-item>
        <el-sub-menu
            v-for="category in categories"
            :key="category.id"
            :index="`category-${category.id}`"
        >
          <template #title>
            <span
                @click.stop="handleCategoryClick(category.id)"
                :class="{
                'active-category': category.id.toString() === activeCategoryId
              }"
            >
              {{ category.categoryName }}
            </span>
          </template>
          <el-menu-item
              v-for="subCategory in category.categoryList"
              :key="subCategory.id"
              :index="`subcategory-${subCategory.id}`"
          >
            {{ subCategory.categoryName }}
          </el-menu-item>
        </el-sub-menu>
      </el-menu>

      <NotificationBadge/>
      <div class="user-section">
        <el-dropdown
            @command="handleDropdownClick"
            trigger="click"
        >
          <span class="user-avatar">
            <el-avatar :size="32"
                       :src="avatarUrl"
            />
            <span class="username">{{ lStore.userInfo.userInfo.nickname || '用户' }}</span>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="myself">个人中心</el-dropdown-item>
<!--              <el-dropdown-item command="settings">设置</el-dropdown-item>-->
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script setup lang="js">
import {computed, onMounted, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {localStores} from "@/stores/localStores.js";
import {sessionStores} from "@/stores/sessionStores.js";
import {logout} from "@/api/auth.js";
import {getCategoryTrees} from "@/api/category.js";
import NotificationBadge from "@/components/NotificationBadge.vue";

const router = useRouter()
const route = useRoute()
const lStore = localStores()
const sStore = sessionStores()
const avatarUrl = ref(lStore.baseURL + (lStore.userInfo?.avatarUrl || ''))
const categories = ref([])

const emit = defineEmits(['category-change'])

// 动态计算activeMenu
const activeMenu = computed(() => {
  if (route.path === '/' || route.path === '') {
    return 'home'
  } else if (/^\/\d+$/.test(route.path)) {
    const id = route.path.substring(1)
    const allSubCategories = categories.value.flatMap(c => c.categoryList)
    const isSubCategory = allSubCategories.some(sub => sub.id.toString() === id)

    return isSubCategory
        ? `subcategory-${id}`
        : `category-${id}`
  }
  return ''
})

// 计算当前激活的分类ID
const activeCategoryId = computed(() => {
  if (/^\/\d+$/.test(route.path)) {
    return route.path.substring(1)
  }
  return null
})
const loadCategories = async () => {
  try {
    const res = await getCategoryTrees()
    categories.value = res.data || []
  } catch (error) {
    console.error('加载分类失败:', error)
    categories.value = []
  }
}

// 处理父分类点击
const handleCategoryClick = (categoryId) => {
  emit('category-change', categoryId)
  router.push({path: `/${categoryId}`})
}

// 处理菜单点击
const handleMenuClick = (index) => {
  try {
    if (index.startsWith('subcategory-')) {
      const categoryId = index.replace('subcategory-', '')
      emit('category-change', categoryId)
      router.push({path: `/${categoryId}`})
    } else if (index === 'home') {
      emit('category-change', null)
      router.push({path: '/'})
    }
  } catch (error) {
    console.error('处理菜单点击时出错:', error)
  }
}

const handleDropdownClick = (command) => {
  try {
    switch (command) {
      case 'myself':
        router.push('/myself')
        break
      case 'settings':
        router.push('/settings')
        break
      case 'logout':
        lStore.clearStorage()
        sStore.clearStorage()
        logout()
        router.push("/login")
        break
      default:
        console.warn('未知的下拉菜单命令:', command)
    }
  } catch (error) {
    console.error('处理下拉菜单点击时出错:', error)
  }
}

onMounted(() => {
  loadCategories()
})
</script>

<style scoped lang="less">
.header-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 50;
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
  align-items: center;
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
    color: inherit;

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

  // 去除所有状态下的阴影
  :deep(.el-menu--horizontal) {
    .el-menu-item,
    .el-sub-menu__title {
      box-shadow: none !important;
    }
  }

  // 去除选中状态的下划线
  :deep(.el-sub-menu) {
    &.is-active {
      .el-sub-menu__title {
        color: var(--el-color-primary) !important;
        border-bottom: none !important;
        box-shadow: none !important;
      }
    }

    .el-sub-menu__title {
      span {
        &.active-category {
          color: var(--el-color-primary);
          font-weight: bold;
        }
      }
    }
  }

  // 去除菜单项选中状态的下划线
  :deep(.el-menu-item) {
    &.is-active {
      color: var(--el-color-primary) !important;
      border-bottom: none !important;
      box-shadow: none !important;
    }
  }

  // 去除所有交互状态的阴影和背景变化
  :deep(.el-menu-item),
  :deep(.el-sub-menu__title) {
    &:hover,
    &:focus,
    &:active {
      background-color: transparent !important;
      box-shadow: none !important;
    }
  }

  :deep(.el-sub-menu__title),
  :deep(.el-menu-item) {
    height: 64px;
    line-height: 64px;
  }

  :deep(.el-menu-item),
  :deep(.el-sub-menu__title) {
    span {
      &.active-menu-item {
        color: var(--el-color-primary);
        font-weight: bold;
      }
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
