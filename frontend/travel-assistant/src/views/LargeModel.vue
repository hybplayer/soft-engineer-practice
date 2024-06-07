<template>
  <div class="large-model">
    <iframe src="https://ai.eth.jht213.com" width="100%" height="1000px" frameborder="0" allowfullscreen>
    </iframe>
  </div>
</template>

<script>
export default {
  name: 'LargeModel'
};
</script>

<style scoped>
.large-model {
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 5px;
  overflow: hidden;
}

iframe {
  width: 100%;
  height: 1000px;
  /* 你可以根据需要调整高度 */
  border: none;
}
</style>
