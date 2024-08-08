<template>
  <header>
    
  </header>

  <main>
    <div class="background-image">
      <div class="ticker">
        <span class="ticker-text">
          Learning to write programs stretches your mind, and helps you think better, creates a way of thinking about things that I think is helpful in all domains. - Bill Gates
        </span>
      </div>
    </div>
    <div class="login-container">
      <h2>Helpdesk Login</h2>
      <form @submit.prevent="login">
        <div>
          <label>Email:</label>
          <input type="email" v-model="email" required />
        </div>
        <div>
          <label>Password:</label>
          <input type="password" v-model="password" required />
        </div>
        <button type="submit">Login</button>
        <p v-if="error">{{ error }}</p>
      </form>
    </div>
  </main>

  <footer></footer>
</template>

<script>
import cookie from 'js-cookie';
import { handleError } from 'vue';

export default {
  name: "Login",
  data() {
    return {
      email: '',
      password: '',
      error: null,
    };
  },
  methods: {
    async login() {
      try {
        const loginResponse = await this.$axios.post('/auth/login', { email: this.email, password: this.password });
        console.log('LOGIN', loginResponse)
        const token = loginResponse.data.role;
        if (token) {

          cookie.set('token', token);
          // await this.getDataUser(token);
          window.location.reload()
        } else {
          this.errorMessage = "Login failed: Token not received.";
        }
      } catch (err) {
        this.error = err.response?.data?.message || 'Invalid email or password';
      }
    },
    async getDataUser(token) {
      try {
        const profileResponse = await this.$axios.get("/users", {
          headers: { Authorization: `Bearer ${token}` },
        });
        const user = profileResponse.data;
        this.login( {user, token} )
        this.$router.push( { name: 'Users'} )
      } catch(e) {
        if (e.response && e.response.status === 401) {
          this.error = "Unauthorized access - please log in again.";
          this.logout();
        } else {
          handleError(e);
        }
      }
    }
  }
};
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
.error {
  color: red;
}
</style>
