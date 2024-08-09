import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import store from './store'
import axios from 'axios';

import { createBootstrap } from 'bootstrap-vue-next';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css';

// Create an Axios instance with the base URL
const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/api', // Base URL for all API requests
    timeout: 180000,
    headers: {
        'Content-Type': 'application/json'
    }
});

// Initialize Vue app
const app = createApp(App);

// Make axiosInstance globally accessible through the app
app.config.globalProperties.$axios = axiosInstance; 

// Make axiosInstance available to the Vuex store by setting it on the store
store.$axios = axiosInstance; // This is important

app.use(store);
app.use(router);
app.use(createBootstrap({ components: true, directives: true }));
app.mount('#app')
