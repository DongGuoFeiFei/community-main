// store/sessionStores.js
import {computed, ref} from "vue";
import {defineStore} from 'pinia'

export const sessionStore = defineStore("sessionStores", () => {
    const token = ref(localStorage.getItem('token') || null)
    const baseURL = ref('http://127.0.0.1:8080')

    const isAuthenticated = computed(() => !!token.value)

    const isEditMode = ref(false)
    const editorArticleId = ref(0)

    function changeEditStatus() {
        isEditMode.value = !isEditMode.value
    }

    return {isAuthenticated, baseURL, isEditMode, changeEditStatus, editorArticleId}
}, {
    persist: {
        key: 'my-sessionStore',
        storage: sessionStorage,
    }
})
