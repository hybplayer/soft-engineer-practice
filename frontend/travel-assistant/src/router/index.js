// src/router/index.js

import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/AppHome.vue';
import Profile from '../views/AppProfile.vue';
import TravelIntention from '../views/TravelIntention.vue';
import NotFound from '../views/NotFound.vue';
import LoginRegister from '../components/LoginRegister.vue';
import TeamFormation from '../components/TeamFormation.vue';
import RoutePlanning from '../components/RoutePlanning.vue';
import ForumPage from '../views/ForumPage.vue';
import LargeModel from '../views/LargeModel.vue';
import store from '../store';

const routes = [
  { path: '/', component: Home, name: 'Home', meta: { showFooter: true } },
  { path: '/travel-intention', component: TravelIntention, name: 'TravelIntention', meta: { showFooter: true } },
  { path: '/login', component: LoginRegister, name: 'Login', meta: { showFooter: true } },
  { path: '/register', component: LoginRegister, name: 'Register', meta: { showFooter: false } },
  { path: '/team-formation', component: TeamFormation, name: 'TeamFormation', meta: { showFooter: true } },
  { path: '/route-planning', component: RoutePlanning, name: 'RoutePlanning', meta: { showFooter: true } },
  {
    path: '/forumpage', component: ForumPage, name: 'ForumPage', meta: { showFooter: true },
    beforeEnter: (to, from, next) => {
      store.dispatch('visitor/loadPosts').then(() => {
        next(); // 继续路由导航
      }).catch(() => {
        next(false); // 在失败时停止路由导航
      });
    }
  },
  { path: '/user/:username', component: Profile, name: 'UserProfile', props: true, meta: { showFooter: true } },
  { path: '/large-model', component: LargeModel, name: 'LargeModel', meta: { showFooter: true } },
  { path: '/:catchAll(.*)', component: NotFound, name: 'NotFound', meta: { showFooter: false } }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 全局导航守卫
router.beforeEach((to, from, next) => {
  const isAuthenticated = store.state.visitor.auth.isAuthenticated;
  if (to.name !== 'Login' && !isAuthenticated) {
    next({ name: 'Login' });
  } else {
    next();
  }
});

export default router;
