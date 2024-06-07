import { createStore } from 'vuex';
import axios from 'axios';
import api from '@/api';

const store = createStore({
  modules: {
    visitor: {
      namespaced: true,
      state: {
        // 登录用户
        auth: {
          isAuthenticated: false,
          username: '',
          avatar: '',
          password: '',
          hobby: ''
        },
        // 当前资料卡的用户
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
        getUserDestinations: state => username => state.userDestinations[username],
        setDestinationData(state, data) {
          state.destinationData = data;
        },
        addDestinationData(state, destination) {
          state.destinationData.push(destination);
        },
        editDestinationData(state, { id, data }) {
          const index = state.destinationData.findIndex(dest => dest.id === id);
          if (index !== -1) {
            state.destinationData.splice(index, 1, data);
          }
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
          // console.log(state.currentProfile.username, state.currentProfile.hobby);
        },
        setPosts(state, posts) {
          state.posts = posts;
        },
        addPost(state, post) {
          state.posts.push(post);
        },
        setComments(state, { postId, comments }) {
          // 将 comments 的 postId 添加到现有的评论状态中
          state.comments = state.comments.filter(comment => comment.postId !== postId).concat(comments);
        },
        addComment(state, comment) {
          state.comments.push(comment);
        },
        // setAllComments(state, comments) {
        //   // 你的设置所有帖子的评论的逻辑
        //   state.allComments = comments;
        // },
        setUsers(state, users) {
          state.users = users;
        },
        setUserDestinations(state, { username, destinations }) {
          state.userDestinations = {
            ...state.userDestinations,
            [username]: destinations
          };
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
            const response = await api.getUser(username);
            commit('loadUser', response.data);
          } catch (error) {
            console.error('加载用户数据失败:', error);
          }
        },
        async fetchAuthUserProfile({ state, commit }) {
          try {
            console.log("state.auth.username:", state.auth.username);
            const response = await api.getUser(state.auth.username); // 根据当前用户名动态获取用户数据
            commit('setCurrentProfile', response.data);
          } catch (error) {
            console.error('获取当前用户数据失败:', error);
          }
        },
        async fetchUserProfile({ commit }, username) {
          try {
            console.log(`Fetching user profile for: ${username}`); // 打印URL调试
            const response = await api.getUser(username);
            console.log(response);
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
        async loadDestinationData({ commit, state }) {
          try {
            const response = await api.getDestinationData();
            console.log("API Response Data:", response.data); // 打印API返回的数据

            const filteredData = response.data.filter(destination => destination.username === state.currentProfile.username);
            console.log("state.currentProfile.username: ", state.currentProfile.username);
            console.log("Filtered Data: ", filteredData);

            commit('setDestinationData', filteredData);
          } catch (error) {
            console.error('加载目的地数据失败:', error);
          }
        },
        async loadUserDestinationData({ commit }, username) {
          try {
            const response = await api.getDestinationData();
            console.log("API Response Data:", response.data); // 打印API返回的数据

            const filteredData = response.data.filter(destination => destination.username === username);
            console.log("Filtered Data: ", filteredData);

            commit('setDestinationData', filteredData);
          } catch (error) {
            console.error('加载目的地数据失败:', error);
          }
        },

        async addDestinationData({ commit, dispatch }, payload) {
          try {
            const response = await api.addDestinationData(payload);
            commit('addDestinationData', response.data);
            await dispatch('loadDestinationData'); // 更新数据后重新加载目的地数据
          } catch (error) {
            console.error('添加目的地数据失败:', error);
          }
        },
        async editDestinationData({ commit, dispatch }, { id, data }) {
          try {
            const response = await api.editDestinationData(id, data);
            commit('editDestinationData', { id, data: response.data });
            await dispatch('loadDestinationData'); // 更新数据后重新加载目的地数据
          } catch (error) {
            console.error('编辑目的地数据失败:', error);
          }
        },
        async deleteDestinationData({ commit, dispatch }, id) {
          try {
            await api.deleteDestinationData(id);
            commit('deleteDestinationData', id);
            await dispatch('loadDestinationData'); // 更新数据后重新加载目的地数据
          } catch (error) {
            console.error('删除目的地数据失败:', error);
          }
        },
        async loadPosts({ commit }) {
          try {
            const response = await api.getPosts();
            commit('setPosts', response.data);
          } catch (error) {
            console.error('加载帖子数据失败:', error);
          }
        },
        async addPost({ commit }, post) {
          try {
            const response = await api.addPost(post);
            commit('addPost', response.data);
            return response.data;
          } catch (error) {
            console.error('添加帖子失败:', error);
            throw error;
          }
        },
        async loadComments({ commit }, postId) {
          const response = await api.getComments(postId);
          commit('setComments', { postId, comments: response.data });
        },
        async addComment({ commit }, comment) {
          try {
            const response = await api.addComment(comment);
            commit('addComment', response.data);
          } catch (error) {
            console.error('添加评论失败:', error);
          }
        },
        async refreshAuthProfile({ dispatch, state, commit }) {
          await dispatch('fetchAuthUser');  // 获取当前登录用户信息
          await dispatch('loadDestinationData');  // 加载当前用户的目的地数据
          const username = state.auth.username;
          const filteredDestinations = state.destinationData.filter(destination => destination.username === username);
          console.log("Filtered Data after refreshAuthProfile: ", filteredDestinations);
          commit('setDestinationData', filteredDestinations); // 设置筛选后的目的地数据
        },
        async fetchUsers({ commit }) {
          return api.getAllUsers() // 返回Promise
            .then(response => {
              console.log("get all users: ", response);
              commit('setUsers', response.data);
            });
        },
        async fetchUserDestinations({ commit }, username) {
          return api.getUserDestinationData(username)
            .then(response => {
              commit('setUserDestinations', { username, destinations: response.data });
            });
        },
      },
      getters: {
        getDestinationData: (state) => (username) => {
          return state.destinationData.filter(destination => destination.username === username);
        },
        getUsers: (state) => state.users,
        // getUserDestinations: state => username => state.userDestinations[username],
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
