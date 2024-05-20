<template>
  <div class="new-post-modal">
    <div class="main-post">
    <el-input v-model="input" style="width: 240px" placeholder="请输入标题" />
    <p></p>
    <el-input
      v-model="newPost.content"
      placeholder="请发表您的评论"
      class="textarea-with-button"
      maxlength="100"
      rows="4"
      show-word-limit
    ></el-input>
    <el-button icon="PictureFilled" @click="selectImage" type="primary">上传图片</el-button>
    <input type="file" ref="fileInput" style="display: none" @change="handleImageUpload">
    <el-button icon="Check" @click="saveAndSubmit" type="success">保存并提交</el-button>
  </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      input: "", // 标题输入框的内容
      newPost: {
        username: "", // 用户名
        image: "", // 图片
        content: "" // 评论内容
      }
    };
  },
  methods: {
    selectImage() {
      // 触发文件选择对话框
      this.$refs.fileInput.click();
    },
    handleImageUpload(event) {
  // 处理图片上传
  const file = event.target.files[0];
  // 在这里你可以对图片进行处理，比如显示预览图等
  console.log("选择的图片:", file);

  // 使用 URL.createObjectURL 创建临时 URL
  this.newPost.image = URL.createObjectURL(file);
},
    saveAndSubmit() {
      // 触发 submitPost 事件，并将用户输入的内容传递给父组件
      this.$emit("submitPost", {
        title: this.input, // 标题输入框的内容作为帖子标题
        content: this.newPost.content, // 文本域的内容作为帖子内容
        image: this.newPost.image // 上传的图片
      });
      // 清空输入
      this.input = "";
      this.newPost.content = "";
      this.newPost.image = null;
    }
  }
};
</script>

<style scoped>
.new-post-modal{
  height:70% ;

}

.main-post{
  padding-top: 50px;

}

.textarea-with-button {
  width: 100%;
  height: 100px;
  margin-bottom: 10px;

  border-radius: 5px;
  box-sizing: border-box;
}

.textarea-with-button:focus {
  outline: none;
}

.el-button {
  margin-top: 10px;
}


</style>
