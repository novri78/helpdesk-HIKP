<template>
  <div class="users-container">
    <!-- Header Section -->
    <header class="users-list">
      <h1>Users</h1>
    </header>

    <!-- Add User Button -->
    <button class="btn btn-primary mb-3" @click="routeToAddUser">
      Add User
    </button>

    <!-- Responsive Table -->
    <main class="table-responsive">
      <table class="table">
        <thead>
          <tr>
            <th>No.</th>
            <th>Id</th>
            <th>Email</th>
            <th>Full Name</th>
            <th>Department</th>
            <th>Phone</th>
            <th>Role</th>
            <th>Approval Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(user, index) in users" :key="user.id">
            <td>{{ index + 1 }}</td>
            <td>{{ user.id }}</td>
            <td>{{ user.email }}</td>
            <td>{{ user.name }}</td>
            <td>{{ user.department }}</td>
            <td>{{ user.phoneNumber }}</td>
            <td>{{ user.role }}</td>
            <td>{{ user.isApproved ? "Approved" : "Pending" }}</td>
            <td>
              <!-- Dropdown Button for Actions -->
              <div class="dropdown">
                <button
                  class="btn btn-secondary btn-sm dropdown-toggle"
                  type="button"
                  id="dropdownMenuButton{{ user.id }}"
                  data-bs-toggle="dropdown"
                  aria-expanded="false"
                >
                  Actions
                </button>
                <ul
                  class="dropdown-menu"
                  :aria-labelledby="'dropdownMenuButton' + user.id"
                >
                  <li>
                    <a
                      class="dropdown-item"
                      href="#"
                      @click.prevent="routeToApproveUser(user.id)"
                      >Approve</a
                    >
                  </li>
                  <li>
                    <a
                      class="dropdown-item"
                      href="#"
                      @click.prevent="routeToEditUser(user.id)"
                      >Edit</a
                    >
                  </li>
                  <li>
                    <a
                      class="dropdown-item text-danger"
                      href="#"
                      @click.prevent="confirmDelete(user.id)"
                      >Delete</a
                    >
                  </li>
                </ul>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <!-- Error Message -->
      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    </main>

    <!-- Footer Section (optional, if needed) -->
    <footer></footer>
  </div>
</template>

<script>
export default {
  name: "Users",
  data() {
    return {
      users: [], // Array to hold the list of users
      errorMessage: "", // To store any error messages
    };
  },
  mounted() {
    this.fetchUsers(); // Fetch users when the component is mounted
  },
  methods: {
    // Navigate to Add User page
    routeToAddUser() {
      this.$router.push({ name: "AddUser" });
    },

    // Fetch users from the server
    fetchUsers() {
      this.$axios
        .get("/users")
        .then((response) => {
          // Filter users to only include those with isDeleted: false
          this.users = response.data
            .filter((user) => !user.isDeleted)
            .map((user) => ({
              ...user,
              isApproved: user.isApproved, // Map other necessary properties
            }));
          console.log("Fetched Users", this.users); // Log the mapped users
        })
        .catch((error) => {
          this.errorMessage = "Failed to fetch users: " + error.message;
          console.error("Error fetching users:", error);
        });
    },

    // Navigate to Edit User page
    routeToEditUser(id) {
      this.$router.push({ name: "EditUser", params: { id } });
    },

    // Navigate to Approve User page
    routeToApproveUser(id) {
      this.$router.push({ name: "ApproveUser", params: { id } });
    },

    // Confirm and delete a user
    confirmDelete(id) {
      if (confirm("Are you sure you want to delete this user?")) {
        this.$axios
          .delete(`/users/${id}`)
          .then(() => {
            this.fetchUsers(); // Refresh the list after deletion
          })
          .catch((error) => {
            this.errorMessage = "Failed to delete user: " + error.message;
          });
      }
    },
  },
};
</script>

<style scoped lang="scss">
@import url("https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap");

* {
  margin: 20px;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: "Roboto", sans-serif;
  background-color: #f4f4f4;
  color: #333;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  padding: 20px;
  box-sizing: border-box;
}

.users-container {
  width: 100%;
  transition: width 0.3s ease-in-out;
  padding: 20px;
  box-sizing: border-box;
}

.table-responsive {
  overflow-x: auto;
}

.table {
  width: 100%;
  margin-bottom: 1rem;
  background-color: #fff;
  border-collapse: collapse;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.table th,
.table td {
  padding: 0.75rem;
  vertical-align: top;
  border-top: 1px solid #dee2e6;
  text-align: left;
}

.table thead th {
  background-color: #f8f9fa;
  text-transform: uppercase;
}

.table tbody tr:nth-of-type(odd) {
  background-color: #f2f2f2;
}

.error {
  color: red;
  text-align: center;
  margin-top: 20px;
}

.btn {
  margin-right: 8px;
  border: none;
  padding: 10px 20px;
  font-size: 14px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  &-primary {
    margin-left: 38px;
    background-color: #007bff;
    color: white;
    &:hover {
      background-color: #0056b3;
    }
  }
  &-secondary {
    background-color: #6c757d;
    color: white;
    &:hover {
      background-color: #5a6268;
    }
  }
  &-danger {
    background-color: #dc3545;
    color: white;
    &:hover {
      background-color: #c82333;
    }
  }
}

/* Style for the dropdown action button */
.btn-group {
  position: relative;
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  left: 0;
  z-index: 1000;
  display: none;
  float: left;
  min-width: 100px;
  padding: 5px 0;
  margin: 0;
  font-size: 14px;
  text-align: left;
  list-style: none;
  background-color: #fff;
  border: 1px solid rgba(0, 0, 0, 0.15);
  border-radius: 4px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.175);

  &.show {
    display: block;
  }
}

.dropdown-item {
  display: block;
  width: 70%;
  padding: 3px 20px;
  clear: both;
  font-weight: 400;
  color: #333;
  text-align: inherit;
  white-space: nowrap;
  background-color: transparent;
  border: 0;

  &:hover {
    color: #fff;
    background-color: #007bff;
  }

  &.text-danger {
    color: #dc3545;
  }
}

.main-content.sidebar-open .users-container {
  width: calc(100% - 10px); // Adjust according to sidebar width
}

/* Responsive Design */
@media (max-width: 1000px) {
  body {
    padding: 15px;
  }

  .users-container {
    padding: 15px;
  }
}

@media (max-width: 768px) {
  .main-content.sidebar-open {
    margin-left: 0;
  }

  .users-container {
    width: 100%;
  }

  .table th,
  .table td {
    padding: 0.5rem;
  }

  .table thead {
    display: none;
  }

  .table tbody tr {
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


