import {computed, ref} from 'vue'
import {defineStore} from 'pinia'
import dayjs from "dayjs";
import config from "@/config.js";

export const localStores = defineStore('localStores', () => {
    const userInfo = ref({
        avatarUrl: "",
        expiresIn: "",
        token: "",
        tokenType: "",
        userInfo: {
            avatar: "",
            createTime: "",
            email: "",
            fileId: "",
            isActive: "",
            lastLogin: "",
            nickname: "",
            password: "",
            phone: "",
            userId: "",
            username: ""
        }
    })

    const tokenInfo = ref({
        token: "",
        refreshTime: "",
        expiresIn: ""
    })
    const baseURL = ref(config.apiBaseUrl)

    // token是否过期
    const isTokenExpired = computed(() => {
        if (!tokenInfo.value.token) return true // 无token视为过期

        const lastLogin = userInfo.value.userInfo.lastLogin
        const expiresIn = Number(userInfo.value.expiresIn) || 259200 // 默认3天(秒)

        if (!lastLogin) return true // 无登录时间视为过期

        try {
            const expiryTime = dayjs(lastLogin).add(expiresIn, 'second')
            return dayjs().isAfter(expiryTime)
        } catch {
            return true // 日期解析失败视为过期
        }
    })

    function clearStorage() {
        localStorage.removeItem('my-localStore')
    }

    // 公告关闭状态
    const announcementClosed = ref(false)

    // 获取公告关闭状态
    const getAnnouncementClosedState = () => {
        return announcementClosed.value
    }

    // 设置公告关闭状态
    const setAnnouncementClosedState = (state) => {
        announcementClosed.value = state
    }

    const hasReadLatestAnnouncement = ref(false)

    const setHasReadLatestAnnouncement = (isRead) => {
        hasReadLatestAnnouncement.value = isRead
    }

    return {
        getAnnouncementClosedState,
        setAnnouncementClosedState,
        userInfo,
        baseURL,
        isTokenExpired,
        tokenInfo,
        clearStorage,
        hasReadLatestAnnouncement,
        setHasReadLatestAnnouncement
    }
}, {
    persist: {
        key: 'my-localStore',
        storage: localStorage,
    }
})
