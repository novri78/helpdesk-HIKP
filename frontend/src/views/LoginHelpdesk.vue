<template>
  <header></header>
  <main>
    <div class="background-image">
      <div class="ticker">
        <span class="ticker-text">
          Learning to write programs stretches your mind, and helps you think
          better, creates a way of thinking about things that I think is helpful
          in all domains. - Bill Gates
        </span>
      </div>
    </div>
    <div class="login-container">
      <h2>Helpdesk Login</h2>
      <form @submit.prevent="handleLogin">
        <div>
          <label>Email:</label>
          <input type="email" v-model="email" required />
        </div>
        <div>
          <label>Password:</label>
          <input type="password" v-model="password" required />
        </div>
        <button type="button" @click="resetFields">Reset</button>
        <button type="submit">Login</button>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      </form>
    </div>
  </main>
  <footer></footer>
</template>

<script>
import { mapState, mapActions } from 'vuex';

export default {
  name: 'HelpdeskLogin',
  data() {
    return {
      email: '',
      password: '',
    };
  },
  computed: {
    ...mapState(['errorMessage']),
  },
  methods: {
    ...mapActions(['authenticateUser']),
    resetFields() {
      this.email = '';
      this.password = '';
    },
    async handleLogin() {
      const loginSuccessful = await this.authenticateUser({ email: this.email, password: this.password });
      if (loginSuccessful) {
        this.$router.push({ name: 'Users' });
      }
    },
  },
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
