<template>
  <div>
    <!-- <header>
      
    </header> -->
    <main>
      <div class="form-container">
        <h1 class="text-center white">Form Add Ticket</h1>
        <form @submit.prevent="addTicket">
          <div class="form-group">
            <label for="title">Title</label>
            <input v-model="form.title" id="title" type="text" placeholder="Enter title (1-255 characters)"
              class="form-control" :class="{ 'is-invalid': !isValidLength(form.title) && form.title !== '' }"
              required />
          </div>

          <div class="form-group">
            <label for="description">Description</label>
            <textarea v-model="form.description" id="description" placeholder="Enter description" class="form-control"
              required></textarea>
          </div>

          <div class="form-group">
            <label for="priorityStatus">Priority</label>
            <select v-model="form.priorityStatus" id="priority" class="form-control" required>
              <option value="" disabled>Select Priority</option>
              <option v-for="option in formFields.priority.options" :key="option.value" :value="option.value">
                {{ option.text }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label for="ticketStatus">Status</label>
            <select v-model="form.ticketStatus" id="status" class="form-control" required>
              <option value="" disabled>Select Status</option>
              <option v-for="option in formFields.status.options" :key="option.value" :value="option.value">
                {{ option.text }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label for="assignTo">Assign To</label>
            <select v-model="form.assignTo" id="assignTo" class="form-control">
              <option value="" disabled>Select Support Personnel</option>
              <option v-for="option in formFields.assignTo.options" :key="option.value" :value="option.value">
                {{ option.text }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label for="createDate">Create Date</label>
            <input type="datetime-local" id="createDate" v-model="form.createDate" class="form-control" required />
          </div>
          <div class="form-group">
            <label for="closeDate">Close Date</label>
            <input type="datetime-local" id="closeDate" v-model="form.closeDate" class="form-control" />
          </div>

          <div class="form-group">
            <label for="userId">User</label>
            <select v-model="form.userId" id="userId" class="form-control" required>
              <option value="" disabled>Select User</option>
              <option v-for="option in formFields.userId.options" :key="option.value" :value="option.value">
                {{ option.text }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label for="categoryId">Category</label>
            <select v-model="form.categoryId" id="categoryId" class="form-control" required>
              <option value="" disabled>Select Category</option>
              <option v-for="option in formFields.categoryId.options" :key="option.value" :value="option.value">
                {{ option.text }}
              </option>
            </select>
          </div>

          <div class="button-group">
            <button type="submit" class="btn btn-primary" :disabled="loading">
              {{ loading ? "Saving..." : "Save" }}
            </button>
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
import Swal from "sweetalert2";

export default {
  data() {
    return {
      form: {
        title: "",
        description: "",
        priorityStatus: "",
        ticketStatus: "",
        assignTo: "",
        createDate: "",
        closeDate: "",
        userId: "",
        categoryId: "",
      },
      users: [],
      categories: [],
      loading: false,
      errorMessage: "",
      formFields: {
        priority: {
          options: [
            { value: "HIGH", text: "HIGH" },
            { value: "MEDIUM", text: "MEDIUM" },
            { value: "LOW", text: "LOW" },
          ],
        },
        status: {
          options: [
            { value: "OPEN", text: "OPEN" },
            { value: "IN_PROGRESS", text: "IN PROGRESS" },
            { value: "CLOSED", text: "CLOSED" },
          ],
        },
        assignTo: {
          options: [],
        },
        userId: {
          options: [],
        },
        categoryId: {
          options: [],
        },
      },
    };
  },
  mounted() {
    this.fetchAssignTo();
    this.fetchUsers();
    this.fetchCategories();
  },
  methods: {
    fetchAssignTo() {
      this.$axios.get("/users").then((res) => {
        this.formFields.assignTo.options = res.data
          .filter((user) => user.role === "SUPPORT")
          .map((user) => ({ value: user.id, text: user.name }));
      });
    },
    fetchUsers() {
      this.$axios.get("/users").then((res) => {
        this.formFields.userId.options = res.data.map((user) => ({
          value: user.id,
          text: user.name,
        }));
      });
    },
    fetchCategories() {
      this.$axios.get("/category").then((res) => {
        this.formFields.categoryId.options = res.data.map((cat) => ({
          value: cat.id,
          text: cat.name,
        }));
      });
    },
    isValidLength(value) {
      return value.length >= 1 && value.length <= 255;
    },
    formatDateForInput(dateStr) {
      if (!dateStr) return "";
      const date = new Date(dateStr);
      const formattedDate = date.toISOString().slice(0, 16);
      return formattedDate;
    },
    addTicket() {
      this.loading = true;
      this.errorMessage = ""; // Clear error message before submission

      if (!this.isValidLength(this.form.title)) {
        this.errorMessage = "Title must be between 1 and 255 characters.";
        this.loading = false;
        return;
      }
      // // Ensure the dates are formatted with seconds before submission
      // if (this.form.createDate) {
      //   this.form.createDate = new Date(this.form.createDate).toISOString().slice(0,19);
      // }
      // if (this.form.closeDate) {
      //   this.form.closeDate = new Date(this.form.closeDate).toISOString().slice(0,19);
      // }

      this.$axios
        .post("/tickets", this.form)
        .then(() => {
          this.$router.push({ name: "Tickets" });
          Swal.fire({
            title: "Add Ticket Successful",
            text: "You have successfully created a ticket.",
            icon: "success",
            confirmButtonText: "OK",
          });
        })
        .catch((error) => {
          this.errorMessage = "Error adding ticket: " + error.message;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    goBack() {
      this.$router.push("/tickets");
    },
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

/* header {
  padding: 20px;
  text-align: center;
  background-color: #343a40;
  color: #fff;
} */

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

input {
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
