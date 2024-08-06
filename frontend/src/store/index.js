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
          cookie.set('token', token, { expires: 7 });
        } else {
          cookie.remove('token');
        }
      },
    },
    actions: {
      async login({ commit }, credentials) {
        const response = await axios.post('/auth/login', credentials);
        const { user, token } = response.data;
        commit('setUser', user);
        commit('setToken', token);
      },
      logout({ commit }) {
        commit('setUser', null);
        commit('setToken', '');
      },
      checkAuth({ commit }) {
        const token = cookie.get('token');
        if (token) {
          axios.get('/auth/me', {
            headers: { Authorization: `Bearer ${token}` },
          })
          .then(response => {
            commit('setUser', response.data);
          })
          .catch(() => {
            commit('setToken', '');
          });
        }
      },
    },
    getters: {
      isAuthenticated: state => !!state.user,
      isUser: state => state.user?.role === 'USER',
    },
  });

  export default store;
