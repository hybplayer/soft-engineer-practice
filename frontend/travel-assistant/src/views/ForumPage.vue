<template>
  <div class="forum-page">
    <div class="forum-header">
      <h2>论坛 | 分享 结伴 找攻略</h2>
      <el-button @click="showModal = true" type="primary">发新帖</el-button>
    </div>
    <NewPostModal v-if="showModal" @closeModal="showModal = false" @submitPost="addPost" />
    <div class="forum-posts">
      <PostPage v-for="post in posts" :key="post.id" :post="post" />
    </div>
  </div>
</template>

<script>
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
      showModal: false,
      posts: [
        {
          id: 1,
          avatar: require("../assets/A.jpg"),
          username: "User1",
          content: "这个城市真美好！这个城市真美好！",
          title: "香港之旅",
          image: require("../assets/hongkong.jpg")
        },
        {
          id: 2,
          avatar: require("../assets/B.jpg"),
          username: "User2",
          content: "今天的天气很好！",
          title: "澳门之旅",
          image: require("../assets/hongkong.jpg")
        }
      ]
    };
  },
  methods: {
    addPost(newPost) {
      // 为新帖子生成唯一的 ID
      const postId = this.posts.length + 1;
      // 将新帖子添加到顶部
      this.posts.unshift({
        id: postId,
        avatar: require("../assets/A.jpg"),
        username: "旅行者",//
        content: newPost.content,//显示新帖子评论区
        title: newPost.title,//显示新帖子标题
        image: newPost.image
      });
      // 关闭发帖模态框
      this.showModal = false;
    }
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

.new-post-modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #fff;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.forum-posts .post {
  display: flex;
  flex-direction: column;
  align-items: flex-start;

  margin-bottom: 30px;
  /* 设置评论间距 */
}

.user-info {
  display: flex;
  align-items: center;
}

.user-info span {
  font-weight: bold;
  margin-right: 50px;
  /* 设置用户昵称和评分之间的间隔 */


}

.user-info img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 10px;

}

.user-info span {
  font-weight: bold;


}

.post {
  border-bottom: 1px dashed #ccc;
  /* 添加虚线隔开 */
  padding-bottom: 20px;
  /* 设置评论之间的间距 */
  margin-right: 25%
}

.post-content {
  width: 100%;
  display: flex;
  justify-content: space-between;
}

.comment-image {
  width: 200px;
  /* 设置评论图片宽度 */
  height: auto;
  /* 让高度自适应 */
  margin-top: 10px;
  /* 设置评论图片与评论内容之间的间距 */
}
</style>
