/// <reference types="vite/client" />

// Vue 组件类型声明
declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

// 环境变量类型声明
interface ImportMetaEnv {
  readonly VITE_API_BASE_URL: string
  readonly VITE_WS_URL: string
  readonly VITE_APP_TITLE: string
  readonly VITE_UPLOAD_URL: string
  // 在这里添加更多环境变量...
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}

// 全局类型扩展
declare global {
  interface Window {
    // 可以在这里声明挂载到 window 上的全局变量
    __APP_VERSION__: string
  }
}

export {}

