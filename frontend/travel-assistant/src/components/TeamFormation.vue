<template>
  <div class="team-formation">
    <h2 class="section-title">自动组建团队</h2>

    <!-- 添加选择框 -->
    <div class="user-count-selector">
      <label for="user-count">显示用户数量：</label>
      <el-select v-model="userCount" placeholder="请选择" @change="updateUserList">
        <el-option v-for="count in userCountOptions" :key="count" :label="count" :value="count"></el-option>
      </el-select>
    </div>

    <el-table :key="userCount" :data="userListToShow" class="user-table">
      <el-table-column prop="username" label="用户名">
        <template #default="{ row }">
          <router-link :to="'/user/' + row.username" class="username-link">{{ row.username }}</router-link>
        </template>
      </el-table-column>
      <el-table-column prop="hobby" label="旅游爱好"></el-table-column>
      <el-table-column prop="destinations" label="目的地">
        <template #default="{ row }">
          <span v-if="row.destinations.length">
            <span v-for="(destination, index) in row.destinations" :key="index">{{ destination }}<span
                v-if="index < row.destinations.length - 1">, </span></span>
          </span>
          <span v-else>未指定</span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button @click="inviteUser(row.username)" type="primary">邀请</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { mapGetters, mapState, mapActions } from 'vuex';
import { ElMessage } from 'element-plus';

export default {
  data() {
    return {
      userCount: 5,
      userCountOptions: [5, 10, 15, 20],
    };
  },
  computed: {
    ...mapState('visitor', ['auth']),
    ...mapGetters('visitor', ['getUsers', 'getUserDestinations']),
    userList() {
      return this.getUsers
        .filter(user => user.username !== this.auth.username)
        .map(user => ({
          username: user.username,
          hobby: user.hobby,
          destinations: this.getUserDestinations(user.username)
        }));
    },
    sortedUserList() {
      // 创建 userList 的深拷贝
      const userListCopy = JSON.parse(JSON.stringify(this.userList));
      const authHobby = this.auth.hobby.toLowerCase();
      const authDestinations = this.getUserDestinations(this.auth.username).length ? this.getUserDestinations(this.auth.username).sort().join(',').toLowerCase() : '';
      return userListCopy.sort((a, b) => {
        const aHobby = a.hobby.toLowerCase();
        const bHobby = b.hobby.toLowerCase();
        const aHobbyDistance = this.levenshteinDistance(aHobby, authHobby);
        const bHobbyDistance = this.levenshteinDistance(bHobby, authHobby);
        
        const aDestinations = a.destinations.length ? a.destinations.sort().join(',').toLowerCase() : '';
        const bDestinations = b.destinations.length ? b.destinations.sort().join(',').toLowerCase() : '';
        const aDestinationsDistance = this.levenshteinDistance(aDestinations, authDestinations);
        const bDestinationsDistance = this.levenshteinDistance(bDestinations, authDestinations);

        return 0.05 * aHobbyDistance + 0.95 * aDestinationsDistance - (0.05 * bHobbyDistance + 0.95 * bDestinationsDistance);
      });
    },
    userListToShow() {
      return this.sortedUserList
      .slice(0, this.userCount);
    }
  },
  methods: {
    ...mapActions('visitor', ['fetchUsers', 'fetchAllUserDestinations', 'sendInvite']),
    async loadUserData() {
      await this.fetchUsers();
      await this.fetchAllUserDestinations();
    },
    async inviteUser(username) {
      try {
        const invite = { from: this.auth.username, to: username };
        await this.$store.dispatch('visitor/sendInvite', invite);
        ElMessage.success('邀请发送成功');
      } catch (error) {
        ElMessage.error('邀请发送失败');
      }
    },
    updateUserList() {
      this.$nextTick(() => {
        console.log("用户数量更新为:", this.userCount);
      });
    },
    levenshteinDistance(s, t) {
      if (!s.length) return t.length;
      if (!t.length) return s.length;
      const arr = [];
      for (let i = 0; i <= t.length; i++) {
        arr[i] = [i];
      }
      for (let j = 0; j <= s.length; j++) {
        arr[0][j] = j;
      }
      for (let i = 1; i <= t.length; i++) {
        for (let j = 1; j <= s.length; j++) {
          arr[i][j] = Math.min(
            arr[i - 1][j] + 1,
            arr[i][j - 1] + 1,
            arr[i - 1][j - 1] + (s[j - 1] === t[i - 1] ? 0 : 1)
          );
        }
      }
      return arr[t.length][s.length];
    },
  },
  async created() {
    await this.loadUserData();
  }
};
</script>

<style scoped>
.team-formation {
  text-align: center;
  padding: 20px;
  background: linear-gradient(to right, #f0f0f0, #ffffff);
  /* 渐变色背景 */
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  /* 阴影效果 */
}

.section-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.user-count-selector {
  margin-bottom: 20px;
  text-align: left;
}

.user-table {
  margin-top: 20px;
}

.username-link {
  color: #2c3e50;
  text-decoration: none;
}

.el-button {
  border-radius: 20px;
  /* 圆角按钮 */
  font-weight: bold;
}

.el-table-column {
  color: #666;
}

.el-table__body-wrapper {
  overflow-x: auto;
  /* 如果表格数据较多，允许水平滚动 */
}

@media screen and (max-width: 768px) {
  .team-formation {
    padding: 10px;
  }
}
</style>
