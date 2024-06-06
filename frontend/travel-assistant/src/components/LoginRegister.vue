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
import { User, Lock } from '@element-plus/icons-vue';
import api from '@/api';

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
    async submitForm() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            let response;
            if (this.isLogin) {
              console.log("Logging in with:", this.form);
              response = await api.login(this.form);
              console.log("Login response:", response.data); // 添加日志
              this.$store.commit('visitor/login', response.data);
              await this.$store.dispatch('visitor/loadUser', response.data.username); // 注册成功后加载当前用户数据
              ElMessage.success('登录成功');
              this.$router.push({ name: 'UserProfile', params: { username: response.data.username } }); // 跳转到用户主页
            } else {
              if (this.form.password !== this.form.confirmPassword) {
                ElMessage.error('密码和确认密码不一致');
                return;
              }
              console.log("Registering with:", this.form);
              response = await api.register(this.form);
              console.log("Register response:", response.data); // 添加日志
              this.$store.commit('visitor.register', response.data);
              await this.$store.dispatch('visitor/loadUsers');
              ElMessage.success('注册成功');
              this.$router.push({ name: 'UserProfile', params: { username: response.data.username } }); // 跳转到用户主页
            }
          } catch (error) {
            console.error("API error:", error);
            ElMessage.error(error.response ? error.response.data : '请求失败');
          }
        } else {
          console.log('表单验证失败');
        }
      });
    }
  }
};

</script>

<style scoped>
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
