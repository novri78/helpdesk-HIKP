<template>
  <header>
    <h1 class="text-center white">Form Approve User {{ form.id }}</h1>
  </header>
  <main>
    <div class="form-container">
      <form @submit.prevent="approveUser">
        <div class="form-group">
          <label for="email">Email:</label>
          <input
            type="email"
            v-model="form.email"
            id="email"
            class="form-control"
            required
            readonly
          />
        </div>
        <div class="form-group">
          <label for="fullName">Full Name:</label>
          <input
            type="text"
            v-model="form.name"
            id="fullName"
            class="form-control"
            required
            readonly
          />
        </div>
        <div class="form-group">
          <label for="password">Password:</label>
          <input
            type="password"
            v-model="form.password"
            id="password"
            class="form-control"
            required
            readonly
          />
        </div>
        <div class="form-group">
          <label for="phoneNumber">Phone Number:</label>
          <input
            type="text"
            v-model="form.phoneNumber"
            id="phoneNumber"
            class="form-control"
            required
            readonly
          />
        </div>
        <div class="form-group">
          <label for="role">Role:</label>
          <select
            v-model="form.role"
            id="role"
            class="form-control"
            required
            disabled
          >
            <option v-for="role in roles" :key="role" :value="role">
              {{ role }}
            </option>
          </select>
        </div>
        <div class="form-group">
          <label for="department">Department:</label>
          <select
            v-model="form.department"
            id="department"
            class="form-control"
            required
            disabled
          >
            <option
              v-for="department in departments"
              :key="department"
              :value="department"
            >
              {{ department }}
            </option>
          </select>
        </div>
        <div class="form-group">
          <label for="isApproved">Approval Status:</label>
          <input type="checkbox" v-model="form.isApproved" id="isApproved" />
        </div>

        <div class="button-group">
          <button type="button" class="btn btn-secondary" @click="cancelEdit">
            Cancel
          </button>
          <button
            type="submit"
            class="btn btn-primary"
            :disabled="isSubmitting"
          >
            <span v-if="!isSubmitting">Save</span>
            <span v-if="isSubmitting">Saving...</span>
          </button>
        </div>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      </form>
    </div>
  </main>
  <footer></footer>
</template>

<script>
export default {
  name: "ApproveNewUser",
  data() {
    return {
      form: {
        id: "",
        email: "",
        name: "",
        password: "",
        phoneNumber: "",
        department: "",
        role: "",
        isApproved: false,
      },
      isSubmitting: false,
      errorMessage: "",
      departments: [
        "OPERATION",
        "FINANCE",
        "FUNDING",
        "ITSUPPORT",
        "DIRECTOR",
        "ITDEVELOPER",
        "GAOPERATION",
        "SUPERADMIN",
      ],
      roles: ["USER", "SUPPORT"],
    };
  },
  methods: {
    cancelEdit() {
      this.$router.push({ name: "Users" });
    },
    fetchUserData() {
      this.isSubmitting = true;
      const id = this.$route.params.id;
      this.$axios
        .get(`/users/${id}`)
        .then((response) => {
          const user = response.data;
          this.form = { ...user, isApproved: user.isApproved || false };
        })
        .catch((error) => {
          this.errorMessage = "Failed to fetch user: " + error.message;
        })
        .finally(() => {
          this.isSubmitting = false;
        });
    },
    approveUser(id) {
      this.isSubmitting = true; // Mengatur status pengiriman agar UI dapat menampilkan loading indicator atau mencegah pengiriman ganda
      this.$axios
        .put(`/users/${this.form.id}/approve`, this.form) // Mengirimkan request PUT ke backend untuk menyetujui pengguna
        .then(() => {
          alert("User approved successfully."); // Menampilkan pesan berhasil kepada pengguna
          this.errorMessage = ""; // Mengosongkan pesan error jika ada
          this.$router.push({ name: "Users" }); // Mengarahkan pengguna kembali ke halaman daftar pengguna
        })
        .catch((error) => {
          this.errorMessage = `Error approving user: ${error.message}`; // Menangani dan menampilkan error jika terjadi masalah saat menyetujui pengguna
        })
        .finally(() => {
          this.isSubmitting = false; // Mengatur kembali status pengiriman
        });
    },
    fetchUsers() {
      this.$axios
        .get("/users")
        .then((response) => {
          this.users = response.data;
        })
        .catch((error) => {
          console.error("Error fetching users:", error);
        });
    },
  },
  created() {
    this.fetchUserData();
  },
};
</script>

<style scoped>
* {
  margin: 0;
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
