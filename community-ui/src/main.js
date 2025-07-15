import './styles/style.scss'
import './styles/theme.scss'
import 'element-plus/dist/index.css'
import {createApp} from 'vue'
import {createPinia} from 'pinia'
import router from './router'
import ElementPlus from 'element-plus'
import App from './App.vue'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';
import {setupProtection} from "@/utils/protect.js";

const app = createApp(App)
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate);
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
if (import.meta.env.PROD) {
    setupProtection();
}
app.use(ElementPlus)
app.use(pinia)
app.use(router)
app.mount('#app')
