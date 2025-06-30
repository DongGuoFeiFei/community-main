<template>
  <div class="admin-menu-container">
    <div class="menu-title" :class="{ 'collapse-title': isCollapse }">
      <el-image src="/favicon.png" class="logo"/>
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
import {
  Bell as ElIconBell,
  ChatDotRound as ElIconChat,
  Collection as ElIconCollection,
  Connection as ElIconConnection,
  Document as ElIconDocument,
  Flag as ElIconFlag,
  Grid as ElIconGrid,
  Key as ElIconKey,
  List as ElIconList,
  Lock as ElIconLock,
  Message as ElIconMessage,
  Monitor as ElIconMonitor,
  Notebook as ElIconLog,
  Setting as ElIconSetting,
  TrendCharts as ElIconTrend,
  User as ElIconUser,
  Warning as ElIconWarning
} from '@element-plus/icons-vue';


const props = defineProps({
  isCollapse: {
    type: Boolean,
    default: false
  }
});

const route = useRoute();

const menuData = [
  {
    path: '/admin/dashboard',
    meta: {
      title: '控制台',
      iconComponent: ElIconMonitor
    }
  },
  {
    path: '/admin/content',
    meta: {
      title: '内容管理',
      iconComponent: ElIconGrid,
    },
    children: [
      {
        path: '/admin/articles',
        meta: {
          title: '文章管理',
          iconComponent: ElIconDocument
        }
      },
      {
        path: '/admin/tags',
        meta: {
          title: '标签管理',
          iconComponent: ElIconCollection
        }
      },
      {
        path: '/admin/comments',
        meta: {
          title: '评论管理',
          iconComponent: ElIconChat
        }
      }
    ]
  },
  {
    path: '/admin/community',
    meta: {
      title: '社区管理',
      iconComponent: ElIconConnection
    },
    children: [
      {
        path: '/admin/topics',
        meta: {
          title: '话题管理',
          iconComponent: ElIconCollection
        }
      },
      {
        path: '/admin/activities',
        meta: {
          title: '活动管理',
          iconComponent: ElIconTrend
        }
      }
    ]
  },
  {
    path: '/admin/user',
    meta: {
      title: '用户管理',
      iconComponent: ElIconUser
    },
    children: [
      {
        path: '/admin/userList',
        meta: {
          title: '用户列表',
          iconComponent: ElIconUser
        }
      },
      {
        path: '/admin/userAudit',
        meta: {
          title: '用户审核',
          iconComponent: ElIconWarning
        }
      }
    ]
  },
  {
    path: '/admin/permission',
    meta: {
      title: '权限管理',
      iconComponent: ElIconLock,
      roles: ['super-admin', 'admin'] // 仅超级管理员和管理员可见
    },
    children: [
      {
        path: '/admin/permissionRole',
        meta: {
          title: '角色管理',
          iconComponent: ElIconKey,
          roles: ['super-admin'] // 仅超级管理员可见
        }
      },
      {
        path: '/admin/permissionUser',
        meta: {
          title: '用户权限',
          iconComponent: ElIconUser,
          roles: ['super-admin', 'admin']
        }
      },
      {
        path: '/admin/permissionMenu',
        meta: {
          title: '菜单权限',
          iconComponent: ElIconList,
          roles: ['super-admin']
        }
      },
      {
        path: '/admin/permissionLog',
        meta: {
          title: '权限操作日志',
          iconComponent: ElIconLog,
          roles: ['super-admin', 'admin']
        }
      },
      {
        path: '/admin/permissionSetting',
        meta: {
          title: '权限设置',
          iconComponent: ElIconSetting,
          roles: ['super-admin']
        }
      }
    ]
  },
  {
    path: '/admin/moderation',
    meta: {
      title: '内容审核',
      iconComponent: ElIconFlag
    },
    children: [
      {
        path: '/admin/moderationReports',
        meta: {
          title: '举报处理',
          iconComponent: ElIconFlag
        }
      },
      {
        path: '/admin/moderationSensitive',
        meta: {
          title: '敏感词过滤',
          iconComponent: ElIconWarning
        }
      }
    ]
  },
  {
    path: '/admin/system',
    meta: {
      title: '系统设置',
      iconComponent: ElIconSetting
    },
    children: [
      {
        path: '/admin/systemBasic',
        meta: {
          title: '基础配置',
          iconComponent: ElIconSetting
        }
      },
      {
        path: '/admin/systemLogs',
        meta: {
          title: '系统操作日志',
          iconComponent: ElIconDocument
        }
      }
    ]
  },
  {
    path: '/admin/notification',
    meta: {
      title: '消息通知',
      iconComponent: ElIconBell,
      roles: ['admin'] // 基础管理员权限
    },
    children: [
      {
        path: '/admin/notificationSystem',
        meta: {
          title: '系统通知',
          iconComponent: ElIconMessage,
          roles: ['admin']
        }
      },
      {
        path: '/admin/notificationUser',
        meta: {
          title: '用户消息',
          iconComponent: ElIconUser,
          roles: ['admin']
        }
      },
      {
        path: '/admin/notificationTemplate',
        meta: {
          title: '消息模板',
          iconComponent: ElIconDocument,
          roles: ['super-admin'] // 仅超级管理员可配置
        }
      },
      {
        path: '/admin/notificationSettings',
        meta: {
          title: '通知设置',
          iconComponent: ElIconSetting,
          roles: ['super-admin']
        }
      }
    ]
  }
];


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