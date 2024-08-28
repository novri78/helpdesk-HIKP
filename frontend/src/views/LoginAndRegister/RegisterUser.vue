<template>
  <header></header>
  <main>
    <div class="form-container">
      <h2>Register</h2>
      <form @submit.prevent="registerUser">
        <div class="form-group">
          <label for="email">Email:</label>
          <input
            type="email"
            v-model="form.email"
            id="email"
            class="form-control"
            required
          />
        </div>
        <div class="form-group">
          <label for="name">Full Name:</label>
          <input
            type="text"
            v-model="form.name"
            id="name"
            class="form-control"
            required
          />
        </div>
        <div class="form-group">
          <label>Password</label>
          <input
            type="password"
            v-model="form.password"
            @input="validatePassword"
            required
            placeholder="Enter your password"
          />
          <div v-if="passwordError" class="error">{{ passwordError }}</div>
        </div>
        <div class="form-group">
          <label for="phoneNumber">Phone Number:</label>
          <input
            type="phoneNumber"
            v-model="form.phoneNumber"
            id="phoneNumber"
            class="form-control"
            required
          />
        </div>
        <div class="form-group">
          <label for="role">ROLE:</label>
          <select v-model="form.role" id="role" class="form-control" required>
            <option value="" disabled>Select Role</option>
            <option v-for="role in roles" :key="role" :value="role">
              {{ role }}
            </option>
          </select>
        </div>
        <div class="form-group">
          <label for="department">DEPARTMENT:</label>
          <select
            v-model="form.department"
            id="department"
            class="form-control"
            required
          >
            <option value="" disabled>Select Department</option>
            <option
              v-for="department in departments"
              :key="department"
              :value="department"
            >
              {{ department }}
            </option>
          </select>
        </div>
        <div class="button-group">
          <button type="button" class="btn btn-secondary" @click="cancelAdd">
            Cancel
          </button>
          <button type="submit" class="btn btn-primary">Register</button>
        </div>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      </form>
    </div>
  </main>
  <footer></footer>
</template>

<script>
export default {
  name: "RegisterUser",
  data() {
    return {
      form: {
        email: "",
        name: "",
        password: "",
        role: "USER", // Default role
        department: "",
        phoneNumber: "",
      },
      passwordError: "",
      errorMessage: "",
      roles: ["USER"], // Restrict registration roles
      departments: ["OPERATION", "FINANCE", "FUNDING", "GAOPERATION"],
      submitted: false,
    };
  },
  methods: {
    cancelAdd() {
      this.$router.push({ name: "Login" });
    },
    validatePassword() {
      this.passwordError = "";

      if (this.password.length < 8) {
        this.passwordError = "Password must be at least 8 characters long.";
      } else if (!/\d/.test(this.password)) {
        this.passwordError = "Password must contain at least one digit.";
      } else if (!/[A-Za-z]/.test(this.password)) {
        this.passwordError = "Password must contain at least one letter.";
      }
    },
    registerUser() {
      this.submitted = true;
      this.validatePassword();
      if (this.emailError || this.passwordError) {
        return;
      }
      if (this.form) {
        this.$axios
          .post("/auth/register", {
            ...this.form,
            isApproved: false, // New users are not approved by default
          })
          .then(() => {
            alert("Registration successful! Please wait for admin approval.");
            this.resetForm();
            this.$router.push({ name: "Login" });
          })
          .catch((error) => {
            if (error.response && error.response.data) {
              // Cek jika error berasal dari server dan ada data yang bisa diambil
              this.errorMessage =
                error.response.data.message ||
                "Registration failed. Please try again.";
            } else {
              // Error dari sisi lain (misalnya network issue)
              this.errorMessage =
                "An unexpected error occurred. Please try again.";
            }
          });
      } else {
        alert("Please fill in all required fields.");
      }
    },
    resetForm() {
      this.form.email = "";
      this.form.password = "";
      this.form.name = "";
      this.form.phoneNumber = "";
      this.form.role = "USER";
      this.form.department = "";
      this.submitted = false;
    },
  },
};
</script>

<style scoped>
* {
  margin: 20px;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: Arial, sans-serif;
  background-color: #f4f4f4;
}

header {
  padding: 20px;
  text-align: center;
  background-color: #343a40;
  color: #fff;
}

main {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
}

.form-container {
  width: 90%;
  max-width: 600px;
  background: #fff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  color: #333;
}

input,
select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 16px;
}

.button-group {
  display: flex;
  justify-content: space-between;
}

button {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  color: #fff;
  cursor: pointer;
}

.btn-secondary {
  background-color: #6c757d;
}

.btn-primary {
  background-color: #007bff;
}

.error {
  color: red;
  margin-top: 10px;
  text-align: center;
}

/* Media Queries for Responsive Design */
@media (max-width: 768px) {
  .form-container {
    padding: 15px;
  }

  .form-group {
    margin-bottom: 10px;
  }

  button {
    width: 48%;
  }
}

@media (max-width: 480px) {
  .form-container {
    padding: 10px;
  }

  .button-group {
    flex-direction: column;
  }

  button {
    width: 100%;
    margin-bottom: 10px;
  }

  .button-group button:last-child {
    margin-bottom: 0;
  }
}
</style>