<template>
  <div class="route-planning">
    <h2>路线规划</h2>
    <form @submit.prevent="searchRoute">
      <label>
        起点：
        <input v-model="startLocation" placeholder="输入起点地名" />
      </label>
      <label>
        终点：
        <input v-model="endLocation" placeholder="输入终点地名" />
      </label>
      <button type="submit">搜索路线</button>
    </form>
    <div id="map"></div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      startLocation: '天安门', // 默认起点地名
      endLocation: '北京站',   // 默认终点地名
      map: null,
      geocoder: null
    };
  },
  mounted() {
    this.loadMapScript();
  },
  methods: {
    loadMapScript() {
      // 检查百度地图脚本是否已经加载
      if (typeof window.BMap === 'undefined') {
        const script = document.createElement('script');
        script.src = `https://api.map.baidu.com/api?v=3.0&ak=你的百度地图API密钥`;
        script.onload = this.initMap;
        script.onerror = () => {
          console.error('加载百度地图脚本失败');
        };
        document.head.appendChild(script);
      } else {
        this.initMap();
      }
    },
    initMap() {
      try {
        const BMap = window.BMap;
        this.map = new BMap.Map("map");
        this.map.centerAndZoom(new BMap.Point(116.404, 39.915), 15);
        this.geocoder = new BMap.Geocoder();

        // 防止跳转到百度地图的网页
        this.map.addEventListener('click', (e) => {
          e.domEvent.preventDefault();
        });

        // 添加缩放监听事件，捕捉并处理缩放过程中可能出现的错误
        this.map.addEventListener('zoomend', () => {
          console.log('地图缩放完成');
        });
      } catch (error) {
        console.error('初始化地图时发生错误:', error);
      }
    },
    geocodeAddress(address, city, callback) {
      this.geocoder.getPoint(address, (point) => {
        if (point) {
          console.log(`地址: ${address} 转换为坐标: ${point.lng}, ${point.lat}`);
          callback(point);
        } else {
          alert(`无法找到地址: ${address}`);
          console.error(`无法找到地址: ${address}`);
        }
      }, city);
    },
    searchRoute() {
      try {
        const BMap = window.BMap;
        this.map.clearOverlays(); // 清除之前的标注和路线
        this.geocodeAddress(this.startLocation, '北京', (startPoint) => {
          if (!startPoint) {
            console.error('起点地址无效');
            return;
          }
          this.geocodeAddress(this.endLocation, '北京', (endPoint) => {
            if (!endPoint) {
              console.error('终点地址无效');
              return;
            }
            this.map.centerAndZoom(startPoint, 15);
            const startMarker = new BMap.Marker(startPoint);
            const endMarker = new BMap.Marker(endPoint);
            this.map.addOverlay(startMarker);
            this.map.addOverlay(endMarker);

            const driving = new BMap.DrivingRoute(this.map, {
              renderOptions: { map: this.map, autoViewport: true }
            });
            driving.search(startPoint, endPoint);
          });
        });
      } catch (error) {
        console.error('搜索路线时发生错误:', error);
      }
    }
  }
};
</script>

<style scoped>
#map {
  width: 100%;
  height: 500px;
}

.route-planning form {
  margin-bottom: 10px;
}

.route-planning label {
  margin-right: 10px;
}
</style>
