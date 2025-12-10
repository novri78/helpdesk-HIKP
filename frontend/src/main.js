import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import store from './store'
import supabase, { fetchSession } from './supabase'
import axios from 'axios';

// Import Bootstrap and BootstrapVueNext
import { createBootstrap } from 'bootstrap-vue-next';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';

// Import the chart library and moment
import { Chart, registerables } from 'chart.js';
import { Bar, Bubble, Doughnut, Line, Pie, PolarArea, Radar, Scatter } from 'vue-chartjs';
import moment from 'moment';
import VueMoment from 'vue-moment';

// Optional: Use ApexCharts for more complex charts
import VueApexCharts from 'vue3-apexcharts';

// Register Chart.js components
Chart.register(...registerables);

// Axios configuration
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

// Note: we'll check auth after fetching Supabase session during bootstrap.

// Initialize Vue app
const app = createApp(App);

// Make axiosInstance globally accessible through the app
app.config.globalProperties.$axios = axios; 

app.use(store);
app.use(router);
app.use(createBootstrap({ components: true, directives: true }));
// app.use(VueMoment, { moment });
app.use(VueApexCharts);

// Register vue-chartjs components globally if needed
app.component('BarChart', Bar);
app.component('BubbleChart', Bubble);
app.component('DoughnutChart', Doughnut);
app.component('LineChart', Line);
app.component('PieChart', Pie);
app.component('PolarAreaChart', PolarArea);
app.component('RadarChart', Radar);
app.component('ScatterChart', Scatter);

// Async bootstrap: fetch session, run auth checks, then mount.
(async () => {
    try {
        const { data, error } = await fetchSession();
        if (error) console.warn('Supabase session fetch error', error);
        // If your store has an action to accept session data, dispatch it here.
        // For now, run the existing checkAuth to reconcile auth state.
        await store.dispatch('checkAuth');
    } catch (err) {
        console.warn('Error during app bootstrap', err);
    }

    app.mount('#app');
})();