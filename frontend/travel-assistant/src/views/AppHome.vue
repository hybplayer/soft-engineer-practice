<template>
  <div>
    <!-- 背景图片轮播图 -->
    <div class="wrapper">
      <el-carousel :interval="3000" type="fade" height="500px">
        <el-carousel-item v-for="(image, index) in images" :key="index">
          <img :src="image" width="100%" height="500px" alt="" />
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 今日推荐 -->
    <div class="section">
      <div class="title">今日推荐</div>
      <ul class="content">
        <li v-for="(item, index) in arr" :key="index">
          <div class="img">
            <a href="##">
              <img :class="{ animate: item.type !== 'video' }" :src="item.data.pic" referrerPolicy="no-referrer" alt="" />
            </a>
          </div>
          <div class="info">
            <a href="##" class="link">
              <div class="desc">{{ item.data.subject }}</div>
              <div class="bottom">
                <strong>{{ item.data.username }}</strong>
                <span v-if="item.data.views">{{ item.data.views }}预览过</span>
                <span v-else-if="item.data.tag">{{ item.data.tag }}</span>
                <span v-else v-html="item.data.price"></span>
              </div>
            </a>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      arr: [], // 推荐数据容器
      images: [
        require('../assets/banner-1.png'),
        require('../assets/banner-2.png'),
        require('../assets/banner-3.png'),
        require('../assets/banner-4.png')
      ]
    };
  },
  created() {
    // 获取今日推荐
    this.$api.getTuijian().then((res) => {
      console.log('今日推荐----', res.data);
      this.arr = res.data.data;
    });
  }
};
</script>

<style scoped lang="less">
.wrapper {
  width: 100%;
  height: 500px;
  position: relative;
}

.container {
  width: 1200px;
  margin: 0 auto;
}

.section {
  width: 1160px;
  margin: 20px auto;
  .title {
    position: relative;
    height: 74px;
    color: #636363;
    text-align: center;
    font: 36px/74px Hiragino Sans GB, Microsoft YaHei, SimHei, SimSun, sans-serif;
  }
}

.content {
  overflow: hidden;
  margin-right: -20px;
  li {
    list-style-type: none; /* 消除小黑点 */
    width: calc(25% - 20px); /* 让每行容纳四个对象 */
    height: 300px;
    float: left;
    box-sizing: border-box;
    border: 1px solid #eee;
    margin-bottom: 20px;
    margin-right: 20px;
  }
  li:hover {
    box-shadow: 0 0 5px rgb(0 0 0 / 20%);
  }
  .img {
    width: 100%;
    height: 185px;
    overflow: hidden;
    img {
      width: 100%;
      height: 185px;
    }
    .animate {
      transition: all 1.5s;
    }
    .animate:hover {
      transform: scale(1.2);
    }
  }
  .info {
    padding-left: 10px;
    padding-right: 10px;
    .desc {
      margin-top: 14px;
      height: 44px;
      font-size: 16px;
      overflow: hidden;
      color: #333;
    }
    .bottom {
      font-size: 14px;
      margin-top: 16px;
      height: 24px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      color: #636363;
      span {
        float: right;
        /deep/ em {
          font-style: normal;
          color: red;
        }
      }
    }
  }
  .link {
    text-decoration: none; /* 移除下划线 */
    color: inherit;
  }
}
</style>
