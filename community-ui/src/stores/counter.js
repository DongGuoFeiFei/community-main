import {computed, ref} from 'vue'
import {defineStore} from 'pinia'

export const useCounterStore = defineStore('counter', () => {
    const count = ref(0)
    const doubleCount = computed(() => count.value * 2)

    function increment() {
        count.value++
    }

    return {count, doubleCount, increment}
}, {
    persist: {
        key: 'my-auth-store', // 自定义存储键名
        storage: sessionStorage, // 使用sessionStorage替代localStorage
        paths: ['token'], // 只持久化token字段
        serializer: { // 自定义序列化
            serialize: JSON.stringify,
            deserialize: JSON.parse
        },
        beforeRestore: (ctx) => {
            console.log(`即将恢复 store: ${ctx.store.$id}`)
        },
        afterRestore: (ctx) => {
            console.log(`已恢复 store: ${ctx.store.$id}`)
        }
    }
})
