import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    port: 5174,  // 管理端后台使用 5174 端口（用户前端使用 5173）
    host: true,  // 监听所有网络接口，与 community-ui 保持一致
    strictPort: true,  // 端口被占用时报错，不自动切换端口
  },
})
