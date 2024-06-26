<template>
  <div class="post">
    <div class="user-info">
      <img :src="userAvatarUrl" alt="User Avatar" />
      <span @click="viewProfile(post.username)" class="username-link">{{ post.username }}</span>
    </div>

    <div class="post-content">
      <div class="post-details">
        <h3>{{ post.title }}</h3>
        <p>{{ post.content }}</p>
      </div>
      <div class="post-images">
        <img v-for="(image, index) in post.images" :key="index" :src="`data:image/jpeg;base64,${image.imageUrl}`"
             alt="Post Image" class="post-image" />
      </div>
    </div>

    <CommentSection :postId="post.id" :currentUser="auth" />
  </div>
</template>

<script>
import CommentSection from "@/components/CommentSection.vue";
import defaultAvatar from "../assets/user-default.png"; // 请确保有一个默认头像
import { mapActions, mapState } from 'vuex';

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
  data() {
    return {
      userAvatarUrl: defaultAvatar
    }
  },
  async created() {
    if (this.post.username) {
      this.userAvatarUrl = await this.getUserAvatar(this.post.username);
    }
  },
  watch: {
    post: {
      immediate: true,
      handler(newPost) {
        if (newPost.username) {
          this.updateUserAvatar(newPost.username);
        }
      }
    }
  },
  methods: {
    ...mapActions('visitor', ['fetchUserProfile']),
    viewProfile(username) {
      this.$router.push({ name: 'UserProfile', params: { username } });
    },
    async getUserAvatar(username) {
      const userProfile = await this.fetchUserProfile(username);
      if (userProfile && userProfile.avatar) {
        console.log("userProfile.avatar: ", userProfile.avatar);
        return `http://localhost:20334/api/users/getAvatar/${userProfile.avatar.url}`;
      } else {
        return defaultAvatar;
      }
    },
    async updateUserAvatar(username) {
      this.userAvatarUrl = await this.getUserAvatar(username);
    }
  },
  computed: {
    ...mapState('visitor', ['auth']),
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

.post-content {
  display: flex;
  flex-direction: column;
  margin-bottom: 15px;
}

.post-details {
  flex: 1;
}

.post-details h3 {
  margin-bottom: 10px;
  font-size: 1.5rem;
}

.post-details p {
  margin-bottom: 0;
}

.post-images {
  display: flex;
  flex-wrap: wrap;
  margin-top: 10px;
}

.post-image {
  width: calc(33.33% - 10px);
  margin-right: 10px;
  margin-bottom: 10px;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>
