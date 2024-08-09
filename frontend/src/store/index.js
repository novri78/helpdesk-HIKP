import { createStore } from 'vuex';
import cookie from 'js-cookie';

export default createStore({
    state: {
        user: null,
        token: null,
        errorMessage: null,
    },
    mutations: {
        setUser(state, userData) {
            state.user = userData;
        },
        setToken(state, tokenData) {
            state.token = tokenData;
        },
        setErrorMessage(state, message) {
            state.errorMessage = message;
        },
    },
    actions: {
        async authenticateUser({ commit }, { email, password }) {
            try {
                // Ensure the Axios instance from the store is used
                const axiosInstance = this.$axios;

                // Make the API request
                const response = await axiosInstance.post('/auth/login', { email, password });
                const { token, ...userData } = response.data;

                // Commit the user and token data to the store
                commit('setUser', userData);
                commit('setToken', token);
                cookie.set('authToken', token);

                console.log('Login successful:', response.data);
                return true;
            } catch (error) {
                let errorMsg = 'Login failed';
                if (error.response) {
                    errorMsg = error.response.data?.message || error.response.statusText || errorMsg;
                }
                commit('setErrorMessage', errorMsg);
                console.error('Login error:', errorMsg);
                return false;
            }
        },
        logoutUser({ commit }) {
            commit('setUser', null);
            commit('setToken', null);
            cookie.remove('authToken');
            console.log('User logged out');
        },
    },
    getters: {
        isAuthenticated: state => !!state.token,
        getUserRole: state => state.user ? state.user.role : null,
        getErrorMessage: state => state.errorMessage,
    },
});
