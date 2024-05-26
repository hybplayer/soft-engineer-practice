import { createStore } from 'vuex';

const store = createStore({
  modules: {
    visitor: {
      namespaced: true,
      state: {
        auth: {
          isAuthenticated: false,
          username: '',
          password: '',
          hobby: '' // 新增存储旅游爱好
        },
        users: [], // 存储注册用户的数据
        destinationData: JSON.parse(localStorage.getItem('destinationData')) || [] // 初始化为本地存储的数据或空数组
      },
      mutations: {
        updateVisitorData(state, payload) {
          state.visitorData = payload;
        },
        login(state, user) {
          state.auth.isAuthenticated = true;
          state.auth.username = user.username;
          state.auth.password = user.password;
          state.auth.hobby = user.hobby || '';
        },
        logout(state) {
          state.auth.isAuthenticated = false;
          state.auth.username = '';
          state.auth.password = '';
          state.auth.hobby = '';
        },
        register(state, user) {
          state.auth.isAuthenticated = true;
          state.auth.username = user.username;
          state.auth.password = user.password;
          state.auth.hobby = user.hobby || '';
          state.users.push(user); // 将注册用户添加到用户列表中
          localStorage.setItem('users', JSON.stringify(state.users)); // 保存到本地存储
        },
        loadUsers(state) {
          const users = JSON.parse(localStorage.getItem('users'));
          if (users) {
            state.users = users;
          }
        },
        updateUserInfo(state, payload) {
          const userIndex = state.users.findIndex(user => user.username === state.auth.username);
          if (userIndex !== -1) {
            state.users[userIndex].username = payload.newNickname || state.users[userIndex].username;
            state.users[userIndex].password = payload.newPassword || state.users[userIndex].password;
            state.users[userIndex].hobby = payload.hobby || state.users[userIndex].hobby;
            state.auth.username = payload.newNickname || state.auth.username;
            state.auth.password = payload.newPassword || state.auth.password;
            state.auth.hobby = payload.hobby || state.auth.hobby;
            localStorage.setItem('users', JSON.stringify(state.users)); // 更新本地存储
          }
        },
        updateDestinationData(state, payload) {
          state.destinationData.push(payload);
          localStorage.setItem('destinationData', JSON.stringify(state.destinationData)); // 保存到本地存储
        },
        editDestinationData(state, { index, data }) {
          state.destinationData.splice(index, 1, data);
          localStorage.setItem('destinationData', JSON.stringify(state.destinationData)); // 更新本地存储
        },
        deleteDestinationData(state, index) {
          state.destinationData.splice(index, 1);
          localStorage.setItem('destinationData', JSON.stringify(state.destinationData)); // 更新本地存储
        }
      },
      actions: {
        updateUserInfo({ commit }, payload) {
          commit('updateUserInfo', payload);
        },
        register({ commit }, user) {
          commit('register', user);
        },
        loadUsers({ commit }) {
          commit('loadUsers');
        },
        updateDestinationData({ commit, state }, payload) {
          const dataWithUsername = {
            ...payload,
            username: state.auth.username // 添加用户名到数据中
          };
          commit('updateDestinationData', dataWithUsername);
        },
        editDestinationData({ commit }, payload) {
          commit('editDestinationData', payload);
        },
        deleteDestinationData({ commit }, index) {
          commit('deleteDestinationData', index);
        }
      },
      getters: {
        getDestinationData: (state) => (username) => {
          return state.destinationData.filter(destination => destination.username === username);
        },
        getUsers: (state) => state.users
      }
    }
  }
});

export default store;
