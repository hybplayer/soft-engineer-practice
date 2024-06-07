<template>
  <div class="new-post-modal">
    <div class="modal-content">
      <h3>发新帖</h3>
      <input v-model="title" placeholder="请输入标题" class="title-input" />
      <textarea v-model="content" placeholder="请输入内容" class="content-textarea"></textarea>

      <div class="image-upload">
        <input type="file" @change="handleImageUpload" multiple />
        <div v-if="uploadedImages.length" class="uploaded-images">
          <div v-for="(image, index) in uploadedImages" :key="index" class="uploaded-image">
            <img :src="image.url" alt="Uploaded Image" />
            <button @click="removeImage(index)">删除</button>
          </div>
        </div>
      </div>

      <div class="button-group">
        <button @click="submitPost" class="btn-submit">提交</button>
        <button @click="closeModal" class="btn-cancel">取消</button>
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex';

export default {
  name: "NewPostModal",
  data() {
    return {
      title: "",
      content: "",
      uploadedImages: []
    };
  },
  computed: {
    ...mapState('visitor', ['auth'])
  },
  methods: {
    ...mapActions('visitor', ['addPost']),
    handleImageUpload(event) {
      const files = event.target.files;
      for (let i = 0; i < files.length; i++) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.uploadedImages.push({ file: files[i], url: e.target.result });
        };
        reader.readAsDataURL(files[i]);
      }
    },
    removeImage(index) {
      this.uploadedImages.splice(index, 1);
    },
    submitPost() {
      if (this.title && this.content) {
        const formData = new FormData();
        formData.append('title', this.title);
        formData.append('content', this.content);
        formData.append('username', this.auth.username); // 使用当前登录用户的用户名
        this.uploadedImages.forEach(image => {
          formData.append('images', image.file);
        });

        this.addPost(formData).then(response => {
          console.log('帖子上传成功:', response);
          this.$emit('submitPost', response.data);
          this.resetForm();
          this.$emit('closeModal');
        }).catch(error => {
          console.error('帖子上传失败:', error);
        });
      }
    },
    closeModal() {
      this.resetForm();
      this.$emit('closeModal');
    },
    resetForm() {
      this.title = "";
      this.content = "";
      this.uploadedImages = [];
    }
  }
};
</script>




<style scoped>
.new-post-modal {
  background-color: rgba(0, 0, 0, 0.5);
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 10px;
  width: 400px;
  max-width: 90%;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  align-items: stretch;
}

.title-input,
.content-textarea {
  width: 90%;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 16px;
}

.content-textarea {
  min-height: 100px;
  resize: vertical;
}

.image-upload {
  margin: 10px 0;
}

.uploaded-images {
  display: flex;
  flex-wrap: wrap;
  margin-top: 10px;
}

.uploaded-image {
  position: relative;
  margin: 5px;
}

.uploaded-image img {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.uploaded-image button {
  position: absolute;
  top: 5px;
  right: 5px;
  background-color: red;
  color: white;
  border: none;
  cursor: pointer;
}

.button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.btn-submit {
  background-color: #28a745;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  flex: 1;
  margin-right: 10px;
}

.btn-cancel {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  flex: 1;
  margin-left: 10px;
}
</style>
