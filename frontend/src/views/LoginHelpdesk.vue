<template>
  <header>
    <h2>Helpdesk Login</h2>
  </header>

  <main>
    <div class="login-container">
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
export default {
  name: "Login",
  data() {
    return {
      email: "",
      password: "",
      error: "",
    };
  },
  methods: {
    async login( ) {
      try {
        const resp = await $store.dispatch()
      } catch (e) {
        this.error = 'Invalid email or password';
      }
    }
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
