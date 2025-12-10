import { createRouter, createWebHistory } from 'vue-router';
import Register from '@/views/LoginAndRegister/RegisterUser.vue';
import Login from '@/views/LoginAndRegister/LoginHelpdesk.vue';
import ApproveUser from '@/views/LoginAndRegister/ApproveUser.vue';
import PendingApprovals from '@/views/LoginAndRegister/PendingApprovals.vue';
import Chat from '@/components/Chat/Chat.vue';
//import ActivityLogs from '../views/ActivityLogs.vue';
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
import AskPage from "../views/AskPage_Wikipedia.vue";
import store from '@/store';

const routes = [
  { path: '/', name: 'Login', component: Login, meta: { requiresAuth: false }, },
  { path: '/register', name: 'Register', component: Register, meta: { requiresAuth: false }, },
  { path: '/pending-approvals', name: 'PendingApprovals', component: PendingApprovals, meta: { requiresAuth: true, rolesAllowed: ['ADMIN'] } },
  { path: '/approve-users/:id', name: 'ApproveUser', component: ApproveUser, meta: { requiresAuth: true, rolesAllowed: ['ADMIN'] } },
  { path: '/chat', name: 'Chat', component: Chat, meta: { requiresAuth: true, rolesAllowed: ['USER', 'SUPPORT', 'ADMIN'] }, },
  { path: '/dashboard', name: 'Dashboard', component: Dashboard, meta: { requiresAuth: true, rolesAllowed: ['USER', 'SUPPORT', 'ADMIN'] }, },
  { path: '/users', name: 'Users', component: Users, meta: { requiresAuth: true, rolesAllowed: ['ADMIN'] }, },
  { path: '/user/add', name: 'AddUser', component: AddUser, meta: { requiresAuth: true, rolesAllowed: ['ADMIN'] }, },
  { path: '/user/edit/:id', name: 'EditUser', component: EditUser, meta: { requiresAuth: true, rolesAllowed: ['ADMIN'] }, },
  { path: '/tickets', name: 'Tickets', component: Tickets, meta: { requiresAuth: true, rolesAllowed: ['USER', 'SUPPORT', 'ADMIN'] } },
  { path: '/ticket/add', name: 'AddTicket', component: AddTicket, meta: { requiresAuth: true } },
  { path: '/ticket/edit/:id', name: 'EditTicket', component: EditTicket, meta: { requiresAuth: true } },
  { path: '/categories', name: 'Categories', component: Categories, meta: { requiresAuth: true, rolesAllowed: ['ADMIN']  } },
  { path: '/category/add', name: 'AddCategory', component: AddCategory, meta: { requiresAuth: true, rolesAllowed: ['ADMIN'] } },
  { path: '/category/edit/:id', name: 'EditCategory', component: EditCategory, meta: { requiresAuth: true, rolesAllowed: ['ADMIN'] } },
  // { path: '/activity-logs', name: 'ActivityLogs', component: ActivityLogs, meta: { requiresAuth: true } },
  { path: '/ask_wikipadia', name: 'AskPage', component: AskPage, meta: { requiresAuth: true, rolesAllowed: ['USER', 'SUPPORT', 'ADMIN']}},
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

router.beforeEach(async (to, from, next) => {
  const isAuthenticated = store.getters.isAuthenticated; // Assuming you have a getter to check authentication
  const userRole = store.getters.userRole; // Assuming you have a getter to get user role

  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isAuthenticated) {
      console.log("User is not authenticated, redirecting to Login");
      return next({ name: 'Login' });
    }

    if (to.meta.allowedRoles && !to.meta.allowedRoles.includes(userRole)) {
      console.log(`User role "${userRole}" is not allowed for this route, redirecting to appropriate route`);
      if (userRole === 'ADMIN') {
        return next({ name: 'Users' });
      } else if (['USER', 'SUPPORT'].includes(userRole)) {
        return next({ name: 'Dashboard' });
      }
    }
  }

  next();
});



export default router;
