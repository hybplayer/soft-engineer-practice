<template>
  <div class="not-found">
    <h2>页面未找到</h2>
    <p>抱歉，您访问的页面不存在。</p>
    <router-link to="/">返回首页</router-link>
  </div>
</template>

<script>
export default {};
</script>

<style scoped>
.not-found {
  text-align: center;
  margin-top: 50px;
}
</style>
