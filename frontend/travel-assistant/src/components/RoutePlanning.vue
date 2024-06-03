<template>
  <div class="route-planning">
    <h2>路线规划</h2>
    <form @submit.prevent="searchRoute">
      <label v-for="(location, index) in locations" :key="index">
        途经点{{ index + 1 }}：
        <input v-model="locations[index]" placeholder="输入地名" />
        <button @click="removeLocation(index)" type="button">移除</button>
      </label>
      <button type="button" @click="addLocation">添加途经点</button>
      <button type="submit">搜索路线</button>
    </form>
    <div class="content">
      <div id="route-info">
        <h3>路线顺序:</h3>
        <ol>
          <li v-for="(location, index) in routeOrder" :key="index">
            {{ index + 1 }}. {{ location }}
          </li>
        </ol>
      </div>
      <div id="map"></div>
    </div>
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
        this.map.centerAndZoom(new BMap.Point(116.404, 39.915), 5);
        this.geocoder = new BMap.Geocoder();
        this.driving = new BMap.DrivingRoute(this.map, {
          renderOptions: { map: this.map, autoViewport: true }
        });

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
          renderOptions: { map: this.map, autoViewport: true }
        });
        driving.search(points[i].point, points[i + 1].point);

        const marker = new BMap.Marker(points[i].point);
        const label = new BMap.Label(i + 1, { position: points[i].point });
        marker.setLabel(label);
        this.map.addOverlay(marker);
      }

      // 添加最后一个点的标记
      const lastPoint = points[points.length - 1].point;
      const lastMarker = new BMap.Marker(lastPoint);
      const lastLabel = new BMap.Label(points.length, { position: lastPoint });
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
#map {
  width: 100%;
  height: 500px;
  float: left;
}

.route-planning form {
  margin-bottom: 10px;
}

.route-planning label {
  display: block;
  margin-bottom: 5px;
}

.route-planning button {
  margin-right: 10px;
  margin-top: 10px;
}

.content {
  display: flex;
  margin-top: 20px;
}

#route-info {
  width: 30%;
  margin-left: 20px;
}

#route-info ol {
  padding-left: 20px;
}
</style>
