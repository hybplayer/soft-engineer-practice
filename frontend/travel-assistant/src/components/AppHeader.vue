<template>
  <header>
    <nav>
      <router-link to="/">首页</router-link>
      <router-link to="/profile">个人资料</router-link>
      <router-link to="/travel-intention">旅游意向</router-link>
      <router-link to="/team-formation">自动组队</router-link>
      <router-link to="/forumpage">论坛</router-link>
      <router-link to="/route-planning">地图</router-link>
      <router-link v-if="!isAuthenticated" to="/login">登录/注册</router-link>
      <button v-if="isAuthenticated" @click="logout">登出</button>
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
nav {
  display: flex;
  justify-content: space-around;
  padding: 1rem;
  background-color: #f8f8f8;
}
</style>
