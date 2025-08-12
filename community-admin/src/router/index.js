// router/sessionStores.js

import {createRouter, createWebHistory} from 'vue-router'
import NProgress from "@/utils/progress.js";
import dayjs from 'dayjs'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/admin',
    },
    {
      path: '/login',
      name: 'login',
      component: () => import("@/views/auth/Login.vue"),
      meta: {
        requiresAuth: false,
        title: '登录'
      }
    },
    {
      path: "/admin",
      name: "admin",
      component: () => import("@/views/admin/Index.vue"),
      meta: {
        title: "后台管理系统",
        requiresAuth: true
      },
      children: [
        {
          path: "articles",
          name: 'adminArticles',
          component: () => import("@/views/admin/views/article/Index.vue"),
          meta: {
            title: "文章管理"
          }
        }, {
          path: "comments",
          name: 'adminComments',
          component: () => import("@/views/admin/views/comments/Index.vue"),
          meta: {
            title: "评论管理"
          }
        }, {
          path: "notificationSystem",
          name: 'adminNotificationSystem',
          component: () => import("@/views/admin/views/notificationSystem/Index.vue"),
          meta: {
            title: "消息通知"
          }
        }, {
          path: "dashboard",
          name: 'dashboard',
          component: () => import("@/views/admin/views/dashboard/Index.vue"),
          meta: {
            title: "控制台"
          }
        }, {
          path: "tags",
          name: 'tags',
          component: () => import("@/views/admin/views/tag/Index.vue"),
          meta: {
            title: "标签管理"
          }
        }, {
          path: "topics",
          name: 'topics',
          component: () => import("@/views/admin/views/topic/Index.vue"),
          meta: {
            title: "话题管理"
          }
        }, {
          path: "activities",
          name: 'activities',
          component: () => import("@/views/admin/views/activity/Index.vue"),
          meta: {
            title: "活动管理"
          }
        }, {
          path: "userList",
          name: 'userList',
          component: () => import("@/views/admin/views/user/Index.vue"),
          meta: {
            title: "活动管理"
          }
        },
        {
          path: "announcement",
          name: 'announcement',
          component: () => import("@/views/admin/views/announcement/Index.vue"),
          meta: {
            title: "系统公告"
          }
        }, {
          path: 'permissionRole',
          component: () => import("@/views/admin/views/permissionRole/Index.vue"),
          meta: {
            title: '角色管理'
          }
        }
      ]
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import("@/views/auth/NotFound.vue"),
      meta: {
        title: '404 - 页面未找到'
      }
    }
  ],
})

// 路由守卫
router.beforeEach((to, from, next) => {
  NProgress.start()

  if (to.meta.title) {
    document.title = to.meta.title;
  }
  // 检查目标路由是否需要认证
  if (to.matched.some(record => record.meta.requiresAuth)) {
    // 从 localStorage 获取 token 和用户信息
    const token = localStorage.getItem('token')
    const userInfoStr = localStorage.getItem('my-localStore')

    // 解析用户信息
    let userInfo = null
    try {
      userInfo = userInfoStr ? JSON.parse(userInfoStr)?.userInfo : null
    } catch (e) {
      console.error('Failed to parse user info:', e)
    }
    // 检查 token 是否存在且未过期
    if (!token || isTokenExpired(userInfo)) {
      next({
        path: '/admin',
        query: {redirect: to.fullPath} // 保存目标路径以便登录后跳转
      })
    } else {
      // 有有效 token，继续导航
      next()
    }
  } else {
    // 不需要认证的路由，直接放行
    next()
  }
})

// 独立验证 token 是否过期的方法
function isTokenExpired(userInfo) {
  if (!userInfo || !userInfo.expiresIn || !userInfo.userInfo?.lastLogin) {
    return true // 缺少必要信息视为过期
  }

  try {
    const expiresIn = Number(userInfo.expiresIn) || 259200 // 默认3天(秒)
    const expiryTime = dayjs(userInfo.userInfo.lastLogin).add(expiresIn, 'second')
    return dayjs().isAfter(expiryTime)
  } catch (e) {
    console.error('Token expiry check failed:', e)
    return true // 日期解析失败视为过期
  }
}

router.afterEach(() => NProgress.done())

export default router
