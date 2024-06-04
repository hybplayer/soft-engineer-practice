import { createStore } from 'vuex';

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
        users: [],
        destinationData: JSON.parse(localStorage.getItem('destinationData')) || [],
        posts: JSON.parse(localStorage.getItem('posts')) || [],
        comments: JSON.parse(localStorage.getItem('comments')) || []
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
        register(state, user) {
          state.auth.isAuthenticated = true;
          state.auth.username = user.username;
          state.auth.avatar = user.avatar || '';
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
            state.users[userIndex].avatar = payload.avatar || state.users[userIndex].avatar;
            state.users[userIndex].password = payload.newPassword || state.users[userIndex].password;
            state.users[userIndex].hobby = payload.hobby || state.users[userIndex].hobby;
            state.auth.username = payload.newNickname || state.auth.username;
            state.auth.avatar = payload.avatar || state.auth.avatar;
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
        },
        addPost(state, post) {
          state.posts.unshift(post);
          localStorage.setItem('posts', JSON.stringify(state.posts));
        },
        loadPosts(state) {
          const posts = JSON.parse(localStorage.getItem('posts'));
          if (posts) {
            state.posts = posts;
          }
        },
        addComment(state, comment) {
          state.comments.push(comment);
          localStorage.setItem('comments', JSON.stringify(state.comments));
        },
        loadComments(state) {
          const comments = JSON.parse(localStorage.getItem('comments'));
          if (comments) {
            state.comments = comments;
          }
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
        },
        addPost({ commit, state }, post) {
          const postWithUsername = {
            ...post,
            username: state.auth.username,
            avatar: state.auth.avatar
          };
          commit('addPost', postWithUsername);
        },
        loadPosts({ commit }) {
          commit('loadPosts');
        },
        addComment({ commit, state }, comment) {
          const commentWithUsername = {
            ...comment,
            username: state.auth.username,
            avatar: state.auth.avatar
          };
          commit('addComment', commentWithUsername);
        },
        loadComments({ commit }) {
          commit('loadComments');
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
