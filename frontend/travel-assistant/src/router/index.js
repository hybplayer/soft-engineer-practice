import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/AppHome.vue';
import Profile from '../views/AppProfile.vue';
import TravelIntention from '../views/TravelIntention.vue';
import NotFound from '../views/NotFound.vue';
import LoginRegister from '../components/LoginRegister.vue';
import TeamFormation from '../components/TeamFormation.vue';
import RoutePlanning from '../components/RoutePlanning.vue';
import ForumPage from '../views/ForumPage.vue';
import LargeModel from '../views/LargeModel.vue'; // 导入新的页面组件
import store from '../store';  // 导入 store

const routes = [
  { path: '/', component: Home, name: 'Home', meta: { showFooter: true } },
  { path: '/travel-intention', component: TravelIntention, name: 'TravelIntention', meta: { showFooter: true } },
  { path: '/login', component: LoginRegister, name: 'Login', meta: { showFooter: true } },
  { path: '/register', component: LoginRegister, name: 'Register', meta: { showFooter: false } },
  { path: '/team-formation', component: TeamFormation, name: 'TeamFormation', meta: { showFooter: true } },
  { path: '/route-planning', component: RoutePlanning, name: 'RoutePlanning', meta: { showFooter: true } }, // 修改 showFooter 为 true
  { path: '/forumpage', component: ForumPage, name: 'ForumPage', meta: { showFooter: true } },
  { path: '/user/:username', component: Profile, name: 'UserProfile', props: true, meta: { showFooter: true } }, // 修改 showFooter 为 true
  { path: '/large-model', component: LargeModel, name: 'LargeModel', meta: { showFooter: true } },
  { path: '/:catchAll(.*)', component: NotFound, name: 'NotFound', meta: { showFooter: false } }
];


const router = createRouter({
  history: createWebHistory(),
  routes
});

// 全局导航守卫
router.beforeEach((to, from, next) => {
  const isAuthenticated = store.state.visitor.auth.isAuthenticated; // 获取当前用户的登录状态
  if (to.name !== 'Login' && !isAuthenticated) {
    // 如果用户未登录且试图访问非登录页面，则重定向到登录页面
    next({ name: 'Login' });
  } else {
    // 允许用户访问其他页面
    next();
  }
});

export default router;
