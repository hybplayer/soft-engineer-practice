<template>
  <div class="forum-page">
    <div class="forum-header">
      <h2>论坛 | 分享 结伴 找攻略</h2>
      <el-button @click="showModal = true" type="primary">发新帖</el-button>
    </div>
    <NewPostModal v-if="showModal" @closeModal="handleCloseModal" @submitPost="addPost" />
    <div class="forum-posts">
      <PostPage v-for="post in posts" :key="post.id" :post="post" />
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
    ...mapState('visitor', ['auth', 'posts'])
  },
  methods: {
    ...mapActions('visitor', ['addPost', 'loadPosts']),
    handleCloseModal() {
      this.showModal = false;
    },
    addPost(newPost) {
      this.addPost(newPost).then(() => {
        this.showModal = false;
      }).catch(error => {
        console.error('添加帖子失败:', error);
      });
    }
  },
  created() {
    this.loadPosts();
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
