import { createApp } from 'vue'
import { createPinia } from 'pinia'
import Antd from 'ant-design-vue'
import App from './App.vue'
import router from './router'
import {useLoginUserStore} from "@/stores/loginUser.ts";

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(Antd)

// 应用启动时获取登录用户信息
const loginUserStore = useLoginUserStore()
await loginUserStore.fetchLoginUser()
app.mount('#app')
