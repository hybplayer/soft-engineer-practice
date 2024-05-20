import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import ElementPlus from 'element-plus';
import api from './api/index';
import 'element-plus/dist/index.css';
import * as Icons from '@element-plus/icons-vue';

const app = createApp(App);

app.config.globalProperties.$api = api; // 设置全局属性

app.use(ElementPlus);
app.use(router);
app.use(store);

for (const [key, component] of Object.entries(Icons)) {
  app.component(key, component);
}

app.mount('#app');
