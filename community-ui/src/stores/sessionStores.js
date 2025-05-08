// store/sessionStores.js
import {computed, ref} from "vue";
import {defineStore} from 'pinia'

export const sessionStore = defineStore("sessionStores", () => {
    const token = ref(localStorage.getItem('token') || null)
    const baseURL = ref('http://127.0.0.1:8080')
    const isAuthenticated = computed(() => !!token.value)

    return {isAuthenticated, baseURL}
}, {
    persist: {
        key: 'my-sessionStore',
        storage: sessionStorage,
    }
})