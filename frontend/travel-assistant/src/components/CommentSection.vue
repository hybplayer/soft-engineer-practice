<template>
  <div class="comment-section">
    <h3>评论</h3>
    <div v-for="comment in comments" :key="comment.id" class="comment">
      <div class="comment-header">
        <img :src="comment.avatar" alt="avatar" class="comment-avatar" />
        <span class="comment-username">{{ comment.username }}</span>
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
      comments: [],
      newComment: ""
    };
  },
  methods: {
    submitComment() {
      if (this.newComment.trim()) {
        const newComment = {
          id: this.comments.length + 1,
          avatar: require("../assets/user-default.jpg"),
          username: "新用户",
          content: this.newComment.trim()
        };
        this.comments.push(newComment);
        this.newComment = "";
      }
    }
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
