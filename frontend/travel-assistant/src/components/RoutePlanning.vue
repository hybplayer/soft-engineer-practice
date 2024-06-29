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
    </div>
    <!-- 弹出窗口部分 -->
    <div v-if="showRouteOrder" class="sidebar2">
      <h2>路线顺序</h2>
      <ol class="list-group">
        <li v-for="(location, index) in routeOrder" :key="index" class="list-group-item">
          {{ location }}
        </li>
      </ol>
      <button @click="closeRouteOrderPopup" class="btn btn-secondary mt-3">关闭</button>
    </div>
    <div id="map" class="map-container"></div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      locations: ['天安门广场', '清华大学', '国家体育场', '北京天文馆'], // 默认途经点
      map: null,
      geocoder: null,
      driving: null,
      routeOrder: [],
      showRouteOrder: false
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
          this.showRouteOrder = true;
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
      this.routeOrder = path.map((item) => `${item.location}`);
    },
    closeRouteOrderPopup() {
      this.showRouteOrder = false;  // 关闭路线顺序弹出窗口
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
  width: 250px;
  height: 95%;
  background-color: #f8f9fa;
  z-index: 10;
  overflow-y: auto;
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.sidebar2 {
  width: 250px;
  height: 95%;
  left: 300px;
  background-color: #f8f9fa;
  z-index: 10;
  overflow-y: auto;
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.form-control {
  border: 2px solid #3498db; /* 设置输入框边框样式 */
  border-radius: 4px; /* 圆角 */
  padding: 8px;
}

.form-control:focus {
  outline: none;
  border-color: #2ecc71; /* 输入框获得焦点时的边框颜色 */
}

.btn-primary, .btn-danger, .btn-success, .btn-secondary {
  border-radius: 4px;
  padding: 5px 10px;
  font-size: 16px;
  transition: background-color 0.3s ease-in-out, color 0.3s ease-in-out;
}

.btn-primary {
  background-color: #3498db;
  border-color: #3498db;
  color: #fff;
}

.btn-primary:hover {
  background-color: #2980b9;
  border-color: #2980b9;
}

.btn-danger {
  background-color: #e74c3c;
  border-color: #e74c3c;
  color: #fff;
}

.btn-danger:hover {
  background-color: #c0392b;
  border-color: #c0392b;
}

.btn-success {
  background-color: #2ecc71;
  border-color: #2ecc71;
  color: #fff;
}

.btn-success:hover {
  background-color: #27ae60;
  border-color: #27ae60;
}

.btn-secondary {
  background-color: #95a5a6;
  border-color: #95a5a6;
  color: #fff;
}

.btn-secondary:hover {
  background-color: #7f8c8d;
  border-color: #7f8c8d;
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
  align-items: center;
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

.list-group-item {
  padding: 8px;
}
</style>
