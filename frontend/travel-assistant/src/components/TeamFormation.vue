<template>
  <div class="team-formation">
    <h2>自动组建团队</h2>
    <el-table :data="userList" style="width: 100%">
      <el-table-column prop="username" label="用户名">
        <template #default="{ row }">
          <router-link :to="'/user/' + row.username">{{ row.username }}</router-link>
        </template>
      </el-table-column>
      <el-table-column prop="hobby" label="旅游爱好"></el-table-column>
      <el-table-column prop="destination" label="目的地"></el-table-column>
    </el-table>
  </div>
</template>

<script>
import { mapGetters } from 'vuex';

export default {
  computed: {
    ...mapGetters('visitor', ['getUsers']),
    userList() {
      return this.getUsers.map(user => ({
        username: user.username,
        hobby: user.hobby,
        destination: user.destination
      }));
    }
  }
};
</script>

<style scoped>
.team-formation {
  text-align: center;
  padding: 20px;
}

.el-table {
  margin-top: 20px;
}
</style>
