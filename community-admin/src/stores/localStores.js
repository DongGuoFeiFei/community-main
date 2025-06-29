import {computed, ref} from 'vue'
import {defineStore} from 'pinia'
import dayjs from "dayjs";

export const localStore = defineStore('localStores', () => {
    const userInfo = ref({
        avatarUrl: "",
        expiresIn: "",
        token: "",
        tokenType: "",
        userInfo: {
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
    const baseURL = ref('http://127.0.0.1:8080')

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

    return {
        userInfo,
        baseURL,
        isTokenExpired,
        tokenInfo,
        clearStorage
    }
}, {
    persist: {
        key: 'my-localStore',
        storage: localStorage,
    }
})
