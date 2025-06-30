import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useArticleStore = defineStore('article', () => {
    const categories = ref([])
    const tags = ref([])

    const setCategories = (data) => {
        categories.value = data
    }

    const setTags = (data) => {
        tags.value = data
    }

    return {
        categories,
        tags,
        setCategories,
        setTags
    }
})