<template>
  <div class="user-page">
    <h2>个人中心</h2>

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
      <el-table :data="destinationData" :style="{ border: '1px solid #ccc' }" class="destination-table">
        <el-table-column prop="departure" label="出发地"></el-table-column>
        <el-table-column prop="destination" label="目的地"></el-table-column>
        <el-table-column prop="departureDate" label="出发日期"></el-table-column>
        <el-table-column prop="checkboxValues" label="选择项">
          <template #default="{ row }">
            <span v-for="(value, index) in row.checkboxValues" :key="index">{{ value }} </span>
          </template>
        </el-table-column>
        <el-table-column prop="priceRange" label="可接受价格">
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
            <el-option label="1000元以下" value="1"></el-option>
            <el-option label="1000-3000元" value="2"></el-option>
            <el-option label="3000-5000元" value="3"></el-option>
            <el-option label="5000元以上" value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="对同伴的要求">
          <el-input type="textarea" v-model="editForm.companionRequirements"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" v-model="editForm.remark"></el-input>
        </el-form-item>
      </el-form>
      <template v-slot:footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>

</template>

<script>
import { mapActions, mapGetters, mapState } from 'vuex';
import { ElMessage } from 'element-plus';

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
      postId: 1 // 示例帖子ID
    };
  },

  computed: {
    ...mapState('visitor', ['auth', 'currentProfile', 'destinationData']),
    ...mapGetters('visitor', ['getUsers']),
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
  methods: {
    ...mapActions('visitor', ['updateUserInfo', 'editDestinationData', 'deleteDestinationData', 'fetchUserProfile', 'loadUserDestinationData']),
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
    }
  },

  async created() {
    console.log("now username is: ", this.username);
    await this.$store.dispatch('visitor/fetchUserProfile', this.username); // 获取对应用户的信息
    await this.$store.dispatch('visitor/loadUserDestinationData', this.username); // 确保在页面加载时获取最新的数据
    this.newNickname = this.currentProfile.username || '';
    this.hobby = this.currentProfile.hobby || '';
    this.hobbyLength = this.hobby.length;
  }


};
</script>

<style scoped lang="less">
.user-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.edit-form {
  margin-bottom: 20px;

  .edit {
    display: flex;
    align-items: center;
    margin-bottom: 10px;

    label {
      margin-right: 10px;
    }

    input,
    textarea,
    .el-input {
      flex: 1;
    }

    button {
      margin-left: 10px;
    }
  }

  .password-input {
    width: calc(50% - 5px);
  }
}

.destination-data {
  margin-top: 20px;

  h3 {
    font-size: 20px;
    margin-bottom: 10px;
  }

  .destination-table {
    width: 100%;

    .el-table-column {
      min-width: 120px;
    }
  }
}

.el-dialog__body {
  max-height: 400px;
  overflow-y: auto;
}

.el-dialog__footer {
  padding: 1px 2px;
  background-color: #ff0000;
  /* 确保背景颜色与对话框一致 */
  border-top: 1px solid #e00808;
  /* 添加顶部边框以区分内容和底部 */
  display: flex;
  justify-content: flex-end;
  /* 确保按钮在右侧对齐 */
}

.el-button {
  margin-left: 10px;
}

.el-dialog__wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
}

.edit-dialog {
  width: 600px;
}
</style>
