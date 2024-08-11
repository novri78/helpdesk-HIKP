import { createRouter, createWebHistory } from 'vue-router';
import Login from '@/views/LoginHelpdesk.vue';
import ActivityLogs from '../views/ActivityLogs.vue';
import Dashboard from '../views/ListDashboard.vue';
import Categories from '../views/Categories/ListCategories.vue';
import AddCategory from '../views/Categories/AddCategory.vue';
import EditCategory from '../views/Categories/EditCategory.vue';
import Tickets from '../views/Tickets/ListTickets.vue';
import AddTicket from '../views/Tickets/AddTicket.vue';
import EditTicket from '../views/Tickets/EditTicket.vue';
import Users from '../views/Users/ListUsers.vue';
import AddUser from '../views/Users/AddUser.vue';
import EditUser from '../views/Users/EditUser.vue';
import store from '@/store';

const routes = [
  { path: '/', name: 'Login', component: Login, meta: { guest: true } },
  { path: '/dashboard', name: 'Dashboard', component: Dashboard, meta: { requiresAuth: true } },
  { path: '/users', name: 'Users', component: Users, meta: { requiresAuth: true, role: 'ADMIN' } },
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
  routes,
});

// router.beforeEach(async (to, from, next) => {

//   await store.dispatch('fetchUserData'); // Ensure user data is loaded if token exists
//   const isAuthenticated = store.getters.isAuthenticated;
//   const userRole = store.getters.getUserRole;

//   //CHECK STATUS AUTH & ROLE_USER
//   console.log('Auth status:', isAuthenticated, 'User role:', userRole);

//   if (to.matched.some(record => record.meta.requiresAuth)) {
//     if (store.state.token == null) {
//       console.log('Not authenticated, redirecting to login');
//       return next({ name: 'Login' });
//     } else if (to.meta.role && to.meta.role !== userRole) {
//       console.log(`Unauthorized, role required: ${to.meta.role}, redirecting to dashboard`);
//       return next({ name: 'Dashboard' });
//     }
//   } else if (to.matched.some(record => record.meta.guest) && !store.state.token == null) {
//     console.log('Already authenticated, redirecting to user page list');
//     return next({ name: 'Users' });
//   }

//   next();
// });

router.beforeEach((to, from, next) => {
  const isAuthenticated = store.getters.isAuthenticated;
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isAuthenticated) {
      next('/');
    }
    else if (to.matched.some(record => record.meta.guest) && isAuthenticated) {
      next('/dashboard');
    } else {
      next();
    }
  } else if (to.meta.role && to.meta.role !== store.getters.getUserRole) {
    next('/users');
  } else {
    next();
  }
});

export default router;
