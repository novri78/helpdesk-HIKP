import { createRouter, createWebHistory } from 'vue-router';
import Login from '@/views/LoginHelpdesk.vue';
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
import store from '@/store';
import cookie from 'js-cookie'

const routes = [
  { path: '/', name: 'Login', component: Login, meta: { guest: true } },
  { path: '/dashboard', name: 'Dashboard', component: Dashboard, meta: { requiresAuth: true } },
  { path: '/users', name: 'Users', component: Users, meta: { requiresAuth: true } },
  { path: '/user/add', name: 'AddUser', component: AddUser, meta: { requiresAuth: true, role: 'ADMIN' } },
  { path: '/user/edit/:id', name: 'EditUser', component: EditUser, meta: { requiresAuth: true, role: 'ADMIN' } },
  { path: '/tickets', name: 'Tickets', component: Tickets, meta: { requiresAuth: true } },
  { path: '/ticket/add', name: 'AddTicket', component: AddTicket, meta: { requiresAuth: true } },
  { path: '/ticket/edit/:id', name: 'EditTicket', component: EditTicket, meta: { requiresAuth: true } },
  { path: '/categories', name: 'Categories', component: Categories, meta: { requiresAuth: true } },
  { path: '/category/add', name: 'AddCategory', component: AddCategory, meta: { requiresAuth: true } },
  { path: '/category/edit/:id', name: 'EditCategory', component: EditCategory, meta: { requiresAuth: true } },
  { path: '/activity-logs', name: 'ActivityLogs', component: ActivityLogs, meta: { requiresAuth: true } },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// store.dispatch('checkAuth');

// if(cookie.get('token') !== undefined){
//   let auth = cookie.get('token');
//   store.commit('SET_LOGIN', auth)
// }

// Check for token in cookies and set it in the store if available
if (cookie.get('token') !== undefined) {
  let token = cookie.get('token');
  store.commit('SET_TOKEN', token);
  store.dispatch('fetchUserData');
}

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!store.state.user) {
      next({ name: 'Login' });
    } else {
      next();
    }
  } else if (to.matched.some((record) => record.meta.guest)) {
    if (store.state.user) {
      next({ name: 'Users' });
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router
