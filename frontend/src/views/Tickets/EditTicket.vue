<template>
  <div>
    <header>
      <h2>Edit Ticket</h2>
    </header>
    <main>
      <div class="form-container">
        <form @submit.prevent="editTicket">
          <div class="form-group" v-for="(value, key) in form" :key="key">
          <label :for="key">{{ key.replace('_', ' ').toUpperCase() }}:</label>
          <input
            :type="key === 'email' ? 'email' : 'text'"
            v-model="form[key]"
            :id="key"
            class="form-control"
            required
          />
        </div>
        <div class="button-group">
          <button type="button" class="btn btn-secondary" @click="goBack">Cancel</button>
          <button type="submit" class="btn btn-primary" @click="editCategory">Save</button>
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
      form: {
        id: "", // Assuming id is used to identify the ticket being edited
        title: "",
        description: "",
        priority: "",
        status: "",
        createdBy: "",
        creationDate: "",
        closureDate: "",
        userId: "",
        categoryId: "",
      },
      users: [],
      categories: [],
      errorMessage: "",
      formFields: {
        title: {
          label: "Title",
          type: "input",
          required: true,
          placeholder: "Enter title",
        },
        description: {
          label: "Description",
          type: "textarea",
          required: true,
          placeholder: "Enter description",
        },
        priority: {
          label: "Priority",
          type: "input",
          required: true,
          placeholder: "Enter priority",
        },
        status: {
          label: "Status",
          type: "select",
          required: true,
          options: [
            { value: "1", text: "Open" },
            { value: "2", text: "In Progress" },
            { value: "3", text: "Closed" },
          ],
        },
        createdBy: {
          label: "Created By",
          type: "input",
          required: true,
          placeholder: "Enter your name",
        },
        creationDate: {
          label: "Creation Date",
          type: "input",
          inputType: "datetime-local",
          required: true,
        },
        closureDate: {
          label: "Closure Date",
          type: "input",
          inputType: "datetime-local",
        },
        userId: { 
          label: "User", 
          type: "select", 
          required: true, 
          options: [] },
        categoryId: {
          label: "Category",
          type: "select",
          required: true,
          options: [],
        },
      },
    };
  },
  mounted() {
    this.fetchUsers();
    this.fetchCategories();
    this.fetchTicket();
  },
  methods: {
    fetchUsers() {
      this.$axios
      .get("/users")
      .then(res => {
        console.log("API Users Res", res.data);
        this.users = response.data.map((user) => ({
          value: user.id,
          text: user.fullName,
        }));
        this.formFields.userId.options = this.users;
      })
      .catch(error => {
        this.errorMessage = "Failed to fetch users: " + error.message;
      });
    },
    fetchCategories() {
      this.$axios
      .get('/category')
        .then(res => {
          console.log("API Category Res", res.data); // Log the entire response
          this.categories = res.data.map(cat => ({
            value: cat.id,
            text: cat.name,
          }));
          this.formFields.categoryId.options = this.categories;
        })
        .catch(error => {
          this.errorMessage = "Failed to fetch categories: " + error.message;
        });
    },
    fetchTicket() {
      const id = this.$route.params.id; // Assuming ticket ID is passed as a route parameter
      this.$axios
        .get(`/tickets/${id}`)
        .then((res) => {
          this.form = res.data;
          console.log("Data Ticket by id", this.form);
        })
        .catch((error) => {
          this.errorMessage = "Failed to fetch ticket: " + error.message;
        });
    },
    editTicket() {
      const ticketData = this.form;
      this.$axios
        .put(`/tickets/${this.form.id}`, ticketData)
        .then(() => {
          this.$router.push({ name: "Tickets" }).then(() => {
            alert("You have successfully edited the ticket.");
          });
        })
        .catch((error) => {
          this.errorMessage = "Error editing ticket: " + error.message;
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
