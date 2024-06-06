import { createStore } from 'vuex';
import axios from 'axios';

const store = createStore({
  modules: {
    visitor: {
      namespaced: true,
      state: {
        auth: {
          isAuthenticated: false,
          username: '',
          avatar: '',
          password: '',
          hobby: ''
        },
        currentProfile: {
          username: '',
          hobby: '',
          avatar: '',
          password: ''
        },
        users: [],
        destinationData: [],
        posts: [],
        comments: []
      },
      mutations: {
        login(state, user) {
          state.auth.isAuthenticated = true;
          state.auth.username = user.username;
          state.auth.avatar = user.avatar || '';
          state.auth.password = user.password;
          state.auth.hobby = user.hobby || '';
        },
        logout(state) {
          state.auth.isAuthenticated = false;
          state.auth.username = '';
          state.auth.avatar = '';
          state.auth.password = '';
          state.auth.hobby = '';
        },
        loadUsers(state, users) {
          state.users = users;
        },
        updateUserInfo(state, payload) {
          state.auth.username = payload.newNickname || state.auth.username;
          state.auth.avatar = payload.avatar || state.auth.avatar;
          state.auth.password = payload.newPassword || state.auth.password;
          state.auth.hobby = payload.hobby || state.auth.hobby;
        },
        setDestinationData(state, data) {
          state.destinationData = data;
        },
        addDestinationData(state, destination) {
          state.destinationData.push(destination);
        },
        editDestinationData(state, { index, data }) {
          state.destinationData.splice(index, 1, data);
        },
        deleteDestinationData(state, id) {
          const index = state.destinationData.findIndex(dest => dest.id === id);
          if (index !== -1) {
            state.destinationData.splice(index, 1);
          }
        },
        setCurrentProfile(state, profile) {
          state.currentProfile.username = profile.username;
          state.currentProfile.hobby = profile.hobby;
          state.currentProfile.avatar = profile.avatar;
          state.currentProfile.password = profile.password;
        }
      },
      actions: {
        async login({ commit }, credentials) {
          try {
            const response = await axios.post('/api/login', credentials);
            commit('login', response.data);
            return response.data;
          } catch (error) {
            throw new Error('登录失败');
          }
        },
        async register({ commit }, user) {
          try {
            const response = await axios.post('/api/register', user);
            commit('login', response.data);
            return response.data;
          } catch (error) {
            throw new Error('注册失败');
          }
        },
        async loadUser({ commit }, username) {
          try {
            console.log(`Fetching user profile for: ${username}`);
            const response = await axios.get(`/api/users/${username}`);
            commit('loadUser', response.data);
          } catch (error) {
            console.error('加载用户数据失败:', error);
          }
        },
        async fetchCurrentUser({ state, commit }) {
          try {
            const response = await axios.get(`/api/users/${state.auth.username}`); // 根据当前用户名动态获取用户数据
            commit('login', response.data);
          } catch (error) {
            console.error('获取当前用户数据失败:', error);
          }
        },
        async fetchUserProfile({ commit }, username) {
          try {
            console.log(`Fetching user profile for: ${username}`); // 打印URL调试
            const response = await axios.get(`/api/users/${username}`);
            commit('setCurrentProfile', response.data);
            return response.data;
          } catch (error) {
            console.error('获取用户信息失败:', error);
            throw error;
          }
        },
        async updateUserInfo({ commit }, payload) {
          try {
            const response = await axios.post('/api/users/update', payload);
            commit('updateUserInfo', response.data);
          } catch (error) {
            console.error('更新用户信息失败:', error);
          }
        },
        async loadDestinationData({ commit }) {
          try {
            const response = await axios.get('/api/destinations');
            commit('setDestinationData', response.data);
          } catch (error) {
            console.error('加载目的地数据失败:', error);
          }
        },
        async addDestinationData({ commit }, payload) {
          try {
            const response = await axios.post('/api/destinations', payload);
            commit('addDestinationData', response.data);
          } catch (error) {
            console.error('添加目的地数据失败:', error);
          }
        },
        async editDestinationData({ commit }, { index, data }) {
          try {
            const response = await axios.put(`/api/destinations/${data.id}`, data);
            commit('editDestinationData', { index, data: response.data });
          } catch (error) {
            console.error('编辑目的地数据失败:', error);
          }
        },
        async deleteDestinationData({ commit }, id) {
          try {
            await axios.delete(`/api/destinations/${id}`);
            commit('deleteDestinationData', id);
          } catch (error) {
            console.error('删除目的地数据失败:', error);
          }
        },
        async loadPosts({ commit }) {
          try {
            const response = await axios.get('/api/posts');
            commit('setPosts', response.data);
          } catch (error) {
            console.error('加载帖子数据失败:', error);
          }
        },
        async addPost({ commit }, post) {
          try {
            const response = await axios.post('/api/posts', post);
            commit('addPost', response.data);
          } catch (error) {
            console.error('添加帖子失败:', error);
          }
        },
        async loadComments({ commit }, postId) {
          try {
            const response = await axios.get(`/api/comments/${postId}`);
            commit('setComments', response.data);
          } catch (error) {
            console.error('加载评论数据失败:', error);
          }
        },
        async addComment({ commit }, comment) {
          try {
            const response = await axios.post('/api/comments', comment);
            commit('addComment', response.data);
          } catch (error) {
            console.error('添加评论失败:', error);
          }
        }
      },
      getters: {
        getDestinationData: (state) => (username) => {
          return state.destinationData.filter(destination => destination.username === username);
        },
        getUsers: (state) => state.users,
        getPosts: (state) => state.posts,
        getComments: (state) => (postId) => {
          return state.comments.filter(comment => comment.postId === postId);
        },
        getCurrentUser: (state) => {
          return state.auth;
        },
        getUserDestinations: (state) => (username) => {
          const destinations = state.destinationData
            .filter(destination => destination.username === username)
            .map(destination => destination.destination);
          return destinations.join(', ');
        }
      }
    }
  }
});

export default store;
