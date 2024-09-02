<template>
  <header>
    
  </header>
  <main>
    <div class="form-container">
      <h1 class="text-center white">Form Edit User</h1>
      <form @submit.prevent="editUser">
        <div class="form-group">
          <label for="email">Email:</label>
          <input type="email" v-model="form.email" id="email" class="form-control" required />
        </div>
        <div class="form-group">
          <label for="name">Full Name:</label>
          <input type="text" v-model="form.name" id="name" class="form-control" required />
        </div>
        <div class="form-group">
          <label for="password">Password:</label>
          <input type="password" v-model="form.password" id="password" class="form-control" required />
        </div>
        <div class="form-group">
          <label for="phoneNumber">Phone Number:</label>
          <input type="text" v-model="form.phoneNumber" id="phoneNumber" class="form-control" required />
        </div>
        <div class="form-group">
          <label for="role">Role:</label>
          <select v-model="form.role" id="role" class="form-control" required>
            <option value="" disabled>Select Role</option>
            <option v-for="role in roles" :key="role" :value="role">
              {{ role }}
            </option>
          </select>
        </div>
        <div class="form-group">
          <label for="department">Department:</label>
          <select v-model="form.department" id="department" class="form-control" required>
            <option value="" disabled>Select Department</option>
            <option v-for="department in departments" :key="department" :value="department">
              {{ department }}
            </option>
          </select>
        </div>

        <div class="button-group">
          <button type="button" class="btn btn-secondary" @click="cancelEdit">Cancel</button>
          <button type="submit" class="btn btn-primary">Save</button>
        </div>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      </form>
    </div>
  </main>
  <footer></footer>
</template>

<script>
export default {
  name: 'EditUser',
  data() {
    return {
      form: {
        id: '',
        email: '',
        name: '',
        password: '',
        phoneNumber: '',
        department: '',
        role: ''
      },
      errorMessage: '',
      departments: [
        'OPERATION',
        'FINANCE',
        'FUNDING',
        'ITSUPPORT',
        'DIRECTOR',
        'ITDEVELOPER',
        'MANRISK',
        'GAOPERATION',
        'SUPERADMIN'
      ],
      roles: [
        'ADMIN',
        'USER',
        'SUPPORT'
      ]
    };
  },
  created() {
    this.fetchUserData();
  },
  methods: {
    cancelEdit() {
      this.$router.push({ name: "Users" });
    },
    fetchUserData() {
      const id = this.$route.params.id;
      this.$axios
        .get(`/users/${id}`)
        .then((response) => {
          const user = response.data;
          this.form.id = user.id;
          this.form.email = user.email;
          this.form.name = user.name;
          this.form.password = user.password;
          this.form.phoneNumber = user.phoneNumber;
          this.form.department = user.department;
          this.form.role = user.role;
        })
        .catch((error) => {
          this.errorMessage = "Error fetching user data: " + error.message;
        });
    },
    editUser() {
      this.$axios
        .put(`/users/${this.form.id}`, this.form)
        .then(() => {
          this.$router.push({ name: "Users" }).then(() => {
            alert("User data updated successfully.");
          });
        })
        .catch((error) => {
          this.errorMessage = "Error updating user data: " + error.message;
        });
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

input, select {
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
