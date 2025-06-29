// store/sessionStores.js
import {computed, ref} from "vue";
import {defineStore} from 'pinia'
import {getUnreadCount} from "@/api/notification.js";

export const sessionStore = defineStore("sessionStores", () => {
    const token = ref(localStorage.getItem('token') || null)
    const baseURL = ref('http://127.0.0.1:8080')

    const isAuthenticated = computed(() => !!token.value)

    const isEditMode = ref(false)
    const editorArticleId = ref(0)
    // 通知相关状态
    const unreadCount = ref(0);
    const lastNotificationCheck = ref(null);

    // 更新未读数量
    const updateUnreadCount = async () => {
        try {
            const res = await getUnreadCount();
            if (res.code === 200) {
                unreadCount.value = res.data;
                lastNotificationCheck.value = new Date();
            }
        } catch (error) {
            console.error('获取未读通知数量失败:', error);
        }
    };

    function changeEditStatus() {
        isEditMode.value = !isEditMode.value
    }

    function clearStorage() {
        localStorage.removeItem('my-sessionStore')
    }

    return {
        isAuthenticated,
        baseURL,
        isEditMode,
        unreadCount,
        lastNotificationCheck,
        updateUnreadCount,
        changeEditStatus,
        editorArticleId,
        clearStorage
    }
}, {
    persist: {
        key: 'my-sessionStore',
        storage: sessionStorage,
    }
})
