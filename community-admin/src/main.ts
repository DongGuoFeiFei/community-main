import './styles/style.less'
import {createApp} from 'vue'
import {createPinia} from 'pinia'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import './styles/theme.css'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';
// import {setupProtection} from '@/utils/protect.js';


const app = createApp(App)
const pinia = createPinia()

pinia.use(piniaPluginPersistedstate);
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

// 拦截开发着工具
// setupProtection()
app.use(ElementPlus)
app.use(pinia)
app.use(router)
app.mount('#app')
