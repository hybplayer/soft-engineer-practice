<template>
  <div class="user-page">
    <h2>个人中心</h2>

    <!-- 修改用户名 -->
    <form @submit.prevent="updateUsername">
      <div class="edit">
        <label for="nickname">新昵称：</label>
        <input type="text" id="nickname" v-model="newNickname">
        <button type="submit">保存昵称</button>
      </div>
    </form>

    <!-- 修改密码 -->
    <form @submit.prevent="updatePassword">
      <div class="edit">
        <label for="password">新密码：</label>
        <el-input :type="passwordFieldType" v-model="newPassword">
          <template #suffix>
            <el-icon :component="passwordFieldType === 'password' ? Eye : EyeOff" @click="togglePasswordVisibility" />
          </template>
        </el-input>

        <label for="confirmPassword">确认新密码：</label>
        <el-input :type="confirmPasswordFieldType" v-model="confirmPassword">
          <template #suffix>
            <el-icon :component="confirmPasswordFieldType === 'password' ? Eye : EyeOff"
              @click="toggleConfirmPasswordVisibility" />
          </template>
        </el-input>

        <button type="submit">保存密码</button>
      </div>
    </form>

    <!-- 修改旅游爱好 -->
    <form @submit.prevent="updateHobby">
      <div class="edit">
        <label for="hobby">旅游爱好：</label>
        <textarea id="hobby" v-model="hobby" @input="updateHobbyLength"></textarea>
        <div>字数：{{ hobbyLength }}/100</div>
        <button type="submit">保存爱好</button>
      </div>
    </form>

    <!-- 新增数据存放区域 -->
    <div class="destination-data">
      <h3>目的地数据存放区</h3>
      <el-table :data="destinationData" :style="{ border: '1px solid #ccc' }">
        <el-table-column prop="departure" label="出发地"></el-table-column>
        <el-table-column prop="destination" label="目的地"></el-table-column>
        <el-table-column prop="departureDate" label="出发日期"></el-table-column>
        <el-table-column prop="checkboxValues" label="选择项">
          <template #default="{ row }">
            <span v-for="(value, index) in row.checkboxValues" :key="index">{{ value }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注"></el-table-column>
        <el-table-column label="操作">
          <template #default="{ $index }">
            <!-- <el-button type="text" size="small" @click="editDestination(row, $index)">编辑</el-button> -->
            <el-button type="text" size="small" @click="deleteDestination($index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 编辑目的地表单 -->
    <el-dialog v-model:visible="editDialogVisible" title="编辑目的地">
      <el-form :model="editForm">
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
        <el-form-item label="备注">
          <el-input type="textarea" v-model="editForm.remark"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { mapActions, mapGetters, mapState } from 'vuex';
import { ElMessage } from 'element-plus';
// import { Eye, EyeOff } from '@element-plus/icons-vue';

export default {
  components: {
    // Eye,
    // EyeOff
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
        departure: '',
        destination: '',
        departureDate: '',
        checkboxValues: [],
        remark: ''
      },
      editIndex: -1
    };
  },
  computed: {
    ...mapState('visitor', ['auth']),
    ...mapGetters('visitor', ['getDestinationData', 'getUsers']), // 新增获取用户列表的计算属性
    destinationData() {
      return this.getDestinationData; // 获取目的地数据
    }
  },
  methods: {
    ...mapActions('visitor', ['updateUserInfo', 'updateDestinationData', 'editDestinationData', 'deleteDestinationData']),
    updateUsername() {
      if (this.newNickname) {
        const userExists = this.getUsers.some(user => user.username === this.newNickname);
        if (userExists) {
          ElMessage.error('用户名已存在');
          return;
        }
        this.updateUserInfo({ newNickname: this.newNickname, newPassword: this.auth.password, hobby: this.hobby })
          .then(() => {
            ElMessage.success('昵称保存成功');
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
      this.updateUserInfo({ newNickname: this.auth.username, newPassword: this.newPassword, hobby: this.hobby })
        .then(() => {
          ElMessage.success('密码保存成功');
        })
        .catch(error => {
          ElMessage.error('保存失败');
          console.error('密码保存失败:', error);
        });
    },
    updateHobby() {
      if (this.hobby) {
        this.updateUserInfo({ newNickname: this.auth.username, newPassword: this.auth.password, hobby: this.hobby })
          .then(() => {
            ElMessage.success('爱好保存成功');
          })
          .catch(error => {
            ElMessage.error('保存失败');
            console.error('爱好保存失败:', error);
          });
      }
    },
    updateHobbyLength() {
      this.hobbyLength = this.hobby.length;
    },
    togglePasswordVisibility() {
      this.passwordFieldType = this.passwordFieldType === 'password' ? 'text' : 'password';
    },
    toggleConfirmPasswordVisibility() {
      this.confirmPasswordFieldType = this.confirmPasswordFieldType === 'password' ? 'text' : 'password';
    },
    editDestination(row, index) {
      this.editForm = { ...row };
      this.editIndex = index;
      this.editDialogVisible = true;
    },
    saveEdit() {
      if (this.editIndex !== -1) {
        this.editDestinationData({ index: this.editIndex, data: this.editForm });
        this.editDialogVisible = false;
        ElMessage.success('目的地信息更新成功');
      }
    },
    deleteDestination(index) {
      this.deleteDestinationData(index);
      ElMessage.success('目的地信息删除成功');
    }
  },
  created() {
    this.newNickname = this.auth.username;
    this.hobby = this.auth.hobby || '';
    this.hobbyLength = this.hobby.length;
  }
};
</script>