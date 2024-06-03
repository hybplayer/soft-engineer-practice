<template>
  <div class="comment-section">
    <h3>评论</h3>
    <div v-for="comment in comments" :key="comment.id" class="comment">
      <div class="comment-header">
        <img :src="comment.avatar" alt="avatar" class="comment-avatar" />
        <span @click="viewProfile(comment.username)" class="comment-username">{{ comment.username }}</span>
      </div>
      <p class="comment-content">{{ comment.content }}</p>
    </div>
    <div class="add-comment">
      <input v-model="newComment" placeholder="添加评论..." />
      <button @click="submitComment">提交</button>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions, mapGetters } from 'vuex';

export default {
  name: "CommentSection",
  props: {
    postId: {
      type: Number,
      required: true
    },
    currentUser: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      newComment: ""
    };
  },
  computed: {
    ...mapState('visitor', ['auth']),
    ...mapGetters('visitor', ['getComments']),
    comments() {
      return this.getComments(this.postId);
    }
  },
  methods: {
    ...mapActions('visitor', ['addComment']),
    submitComment() {
      if (this.newComment.trim()) {
        const newComment = {
          id: Date.now(), // 使用时间戳作为唯一 ID
          postId: this.postId,
          avatar: this.currentUser.avatar || require("../assets/user-default.jpg"),
          username: this.currentUser.username, // 使用当前登录用户的用户名
          content: this.newComment.trim()
        };
        this.addComment(newComment);
        this.newComment = "";
      }
    },
    viewProfile(username) {
      this.$router.push({ name: 'UserProfile', params: { username } });
    }
  },
  created() {
    this.$store.dispatch('visitor/loadComments'); // 在组件创建时加载评论
  }
};
</script>

<style scoped>
.comment-section {
  margin-top: 20px;
}

.comment {
  margin-bottom: 10px;
  padding: 10px;
  border-bottom: 1px solid #ddd;
}

.comment-header {
  display: flex;
  align-items: center;
}

.comment-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  margin-right: 10px;
}

.comment-username {
  font-weight: bold;
  cursor: pointer;
  /* 增加指针样式以表明可点击 */
}

.add-comment {
  display: flex;
  align-items: center;
}

.add-comment input {
  flex: 1;
  padding: 5px;
  margin-right: 10px;
}

.add-comment button {
  padding: 5px 10px;
}
</style>
