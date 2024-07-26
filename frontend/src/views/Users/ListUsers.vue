<template>
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
          <th>Departemen</th>
          <th>Email</th>
          <th>Full Name</th>
          <th>Phone</th>
          <th>Position</th>
          <th>User Name</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(user, index) in users" :key="user.id">
          <td>{{ index + 1 }}</td>
          <td>{{ user.iD }}</td>
          <td>{{ user.dept }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.fullName }}</td>
          <td>{{ user.phone }}</td>
          <td>{{ user.position }}</td>
          <td>{{ user.username }}</td>
          <td>
            <button class="btn btn-secondary btn-sm" @click="routeToEditUser(user.id)">
              Edit
            </button>
            <button class="btn btn-danger btn-sm" @click="confirmDelete(user.id)">
              Delete
            </button>
          </td>
        </tr>
      </tbody>
    </table>
    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </main>
  <footer></footer>
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
            dept: user.department, // Adjust this field name according to your API response
            email: user.email,
            fullName: user.full_name, // Adjust this field name according to your API response
            phone: user.phone_number, // Adjust this field name according to your API response
            position: user.position, // Adjust this field name according to your API response
            username: user.username // Adjust this field name according to your API response
          }));
          console.log("Mapped Users", this.users); // Log the mapped users
        })
        .catch(error => {
          this.errorMessage = "Failed to fetch users: " + error.message;
        });
    },
    routeToEditUser(id) {
      this.$router.push({ name: "EditUser", params: { id } });
    },
    confirmDelete(id) {
      if (confirm("Are you sure you want to delete this user?")) {
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
