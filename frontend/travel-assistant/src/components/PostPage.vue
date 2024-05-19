<template>
  <div class="post">
    <div class="user-info">
      <img :src="post.avatar" alt="User Avatar" />
      <span>{{ post.username }}</span>
    </div>

    <div class="forum">
      <div>
        <h3>{{ post.title }}</h3>
        <p>{{ post.content }}</p>
      </div>
      <div class="forum-img">
        <img v-if="post.image" :src="post.image" alt="Post Image" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "PostPage",
  props: {
    post: {
      type: Object,
      required: true,
    },
  },
};
</script>

<style scoped>
.post {
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 10px;
  margin-bottom: 20px;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-info img {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-right: 10px;
}

.user-info span {
  font-weight: bold;
}
.forum-img img {
  height: 100px;
  width: auto;
  padding-right: 15px ;

}
.forum{
  display: flex;
  justify-content: space-between;
  width: 100%;
}
</style>
