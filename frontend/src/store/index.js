import { createStore } from 'vuex';
import axios from 'axios';
import cookie from 'js-cookie';

export default createStore({
  state: {
    user: null,
    token: null,
  },
  mutations: {
    // SET_LOGIN(state, { user, token}) {
    //   state.user = user;
    //   state.token = token;
    // },
    SET_LOGIN(state, user) {
      state.user = user;
    },
    SET_LOGOUT(state) {
      state.user = null;
      state.token = null;
    },
  },
  actions: {
    async login({ commit }, { user, token}) {
      try {
        const resp = await this.$axios.post(`/auth/login`, credentials);
        console.log('value_login', resp.data)

        commit('SET_LOGIN', { user, token })
        cookie.set('users', JSON.stringify({ user, token }), { expires: 1 });
        
      } catch (error) {
        commit('SET_LOGOUT');
        throw error;
      }
    },
    logout({ commit }) {
      commit('SET_LOGOUT');
      cookie.remove('userdata');
    },
    checkAuth( {commit} ) {
      const cookieData = cookie.get('users');
      if (cookieData) {
        try {

        } catch (e) {
          console.error("Failed to parse cookie data:", e);
        }
      }
    },
  },
  getters: {
    isAuthenticated: state => !!state.token,
    isUser: state => state.user
  },
});