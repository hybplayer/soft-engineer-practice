<template>
  <div class="team-formation">
    <h2>自动组建团队</h2>

    <!-- 添加选择框 -->
    <div class="user-count-selector">
      <label for="user-count">显示用户数量：</label>
      <el-select v-model="userCount" placeholder="请选择" @change="updateUserList">
        <el-option v-for="count in userCountOptions" :key="count" :label="count" :value="count"></el-option>
      </el-select>
    </div>

    <el-table :data="userListToShow" style="width: 100%">
      <el-table-column prop="username" label="用户名">
        <template #default="{ row }">
          <router-link :to="'/user/' + row.username">{{ row.username }}</router-link>
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
          <el-button @click="inviteUser(row.username)">邀请</el-button>
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
      userCount: 5, // 默认显示用户数量
      userCountOptions: [5, 10, 15, 20], // 可选的用户数量
    };
  },
  computed: {
    ...mapState('visitor', ['auth']),
    ...mapGetters('visitor', ['getUsers', 'getUserDestinations']),
    userList() {
      const users = this.getUsers
        .filter(user => user.username !== this.auth.username) // 排除当前用户
        .map(user => ({
          username: user.username,
          hobby: user.hobby,
          destinations: this.getUserDestinations(user.username) // 获取并返回目的地数组
        }));
      console.log("用户列表:", users); // 调试信息
      return users;
    },
    userListToShow() {
      return this.userList.slice(0, this.userCount); // 返回指定数量的用户
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
        console.log("Sending invite:", invite); // 打印邀请信息
        await this.$store.dispatch('visitor/sendInvite', invite);
        ElMessage.success('邀请发送成功');
      } catch (error) {
        ElMessage.error('邀请发送失败');
      }
    },
    updateUserList() {
      // 更新用户列表时触发
      console.log("用户数量更新为:", this.userCount); // 调试信息
    }
  },
  async created() {
    await this.loadUserData(); // 确保在created钩子中等待所有数据加载完成
  }
};
</script>

<style scoped>
.team-formation {
  text-align: center;
  padding: 20px;
}

.user-count-selector {
  margin-bottom: 20px;
  text-align: left;
}

.el-table {
  margin-top: 20px;
}
</style>
