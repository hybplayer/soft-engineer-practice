<template>
  <div class="public-page">
    <div class="content-container">
      <div class="carousel-container">
        <el-carousel :indicator-position="'outside'" height="400px">
          <el-carousel-item v-for="(image, index) in carouselImages" :key="index">
            <img :src="image" alt="轮播图片">
          </el-carousel-item>
        </el-carousel>
      </div>

      <el-card class="form-container">
        <el-form label-width="100px" class="form">
          <el-form-item label="出发地">
            <el-input v-model="departure" placeholder="请输入出发地"></el-input>
          </el-form-item>
          <el-form-item label="目的地">
            <el-input v-model="destination" placeholder="请输入目的地"></el-input>
          </el-form-item>
          <el-form-item label="出发日期">
            <el-date-picker v-model="departureDate" type="date" placeholder="选择出发日期"></el-date-picker>
          </el-form-item>
          <el-form-item label="选择项">
            <el-checkbox-group v-model="checkboxValues">
              <el-checkbox label="自驾游"></el-checkbox>
              <el-checkbox label="组团"></el-checkbox>
              <el-checkbox label="成人"></el-checkbox>
              <el-checkbox label="男生"></el-checkbox>
              <el-checkbox label="女生"></el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="备注">
            <el-input type="textarea" v-model="remark" placeholder="请输入备注信息"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSubmit">提交</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex';
import { ElMessage } from 'element-plus';

export default {
  data() {
    return {
      carouselImages: [
        require('@/assets/tokyo.jpg'),
        require('@/assets/meizhou.jpg'),
        require('@/assets/hongkong.jpg'),
        require('@/assets/paris.jpg')
      ],
      departure: '',
      destination: '',
      departureDate: '',
      checkboxValues: [],
      remark: ''
    };
  },
  methods: {
    ...mapActions('visitor', ['updateDestinationData']), // 引入更新目的地数据的动作
    handleSubmit() {
      if (!this.departure || !this.destination || !this.departureDate || this.checkboxValues.length === 0 || !this.remark) {
        ElMessage.error('提交失败，请填写完整表格');
        return;
      }

      this.updateDestinationData({ // 更新目的地数据
        departure: this.departure,
        destination: this.destination,
        departureDate: this.departureDate,
        checkboxValues: this.checkboxValues,
        remark: this.remark
      });

      ElMessage.success('提交成功');
    }
  }
};
</script>

<style scoped>
.public-page {
  width: calc(100vw - 200px);
  height: calc(100vh - 64px);
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url('@/assets/B.jpg');
  background-size: cover;
  background-position: center;
}

.content-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 900px;
}

.carousel-container {
  width: 500px;
}

.form-container {
  width: 400px;
}

.carousel-container img {
  width: 100%;
  height: 100%;
}
</style>
