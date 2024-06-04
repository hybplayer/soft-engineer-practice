<template>
  <header>
    <nav>
      <router-link :class="{ 'active': $route.path === '/' }" class="nav-link" to="/">首页</router-link>
      <router-link :class="{ 'active': isProfileActive }" class="nav-link" :to="`/user/${username}`">个人资料</router-link>
      <router-link :class="{ 'active': $route.path === '/travel-intention' }" class="nav-link"
        to="/travel-intention">旅游意向</router-link>
      <router-link :class="{ 'active': $route.path === '/team-formation' }" class="nav-link"
        to="/team-formation">自动组队</router-link>
      <router-link :class="{ 'active': $route.path === '/forumpage' }" class="nav-link" to="/forumpage">论坛</router-link>
      <router-link :class="{ 'active': $route.path === '/route-planning' }" class="nav-link"
        to="/route-planning">地图</router-link>
      <router-link v-if="!isAuthenticated" class="nav-link" to="/login">登录/注册</router-link>
      <button v-if="isAuthenticated" @click="logout" class="logout-btn">退出登录</button>
    </nav>
  </header>
</template>

<script>
import { mapState } from 'vuex';

export default {
  computed: {
    ...mapState('visitor', ['auth']),
    isAuthenticated() {
      return this.auth.isAuthenticated;
    },
    username() {
      // 假设你将用户名存储在Vuex
      return this.auth.username;
    },
    isProfileActive() {
      return this.$route.path.startsWith(`/user/${this.username}`);
    }
  },
  methods: {
    logout() {
      this.$store.commit('visitor/logout');
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
header {
  background-color: #545c64;
  padding: 1rem;
}

nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.nav-link {
  text-decoration: none;
  color: #fff;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  transition: transform 0.3s ease, background-color 0.3s ease;
}

.nav-link:hover {
  background-color: #333;
  transform: scale(1.1);
}

.active {
  background-color: #333;
  color: #ffd700;
  /* 黄色 */
}

.logout-btn {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 5px;
  background-color: #fff;
  color: #000;
  cursor: pointer;
  transition: transform 0.3s ease, background-color 0.3s ease, color 0.3s ease;
}

.logout-btn:hover {
  background-color: #f8f8f8;
  color: #333;
  transform: scale(1.1);
}
</style>
