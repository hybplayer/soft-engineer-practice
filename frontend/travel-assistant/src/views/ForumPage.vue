<template>
  <div class="forum-page">
    <div class="forum-header">
      <h2>论坛 | 分享 结伴 找攻略</h2>
      <el-button @click="showModal = true" type="primary">发新帖</el-button>
    </div>
    <NewPostModal v-if="showModal" @closeModal="handleCloseModal" @submitPost="handleSubmitPost" />
    <div class="forum-posts">
      <PostPage v-for="post in filteredPosts" :key="post.id" :post="post" />
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex';
import NewPostModal from "@/components/NewPostModal.vue";
import PostPage from "@/components/PostPage.vue";

export default {
  name: "ForumPage",
  components: {
    NewPostModal,
    PostPage
  },
  data() {
    return {
      showModal: false
    };
  },
  computed: {
    ...mapState('visitor', ['auth', 'posts']),
    filteredPosts() {
      const postId = this.$route.query.postId;
      if (postId) {
        return this.posts.filter(post => post.id.toString() === postId);
      }
      return this.posts;
    }
  },
  methods: {
    ...mapActions('visitor', ['loadPosts']),
    handleCloseModal() {
      this.showModal = false;
    },
    handleSubmitPost() {
      this.showModal = false;
      this.loadPosts();
    }
  },
  created() {
    this.loadPosts().then(() => {
      console.log('Posts loaded:', this.posts);
    })
  }
};
</script>

<style scoped>
.forum-page {
  padding: 20px;
  background-image: url("../assets/B.jpg");
}

.forum-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.forum-posts .post {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-bottom: 30px;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-info span {
  font-weight: bold;
  margin-right: 50px;
}

.user-info img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 10px;
}

.post {
  border-bottom: 1px dashed #ccc;
  padding-bottom: 20px;
  margin-right: 25%;
}

.post-content {
  width: 100%;
  display: flex;
  justify-content: space-between;
}

.comment-image {
  width: 200px;
  height: auto;
  margin-top: 10px;
}
</style>
