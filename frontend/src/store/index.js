import { createStore } from 'vuex';
import cookie from 'js-cookie';

const store =  createStore({
    state: {
        user: null,
        token: null,
        // listDataUser: null,
        // errorMessage: null,
    },
    mutations: {
        SET_LOGIN(state, { user, token } ) {
            state.user = user;
            state.token = token;
        },
        SET_LOGOUT(state, { }) {
            state.user = null;
            state.token = null;
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
                    }    
                } catch (error) {
                    console.error("Failed to parse cookie data:", e);
                }
            }    
        },    
        login({ commit }, { user, token }) {
            commit('SET_LOGIN', { user, token });
            cookie.set('userdata', JSON.stringify({ user, token }), { expires: 1 });
        },
        logout({ commit }) {
            commit('SET_LOGOUT');
            cookie.remove('userdata');
            cookie.remove('token');
        }    
    },
    getters: {
        isAuthenticated: (state) => !!state.token,
        getUserRole: (state) => {
          console.log('Vuex User:', state.user); // Check what is being stored in Vuex
          return state.user ? state.user.role : null;
        },        
    },
});

export default store;