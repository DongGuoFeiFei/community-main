<template>
  <div class="admin-menu-container">
    <div class="menu-title" :class="{ 'collapse-title': isCollapse }">
      <el-image src="/芙蓉花.png" class="logo"/>
      <!--      <h2 class="title">采芙蓉</h2>-->
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
        <el-sub-menu v-if="item.children && item.children.length > 0" :index="item.path">
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
import {computed} from 'vue';
import {useRoute} from 'vue-router';
import {useMenuStore} from "@/stores/useMenuStore.js";


const props = defineProps({
  isCollapse: {
    type: Boolean,
    default: false
  }
});

const route = useRoute();
const store = useMenuStore()

const menuData = store.rawMenuData


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

    .title {
      max-width: 250px;
      opacity: 1;
      margin-left: 8px;
      white-space: nowrap;
      overflow: hidden;
      transition: max-width 0.3s ease,
      opacity 0.3s ease,
      margin-left 0.3s ease;
    }

    &.collapse-title {
      .title {
        max-width: 0; /* 收缩时宽度归零 */
        opacity: 0;
        margin-left: 0;
      }

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