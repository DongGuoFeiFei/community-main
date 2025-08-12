<template>
  <div class="admin-menu-container">
    <div class="menu-title" :class="{ 'collapse-title': isCollapse }">
      <el-image src="/芙蓉花.png" class="logo"/>
    </div>
    <el-menu
      :default-active="activeMenu"
      class="admin-menu"
      :collapse="isCollapse"
      background-color="#304156"
      text-color="#bfcbd9"
      active-text-color="#409EFF"
      :unique-opened="true"
      router
    >
      <template v-for="item in menuData" :key="item.path">
        <!-- 有子菜单的项 -->
        <el-sub-menu
          v-if="item.children && item.children.length > 0"
          :index="item.path"
        >
          <template #title>
            <el-icon v-if="item.meta.iconComponent">
              <component :is="item.meta.iconComponent"/>
            </el-icon>
            <span>{{ item.meta.title }}</span>
          </template>
          <el-menu-item
            v-for="child in item.children"
            :key="child.path"
            :index="child.path"
          >
            <el-icon v-if="child.meta.iconComponent">
              <component :is="child.meta.iconComponent"/>
            </el-icon>
            <span>{{ child.meta.title }}</span>
          </el-menu-item>
        </el-sub-menu>

        <!-- 没有子菜单的项 -->
        <el-menu-item v-else :index="item.path">
          <el-icon v-if="item.meta.iconComponent">
            <component :is="item.meta.iconComponent"/>
          </el-icon>
          <span>{{ item.meta.title }}</span>
        </el-menu-item>
      </template>
    </el-menu>
  </div>
</template>

<script setup>
import {computed, markRaw, onMounted, ref} from 'vue';
import {useRoute} from 'vue-router';
import {
  Bell,
  ChatDotRound,
  Collection,
  Connection,
  Document,
  Flag,
  Grid,
  Key,
  List,
  Lock,
  Message,
  Monitor,
  Notebook,
  Setting,
  TrendCharts,
  User,
  Warning
} from "@element-plus/icons-vue";
import {getUserMenuTree} from "@/api/menu.js";

const props = defineProps({
  isCollapse: {
    type: Boolean,
    default: false
  }
});

const iconComponents = {
  monitor: markRaw(Monitor),
  grid: markRaw(Grid),
  document: markRaw(Document),
  collection: markRaw(Collection),
  chat: markRaw(ChatDotRound),
  connection: markRaw(Connection),
  trend: markRaw(TrendCharts),
  user: markRaw(User),
  warning: markRaw(Warning),
  lock: markRaw(Lock),
  key: markRaw(Key),
  list: markRaw(List),
  log: markRaw(Notebook),
  flag: markRaw(Flag),
  bell: markRaw(Bell),
  message: markRaw(Message),
  setting: markRaw(Setting)
};

const menuData = ref([]);
const route = useRoute();

const transformMenuData = (menuItems) => {
  return menuItems.map(item => {
    // 处理children为null的情况
    const children = item.children ? transformMenuData(item.children) : [];
    return {
      ...item,
      meta: {
        ...item.meta,
        iconComponent: item.meta.icon ? iconComponents[item.meta.icon] : null
      },
      children
    };
  });
};

const loadMenuData = async () => {
  try {
    const res = await getUserMenuTree();
    if (res.code === 200) {
      menuData.value = transformMenuData(res.data);
    }
  } catch (error) {
    console.error('加载菜单失败:', error);
  }
};

onMounted(() => {
  loadMenuData()
});


const activeMenu = computed(() => {
  const {path} = route;
  return path;
});
</script>

<style lang="scss" scoped>
.admin-menu-container {
  height: 100%;

  .menu-title {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 60px;
    background-color: #263445;
    border-bottom: 1px solid #1f2d3d;
    padding: 0 16px;
    transition: all 0.3s ease;

    .logo {
      width: 32px;
      height: 32px;
      transition: all 0.3s ease;
      flex-shrink: 0;
    }

    &.collapse-title {
      .logo {
        width: 24px;
        height: 24px;
      }
    }
  }

  .admin-menu {
    border-right: none;

    &:not(.el-menu--collapse) {
      width: 200px;
    }

    .el-menu-item,
    .el-sub-menu__title {
      &:hover {
        background-color: #263445 !important;
      }
    }

    .el-menu-item.is-active {
      background-color: #1890ff !important;
      color: #fff !important;
    }
  }
}
</style>
