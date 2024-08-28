import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import store from './store'
import axios from 'axios';


import { createBootstrap } from 'bootstrap-vue-next';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';

// Import the chart library and moment
import { Chart, registerables } from 'chart.js';
import VueChartJS from 'vue-chartjs';
import moment from 'moment';
import VueMoment from 'vue-moment';

// Optional: Use ApexCharts for more complex charts
import VueApexCharts from 'vue3-apexcharts';

Chart.register(...registerables); // Register Chart.js components


axios.defaults.baseURL = 'http://localhost:8080/api';

axios.interceptors.request.use(
    (config) => {
        const token = store.state.token;
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    }, (error) => {
        return Promise.reject(error);
    }
);

store.dispatch('checkAuth');

// Initialize Vue app
const app = createApp(App);

// Make axiosInstance globally accessible through the app
app.config.globalProperties.$axios = axios; 

app.use(store);
app.use(router);
app.use(createBootstrap({ components: true, directives: true }));
// app.use(VueMoment, { moment });
app.use(VueChartJS);
app.use(VueApexCharts);
app.mount('#app');