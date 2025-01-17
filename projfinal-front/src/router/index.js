import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import Cart from '../views/Cart.vue';
import Payment from '../views/Payment.vue';

const routes = [
  { path: '/', component: Home },
  { path: '/cart', component: Cart },
  { path: '/payment', component: Payment }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
