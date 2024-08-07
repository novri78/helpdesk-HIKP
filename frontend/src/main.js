import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import store from './store'
import axios from 'axios';

import { createBootstrap } from 'bootstrap-vue-next';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css';

// Set base URL for Axios
const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/api',
    timeout: 180000,
    headers: {
        'Content-Type': 'application/json'
    }
});

// Initialize Vue app
const app = createApp(App);

// Make axios accessible globally
app.config.globalProperties.$axios = axiosInstance; 

app.use(store);
app.use(router);
app.use(createBootstrap({ components: true, directives: true }));
app.mount('#app')
