<template>
  <div id="app" class="login-page">
    <div class="background-overlay">
      <div class="ticker">
        <div class="quote-ticker">
          <span>
            "The magic of a helpdesk isn't just in solving problems, but in
            understanding people." - Anonymous
          </span>
        </div>
      </div>
    </div>
    <div class="login-container">
      <div class="navbar-brand">
        <a href="">
          <img src="@/assets/img/LOGO HIKP.png" alt="Logo" class="navbar-logo" />
        </a>
      </div>
      <h2>Welcome to Helpdesk</h2>
      <form @submit.prevent="handleLogin">
        <div class="input-group">
          <label>Email</label>
          <input type="email" v-model="email" @input="validateEmail" required placeholder="Enter your email" />
          <div v-if="emailError" class="error">{{ emailError }}</div>
        </div>
        <div class="input-group">
          <label>Password</label>
          <div class="control has-icons-right">
            <input :type="showPassword ? 'text' : 'password'" v-model="password" @input="validatePassword" required
              placeholder="Enter your password" class="input" />
            <span class="icon is-right has-text-primary is-clickable" @click="togglePasswordVisibility">
              <i :class="showPassword ? 'mdi mdi-eye-off mdi-24px' : 'mdi mdi-eye mdi-24px'"></i>
            </span>
          </div>
          <!-- Password error message -->
          <div v-if="passwordError" class="error">{{ passwordError }}</div>
        </div>

        <div class="button-group">
          <button type="button" @click="resetFields" class="btn secondary">
            Reset
          </button>
          <button type="submit" class="btn primary">Login</button>
        </div>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      </form>
      <p class="register-link">
        Don't have an account?
        <router-link to="/register">Register here</router-link>
      </p>
    </div>
  </div>
</template>

<script>
import { mapActions } from "vuex";
import Swal from "sweetalert2";
import cookie from "js-cookie";

export default {
  name: "HelpdeskLogin",
  data() {
    return {
      email: "",
      password: "",
      emailError: "",
      passwordError: "",
      errorMessage: "",
      showPassword: false // Toggles between password visibility
    };
  },
  methods: {
    ...mapActions(["checkAuth", "login", "logout"]),

    resetFields() {
      this.email = "";
      this.password = "";
      this.emailError = "";
    },
    togglePasswordVisibility() {
      this.showPassword = !this.showPassword; // Toggle between text and password type
    },
    validateEmail() {
      this.emailError = "";
      const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!this.email) {
        this.emailError = "This field is required.";
      } else if (this.email.length < 8 || this.email.length > 30) {
        this.emailError = "Email must be between 8 and 30 characters.";
      } else if (!this.email.includes("@")) {
        this.emailError = "Email must contain '@'.";
      } else if (!emailPattern.test(this.email)) {
        this.emailError = "Please enter a valid email address.";
      }
    },
    validatePassword() {
      this.passwordError = "";

      if (this.password.length < 8) {
        this.passwordError = 'Password must be at least 8 characters long';
      } else if (!/[A-Z]/.test(this.password)) {
        this.passwordError = 'Password must contain at least one uppercase letter';
      } else if (!/[a-z]/.test(this.password)) {
        this.passwordError = 'Password must contain at least one lowercase letter';
      } else if (!/[0-9]/.test(this.password)) {
        this.passwordError = 'Password must contain at least one number';
      } else if (!/[!@#$_%^&*]/.test(this.password)) {
        this.passwordError = 'Password must contain at least one special character (!@#$%^&*)';
      } else {
        this.passwordError = ''; // Clear the error if the password is valid
      }
    },
    async handleLogin() {
      this.validateEmail();
      this.validatePassword();
      if (this.emailError || this.passwordError) {
        return;
      }
      try {
        console.log("Attempting login with:", {
          email: this.email,
          password: this.password,
        });

        const loginResponse = await this.$axios.post("/auth/login", {
          email: this.email,
          password: this.password,
        });

        Swal.fire({
          title: "Login Successful",
          text: "You have successfully logged in.",
          icon: "success",
          confirmButtonText: "OK",
        });

        console.log("Login response:", loginResponse.data);

        const { token } = loginResponse.data;
        this.fetchUserData(token);
      } catch (error) {
        console.error(
          "Login failed:",
          error.response ? error.response.data : error.message
        );
        Swal.fire({
          title: "Login Failed",
          text:
            error.response?.data?.message || "Login failed. Please try again.",
          icon: "error",
          confirmButtonText: "Retry",
        });
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

        const user = users.find((u) => u.email === this.email);

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
        Swal.fire({
          title: "Error",
          text: "Failed to retrieve user data. Please try again.",
          icon: "error",
          confirmButtonText: "OK",
        });
        if (error.response && error.response.status === 401) {
          this.logout();
        }
      }
    },
  },
};
</script>

<style lang="scss" scoped>
@import url("https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap");

$primary-color: #a3d2e3; // Soft Blue
$secondary-color: #f7cac9; // Pale Peach
$accent-color: #c0e8e0; // Mint Green
$error-color: #ffb3b3; // Soft Red
$background-color: #fefefe; // Off-White
$text-color: #19b16f; // Charcoal Gray
$input-background: #f0f0f0; // Light Gray
$border-radius: 15px; // More rounded corners
$box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);

.login-page {
  display: flex;
  height: 95vh;
  font-family: "Poppins", sans-serif;

  .background-overlay {
    flex: 5; // 60% of the screen
    // top: 0;
    // left: 0;
    // right: 0;
    // bottom: 0;
    background: url("https://images.unsplash.com/photo-1507525428034-b723cf961d3e?ixlib=rb-1.2.1&auto=format&fit=crop&w=1950&q=80") no-repeat center center/cover;
    opacity: 0.6;
    z-index: 1;
    position: relative;
    display: flex;
    align-items: flex-end;
    padding: 20px;
    animation: backgroundMove 20s ease-in-out infinite;


  }

  .login-container {
    flex: 5; // 40% of the screen
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    background: $background-color;
    padding: 40px;
    border-radius: $border-radius;
    box-shadow: $box-shadow;
    max-width: 300px;
    width: 100%;
    margin: 0 15px 0 15px;

    h2 {
      text-align: center;
      margin-bottom: 30px;
      color: $text-color;
      font-size: 1.8rem;
    }

    form {
      width: 100%;
      display: flex;
      flex-direction: column;

      .input-group {
        display: flex;
        flex-direction: column;
        margin-bottom: 20px;

        label {
          margin-bottom: 5px;
          color: $text-color;
          font-weight: 600;
          font-size: 1rem;
          display: block;
        }

        input {
          padding: 12px;
          border: none;
          border-radius: $border-radius;
          background: $input-background;
          font-size: 1rem;
          transition: background 0.3s, box-shadow 0.3s;

          &:focus {
            background: lighten($input-background, 5%);
            outline: none;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            display: block;
          }
        }

        /* Positioning the eye icon */
        .mdi {
          position: relative;
          right: -190px;
          top: -50%;
          transform: translateY(-50%);
          cursor: pointer;
          color: $text-color;

          i {
            font-size: 24px;
          }
        }
    }

    .button-group {
      display: flex;
      justify-content: space-between;

      .btn {
        padding: 12px 20px;
        border: none;
        border-radius: $border-radius;
        font-size: 1rem;
        cursor: pointer;
        transition: background 0.3s, transform 0.3s;

        &.primary {
          background: $primary-color;
          color: #fff;

          &:hover {
            background: darken($primary-color, 10%);
            transform: translateY(-2px);
          }
        }

        &.secondary {
          background: $accent-color;
          color: #fff;

          &:hover {
            background: darken($accent-color, 10%);
            transform: translateY(-2px);
          }
        }
      }
    }

    .error {
      margin-top: 15px;
      color: $error-color;
      text-align: center;
      font-weight: 150;
    }
  }

  .register-link {
    margin-top: 20px;
    text-align: center;
    color: $text-color;

    a {
      color: $primary-color;
      font-weight: 600;
      text-decoration: none;
      transition: color 0.3s;

      &:hover {
        color: darken($primary-color, 10%);
      }
    }
  }
}

.ticker {
  position: absolute;
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  animation: positionChange 25s linear infinite;
  transform: translateY(-10px);

  .quote-ticker {
    display: inline-block;
    padding: 10px 20px;
    background: rgba(0, 0, 0, 0.4);
    color: white;
    font-size: 16px;
    border-radius: 12px;
    animation: mottoScroll 20s linear infinite;
  }
}

.navbar-brand {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px;
  border-radius: $border-radius;
  box-shadow: $box-shadow;
  transition: background-color 0.3s ease;

  a {
    display: flex;
    justify-content: center;
    align-items: center;

    .navbar-logo {
      max-width: 100px; // Adjust this value as necessary
      max-height: 50px; // Adjust this value as necessary
      width: auto;
      height: auto;
      object-fit: contain; // Ensures the image scales proportionally
      transition: transform 0.3s ease, opacity 0.3s ease;

      &:hover {
        transform: scale(1.1); // Slight zoom on hover
        opacity: 0.8; // Slight transparency on hover
      }
    }
  }
}


}

/* Animations */
@keyframes backgroundMove {
  0% {
    background-position: 100% 100%;
  }

  100% {
    background-position: 0% 0%;
  }
}

@keyframes mottoScroll {
  0% {
    transform: translateX(100%);
  }

  100% {
    transform: translateX(-100%);
  }
}

@keyframes positionChange {

  0%,
  100% {
    top: 80%;
  }

  33% {
    top: 10%;
  }

  66% {
    top: 45%;
  }
}

@media (max-width: 1024px) {
  .login-container {
    padding: 30px;
    max-width: 90%;
  }

  .ticker .quote-ticker {
    font-size: 15px;
  }
}

@media (max-width: 768px) {
  .login-page {
    flex-direction: column;
    position: relative;
  }

  .background-overlay {
    height: 100%;
    flex: none;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    z-index: 1; // Ensure the background is behind the form
  }

  .login-container {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 90%;
    height: auto;
    padding: 20px;
    background: rgba(255, 255, 255, 0.9); // Slightly transparent background
    z-index: 2; // Ensure the form is above the background

    h2 {
      font-size: 1.5rem;
    }

    .input-group {
      margin-bottom: 20px;

      label {
        margin-bottom: 5px;
        color: $text-color;
        font-weight: 600;
        font-size: 1rem;
        display: block;
      }

      input {
        font-size: 0.9rem;
        padding: 10px;
        border-radius: $border-radius;
        background: $input-background;
        transition: background 0.3s, box-shadow 0.3s;

        &:focus {
          background: lighten($input-background, 5%);
          outline: none;
          box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
      }
    }

    .button-group {
      display: flex;
      flex-direction: column;
      gap: 10px;

      .btn {
        padding: 10px 18px;
        width: 100%;
        font-size: 1rem;
      }
    }
  }
}

@media (max-width: 480px) {
  .login-container {
    margin: auto;
    padding: 3px;

    h2 {
      font-size: 1.25rem;
    }

    .input-group {
      input {
        font-size: 0.8rem;
        padding: 8px;
      }
    }

    .button-group {
      flex-direction: column;

      .btn {
        width: 100%;
        margin-bottom: 10px;
      }
    }
  }

  .ticker .quote-ticker {
    font-size: 12px;
  }
}
</style>
