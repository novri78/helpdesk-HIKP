import { createRouter, createWebHistory } from 'vue-router';
import ActivityLogs from '../views/ActivityLogs.vue';
import Dashboard from '../views/ListDashboard.vue';
import Categories from '../views/Categories/ListCategories.vue';
import AddCategory from '../views/Categories/AddCategory.vue';
import EditCategory from '../views/Categories/EditCategory.vue';
import Tickets from '../views/Tickets/ListTickets.vue'
import AddTicket from '../views/Tickets/AddTicket.vue';
import EditTicket from '../views/Tickets/EditTicket.vue';
import Users from '../views/Users/ListUsers.vue';
import AddUser from '../views/Users/AddUser.vue';
import EditUser from '../views/Users/EditUser.vue';

const routes = [
  { path: '/', name: 'Dashboard', component: Dashboard },
  { path: '/users', name: 'Users', component: Users },
  { path: '/user/add', name: 'AddUser', component: AddUser },
  { path: '/user/edit/:id', name: 'EditUser', component: EditUser },
  { path: '/tickets', name: 'Tickets', component: Tickets },
  { path: '/ticket/add', name: 'AddTicket', component: AddTicket },
  { path: '/ticket/edit/:id', name: 'EditTicket', component: EditTicket },
  { path: '/categories', name: 'Categories', component: Categories },
  { path: '/category/add', name: 'AddCategory', component: AddCategory },
  { path: '/category/edit/:id', name: 'EditCategory', component: EditCategory },
  { path: '/activity-logs', name: 'ActivityLogs', component: ActivityLogs }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
