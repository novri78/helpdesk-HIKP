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
      <p>
        Don't have an account?
        <router-link to="/register">Register here</router-link>
      </p>
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
      try {
        console.log("Attempting login with:", { email: this.email, password: this.password });

        const loginResponse = await this.$axios.post("/auth/login", {
          email: this.email,
          password: this.password,
        });

        alert("Login successful");
        console.log("Login response:", loginResponse.data);

        const { token } = loginResponse.data;
        this.fetchUserData(token);

      } catch (error) {
        console.error("Login failed:", error.response ? error.response.data : error.message);
        this.errorMessage = error.response?.data?.message || "Login failed. Please try again.";
      }
    },

    async fetchUserData(token) {
      try {
        const profileResp = await this.$axios.get("/users", {
          headers: { Authorization: `Bearer ${token}` },
        });

        const users = profileResp.data;
        if (!Array.isArray(users) || users.length === 0) {
          throw new Error("No users found");
        }
        
        const user = users.find(u => u.email === this.email);

        this.login({ user, token });

        if (user.role === "ADMIN") {
          this.$router.push({ name: "Users" });
        } else if (["USER", "SUPPORT"].includes(user.role)) {
          this.$router.push({ name: "Dashboard" });
        } else {
          this.errorMessage = "Unauthorized role.";
          this.logout();
        }

      } catch (error) {
        console.error("Failed to fetch user data:", error);
        this.errorMessage = "Failed to retrieve user data. Please try again.";
        if (error.response && error.response.status === 401) {
          this.logout();
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
