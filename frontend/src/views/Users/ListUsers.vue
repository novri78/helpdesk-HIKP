<template>
  <div class="users-container">
    <!-- Header Section -->
    <header class="users-list-header">
      <h1>User Management</h1>
      <!-- Add User Button -->
      <button class="btn btn-primary mb-3" @click="routeToAddUser">
        +User
      </button>
    </header>

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
  padding: 10px;
  background-color: #f9f9f9;

  .users-list-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    h2 {
      font-size: 24px;
      margin: 0;
      color: #333;
    }
    .btn-primary {
      padding: 10px 20px;
      font-size: 16px;
      background-color: #007bff;
      color: white;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      transition: background-color 0.3s;
      &:hover {
        background-color: #0056b3;
      }
    }
  }

.table-responsive {
    overflow-x: auto;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    border-radius: 10px;
    background: white;
    padding: 15px;
  }


.table {
    width: 100%;
    border-collapse: collapse;

    th,
    td {
      padding: 12px 15px;
      border-bottom: 1px solid #ddd;
      text-align: left;
    }

    th {
      background-color: #007bff;
      color: white;
      text-transform: uppercase;
    }

    td {
      background-color: #fff;
    }

    td button {
      margin-right: 5px;
      padding: 6px 12px;
      border: none;
      border-radius: 5px;
      font-size: 14px;
      cursor: pointer;
      transition: background-color 0.3s, transform 0.2s;
    }

    .btn-approve {
      background-color: #28a745;
      color: white;
    }
    .btn-edit {
      background-color: #ffc107;
      color: black;
    }
    .btn-delete {
      background-color: #dc3545;
      color: white;
    }

    td button:hover {
      transform: translateY(-2px);
    }
  }

.error {
  color: red;
  text-align: center;
  margin-top: 20px;
}

.btn {
  margin-right: 0px;
  border: none;
  padding: 10px 20px;
  font-size: 14px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  &-primary {
    margin-left: 15px;
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
  @media (max-width: 768px) {
    .table thead {
      display: none;
    }

    .table tr {
      display: block;
      margin-bottom: 10px;
      border-bottom: 2px solid #ddd;
      border-radius: 10px;
      padding: 10px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
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
  }

  @media (max-width: 480px) {
    .table td {
      font-size: 10px;
    }

    .table td button {
      font-size: 10px;
      padding: 5px 8px;
    }
  }
}
</style>


