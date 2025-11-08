<template>
  <div class="header-wrapper">
    <!-- 装饰性气泡 -->
    <div class="decoration-bubbles">
      <span class="bubble bubble-1">✨</span>
      <span class="bubble bubble-2">🌸</span>
      <span class="bubble bubble-3">💫</span>
      <span class="bubble bubble-4">🌟</span>
      <span class="bubble bubble-5">💝</span>
    </div>

    <div class="header-content">
      <!-- Logo区域 -->
      <div class="logo-section" @click="handleMenuClick('home')">
        <div class="logo-container">
          <div class="logo-circle">
            <el-image src="/芙蓉花.png" class="logo-image" />
          </div>
          <div class="logo-text">
            <span class="site-name">采芙蓉</span>
            <span class="site-slogan">梦幻二次元社区</span>
          </div>
        </div>
      </div>

      <!-- 导航菜单 -->
      <el-menu
        mode="horizontal"
        :default-active="activeMenu"
        @select="handleMenuClick"
        class="nav-menu"
        background-color="transparent"
        popper-class="anime-submenu-dropdown"
      >
        <el-menu-item index="home" class="menu-bubble">
          <span class="menu-text">🏠 首页</span>
        </el-menu-item>
        <el-sub-menu
          v-for="category in categories"
          :key="category.id"
          :index="`category-${category.id}`"
          class="menu-bubble"
          popper-class="anime-submenu-dropdown"
        >
          <template #title>
            <span
              class="menu-text"
              @click.stop="handleCategoryClick(category.id)"
              :class="{
                'active-category': category.id.toString() === activeCategoryId,
              }"
            >
              {{ category.categoryName }}
            </span>
          </template>
          <el-menu-item
            v-for="subCategory in category.categoryList"
            :key="subCategory.id"
            :index="`subcategory-${subCategory.id}`"
            class="submenu-item"
          >
            <span class="submenu-text">{{ subCategory.categoryName }}</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>

      <!-- 右侧功能区 -->
      <div class="header-actions">
        <!-- 快捷入口按钮 -->
        <div class="quick-actions">
          <button
            v-for="action in quickActions"
            :key="action.path"
            class="quick-action-btn"
            :style="{ '--action-color': action.color }"
            @click="handleQuickAction(action.path)"
          >
            <span class="action-icon">{{ action.icon }}</span>
            <span class="action-label">{{ action.label }}</span>
          </button>
        </div>

        <!-- 通知徽章 -->
        <NotificationBadge class="notification-badge" />

        <!-- 用户信息 -->
        <div class="user-section">
          <el-dropdown
            @command="handleDropdownClick"
            trigger="click"
            popper-class="anime-user-dropdown"
          >
            <div class="user-avatar-wrapper">
              <div class="avatar-container">
                <el-avatar
                  :size="40"
                  :src="avatarUrl"
                  class="user-avatar-img"
                />
                <div class="avatar-glow"></div>
                <div class="avatar-ring"></div>
              </div>
              <span class="username">{{
                lStore.userInfo.userInfo.nickname || "用户"
              }}</span>
              <span class="user-icon">▼</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="myself">
                  <span class="dropdown-icon">👤</span>
                  <span>个人中心</span>
                </el-dropdown-item>
                <el-dropdown-item command="logout" divided>
                  <span class="dropdown-icon">🚪</span>
                  <span>退出登录</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, type Ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { localStores } from "@/stores/localStores.js";
import { sessionStores } from "@/stores/sessionStores.js";
import { logout } from "@/api/auth.js";
import { getCategoryTrees } from "@/api/category.js";
import NotificationBadge from "@/components/NotificationBadge.vue";

interface Category {
  id: number;
  categoryName: string;
  categoryList: SubCategory[];
}

interface SubCategory {
  id: number;
  categoryName: string;
}

const router = useRouter();
const route = useRoute();
const lStore = localStores();
const sStore = sessionStores();
const avatarUrl: Ref<string> = ref(
  lStore.baseURL + (lStore.userInfo?.avatarUrl || "")
);
const categories: Ref<Category[]> = ref([]);

const emit = defineEmits<{
  (e: "category-change", categoryId: number | null): void;
}>();

// 快捷入口按钮
const quickActions = ref([
  { icon: "✍️", label: "发布", path: "/editor", color: "#FF69B4" },
  { icon: "💬", label: "聊天", path: "/chat", color: "#84C7D0" },
  // { icon: "👤", label: "我的", path: "/myself", color: "#C9A0DC" },
]);

// 动态计算activeMenu
const activeMenu = computed(() => {
  if (route.path === "/" || route.path === "") {
    return "home";
  } else if (/^\/\d+$/.test(route.path)) {
    const id = route.path.substring(1);
    const allSubCategories = categories.value.flatMap((c) => c.categoryList);
    const isSubCategory = allSubCategories.some(
      (sub) => sub.id.toString() === id
    );

    return isSubCategory ? `subcategory-${id}` : `category-${id}`;
  }
  return "";
});

// 计算当前激活的分类ID
const activeCategoryId = computed(() => {
  if (/^\/\d+$/.test(route.path)) {
    return route.path.substring(1);
  }
  return null;
});
const loadCategories = async (): Promise<void> => {
  try {
    const res = await getCategoryTrees();
    categories.value = res.data || [];
  } catch (error) {
    console.error("加载分类失败:", error);
    categories.value = [];
  }
};

// 处理父分类点击
const handleCategoryClick = (categoryId: number): void => {
  emit("category-change", categoryId);
  router.push({ path: `/${categoryId}` });
};

// 处理快捷按钮点击
const handleQuickAction = (path: string): void => {
  router.push(path);
};

// 处理菜单点击
const handleMenuClick = (index: string): void => {
  try {
    if (index.startsWith("subcategory-")) {
      const categoryId = parseInt(index.replace("subcategory-", ""));
      emit("category-change", categoryId);
      router.push({ path: `/${categoryId}` });
    } else if (index === "home") {
      emit("category-change", null);
      router.push({ path: "/" });
    }
  } catch (error) {
    console.error("处理菜单点击时出错:", error);
  }
};

const handleDropdownClick = (command: string): void => {
  try {
    switch (command) {
      case "myself":
        router.push("/myself");
        break;
      case "settings":
        router.push("/settings");
        break;
      case "logout":
        lStore.clearStorage();
        sStore.clearStorage();
        logout();
        router.push("/login");
        break;
      default:
        console.warn("未知的下拉菜单命令:", command);
    }
  } catch (error) {
    console.error("处理下拉菜单点击时出错:", error);
  }
};

onMounted(() => {
  loadCategories();
});
</script>

<style scoped lang="scss">
.header-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  height: 80px;

  // 清新蓝紫色系渐变背景
  background: linear-gradient(
    135deg,
    rgba(224, 247, 250, 0.95) 0%,
    /* 薄荷蓝 */ rgba(230, 240, 255, 0.95) 25%,
    rgba(232, 245, 255, 0.95) 50%,
    rgba(220, 235, 255, 0.95) 75%,
    /* 天空蓝 */ rgba(230, 230, 250, 0.95) 100% /* 淡紫 */
  );

  backdrop-filter: blur(20px) saturate(180%);
  box-shadow: 0 4px 20px rgba(135, 206, 235, 0.3),
    0 8px 40px rgba(147, 112, 219, 0.2), inset 0 1px 0 rgba(255, 255, 255, 0.6);

  border-bottom: 2px solid rgba(255, 255, 255, 0.5);
  animation: headerFloat 3s ease-in-out infinite;

  // 装饰性气泡
  .decoration-bubbles {
    position: absolute;
    width: 100%;
    height: 100%;
    overflow: hidden;
    pointer-events: none;

    .bubble {
      position: absolute;
      font-size: 20px;
      animation: bubbleFloat 4s ease-in-out infinite;
      opacity: 0.6;
      filter: drop-shadow(0 0 10px rgba(255, 255, 255, 0.8));

      &.bubble-1 {
        top: 15%;
        left: 5%;
        animation-delay: 0s;
      }

      &.bubble-2 {
        top: 60%;
        left: 15%;
        animation-delay: 1s;
      }

      &.bubble-3 {
        top: 30%;
        right: 20%;
        animation-delay: 2s;
      }

      &.bubble-4 {
        top: 70%;
        right: 10%;
        animation-delay: 1.5s;
      }

      &.bubble-5 {
        top: 45%;
        left: 50%;
        animation-delay: 0.5s;
      }
    }
  }
}

.header-content {
  max-width: 1400px;
  height: 100%;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px;
  position: relative;
  z-index: 2;
}

// Logo区域
.logo-section {
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);

  &:hover {
    transform: scale(1.05) translateY(-2px);

    .logo-circle {
      transform: rotate(360deg);
      box-shadow: 0 8px 25px rgba(135, 206, 235, 0.6),
        0 0 30px rgba(91, 155, 213, 0.4);
    }
  }

  &:active {
    transform: scale(0.95);
  }
}

.logo-container {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-circle {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: linear-gradient(135deg, #87ceeb, #b0e0e6, #dda0dd);
  padding: 3px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 15px rgba(135, 206, 235, 0.5),
    inset 0 2px 8px rgba(255, 255, 255, 0.6);
  transition: all 0.5s cubic-bezier(0.34, 1.56, 0.64, 1);

  .logo-image {
    width: 44px;
    height: 44px;
    border-radius: 50%;
    background: white;
  }
}

.logo-text {
  display: flex;
  flex-direction: column;
  gap: 2px;

  .site-name {
    font-size: 24px;
    font-weight: 800;
    background: linear-gradient(135deg, #5b9bd5, #4169e1, #8e44ad);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    letter-spacing: 1px;
    text-shadow: 2px 2px 4px rgba(91, 155, 213, 0.2);
  }

  .site-slogan {
    font-size: 11px;
    color: #5b9bd5;
    font-weight: 500;
    letter-spacing: 0.5px;
    opacity: 0.8;
  }
}

// 导航菜单
.nav-menu {
  flex: 1;
  margin: 0 40px;
  border-bottom: none !important;

  :deep(.el-menu-item),
  :deep(.el-sub-menu__title) {
    height: 45px;
    line-height: 45px;
    border-radius: 25px;
    margin: 0 6px;
    padding: 0 20px !important;
    border: none !important;
    background: rgba(255, 255, 255, 0.4);
    backdrop-filter: blur(10px);
    transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
    box-shadow: 0 2px 8px rgba(135, 206, 235, 0.2),
      inset 0 1px 2px rgba(255, 255, 255, 0.5);

    &:hover {
      background: rgba(255, 255, 255, 0.7);
      transform: translateY(-3px) scale(1.05);
      box-shadow: 0 6px 20px rgba(135, 206, 235, 0.4),
        inset 0 2px 4px rgba(255, 255, 255, 0.8);
    }

    &.is-active {
      background: linear-gradient(135deg, #87ceeb, #b0e0e6);
      color: #f0f8ff !important;
      font-weight: 600;
      box-shadow: 0 4px 15px rgba(135, 206, 235, 0.5),
        inset 0 2px 8px rgba(255, 255, 255, 0.3);

      .menu-text {
        color: #f0f8ff !important;
        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      }
    }
  }

  .menu-text {
    font-size: 15px;
    font-weight: 600;
    color: #5b9bd5;
    transition: all 0.3s;
  }

  .active-category {
    color: #f0f8ff !important;
    font-weight: 700;
  }
}

// 右侧功能区
.header-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

// 快捷入口按钮
.quick-actions {
  display: flex;
  gap: 8px;

  .quick-action-btn {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 8px 16px;
    border: none;
    border-radius: 20px;
    background: rgba(255, 255, 255, 0.5);
    backdrop-filter: blur(10px);
    border: 2px solid rgba(255, 255, 255, 0.7);
    box-shadow: 0 2px 8px rgba(135, 206, 235, 0.2),
      inset 0 1px 3px rgba(255, 255, 255, 0.6);
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
    position: relative;
    overflow: hidden;

    &::before {
      content: "";
      position: absolute;
      top: 50%;
      left: 50%;
      width: 0;
      height: 0;
      border-radius: 50%;
      background: var(--action-color, #87ceeb);
      opacity: 0.2;
      transform: translate(-50%, -50%);
      transition: width 0.6s, height 0.6s;
    }

    &:hover {
      transform: translateY(-3px) scale(1.05);
      box-shadow: 0 6px 20px rgba(135, 206, 235, 0.3),
        inset 0 2px 5px rgba(255, 255, 255, 0.8);
      background: rgba(255, 255, 255, 0.8);

      &::before {
        width: 200px;
        height: 200px;
      }

      .action-icon {
        transform: scale(1.2) rotate(10deg);
      }
    }

    &:active {
      transform: translateY(-1px) scale(0.98);
    }

    .action-icon {
      font-size: 18px;
      transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
      position: relative;
      z-index: 1;
    }

    .action-label {
      font-size: 13px;
      font-weight: 600;
      color: var(--action-color, #ff69b4);
      position: relative;
      z-index: 1;
    }
  }
}

.notification-badge {
  :deep(.el-badge__content) {
    background: linear-gradient(135deg, #ff69b4, #ff1493);
    border: 2px solid #fff;
    box-shadow: 0 2px 8px rgba(255, 20, 147, 0.4);
  }
}

// 用户区域
.user-section {
  .user-avatar-wrapper {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 8px 20px;
    border-radius: 30px;
    background: rgba(255, 255, 255, 0.5);
    backdrop-filter: blur(10px);
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
    box-shadow: 0 2px 10px rgba(135, 206, 235, 0.3),
      inset 0 1px 2px rgba(255, 255, 255, 0.6);

    &:hover {
      background: rgba(255, 255, 255, 0.8);
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(135, 206, 235, 0.4),
        inset 0 2px 4px rgba(255, 255, 255, 0.8);

      .avatar-glow {
        opacity: 1;
        transform: scale(1.2);
      }
    }

    &:active {
      transform: scale(0.95);
    }
  }

  .avatar-container {
    position: relative;
    width: 40px;
    height: 40px;

    .user-avatar-img {
      border: 3px solid #fff;
      box-shadow: 0 4px 12px rgba(135, 206, 235, 0.3);
      transition: all 0.3s;
      position: relative;
      z-index: 2;
    }

    .avatar-glow {
      position: absolute;
      top: -4px;
      left: -4px;
      right: -4px;
      bottom: -4px;
      border-radius: 50%;
      background: linear-gradient(135deg, #87ceeb, #5b9bd5);
      opacity: 0;
      filter: blur(8px);
      transition: all 0.3s;
      z-index: 1;
    }

    .avatar-ring {
      position: absolute;
      top: -6px;
      left: -6px;
      right: -6px;
      bottom: -6px;
      border-radius: 50%;
      border: 2px solid transparent;
      background: linear-gradient(135deg, #87ceeb, #9370db, #84c7d0);
      background-clip: padding-box;
      opacity: 0;
      animation: ringRotate 3s linear infinite;
      transition: opacity 0.3s;
      z-index: 0;
    }
  }

  .user-avatar-wrapper:hover .avatar-ring {
    opacity: 1;
  }

  .username {
    font-size: 14px;
    font-weight: 600;
    color: #5b9bd5;
    max-width: 100px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .user-icon {
    font-size: 10px;
    color: #5b9bd5;
    transition: transform 0.3s;
  }
}

// 动画关键帧
@keyframes headerFloat {
  0%,
  100% {
    box-shadow: 0 4px 20px rgba(135, 206, 235, 0.3),
      0 8px 40px rgba(147, 112, 219, 0.2),
      inset 0 1px 0 rgba(255, 255, 255, 0.6);
  }
  50% {
    box-shadow: 0 6px 25px rgba(135, 206, 235, 0.4),
      0 12px 50px rgba(147, 112, 219, 0.3),
      inset 0 1px 0 rgba(255, 255, 255, 0.8);
  }
}

@keyframes bubbleFloat {
  0%,
  100% {
    transform: translateY(0) scale(1);
    opacity: 0.6;
  }
  50% {
    transform: translateY(-10px) scale(1.1);
    opacity: 0.8;
  }
}

@keyframes ringRotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

// 响应式设计
@media (max-width: 1200px) {
  .header-content {
    padding: 0 20px;
  }

  .nav-menu {
    margin: 0 20px;
  }
}

@media (max-width: 768px) {
  .header-wrapper {
    height: 70px;
  }

  .header-content {
    padding: 0 16px;
  }

  .logo-circle {
    width: 40px;
    height: 40px;

    .logo-image {
      width: 36px;
      height: 36px;
    }
  }

  .logo-text {
    .site-name {
      font-size: 20px;
    }

    .site-slogan {
      font-size: 10px;
    }
  }

  .nav-menu {
    margin: 0 10px;

    :deep(.el-menu-item),
    :deep(.el-sub-menu__title) {
      padding: 0 12px !important;
      margin: 0 3px;
    }

    .menu-text {
      font-size: 13px;
    }
  }

  .username {
    display: none;
  }

  .quick-actions {
    gap: 5px;

    .quick-action-btn {
      padding: 6px 12px;

      .action-icon {
        font-size: 16px;
      }

      .action-label {
        font-size: 11px;
      }
    }
  }
}
</style>

<!-- 全局样式：美化下拉菜单 -->
<style lang="scss">
// 用户下拉菜单美化
.anime-user-dropdown {
  border-radius: 20px !important;
  padding: 10px !important;
  background: rgba(255, 255, 255, 0.95) !important;
  backdrop-filter: blur(20px) !important;
  box-shadow: 0 10px 40px rgba(135, 206, 235, 0.3),
    0 15px 60px rgba(147, 112, 219, 0.2), inset 0 1px 0 rgba(255, 255, 255, 0.8) !important;
  border: 2px solid rgba(135, 206, 235, 0.3) !important;
  animation: dropdownSlideIn 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);

  .el-dropdown-menu__item {
    border-radius: 12px !important;
    margin: 4px 0 !important;
    padding: 10px 16px !important;
    color: #5b9bd5 !important;
    font-weight: 500 !important;
    transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1) !important;

    &:hover {
      background: linear-gradient(135deg, #e0f7fa, #e6f3ff) !important;
      transform: translateX(5px);
      box-shadow: 0 4px 12px rgba(135, 206, 235, 0.2);

      .dropdown-icon {
        transform: scale(1.2) rotate(10deg);
      }
    }

    &:not(:last-child) {
      margin-bottom: 4px;
    }

    .dropdown-icon {
      margin-right: 8px;
      font-size: 16px;
      transition: all 0.3s;
      display: inline-block;
    }
  }

  // 分割线美化
  .el-dropdown-menu__item--divided {
    border-top: 2px solid rgba(135, 206, 235, 0.2) !important;
    margin-top: 8px !important;
    padding-top: 12px !important;
  }
}

// 子菜单下拉框美化
.anime-submenu-dropdown {
  border-radius: 20px !important;
  padding: 10px !important;
  background: rgba(255, 255, 255, 0.95) !important;
  backdrop-filter: blur(20px) !important;
  box-shadow: 0 10px 40px rgba(135, 206, 235, 0.3),
    0 15px 60px rgba(147, 112, 219, 0.2), inset 0 1px 0 rgba(255, 255, 255, 0.8) !important;
  border: 2px solid rgba(135, 206, 235, 0.3) !important;
  animation: dropdownSlideIn 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);

  .el-menu-item {
    border-radius: 15px !important;
    margin: 4px 0 !important;
    padding: 0 20px !important;
    height: 42px !important;
    line-height: 42px !important;
    transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1) !important;

    &:hover {
      background: linear-gradient(135deg, #e0f7fa, #e6f3ff) !important;
      transform: translateX(5px);
      box-shadow: 0 4px 12px rgba(135, 206, 235, 0.2);
    }

    &.is-active {
      background: linear-gradient(135deg, #87ceeb, #b0e0e6) !important;
      color: #f0f8ff !important;
      font-weight: 600;
      box-shadow: 0 4px 15px rgba(135, 206, 235, 0.4);
    }

    .submenu-text {
      color: #5b9bd5;
      font-weight: 500;
      transition: all 0.3s;
    }

    &.is-active .submenu-text {
      color: #f0f8ff !important;
    }
  }
}

// 下拉动画
@keyframes dropdownSlideIn {
  0% {
    opacity: 0;
    transform: translateY(-10px) scale(0.95);
  }
  100% {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}
</style>
