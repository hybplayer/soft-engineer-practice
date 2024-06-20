<template>
  <div class="user-page">
    <h2>个人中心</h2>

    <!-- 用户头像 -->
    <div class="avatar-section" v-if="isCurrentUser">
      <img :src="avatarUrl" alt="用户头像" class="avatar">
      <input type="file" @change="onAvatarChange">
      <button @click="updateAvatar">上传头像</button>
    </div>
    <div class="avatar-section" v-else>
      <img :src="currentProfile.avatar || defaultAvatarUrl" alt="用户头像" class="avatar">
    </div>

    <!-- 修改用户名 -->
    <form @submit.prevent="updateUsername" class="edit-form" v-if="isCurrentUser">
      <div class="edit">
        <label for="nickname">新昵称：</label>
        <input type="text" id="nickname" v-model="newNickname">
        <button type="submit">保存昵称</button>
      </div>
    </form>

    <!-- 修改密码 -->
    <form @submit.prevent="updatePassword" class="edit-form" v-if="isCurrentUser">
      <div class="edit">
        <label for="password">新密码：</label>
        <el-input :type="passwordFieldType" v-model="newPassword" class="password-input">
          <template #suffix>
            <el-icon :component="passwordFieldType === 'password' ? Eye : EyeOff" @click="togglePasswordVisibility" />
          </template>
        </el-input>

        <label for="confirmPassword">确认新密码：</label>
        <el-input :type="confirmPasswordFieldType" v-model="confirmPassword" class="password-input">
          <template #suffix>
            <el-icon :component="confirmPasswordFieldType === 'password' ? Eye : EyeOff"
              @click="toggleConfirmPasswordVisibility" />
          </template>
        </el-input>

        <button type="submit">保存密码</button>
      </div>
    </form>

    <!-- 修改旅游爱好 -->
    <form @submit.prevent="updateHobby" class="edit-form" v-if="isCurrentUser">
      <div class="edit">
        <label for="hobby">旅游爱好：</label>
        <textarea id="hobby" v-model="hobby" @input="updateHobbyLength"></textarea>
        <div>字数：{{ hobbyLength }}/100</div>
        <button type="submit">保存爱好</button>
      </div>
    </form>

    <!-- 显示用户信息 -->
    <div v-else>
      <p><strong>昵称:</strong> {{ currentProfile.username }}</p>
      <p><strong>旅游爱好:</strong> {{ currentProfile.hobby }}</p>
    </div>

    <!-- 新增数据存放区域 -->
    <div class="destination-data">
      <h3>目的地数据存放区</h3>
      <el-table :data="destinationData" class="destination-table">
        <el-table-column prop="departure" label="出发地"></el-table-column>
        <el-table-column prop="destination" label="目的地"></el-table-column>
        <el-table-column prop="departureDate" label="出发日期"></el-table-column>
        <el-table-column prop="checkboxValues" label="选择项">
          <template #default="{ row }">
            <span>{{ row.checkboxValues.join(', ') }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="priceRange" label="预算">
          <template #default="{ row }">
            <span>{{ priceRangeMap[row.priceRange] }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="companionRequirements" label="对同伴的要求"></el-table-column>
        <el-table-column prop="remark" label="备注"></el-table-column>
        <el-table-column label="操作" v-if="isCurrentUser">
          <template #default="{ row, $index }">
            <el-button type="text" size="small" @click="editDestination(row, $index)">编辑</el-button>
            <el-button type="text" size="small" @click="deleteDestination(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 编辑目的地表单 -->
    <el-dialog v-model="editDialogVisible" title="编辑目的地" width="600px" class="edit-dialog">
      <el-form :model="editForm" class="edit-form">
        <el-form-item label="出发地">
          <el-input v-model="editForm.departure"></el-input>
        </el-form-item>
        <el-form-item label="目的地">
          <el-input v-model="editForm.destination"></el-input>
        </el-form-item>
        <el-form-item label="出发日期">
          <el-date-picker v-model="editForm.departureDate" type="date"></el-date-picker>
        </el-form-item>
        <el-form-item label="选择项">
          <el-checkbox-group v-model="editForm.checkboxValues">
            <el-checkbox label="自驾游"></el-checkbox>
            <el-checkbox label="组团"></el-checkbox>
            <el-checkbox label="成人"></el-checkbox>
            <el-checkbox label="男生"></el-checkbox>
            <el-checkbox label="女生"></el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="可接受价格">
          <el-select v-model="editForm.priceRange" placeholder="请选择">
            <el-option label="1000-3000元" value="1"></el-option>
            <el-option label="3000-5000元" value="2"></el-option>
            <el-option label="5000-10000元" value="3"></el-option>
            <el-option label="10000元以上" value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="对同伴的要求">
          <el-input type="textarea" v-model="editForm.companionRequirements"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" v-model="editForm.remark"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveEdit">保存</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!-- 团队成员 -->
    <div class="team-members"
      v-if="currentProfile.team && currentProfile.team.members && currentProfile.team.members.length > 1">
      <h3>团队成员</h3>
      <ul>
        <li v-for="member in currentProfile.team.members" :key="member.id">
          <router-link :to="{ name: 'UserProfile', params: { username: member.username } }">{{ member.username
            }}</router-link>
        </li>
      </ul>
      <el-button class="leave-team-btn" v-if="isCurrentUser" type="danger" @click="leaveTeam">退出团队</el-button>
    </div>
    <div v-else>
      <h3>团队成员</h3>
      <p>暂无团队</p>
    </div>


    <!-- 显示和管理邀请 -->
    <div class="invitations" v-if="isCurrentUser && auth.invitations && auth.invitations.length">
      <h3>邀请</h3>
      <el-table :data="auth.invitations" class="invitation-table">
        <el-table-column label="来自">
          <template #default="{ row }">
            <router-link :to="`/user/${row.from.username}`" class="username-link">{{ row.from.username }}</router-link>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="{ row, $index }">
            <el-button type="success" @click="acceptInvite(row, $index)">接受</el-button>
            <el-button type="danger" @click="declineInvite(row, $index)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters, mapState } from 'vuex';
import { ElMessage } from 'element-plus';
import defaultAvatarUrl from '@/assets/default-avatar.png';  // 默认头像

export default {
  name: "UserPage",
  props: {
    username: {
      type: String,
      required: false
    }
  },
  data() {
    return {
      newNickname: '',
      newPassword: '',
      confirmPassword: '',
      hobby: '',
      hobbyLength: 0,
      passwordFieldType: 'password',
      confirmPasswordFieldType: 'password',
      editDialogVisible: false,
      editForm: {
        id: null,
        departure: '',
        destination: '',
        departureDate: '',
        checkboxValues: [],
        priceRange: '',
        companionRequirements: '',
        remark: ''
      },
      editIndex: -1,
      postId: 1, // 示例帖子ID
      invitations: [], // 初始化 invitations 数组
      defaultAvatarUrl // 默认头像URL
    };
  },

  computed: {
    ...mapState('visitor', ['auth', 'currentProfile', 'destinationData']),
    ...mapGetters('visitor', ['getUsers']),
    avatarUrl() {
      return this.currentProfile.avatar ? `${window.location.origin}${this.currentProfile.avatar}` : defaultAvatarUrl;
    },
    priceRangeMap() {
      return {
        1: '1000元以下',
        2: '1000-3000元',
        3: '3000-5000元',
        4: '5000元以上'
      };
    },
    isCurrentUser() {
      return this.username === this.auth.username;
    }
  },
  watch: {
    username: 'loadUserProfile' // 监听路由参数变化
  },
  methods: {
    ...mapActions('visitor', [
      'updateUserInfo',
      'editDestinationData',
      'deleteDestinationData',
      'fetchUserProfile',
      'loadUserDestinationData',
      'fetchInvitations',
      'respondInvite',
      'fetchTeamByUsername', // 确保映射 fetchTeamByUsername 方法
      'leaveTeam',
    ]),
    async loadUserProfile() {
      console.log("Loading user profile for:", this.username);
      await this.fetchUserProfile(this.username);
      await this.loadUserDestinationData(this.username);
      await this.fetchTeamByUsername(this.username);
    },
    onAvatarChange(event) {
      this.avatarFile = event.target.files[0];
    },
    updateAvatar() {
      if (this.avatarFile) {
        const formData = new FormData();
        formData.append('avatar', this.avatarFile);
        console.log("formData:", formData);
        this.updateUserInfo({ username: this.auth.username, avatar: formData })
          .then(() => {
            ElMessage.success('头像上传成功');
            this.avatarFile = null;
            this.fetchUserProfile(this.auth.username);
          })
          .catch(error => {
            ElMessage.error('头像上传失败');
            console.error('头像上传失败:', error);
          });
      } else {
        ElMessage.error('请选择一个头像文件');
      }
    },
    updateUsername() {
      if (this.newNickname) {
        const userExists = this.getUsers.some(user => user.username === this.newNickname);
        if (userExists) {
          ElMessage.error('用户名已存在');
          return;
        }
        this.updateUserInfo({ username: this.auth.username, newNickname: this.newNickname, newPassword: this.auth.password, hobby: this.hobby })
          .then(() => {
            ElMessage.success('昵称保存成功');
            this.auth.username = this.newNickname;
            this.fetchUserProfile(this.auth.username).then(() => {
              this.$router.push(`/user/${this.newNickname}`); // 修改成功后刷新用户信息并更新URL
            });
          })
          .catch(error => {
            ElMessage.error('保存失败');
            console.error('昵称保存失败:', error);
          });
      }
    },
    updatePassword() {
      if (this.newPassword !== this.confirmPassword) {
        ElMessage.error('新密码和确认新密码不一致');
        return;
      }
      this.updateUserInfo({ username: this.auth.username, newNickname: this.auth.username, newPassword: this.newPassword, hobby: this.hobby })
        .then(() => {
          ElMessage.success('密码保存成功');
          this.fetchUserProfile(this.username); // 修改成功后刷新用户信息
        })
        .catch(error => {
          ElMessage.error('保存失败');
          console.error('密码保存失败:', error);
        });
    },
    updateHobby() {
      if (this.hobby) {
        this.updateUserInfo({ username: this.auth.username, newNickname: this.auth.username, newPassword: this.auth.password, hobby: this.hobby })
          .then(() => {
            ElMessage.success('爱好保存成功');
            this.fetchUserProfile(this.username); // 修改成功后刷新用户信息
          })
          .catch(error => {
            ElMessage.error('保存失败');
            console.error('爱好保存失败:', error);
          });
      }
    },
    deleteDestination(id) {
      this.deleteDestinationData(id)
        .then(() => {
          ElMessage.success('删除成功');
        })
        .catch(error => {
          ElMessage.error('删除失败');
          console.error('删除目的地失败:', error);
        });
    },
    editDestination(row, index) {
      console.log('Editing destination:', row);
      this.editIndex = index;
      this.editForm = { ...row };
      this.editDialogVisible = true;
    },
    saveEdit() {
      const { id, ...data } = this.editForm;
      this.editDestinationData({ id, data })
        .then(() => {
          ElMessage.success('编辑成功');
          this.editDialogVisible = false;
        })
        .catch(error => {
          ElMessage.error('编辑失败');
          console.error('编辑目的地失败:', error);
        });
    },
    async acceptInvite(invite) {
      try {
        await this.respondInvite({ id: invite.id, response: 'accept' });
        ElMessage.success('已接受邀请');
        // 刷新邀请列表
        await this.fetchInvitations();
      } catch (error) {
        ElMessage.error('接受邀请失败');
      }
    },
    async declineInvite(invite) {
      try {
        await this.respondInvite({ id: invite.id, response: 'decline' });
        ElMessage.success('已拒绝邀请');
        // 刷新邀请列表
        this.fetchInvitations();
      } catch (error) {
        ElMessage.error('拒绝邀请失败');
      }
    }
  },
  async leaveTeam() {
    try {
      console.log("Trying to leave the team for user:", this.auth.username);
      await this.$store.dispatch('visitor/leaveTeam');
      console.log("Left the team successfully");
      this.$message.success('已成功退出团队');
    } catch (error) {
      console.error("Failed to leave the team:", error);
      this.$message.error('退出团队失败');
    }
  },
  async created() {
    console.log("now username is: ", this.username);
    await this.loadUserProfile(); // 获取对应用户的信息
    console.log("this.isCurrentUser:", this.isCurrentUser);
    if (this.isCurrentUser) {
      await this.$store.dispatch('visitor/fetchInvitations'); // 如果是当前用户，加载邀请信息
    }

    this.newNickname = this.currentProfile.username || '';
    this.hobby = this.currentProfile.hobby || '';
    this.hobbyLength = this.hobby.length;
  }
};
</script>

<style scoped lang="less">
@primary-color: #2c3e50;
@secondary-color: #18bc9c;
@background-color: #ecf0f1;
@card-background: #ffffff;
@font-family: 'Roboto', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
@button-hover-color: lighten(@primary-color, 10%);
@danger-color: #e74c3c;
@border-color: #dcdcdc;

/* 添加头像样式 */
.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 20px;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin-bottom: 10px;
}

.user-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background: @background-color;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  font-family: @font-family;
  color: @primary-color;

  h2 {
    color: @primary-color;
    text-align: center;
    font-size: 2em;
    margin-bottom: 20px;
  }
}

.edit-form {
  margin-bottom: 20px;
  padding: 20px;
  background: @card-background;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);

  .edit {
    display: flex;
    align-items: center;
    margin-bottom: 10px;

    label {
      margin-right: 10px;
      font-weight: bold;
      color: @primary-color;
    }

    input,
    textarea,
    .el-input {
      flex: 1;
      padding: 10px;
      border: 1px solid @border-color;
      border-radius: 5px;
      transition: border-color 0.3s;

      &:focus {
        border-color: @primary-color;
      }
    }

    button {
      margin-left: 10px;
      padding: 10px 20px;
      background: @primary-color;
      color: #fff;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      transition: background-color 0.3s;

      &:hover {
        background-color: @button-hover-color;
      }
    }
  }

  .password-input {
    width: calc(50% - 5px);
  }
}

.destination-data {
  margin-top: 20px;
  padding: 20px;
  background: @card-background;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);

  h3 {
    font-size: 1.5em;
    margin-bottom: 10px;
  }

  .destination-table {
    width: 100%;

    .el-table-column {
      min-width: 120px;
    }
  }
}

.team-members {
  margin-top: 20px;
  padding: 20px;
  background: @card-background;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);

  h3 {
    font-size: 1.5em;
    margin-bottom: 10px;
  }

  ul {
    list-style: none;
    padding: 0;

    li {
      margin-bottom: 10px;

      a {
        color: @primary-color;
        text-decoration: none;

        &:hover {
          text-decoration: underline;
        }
      }
    }
  }

  .leave-team-btn {
    margin-top: 10px;
    padding: 10px 20px;
    background: @danger-color;
    color: #fff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;

    &:hover {
      background-color: darken(@danger-color, 10%);
    }
  }
}

.username-link {
  color: @primary-color;
  text-decoration: none;

  &:hover {
    color: darken(@primary-color, 10%);
  }
}

.invitations {
  margin-top: 20px;
  padding: 20px;
  background: @card-background;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);

  h3 {
    font-size: 1.5em;
    margin-bottom: 10px;
  }

  .el-table {
    width: 100%;
  }

  .el-button {
    margin-left: 10px;
    padding: 10px 20px;
    background: @primary-color;
    color: #fff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;

    &:hover {
      background-color: @button-hover-color;
    }

    &.el-button--success {
      background: @secondary-color;

      &:hover {
        background-color: darken(@secondary-color, 10%);
      }
    }

    &.el-button--danger {
      background: @danger-color;

      &:hover {
        background-color: darken(@danger-color, 10%);
      }
    }
  }
}

.el-dialog__body {
  max-height: 400px;
  overflow-y: auto;
}

.el-dialog__footer {
  padding: 10px 20px;
  background-color: @background-color;
  border-top: 1px solid @border-color;
  display: flex;
  justify-content: flex-end;
}

.el-dialog__wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
}

.edit-dialog {
  width: 600px;

  .el-dialog__header {
    background: @primary-color;
    color: #fff;
    padding: 10px 20px;
    border-radius: 10px 10px 0 0;
  }
}
</style>
