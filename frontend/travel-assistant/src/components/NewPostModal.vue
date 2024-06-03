<template>
  <div class="new-post-modal">
    <div class="modal-content">
      <h3>发新帖</h3>
      <input v-model="title" placeholder="请输入标题" />
      <textarea v-model="content" placeholder="请输入内容"></textarea>

      <div class="image-upload">
        <input type="file" @change="handleImageUpload" multiple />
        <div v-if="uploadedImages.length" class="uploaded-images">
          <div v-for="(image, index) in uploadedImages" :key="index" class="uploaded-image">
            <img :src="image.url" alt="Uploaded Image" />
            <button @click="removeImage(index)">删除</button>
          </div>
        </div>
      </div>

      <button @click="submitPost">提交</button>
      <button @click="closeModal">取消</button>
    </div>
  </div>
</template>

<script>
export default {
  name: "NewPostModal",
  data() {
    return {
      title: "",
      content: "",
      uploadedImages: []
    };
  },
  methods: {
    handleImageUpload(event) {
      const files = event.target.files;
      for (let i = 0; i < files.length; i++) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.uploadedImages.push({ url: e.target.result });
        };
        reader.readAsDataURL(files[i]);
      }
    },
    removeImage(index) {
      this.uploadedImages.splice(index, 1);
    },
    submitPost() {
      if (this.title && this.content) {
        const newPost = {
          id: Date.now(), // 使用时间戳作为唯一 ID
          title: this.title,
          content: this.content,
          image: this.uploadedImages.length > 0 ? this.uploadedImages[0].url : null
        };
        this.$emit('submitPost', newPost);
        this.resetForm();
        this.$emit('closeModal');
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
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 5px;
  width: 400px;
  max-width: 90%;
}

.image-upload {
  margin-top: 10px;
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

button {
  margin-top: 10px;
}
</style>
