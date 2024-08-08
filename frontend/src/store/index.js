import { createStore } from 'vuex';
import axios from 'axios';
import cookie from 'js-cookie';

export default createStore({
  state: {
    user: null,
    token: null,
  },
  mutations: {
    SET_LOGIN(state, {user,token}) {
      state.user = user;
      state.token = token;
    },
    SET_LOGOUT(state) {
      state.user = null;
      state.token = null;
    },
  },
  actions: {
    // async login({ commit }, { user, token}) {
    //   try {
    //     const resp = await this.$axios.post(`/auth/login`, credentials);
    //     console.log('value_login', resp.data)

    //     commit('SET_LOGIN', { user, token })
    //     cookie.set('users', JSON.stringify({ user, token }), { expires: 1 });
        
    //   } catch (error) {
    //     commit('SET_LOGOUT');
    //     throw error;
    //   }
    // },
    async login({ commit }, credentials) {
      try {
        const resp = await axios.post(`/auth/login`, credentials);
        const { token, user } = resp.data;

        commit('SET_LOGIN', user);
        commit('SET_TOKEN', token);
        cookie.set('token', token, { expires: 1 });
        cookie.set('user', JSON.stringify(user), { expires: 1 });
      } catch (error) {
        commit('SET_LOGOUT');
        throw error;
      }
    },
    logout({ commit }) {
      commit('SET_LOGOUT');
      cookie.remove('user');
      cookie.remove('token');
    },
    checkAuth({ commit }) {
      const token = cookie.get('token');
      if (token) {
        commit('SET_TOKEN', token);
        this.dispatch('fetchUserData');
      }
    },
    fetchUserData({ commit }) {
      const user = JSON.parse(cookie.get('user'));
      if (user) {
        commit('SET_LOGIN', user);
      }
    },
  },
  getters: {
    isAuthenticated: state => !!state.token,
    isUser: state => state.user
  },
});