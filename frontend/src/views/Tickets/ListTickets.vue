<template>
  <div class="users-container">
    <header class="users-list"></header>
    <h2>Tickets</h2>
    <button class="btn btn-primary mb-3" @click="routeToAddTicket">
      Add Ticket
    </button>
    <div class="table-responsive">
      <table class="table">
        <thead>
          <tr>
            <th>Id</th>
            <th>Ticket No.</th>
            <th>Title</th>
            <th>Description</th>
            <th>Priority</th>
            <th>Status</th>
            <th>Assign To</th>
            <th>Create Date</th>
            <th>Close Date</th>
            <th>User</th>
            <th>Category</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="ticket in tickets" :key="ticket.id">
            <td>{{ ticket.id }}</td>
            <td>{{ ticket.ticketNo }}</td>
            <td>{{ ticket.title }}</td>
            <td>{{ ticket.description }}</td>
            <td>{{ ticket.priority }}</td>
            <td>{{ ticket.status }}</td>
            <td>{{ getUserName(ticket.assign) }}</td>
            <td>{{ ticket.createDate }}</td>
            <td>{{ ticket.closeDate }}</td>
            <td>{{ getUserName(ticket.userId) }}</td>
            <td>{{ getCategoryName(ticket.categoryId) }}</td>
            <td>
              <button class="btn btn-primary" @click="editTicket(ticket.id)">
                Edit
              </button>
              <button
                class="btn btn-danger"
                @click="confirmDeleteTicket(ticket.id)"
              >
                Delete
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tickets: [],
      users: [],
      categories: [],
      errorMessage: "",
    };
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    routeToAddTicket() {
      this.$router.push({ name: "AddTicket" });
    },
    fetchData() {
      Promise.all([this.fetchTickets(), this.fetchUsers(), this.fetchCategories()])
        .then(() => {
          console.log("Data fetched successfully");
        })
        .catch((error) => {
          this.errorMessage = "Failed to fetch data: " + error.message;
        });
    },
    fetchTickets() {
      return this.$axios
        .get("/tickets")
        .then((res) => {
          this.tickets = res.data.map((ticket) => ({
            id: ticket.id,
            ticketNo: ticket.ticketNo,
            title: ticket.title,
            description: ticket.description,
            priority: ticket.priorityStatus,
            status: ticket.ticketStatus,
            assign: ticket.assignTo,
            createDate: ticket.createDate,
            closeDate: ticket.closeDate,
            userId: ticket.userId,
            categoryId: ticket.categoryId,
          }));
        });
    },
    fetchUsers() {
      return this.$axios
        .get("/users")
        .then((res) => {
          this.users = res.data;
        });
    },
    fetchCategories() {
      return this.$axios
        .get("/category")
        .then((res) => {
          this.categories = res.data;
        });
    },
    getUserName(userId) {
      const user = this.users.find((user) => user.id === userId);
      return user ? user.name : "Unknown User";
    },
    getCategoryName(categoryId) {
      const category = this.categories.find((category) => category.id === categoryId);
      return category ? category.name : "Unknown Category";
    },
    editTicket(id) {
      this.$router.push({ name: "EditTicket", params: { id } });
    },
    confirmDeleteTicket(id) {
      if (confirm("Are you sure you want to delete this ticket with ID: " + id)) {
        this.$axios
          .delete(`/tickets/${id}`)
          .then(() => {
            this.fetchTickets(); // Refresh the list after deletion
          })
          .catch((error) => {
            this.errorMessage = "Failed to delete ticket: " + error.message;
          });
      }
    },
  },
};
</script>

<style scoped>
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
}

.users-container {
  padding: 20px;
}

header.users-list {
  text-align: center;
  margin-bottom: 20px;
}

button {
  font-family: "Roboto", sans-serif;
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

.table th,
.table td {
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
.table,
.btn {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.table th,
.table td,
.btn {
  transition: transform 0.2s;
}

.table th:hover,
.table td:hover,
.btn:hover {
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
