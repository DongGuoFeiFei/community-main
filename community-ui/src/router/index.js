// router/sessionStores.js

import {createRouter, createWebHistory} from 'vue-router'
import Index from '../views/Index.vue'
import Login from "@/views/Login.vue";
import NProgress from "@/utils/progress.js";
import dayjs from 'dayjs'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            redirect: '/index',
        },
        {
            path: '/login',
            name: 'login',
            component: Login,
        },
        {
            path: '/',
            name: 'index',
            component: Index,
            meta: {
                requiresAuth: true
            },
            children: [
                {
                    path: 'index',
                    name: 'card',
                    component: () => import("@/views/Card.vue"),

                },
                {
                    path: 'index/article/:id',
                    name: 'article',
                    component: () => import("@/views/PostDetail.vue"),
                },
            ]
        },
        {
            path: '/editor',
            name: 'editor',
            component: () => import("@/views/Editor.vue"),
            meta: {requiresAuth: true}
        },
        {
            path: '/editor-edit',
            name: 'editor-edit',
            component: () => import("@/views/Editor.vue"),
            meta: {requiresAuth: true}
        },
        {
            path: '/user',
            name: 'user',
            component: () => import('@/views/user/Index.vue'),
            meta: {requiresAuth: true},
            children: [
                {
                    path: "profile",
                    name: "profile",
                    component: () => import("@/views/user/UserProfile.vue"),
                    meta: {requiresAuth: true},
                },
                {
                    path: "articles",
                    name: "articles",
                    component: () => import("@/views/user/UserArticles.vue"),
                    mete: {
                        title: '我的文章',
                        requiresAuth: true,
                    }
                },
                {
                    path: 'dustbin',
                    name: 'dustbin',
                    component: () => import("@/views/user/UserTrash.vue"),
                    meta: {
                        title: "我的垃圾箱",
                        requiresAuth: true,
                    }
                },
                {
                    path: 'notifications',
                    name: 'notifications',
                    component: () => import('@/views/user/UserNotification.vue'),
                    meta: {
                        title: '通知',
                        requiresAuth: true,
                    }
                },
            ]
        }
    ],
})

// 路由守卫
router.beforeEach((to, from, next) => {
    NProgress.start()

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
                path: '/login',
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