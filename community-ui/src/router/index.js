// router/sessionStores.js

import {createRouter, createWebHistory} from 'vue-router'
import Index from '../views/Index.vue'
import Login from "@/views/Login.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            redirect: '/login',
        },
        {
            path: '/login',
            name: 'login',
            component: Login,
        },
        {
            path: '/index',
            name: 'index',
            component: Index,
            meta: {requiresAuth: true}, // 添加元信息，表示需要认证
            children: [
                {
                    path: 'card',
                    name: 'card',
                    component: () => import("@/views/Card.vue"),
                    meta: {requiresAuth: true}
                }, {
                    path: 'card/:id',
                    name: 'postDetail',
                    component: () => import("@/views/PostDetail.vue"),
                    meta: {requiresAuth: true}
                },
            ]
        }, {
            path: '/editor',
            name: 'editor',
            component: () => import("@/views/Editor.vue"),
            meta: {requiresAuth: true}
        }, {
            path: '/user',
            name: 'user',
            component: () => import("@/views/user/Index.vue"),
            meta: {requiresAuth: true}
        }
    ],
})

// 路由守卫
router.beforeEach((to, from, next) => {
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

export default router