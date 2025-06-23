<template>
  <div class="layout-container">
    <!-- 左侧菜单栏 -->
    <el-menu
        class="el-menu-vertical"
        :default-active="activeMenu"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
    >
      <!-- 菜单顶部Logo/标题 -->
      <div class="menu-logo">
        <span>个人中心</span>
      </div>

      <!-- 动态生成菜单（目前只有一集菜单，有需要时在进行添加） -->
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
</template>

<script setup>
import {ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'

const router = useRouter()
const route = useRoute()
// 响应式状态
const activeMenu = ref('/user/profile')
activeMenu.value = route.path
const props = defineProps({
  isCollapse: {
    type: Boolean,
    default: false
  }
})
const emit = defineEmits(["changeCollapse"])

// 菜单数据 - 个人中心相关功能
const menuData = ref([
  // {
  //   path: '/user/dashboard',
  //   meta: {
  //     title: '仪表板',
  //     icon: 'el-icon-s-data' // 或者 'el-icon-monitor'
  //   }
  // },
  {
    path: '/user/profile',
    meta: {
      title: '个人信息',
      icon: 'el-icon-user'
    }
  },
  // {
  //   path: '/user/account',
  //   meta: {
  //     title: '账号安全',
  //     icon: 'el-icon-lock'
  //   }
  // },
  {
    path: '/user/articles',
    meta: {
      title: '我的文章',
      icon: 'el-icon-document'
    }
  },
  {
    path: '/user/dustbin',
    meta: {
      title: '垃圾箱',
      icon: 'el-icon-edit-outline'
    }
  },
  // {
  //   path: '/user/comments',
  //   meta: {
  //     title: '我的评论',
  //     icon: 'el-icon-chat-dot-round'
  //   }
  // },
  {
    path: '/user/collections',
    meta: {
      title: '我的收藏',
      icon: 'el-icon-star-off'
    }
  },
  // {
  //   path: '/user/likes',
  //   meta: {
  //     title: '我的点赞',
  //     icon: 'el-icon-thumb'
  //   }
  // },
  // {
  //   path: '/user/follows',
  //   meta: {
  //     title: '关注/粉丝',
  //     icon: 'el-icon-connection'
  //   }
  // },
  {
    path: '/user/notifications',
    meta: {
      title: '消息通知',
      icon: 'el-icon-bell'
    }
  },
  // {
  //   path: '/user/settings',
  //   meta: {
  //     title: '个人设置',
  //     icon: 'el-icon-setting'
  //   }
  // }
])

// 方法
const toggleCollapse = () => {
  emit("changeCollapse", !props.isCollapse)
}

// 改变路由
const handleMenuSelect = (menuItem) => {
  activeMenu.value = menuItem.path
  router.push(menuItem.path)
}
</script>

<style scoped>
.layout-container {
  display: flex;
  height: 100vh;
  width: 100%;
}

.el-menu-vertical {
  height: 100%;
  border-right: none;
  width: 100%;
  background-color: #545c64; /* 确保背景色设置在这里 */
  overflow-y: auto; /* 添加滚动条 */
}

.menu-logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  background-color: #434a50;
  cursor: pointer;
}

.el-menu-item [class^=el-icon-] {
  vertical-align: middle;
  margin-right: 5px;
  width: 24px;
  text-align: center;
  font-size: 18px;
}

/*隐藏滚轮*/
.el-menu-vertical::-webkit-scrollbar {
  display: none; /* Chrome Safari */
}

</style>