<template>
  <div id="app">
    <AppHeader />
    <main>
      <router-view></router-view>
    </main>
    <AppFooter v-if="showFooter" />
  </div>
</template>

<script>
import AppHeader from './components/AppHeader.vue';
import AppFooter from './components/AppFooter.vue';

export default {
  components: {
    AppHeader,
    AppFooter
  },
  computed: {
    showFooter() {
      return this.$route.meta.showFooter;
    }
  }
};
</script>

<style>
html,
body {
  height: 100%;
  margin: 0;
}

#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

main {
  flex: 1;
  padding: 20px;
}

footer {
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  padding: 1rem;
  background-color: #f8f8f8;
}
</style>
