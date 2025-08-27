// router/sessionStores.js

import {createRouter, createWebHistory} from 'vue-router'
import NProgress from "@/utils/progress.js";
import dayjs from 'dayjs'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/register',
            name: 'Register',
            component: () => import('@/views/auth/Register.vue'),
            meta: {
                requiresAuth: false,
                title: '注册'
            }
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
            path: '/forgot-password',
            name: 'ForgotPassword',
            component: () => import('@/views/auth/ForgotPassword.vue'),
            meta: {
                requiresAuth: false,
                title: '找回密码'
            }
        },
        {
            path: '/',
            name: 'index',
            component: () => import("@/views/pages/views/home/Index.vue"),
            meta: {
                requiresAuth: true,
                title: "采芙蓉"
            }
        },
        {
            path: '/:id',
            name: 'routeCategoryId',
            component: () => import("@/views/pages/views/home/Index.vue"),
            meta: {
                requiresAuth: true,
                title: "采芙蓉"
            },
        },
        {
            path: '/article/:id',
            name: 'article',
            component: () => import("@/views/pages/views/article/Index.vue"),
            meta: {
                title: "",
                requiresAuth: true
            },
        },
        {
            path: '/editor',
            name: 'editor',
            component: () => import("@/views/pages/views/edit/Index.vue"),
            meta: {
                requiresAuth: true,
                title: "编辑文章"
            }
        },
        {
            path: '/test',
            name: 'test',
            component: () => import("@/views/pages/views/test/Index.vue"),
            meta: {
                requiresAuth: true,
                title: "页面测试"
            }
        },
        {
            path: '/ws-chat',
            name: 'ws-chat',
            component: () => import("@/views/pages/views/wsChat/Index.vue"),
            meta: {
                requiresAuth: true,
                title: "聊天室"
            }
        },
        {
            path: '/editor-edit',
            name: 'editor-edit',
            component: () => import("@/views/pages/views/edit/Index.vue"),
            meta: {
                requiresAuth: true,
                title: "修改文章"
            }
        },
        {
            path: '/myself',
            name: 'myself',
            component: () => import('@/views/user/Index.vue'),
            meta: {requiresAuth: true},
            children: [
                {
                    path: "profile",
                    name: "profile",
                    component: () => import("@/views/user/views/profile/index.vue"),
                    meta: {
                        requiresAuth: true,
                        title: "个人中心"
                    },
                },
                {
                    path: "articles",
                    name: "articles",
                    component: () => import("@/views/user/views/articles/index.vue"),
                    meta: {
                        requiresAuth: true,
                        title: '我的文章',
                    }
                },
                {
                    path: 'collections',
                    name: 'collections',
                    component: () => import("@/views/user/views/collection/index.vue"),
                    meta: {
                        title: "我的收藏",
                        requiresAuth: true,
                    }
                },
                {
                    path: 'notifications',
                    name: 'notifications',
                    component: () => import('@/views/user/views/notification/index.vue'),
                    meta: {
                        title: '通知',
                        requiresAuth: true,
                    }
                },
                {
                    path: 'follow&fans',
                    name: 'follow&fans',
                    component: () => import('@/views/user/views/follow&fans/index.vue'),
                    meta: {
                        title: '关注&粉丝',
                        requiresAuth: true,
                    }
                },
                {
                    path: 'pointAccount',
                    name: 'pointAccount',
                    component: () => import('@/views/user/views/pointAccount/Index.vue'),
                    meta: {
                        title: '积分',
                        requiresAuth: true,
                    }
                },
            ]
        },
        {
            path: '/author/:id',
            name: 'author',
            component: () => import("@/views/pages/views/author/Index.vue"),
            meta: {
                title: "作者主页",
                requiresAuth: true
            },
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
                path: '/login',
                query: {redirect: to.fullPath}
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
        return true
    }
}

router.afterEach(() => NProgress.done())

export default router
