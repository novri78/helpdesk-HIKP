import { createStore } from 'vuex';
import cookie from 'js-cookie';
import axios from 'axios';

const store = createStore({
    state: {
        user: null,
        token: null,
        // listDataUser: null,
        // errorMessage: null,
    },
    mutations: {
        SET_LOGIN(state, { user, token }) {
            state.user = user;
            state.token = token;
        },

        SET_LOGOUT(state) {
            if (state.user && state.token) {
                state.user = null;
                state.token = null;
            }
        },

    },
    actions: {
        checkAuth({ commit }) {
            const cookieData = cookie.get('userdata');
            if (cookieData) {
                try {
                    const userdata = JSON.parse(cookieData);
                    if (userdata && userdata.token) {
                        commit('SET_LOGIN', { user: userdata.user, token: userdata.token });
                        // Set axios default Authorization header for subsequent requests
                        axios.defaults.headers.common['Authorization'] = `Bearer ${userdata.token}`;
                    }
                } catch (error) {
                    console.error("Failed to parse cookie data:", error);
                }
            }
        },
        login({ commit }, { user, token }) {
            commit('SET_LOGIN', { user, token });
            cookie.set('userdata', JSON.stringify({ user, token }), { expires: 1 });
            // Set axios default Authorization header globally
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        },
        logout({ commit }) {
            commit('SET_LOGOUT');
            cookie.remove('userdata');
            // Remove axios default Authorization header
            delete axios.defaults.headers.common['Authorization'];
        }
        ,
        async approveUser({ state }, { id }) {
            try {
                const headers = state.token ? { Authorization: `Bearer ${state.token}` } : {};
                const resp = await axios.put(`/users/${id}/approve`, { isApproved: true }, { headers });
                return resp.data;
            } catch (err) {
                throw err;
            }
        },
        async rejectUser({ state }, { id }) {
            try {
                const headers = state.token ? { Authorization: `Bearer ${state.token}` } : {};
                const resp = await axios.delete(`/users/${id}`, { headers });
                return resp.data;
            } catch (err) {
                throw err;
            }
        }
    },
    getters: {
        isAuthenticated: state => !!state.token,
        userRole: state => (state.user ? state.user.role : null),
    },
});

export default store;