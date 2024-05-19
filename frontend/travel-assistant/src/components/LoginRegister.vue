<template>
  <div class="login-register-page">
    <el-card class="login-card">
      <div class="card-header">
        <el-button-group>
          <el-button :type="isLogin ? 'primary' : ''" @click="toggleMode(true)">登录</el-button>
          <el-button :type="!isLogin ? 'primary' : ''" @click="toggleMode(false)">注册</el-button>
        </el-button-group>
      </div>
      <el-form :model="form" :rules="formRules" ref="form" class="login-register-form">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名">
            <template #prepend>
              <el-icon>
                <User />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input type="password" v-model="form.password" placeholder="请输入密码">
            <template #prepend>
              <el-icon>
                <Lock />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item v-if="!isLogin" prop="confirmPassword">
          <el-input type="password" v-model="form.confirmPassword" placeholder="请确认密码">
            <template #prepend>
              <el-icon>
                <Lock />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm" class="login-btn">{{ isLogin ? '登录' : '注册' }}</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus';
import { mapState } from 'vuex';
import { User, Lock } from '@element-plus/icons-vue'; // 导入图标

export default {
  components: {
    User,
    Lock
  },
  data() {
    return {
      isLogin: true,
      form: {
        username: '',
        password: '',
        confirmPassword: ''
      },
      formRules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        confirmPassword: [{ required: true, message: '请确认密码', trigger: 'blur' }]
      }
    };
  },
  computed: {
    ...mapState('visitor', {
      isAuthenticated: state => state.auth.isAuthenticated,
      username: state => state.auth.username,
      password: state => state.auth.password,
    }),
    users() {
      return this.$store.getters['visitor/getUsers'];
    }
  },
  methods: {
    toggleMode(isLogin) {
      this.isLogin = isLogin;
    },
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          if (this.isLogin) {
            // 登录逻辑
            const user = this.users.find(
              user => user.username === this.form.username && user.password === this.form.password
            );
            if (user) {
              this.$store.commit('visitor/login', user);
              ElMessage.success('登录成功');
              this.$router.push({ name: 'Home' });
            } else {
              ElMessage.error('用户名或密码错误');
            }
          } else {
            // 注册逻辑
            if (this.form.password !== this.form.confirmPassword) {
              ElMessage.error('密码和确认密码不一致');
              return;
            }
            const userExists = this.users.some(user => user.username === this.form.username);
            if (userExists) {
              ElMessage.error('用户名已存在');
              return;
            }
            const newUser = {
              username: this.form.username,
              password: this.form.password
            };
            this.$store.dispatch('visitor/register', newUser);
            ElMessage.success('注册成功');
            this.$router.push({ name: 'Home' });
          }
        } else {
          console.log('表单验证失败');
        }
      });
    }
  },
  created() {
    this.$store.dispatch('visitor/loadUsers'); // 加载本地存储中的用户数据
  }
};
</script>

<style scoped>
/* 登录页面样式 */
.login-register-page {
  background-image: url('@/assets/A.jpg');
  background-size: cover;
  background-repeat: no-repeat;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-position: center;
}

.login-card {
  width: 400px;
  border-radius: 10px;
  background-color: rgba(255, 255, 255, 0.85);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: center;
  padding: 20px;
}

.login-register-form {
  padding: 20px;
}

.login-btn {
  width: 100%;
}
</style>
