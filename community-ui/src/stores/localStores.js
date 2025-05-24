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
    const token = ref(localStorage.getItem('token') || null)

    function setToken(newToken) {
        token.value = newToken
        localStorage.setItem('token', newToken)
    }

    function clearToken() {
        token.value = null
        localStorage.removeItem('token')
    }

    // token是否过期
    const isTokenExpired = computed(() => {
        if (!token.value) return true // 无token视为过期

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

    return {userInfo, token, setToken, clearToken, baseURL, isTokenExpired, tokenInfo}
}, { // 持久化配置（第三个参数）
    persist: {
        key: 'my-localStore',
        storage: localStorage,
    }
})
