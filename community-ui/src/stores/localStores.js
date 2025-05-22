import {ref} from 'vue'
import {defineStore} from 'pinia'

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

    return {userInfo, token, setToken, clearToken,baseURL}
}, { // 持久化配置（第三个参数）
    persist: {
        key: 'my-localStore',
        storage: localStorage,
    }
})
