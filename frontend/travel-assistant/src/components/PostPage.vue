<template>
  <div class="post">
    <div class="user-info">
      <img :src="post.avatar || defaultAvatar" alt="User Avatar" />
      <span @click="viewProfile(post.username)" class="username-link">{{ post.username }}</span>
    </div>

    <div class="forum">
      <div>
        <h3>{{ post.title }}</h3>
        <p>{{ post.content }}</p>
      </div>
      <div class="forum-imgs">
        <img v-for="(image, index) in post.images" :key="index" :src="`data:image/jpeg;base64,${image.imageUrl}`"
          alt="Post Image" />
      </div>
    </div>

    <CommentSection :postId="post.id" :currentUser="auth" />
  </div>
</template>

<script>
import CommentSection from "@/components/CommentSection.vue";
import defaultAvatar from "../assets/B.jpg";
import { mapState } from 'vuex';

export default {
  name: "PostPage",
  props: {
    post: {
      type: Object,
      required: true,
    },
  },
  components: {
    CommentSection,
  },
  methods: {
    viewProfile(username) {
      this.$router.push({ name: 'UserProfile', params: { username } });
    }
  },
  data() {
    return {
      defaultAvatar
    }
  },
  computed: {
    ...mapState('visitor', ['auth'])
  }
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

.user-info .username-link {
  font-weight: bold;
  cursor: pointer;
}

.forum-imgs img {
  height: 100px;
  width: auto;
  padding-right: 15px;
  margin-bottom: 10px;
}

.forum {
  display: flex;
  justify-content: space-between;
  width: 100%;
}
</style>