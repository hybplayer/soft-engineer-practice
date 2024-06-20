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
          <el-form-item label="预算">
            <el-select v-model="priceRange" placeholder="请选择">
              <el-option label="1000-3000元" value="1"></el-option>
              <el-option label="3000-5000元" value="2"></el-option>
              <el-option label="5000-10000元" value="3"></el-option>
              <el-option label="10000元以上" value="4"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="对同伴的要求">
            <el-input type="textarea" v-model="companionRequirements" placeholder="请输入对同伴的要求"></el-input>
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
import { mapState, mapActions } from 'vuex';
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
      priceRange: '',
      companionRequirements: '',
      remark: ''
    };
  },
  computed: {
    ...mapState('visitor', ['auth']) // 确保 auth 被正确映射
  },
  methods: {
    ...mapActions('visitor', ['addDestinationData', 'refreshAuthProfile']),
    handleSubmit() {
      if (!this.departure || !this.destination || !this.departureDate || this.checkboxValues.length === 0 || !this.priceRange || !this.companionRequirements || !this.remark) {
        ElMessage.error('提交失败，请填写完整表格');
        return;
      }

      this.addDestinationData({
        departure: this.departure,
        destination: this.destination,
        departureDate: this.departureDate,
        checkboxValues: this.checkboxValues,
        priceRange: this.priceRange,
        companionRequirements: this.companionRequirements,
        remark: this.remark,
        username: this.auth.username // 确保包含用户名
      }).then(() => {
        ElMessage.success('提交成功');
        this.clearForm();
        this.refreshAuthProfile(); // 提交成功后刷新用户信息
      }).catch((error) => {
        ElMessage.error('提交失败');
        console.error(error);
      });
    },
    clearForm() {
      this.departure = '';
      this.destination = '';
      this.departureDate = '';
      this.checkboxValues = [];
      this.priceRange = '';
      this.companionRequirements = '';
      this.remark = '';
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
