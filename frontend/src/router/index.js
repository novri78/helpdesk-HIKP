import { createRouter, createWebHistory } from 'vue-router'
import ActivityLogs from '../views/ActivityLogs.vue'
import Categories from '../views/Categories.vue'
import Dashboard from '../views/Dasbboard.vue'
import Tickets from '../views/Tickets.vue'
import Users from '../views/Users.vue'

const routes = [
  { path: '/', name: 'Dashboard', component: Dashboard },
  { path: '/users', name: 'Users', component: Users },
  { path: '/tickets', name: 'Tickets', component: Tickets },
  { path: '/categories', name: 'Categories', component: Categories },
  { path: '/activity-logs', name: 'ActivityLogs', component: ActivityLogs },
  {
    path: '/home',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
