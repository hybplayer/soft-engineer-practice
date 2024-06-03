import { createStore } from 'vuex';

const store = createStore({
  modules: {
    visitor: {
      namespaced: true,
      state: {
        auth: {
          isAuthenticated: false,
          username: '', // 确保这里存储了当前登录用户的用户名
          password: '',
          hobby: ''
        },
        users: [],
        destinationData: JSON.parse(localStorage.getItem('destinationData')) || []
      },
      mutations: {
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
          state.users.push(user);
          localStorage.setItem('users', JSON.stringify(state.users));
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
            localStorage.setItem('users', JSON.stringify(state.users));
          }
        },
        updateDestinationData(state, payload) {
          state.destinationData.push(payload);
          localStorage.setItem('destinationData', JSON.stringify(state.destinationData));
        },
        editDestinationData(state, { index, data }) {
          state.destinationData.splice(index, 1, data);
          localStorage.setItem('destinationData', JSON.stringify(state.destinationData));
        },
        deleteDestinationData(state, index) {
          state.destinationData.splice(index, 1);
          localStorage.setItem('destinationData', JSON.stringify(state.destinationData));
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
            username: state.auth.username
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
