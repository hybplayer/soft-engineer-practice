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
    }
  },
  data() {
    return {
      newComment: "",
      defaultAvatar: require("../assets/user-default.png") // 默认头像的路径
    };
  },
  computed: {
    ...mapState('visitor', {
      currentUser: state => state.auth // 映射整个 auth 对象作为 currentUser
    }),
    ...mapGetters('visitor', ['getComments']),
    comments() {
      return this.getComments(this.postId).map(comment => {
        console.log(comment.avatar);
        const avatarUrl = comment.avatar ? comment.avatar : this.defaultAvatar;
        return {
          ...comment,
          avatar: avatarUrl
        };
      });
    }
  },
  methods: {
    ...mapActions('visitor', ['loadComments', 'addComment', 'fetchUserProfile']),
    async submitComment() {
      if (!this.currentUser) {
        console.error("No current user data available.");
        return;  // 如果没有 currentUser，直接返回避免执行后续代码
      }
      // const avatarUrl = this.currentUser.avatar || this.defaultAvatar;
      const userProfile = await this.fetchUserProfile(this.currentUser.username);
      let avatarUrl = "";
      if (userProfile && userProfile.avatar) {
        console.log("userProfile.avatar: ", userProfile.avatar);
        avatarUrl = `http://eth.jht213.com:20334/api/users/getAvatar/${userProfile.avatar.url}`;
      } else {
        avatarUrl = this.defaultAvatar;
      }
      // const avatarUrl = this.currentUser.avatar ? `http://localhost:20334/api/users/getAvatar/${userProfile.avatar.url}` : this.defaultAvatar;
      console.log("avatarUrl: ", avatarUrl);
      const newComment = {
        postId: this.postId,
        avatar: avatarUrl,
        username: this.currentUser.username,
        content: this.newComment.trim()
      };
      await this.addComment(newComment).catch(error => {
        console.error("Failed to add comment:", error);
      });
      this.newComment = "";
      this.loadComments(this.postId); // 重新加载评论
    },
    viewProfile(username) {
      this.$router.push({ name: 'UserProfile', params: { username } });
    }
  },
  created() {
    this.loadComments(this.postId); // 在组件创建时加载评论
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
