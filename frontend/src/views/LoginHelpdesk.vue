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
import { mapActions } from "vuex";
import cookie from "js-cookie";

export default {
  name: "HelpdeskLogin",
  data() {
    return {
      email: "",
      password: "",
      errorMessage: "",
    };
  },
  methods: {
    ...mapActions(["checkAuth", "login", "logout"]),
    resetFields() {
      this.email = "";
      this.password = "";
    },
    async handleLogin() {
      // Updated method name
      try {
        console.log("Submitting login request with:", {
          email: this.email,
          password: this.password,
        });
        const loginResponse = await this.$axios.post("/auth/login", {
          email: this.email,
          password: this.password,
        });
        
        // Handle successful login
        console.log("Login successful:", loginResponse.data);

        // Store the token in Vuex
        const { token } = loginResponse.data;
        this.getDataUser(token); // Retrieve user data after login

      } catch (error) {
        console.error(
          "Login failed with error:",
          error.response ? error.response.data : error.message
        );
        this.errorMessage = error.response?.data?.message || "Login failed.";
      }
    },
    async getDataUser(token) {
      try {
        const profileResp = await this.$axios.get("/users", {
          headers: { Authorization: `Bearer ${token}` },
        });
        const user = profileResp.data;
        console.log('info data: ', user);

        this.login({ user, token }); // Dispatch Vuex login action
        this.$router.push({ name: "Users" });
      } catch (error) {
        if (error.response && error.response.status === 401) {
          this.errorMessage = "Unauthorized access - please log in again.";
          this.logout();
        } else {
          console.error(error);
          this.errorMessage = "Failed to retrieve user data.";
        }
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
