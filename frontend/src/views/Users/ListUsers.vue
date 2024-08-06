<template>
  <div class="users-container">
    <header class="users-list">
      <h1>Users</h1>
    </header>
    <button class="btn btn-primary mb-3" @click="routeToAddUser">Add User</button>
    <main class="table-responsive">
      <table class="table">
        <thead>
          <tr>
            <th>No.</th>
            <th>Id</th>
            <th>Email</th>
            <th>Full Name</th>
            <th>Password</th>
            <th>Department</th>
            <th>Phone</th>
            <th>Role</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(user, index) in users" :key="user.id">
            <td>{{ index + 1 }}</td>
            <td>{{ user.iD }}</td>
            <td>{{ user.email }}</td>
            <td>{{ user.fullName }}</td>
            <td>{{ user.pass }}</td>
            <td>{{ user.dept }}</td>
            <td>{{ user.phone }}</td>
            <td>{{ user.roles }}</td>
            <td>
              <button class="btn btn-secondary btn-sm" @click="routeToEditUser(user.iD)">
                Edit
              </button>
              <button class="btn btn-danger btn-sm" @click="confirmDelete(user.iD)">
                Delete
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    </main>
    <footer></footer>
  </div>
</template>

<script>
export default {
  name: "Users",
  data() {
    return {
      users: [],
      errorMessage: "",
    };
  },
  mounted() {
    this.fetchUsers();
  },
  methods: {
    routeToAddUser() {
      this.$router.push({ name: "AddUser" });
    },
    fetchUsers() {
      this.$axios
        .get('/users')
        .then(res => {
          console.log("API Response", res.data); // Log the entire response
          this.users = res.data.map(user => ({
            iD: user.id,
            email: user.email,
            pass: user.password,
            fullName: user.fullName, // Adjust this field name according to your API response
            phone: user.phoneNumber, // Adjust this field name according to your API response
            dept: user.department, // Adjust this field name according to your API response
            roles: user.role // Adjust this field name according to your API response
          }));
          console.log("Mapped Users", this.users); // Log the mapped users
        })
        .catch(error => {
          this.errorMessage = "Failed to fetch users: " + error.message;
        });
    },
    routeToEditUser(id) {
      this.$router.push({ name: "EditUser",  params: { id }  });
      console.log("no id", id);
    },
    confirmDelete(id) {
      if (confirm("Are you sure you want to delete this user no ID: " + id)) {
        this.$axios
          .delete(`/users/${id}`)
          .then(() => {
            this.fetchUsers(); // Refresh the list after deletion
          })
          .catch(error => {
            this.errorMessage = "Failed to delete user: " + error.message;
          });
      }
    }
  }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap');

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Roboto', sans-serif;
  background-color: #f4f4f4;
  color: #333;
}

.users-container {
  padding: 20px;
}

header.users-list {
  text-align: center;
  margin-bottom: 20px;
}

button {
  font-family: 'Roboto', sans-serif;
}

.table-responsive {
  overflow-x: auto;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  background: white;
}

.table {
  width: 100%;
  border-collapse: collapse;
  margin: 0;
}

.table th, .table td {
  padding: 12px 15px;
  border-bottom: 1px solid #ddd;
  text-align: left;
}

.table th {
  background-color: #007bff;
  color: white;
  text-transform: uppercase;
}

.table td {
  background-color: #fff;
}

.table td button {
  margin-right: 5px;
}

.error {
  color: red;
  text-align: center;
  margin-top: 20px;
}

/* Buttons */
.btn {
  border: none;
  padding: 10px 20px;
  font-size: 14px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-primary {
  background-color: #007bff;
  color: white;
}

.btn-primary:hover {
  background-color: #0056b3;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
}

.btn-secondary:hover {
  background-color: #5a6268;
}

.btn-danger {
  background-color: #dc3545;
  color: white;
}

.btn-danger:hover {
  background-color: #c82333;
}

/* 3D Effects */
.table, .btn {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.table th, .table td, .btn {
  transition: transform 0.2s;
}

.table th:hover, .table td:hover, .btn:hover {
  transform: translateY(-2px);
}

/* Responsive Design */
@media (max-width: 768px) {
  .table thead {
    display: none;
  }
  
  .table tr {
    display: block;
    margin-bottom: 10px;
    border-bottom: 2px solid #ddd;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    padding: 10px;
  }
  
  .table td {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 0;
    font-size: 12px;
    border-bottom: 1px solid #ddd;
  }

  .table td::before {
    content: attr(data-label);
    font-weight: bold;
    width: 50%;
    text-transform: uppercase;
  }

  .table td:last-child {
    border-bottom: none;
  }

  .btn {
    font-size: 12px;
    padding: 8px 15px;
  }
}

@media (max-width: 480px) {
  .table td {
    font-size: 10px;
  }

  .btn {
    font-size: 10px;
    padding: 6px 10px;
  }
}
</style>

