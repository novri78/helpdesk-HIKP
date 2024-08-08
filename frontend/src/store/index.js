import { createStore } from 'vuex';
import axios from 'axios';
import cookie from 'js-cookie';

const store = 
  createStore({
    state: {
      user: null,
      token: cookie.get('token') || '',
    },
    mutations: {
      setUser(state, user) {
        state.user = user;
      },
      setToken(state, token) {
        state.token = token;
        if (token) {
          cookie.set('token', token, { expires: 1 });
        } else {
          cookie.remove('token');
        }
      },
      CLEAR_AUTH(state) {
        state.user = null;
        state.token = '';
        cookie.remove('token');
      }
    },
    actions: {
      async login({ commit }, credentials) {
        try {
          const resp = await axios.post(`/auth/login`, credentials);
          const { token, user } = resp.data;

          cookie.setToken('token', token);
          commit('SET_USER', user);
          commit('SET_TOKEN', token);

          axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

        } catch(error) {
          commit('CLEAR_AUTH');
          throw error;
        }
      },
      logout({ commit }) {
        commit('CLEAR_AUTH');
        delete axios.defaults.headers.common[`Authorization`];
      },
    },
    getters: {
      isAuthenticated: state => !!state.token,
      isUser: state => state.user
    },
  });

  export default store;
