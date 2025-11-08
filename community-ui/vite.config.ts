import { fileURLToPath, URL } from "node:url";
import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import vueDevTools from "vite-plugin-vue-devtools";

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    // vueDevTools(),
    vueDevTools({
      launchEditor:
        "D:\\myapps\\JetBrains\\IntelliJ IDEA 2023.1.3\\bin\\idea64.exe",
        // "D:\\myapps\\cursor\\Cursor.exe",
        // "C:\\Users\\HONOR\\AppData\\Local\\Programs\\Microsoft VS Code\\Code.exe",
    }),
  ],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
  server: {
    port: 5173,
    host: true,
    // 代理配置（根据实际后端地址修改）
    // todo 修改为后端地址,配置不理解，和我之前有过的后端接口配置冲突，不理解
    proxy: {
      "/api": {
        target: "http://localhost:8080", // 修改为你的后端地址
        changeOrigin: true,
        // rewrite: (path) => path.replace(/^\/api/, '')
      },
      "/ws": {
        target: "ws://localhost:8080", // WebSocket 地址
        ws: true,
        changeOrigin: true,
      },
    },
  },
  build: {
    // 生产环境配置
    outDir: "dist",
    assetsDir: "assets",
    sourcemap: false,
    // 分包策略
    rollupOptions: {
      output: {
        manualChunks: {
          "vue-vendor": ["vue", "vue-router", "pinia"],
          "element-plus": ["element-plus"],
          editor: ["@tiptap/vue-3", "@tiptap/starter-kit", "vditor"],
          utils: ["axios", "dayjs", "dompurify"],
        },
      },
    },
    // 压缩配置
    minify: "terser",
    terserOptions: {
      compress: {
        drop_console: true,
        drop_debugger: true,
      },
    },
  },
});
