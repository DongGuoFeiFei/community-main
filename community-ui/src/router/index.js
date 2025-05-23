// router/sessionStores.js

import {createRouter, createWebHistory} from 'vue-router'
import Index from '../views/Index.vue'
import Login from "@/views/Login.vue";
import NProgress from "@/utils/progress.js";

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
            meta: {requiresAuth: true}, // 添加元信息，表示需要认证
            children: [
                {
                    path: 'index',
                    name: 'card',
                    component: () => import("@/views/Card.vue"),
                    meta: {requiresAuth: true}
                }, {
                    path: 'index/article/:id',
                    name: 'article',
                    component: () => import("@/views/PostDetail.vue"),
                    meta: {requiresAuth: true}
                },
            ]
        },
        {
            path: '/editor',
            name: 'editor',
            component: () => import("@/views/Editor.vue"),
            meta: {requiresAuth: true}
        }, {
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
                    component: () => import("@/views/user/UserProfile.vue")
                },
                {
                    path: "articles",
                    name: "articles",
                    component: () => import("@/views/user/UserArticles.vue"),
                    mete: {
                        title: '我的文章',
                        requiresAuth: true
                    }
                }, {
                    path: 'dustbin',
                    name: 'dustbin',
                    component: () => import("@/views/user/UserTrash.vue"),
                    meta: {
                        title: "我的垃圾箱",
                        requiresAuth: true
                    }
                }
            ]
        }
    ],
})


// 路由守卫
router.beforeEach((to, from, next) => {
    NProgress.start()
    // 检查目标路由是否需要认证
    if (to.matched.some(record => record.meta.requiresAuth)) {
        // 检查本地存储中是否有token
        const token = localStorage.getItem('token');
        if (!token) {
            // 如果没有token，重定向到登录页面
            next({
                path: '/login',
                query: {redirect: to.fullPath} // 可选：保存目标路径以便登录后跳转
            });
        } else {
            next();
        }
    } else {
        // 不需要认证的路由，直接放行
        next();
    }
});

router.afterEach(() => NProgress.done());

export default router