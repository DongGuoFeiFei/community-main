import {fileURLToPath, URL} from 'node:url'

import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
    plugins: [
        vue(),
        vueDevTools({
            launchEditor: 'D:\\myapps\\JetBrains\\IntelliJ IDEA 2023.1.3\\bin\\idea64.exe'
        }),
    ],
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        },
    },
    define: {
        global: 'globalThis', // 修复global未定义的问题
    },
    optimizeDeps: {
        include: ['sockjs-client'] // 确保sockjs-client被正确优化
    }
})
