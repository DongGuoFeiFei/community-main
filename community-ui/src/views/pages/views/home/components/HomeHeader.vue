<template>
  <div class="header-wrapper">
    <div class="header-content">
      <div class="logo">
        <el-image src="/芙蓉花.png" style="width: 32px; height: 32px; margin-right: 8px;"/>
        <router-link to="/" class="text">采芙蓉</router-link>
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
  if (route.path === '/') {
    return 'home'
  } else if (route.params.id) {
    // 检查是否是子分类
    const allSubCategories = categories.value.flatMap(c => c.categoryList)
    const isSubCategory = allSubCategories.some(sub => sub.id.toString() === route.params.id)

    return isSubCategory
        ? `subcategory-${route.params.id}`
        : `category-${route.params.id}`
  }
  return 'home' // 默认值
})

// 计算当前激活的分类ID
const activeCategoryId = computed(() => route.params.id)

// 判断分类是否激活
const isCategoryActive = (category) => {
  return category.id.toString() === activeCategoryId.value ||
      category.categoryList.some(sub => sub.id.toString() === activeCategoryId.value)
}

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

  :deep(.el-sub-menu) {
    &.is-active {
      .el-sub-menu__title {
        color: var(--el-color-primary) !important;
        border-bottom: 2px solid var(--el-color-primary);
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

  :deep(.el-menu-item) {
    &.is-active {
      color: var(--el-color-primary) !important;
      border-bottom: 2px solid var(--el-color-primary);
    }
  }

  :deep(.el-sub-menu__title),
  :deep(.el-menu-item) {
    height: 64px;
    line-height: 64px;
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