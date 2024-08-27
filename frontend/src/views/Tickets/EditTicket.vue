<template>
  <div>
    <header></header>
    <main>
      <div class="form-container">
        <h2 class="text-center white">Edit Ticket</h2>
        <form @submit.prevent="submit">
          <div class="form-group">
            <label for="ticketNo">Ticket No</label>
            <input
              type="text"
              id="ticketNo"
              v-model="ticket.ticketNo"
              class="form-control"
              disabled
            />
          </div>
          <div class="form-group">
            <label for="title">Title</label>
            <input
              type="text"
              id="title"
              v-model="ticket.title"
              class="form-control"
              required
            />
          </div>
          <div class="form-group">
            <label for="description">Description</label>
            <textarea
              id="description"
              v-model="ticket.description"
              class="form-control"
              required
            ></textarea>
          </div>
          <!-- <div class="form-group">
            <label for="createDate">Create Date</label>
            <input
              type="datetime-local"
              id="createDate"
              v-model="ticket.createDate"
              class="form-control"
              required
            />
          </div> -->
          <div class="form-group">
            <label for="closeDate">Close Date</label>
            <input
              type="datetime-local"
              id="closeDate"
              v-model="ticket.closeDate"
              class="form-control"
            />
          </div>
          <div class="form-group">
            <label for="priority">Priority</label>
            <select
              id="priority"
              v-model="ticket.priorityStatus"
              class="form-control"
              required
            >
              <option value="LOW">Low</option>
              <option value="MEDIUM">Medium</option>
              <option value="HIGH">High</option>
            </select>
          </div>
          <div class="form-group">
            <label for="status">Status</label>
            <select
              id="status"
              v-model="ticket.ticketStatus"
              class="form-control"
              required
            >
              <option value="OPEN">Open</option>
              <option value="IN_PROGRESS">In Progress</option>
              <option value="CLOSED">Closed</option>
            </select>
          </div>
          <div class="form-group">
            <label for="assign">Assign To</label>
            <select
              id="assignTo"
              v-model="ticket.assignTo"
              class="form-control"
              required
            >
              <option
                v-for="support in assignToOptions"
                :key="support.value"
                :value="support.value"
              >
                {{ support.text }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="userId">User</label>
            <select
              id="userId"
              v-model="ticket.userId"
              class="form-control"
              required
            >
              <option v-for="user in users" :key="user.id" :value="user.id">
                {{ user.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label for="categoryId">Category</label>
            <select
              id="categoryId"
              v-model="ticket.categoryId"
              class="form-control"
              required
            >
              <option
                v-for="category in categories"
                :key="category.id"
                :value="category.id"
              >
                {{ category.name }}
              </option>
            </select>
          </div>

          <div class="button-group">
            <button type="submit" class="btn btn-primary">Save</button>
            <button type="button" class="btn btn-secondary" @click="goBack">
              Cancel
            </button>
          </div>
          <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
        </form>
      </div>
    </main>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      ticket: {
        id: "",
        ticketNo: "",
        title: "",
        description: "",
        priority: "",
        status: "",
        assign: "",
        userId: "",
        categoryId: "",
      },
      users: [],
      categories: [],
      assignToOptions: [],
      errorMessage: "",
    };
  },
  mounted() {
    this.fetchTicket();
    this.fetchUsers();
    this.fetchCategories();
    this.fetchAssignTo();
  },
  methods: {
    fetchTicket() {
      const id = this.$route.params.id;
      this.$axios
        .get(`/tickets/${id}`)
        .then((res) => {
          this.ticket = res.data;
        })
        .catch((error) => {
          this.errorMessage = "Failed to fetch ticket: " + error.message;
        });
    },
    fetchUsers() {
      this.$axios
        .get("/users")
        .then((res) => {
          this.users = res.data;
        })
        .catch((error) => {
          this.errorMessage = "Failed to fetch users: " + error.message;
        });
    },
    fetchAssignTo() {
      this.$axios
        .get("/users")
        .then((res) => {
          this.assignToOptions = res.data
            .filter((user) => user.role === "SUPPORT")
            .map((user) => ({ value: user.id, text: user.name }));
        })
        .catch((error) => {
          this.errorMessage =
            "Failed to fetch support personnel: " + error.message;
        });
    },
    fetchCategories() {
      this.$axios
        .get("/category")
        .then((res) => {
          this.categories = res.data;
        })
        .catch((error) => {
          this.errorMessage = "Failed to fetch categories: " + error.message;
        });
    },
    formatDateForInput(dateStr) {
      if (!dateStr) return "";
      const date = new Date(dateStr);
      const formattedDate = date.toISOString().slice(0, 16); // Format to 'YYYY-MM-DDTHH:MM'
      return formattedDate;
    },
    submit() {
      const id = this.$route.params.id;
      this.$axios
        .put(`/tickets/${id}`, this.ticket)
        .then(() => {
          this.$router.push({ name: "Tickets" });
        })
        .catch((error) => {
          this.errorMessage = "Failed to update ticket: " + error.message;
        });
    },
    goBack() {
      this.$router.push({ name: "Tickets" });
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
textarea,
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
