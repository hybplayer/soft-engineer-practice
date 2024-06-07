import axios from 'axios';
import base from './base'

const API_BASE_URL = 'http://localhost:20334/api';

const api = {
    // 推荐
    getTuijian() {
        return axios.get(base.recommend);
    },

    // 用户登录
    login(credentials) {
        return axios.post(`${API_BASE_URL}/login`, credentials);
    },

    // 用户注册
    register(user) {
        return axios.post(`${API_BASE_URL}/register`, user);
    },

    getUser(username) {
        return axios.get(`${API_BASE_URL}/users/${username}`);
    },

    getAllUsers() {
        return axios.get(`${API_BASE_URL}/allusers`);
    },

    getUserDestinationData(username) {
        return axios.get(`${API_BASE_URL}/${username}/destinations`) // 返回Promise
        // return axios.get(`/api/users/${username}/destinations`) // 返回Promise
    },

    // 更新用户信息
    updateUserInfo(payload) {
        return axios.put(`${API_BASE_URL}/users/update`, payload);
    },

    // 获取所有目的地数据
    getDestinationData() {
        return axios.get(`${API_BASE_URL}/destinations`);
    },

    // 添加目的地数据
    addDestinationData(payload) {
        return axios.post(`${API_BASE_URL}/destinations`, payload);
    },

    // 编辑目的地数据
    editDestinationData(id, data) {
        return axios.put(`${API_BASE_URL}/destinations/${id}`, data);
    },

    // 删除目的地数据
    deleteDestinationData(id) {
        return axios.delete(`${API_BASE_URL}/destinations/${id}`);
    },

    // 获取所有帖子
    getPosts() {
        return axios.get(`${API_BASE_URL}/posts`);
    },

    // 添加帖子
    addPost(post) {
        return axios.post(`${API_BASE_URL}/posts`, post, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
    },

    // 获取某个帖子的所有评论
    getComments(postId) {
        return axios.get(`${API_BASE_URL}/comments/${postId}`);
    },

    loadAllComments() {
        return axios.get(`${API_BASE_URL}/allcomments`);
    },

    // 添加评论
    addComment(comment) {
        return axios.post(`${API_BASE_URL}/comments`, comment);
    }
};

export default api;
