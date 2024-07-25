import { createRouter, createWebHistory } from 'vue-router';
import ActivityLogs from '../views/ActivityLogs.vue';
import Categories from '../views/Categories.vue';
import Dashboard from '../views/Dashboard.vue';
import Tickets from '../views/Tickets.vue';
import Users from '../views/Users.vue';

const routes = [
  { path: '/', name: 'Dashboard', component: Dashboard },
  { path: '/users', name: 'Users', component: Users },
  { path: '/tickets', name: 'Tickets', component: Tickets },
  { path: '/categories', name: 'Categories', component: Categories },
  { path: '/activity-logs', name: 'ActivityLogs', component: ActivityLogs }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
