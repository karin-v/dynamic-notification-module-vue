import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import AdminView from '../views/AdminView.vue';
import { isLoggedIn } from '../api.js'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        { path: '/', name: 'home', component: HomeView },
        { path: '/login', name: 'login', component: LoginView },
        { path: '/admin', name: 'admin', component: AdminView, meta: { requiresAuth: true } },
    ],
})

router.beforeEach((to) => {
    if (to.meta.requiresAuth && !isLoggedIn()) {
        return '/login'
    }
})

export default router
