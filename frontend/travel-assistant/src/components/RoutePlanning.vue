<template>
  <div class="route-planning" @wheel.prevent>
    <div class="sidebar">
      <h2>路线规划</h2>
      <form @submit.prevent="searchRoute">
        <div class="form-group" v-for="(location, index) in locations" :key="index">
          <label :for="'location' + index">途经点{{ index + 1 }}：</label>
          <div class="input-group mb-2">
            <input v-model="locations[index]" :id="'location' + index" class="form-control" placeholder="输入地名" />
            <div class="input-group-append">
              <button @click="removeLocation(index)" type="button" class="btn btn-danger">移除</button>
            </div>
          </div>
        </div>
        <button type="button" @click="addLocation" class="btn btn-primary mb-2">添加途经点</button>
        <button type="submit" class="btn btn-success">搜索路线</button>
      </form>
      <div class="route-info">
        <h3>路线顺序:</h3>
        <ol class="list-group">
          <li v-for="(location, index) in routeOrder" :key="index" class="list-group-item">
            {{ index + 1 }}. {{ location }}
          </li>
        </ol>
      </div>
    </div>
    <div id="map" class="map-container"></div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      locations: ['哈尔滨市', '北京市', '乌鲁木齐市', '东莞市', '南京市', '杭州市'], // 默认途经点
      map: null,
      geocoder: null,
      driving: null,
      routeOrder: []
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
        this.map.centerAndZoom(new BMap.Point(116.404, 39.915), 11);
        this.map.enableScrollWheelZoom(true); // 启用滚轮缩放
        this.geocoder = new BMap.Geocoder();
        this.driving = new BMap.DrivingRoute(this.map, {
          renderOptions: { map: this.map, autoViewport: true }
        });
        // 禁用页面滚动
        document.querySelector('.route-planning').addEventListener('wheel', (e) => {
          e.preventDefault();
        });
      } catch (error) {
        console.error('初始化百度地图失败', error);
      }
    },
    addLocation() {
      this.locations.push('');
    },
    removeLocation(index) {
      this.locations.splice(index, 1);
    },
    geocodeAddress(address, callback) {
      this.geocoder.getPoint(address, (point) => {
        if (point) {
          console.log(`地址: ${address} 转换为坐标: ${point.lng}, ${point.lat}`);
          callback(point);
        } else {
          alert(`无法找到地址: ${address}`);
          console.error(`无法找到地址: ${address}`);
        }
      });
    },
    async searchRoute() {
      try {
        // const BMap = window.BMap;
        this.map.clearOverlays(); // 清除之前的标注和路线

        const points = [];
        for (let location of this.locations) {
          points.push(await this.geocodeLocation(location));
        }

        if (points.length > 1) {
          const bestPath = this.calculateBestPath(points);
          this.plotRoute(bestPath);
          this.updateRouteOrder(bestPath);
        }
      } catch (error) {
        console.error('搜索路线时发生错误:', error);
      }
    },
    async geocodeLocation(location) {
      return new Promise((resolve, reject) => {
        this.geocoder.getPoint(location, (point) => {
          if (point) {
            resolve({ point, location });
          } else {
            reject(`无法找到地址: ${location}`);
          }
        });
      });
    },
    calculateBestPath(points) {
      // 使用简单的贪心算法计算最佳路径
      const start = points[0];
      const remaining = points.slice(1);
      const path = [start];
      let current = start;

      while (remaining.length > 0) {
        let nearestIndex = 0;
        let nearestDistance = this.getDistance(current.point, remaining[0].point);

        for (let i = 1; i < remaining.length; i++) {
          const distance = this.getDistance(current.point, remaining[i].point);
          if (distance < nearestDistance) {
            nearestDistance = distance;
            nearestIndex = i;
          }
        }

        current = remaining.splice(nearestIndex, 1)[0];
        path.push(current);
      }

      path.push(start); // 回到起点
      return path;
    },
    getDistance(point1, point2) {
      const dx = point1.lng - point2.lng;
      const dy = point1.lat - point2.lat;
      return Math.sqrt(dx * dx + dy * dy);
    },
    plotRoute(points) {
      const BMap = window.BMap;

      for (let i = 0; i < points.length - 1; i++) {
        const driving = new BMap.DrivingRoute(this.map, {
          renderOptions: {map: this.map, autoViewport: true}
        });
        driving.search(points[i].point, points[i + 1].point);

        const marker = new BMap.Marker(points[i].point);
        const label = new BMap.Label(i + 1, {position: points[i].point});
        marker.setLabel(label);
        this.map.addOverlay(marker);
      }

      // 添加最后一个点的标记
      const lastPoint = points[points.length - 1].point;
      const lastMarker = new BMap.Marker(lastPoint);
      const lastLabel = new BMap.Label(points.length, {position: lastPoint});
      lastMarker.setLabel(lastLabel);
      this.map.addOverlay(lastMarker);
    },
    updateRouteOrder(path) {
      this.routeOrder = path.map((item, index) => `${index + 1}. ${item.location}`);
    }
  }
};
</script>

<style scoped>
.route-planning {
  display: flex;
  height: 100vh;
}

.sidebar {
  width: 300px;
  height: 95%;
  background-color: #f8f9fa;
  padding: 20px;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  z-index: 10;
  overflow-y: auto;
}

.map-container {
  flex: 1;
  height: 100%;
}

.form-group {
  margin-bottom: 1rem;
}

.input-group {
  display: flex;
}

.input-group-append {
  margin-left: 0.5rem;
}

.btn {
  width: 100%;
  margin-bottom: 0.5rem;
}

.route-info {
  margin-top: 20px;
}

.route-info h3 {
  margin-bottom: 10px;
}

.route-info ol {
  padding-left: 20px;
}
</style>
