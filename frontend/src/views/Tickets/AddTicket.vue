<template>
  <div>
    <header>
      <h2>Add New Ticket</h2>
    </header>
    <main>
      <div class="form-container">
        <form @submit.prevent="addTicket">
          <div class="form-group">
            <label for="title">Title:</label>
            <input type="text" v-model="form.title" id="title" class="form-control" required />
          </div>

          <div class="form-group">
            <label for="description">Description:</label>
            <textarea v-model="form.description" id="description" class="form-control" required></textarea>
          </div>

          <div class="form-group">
            <label for="priority">Priority:</label>
            <input type="text" v-model="form.priority" id="priority" class="form-control" required />
          </div>

          <div class="form-group">
            <label for="status">Status:</label>
            <input type="text" v-model="form.status" id="status" class="form-control" required />
          </div>

          <div class="form-group">
            <label for="userId">User:</label>
            <select v-model="form.userId" id="userId" class="form-control" required>
              <option v-for="user in users" :key="user.id" :value="user.id">
                {{ user.fullName }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label for="categoryId">Category:</label>
            <select v-model="form.categoryId" id="categoryId" class="form-control" required>
              <option v-for="category in categories" :key="category.id" :value="category.id">
                {{ category.name }}
              </option>
            </select>
          </div>

          <div class="button-group">
            <button type="submit" class="btn btn-primary">Save</button>
            <button type="button" class="btn btn-secondary" @click="goBack">Cancel</button>
          </div>

          <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
        </form>
      </div>
    </main>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {
        title: '',
        description: '',
        priority: '',
        status: '',
        userId: '',
        categoryId: '',
      },
      users: [],
      categories: [],
      errorMessage: ''
    };
  },
  mounted() {
    this.fetchUsers();
    this.fetchCategories();
  },
  methods: {
    fetchUsers() {
      this.$axios
        .get('/users')
        .then(res => {
          console.log("API Response", res.data); // Log the entire response
          this.users = res.data.map(user => ({
            id: user.id,
            fullName: user.fullName // Adjust this field name according to your API response
          }));
          console.log("Mapped Users", this.users); // Log the mapped users
        })
        .catch(error => {
          this.errorMessage = "Failed to fetch users: " + error.message;
        });
    },
    fetchCategories() {
      this.$axios
        .get('/category')
        .then(res => {
          console.log("API Response", res.data); // Log the entire response
          this.categories = res.data.map(cat => ({
            id: cat.id,
            name: cat.name // Adjust this field name according to your API response
          }));
          console.log("Mapped Categories", this.categories); // Log the mapped categories
        })
        .catch(error => {
          this.errorMessage = "Failed to fetch categories: " + error.message;
        });
    },
    goBack() {
      this.$router.push('/tickets');
    },
    addTicket() {
      this.$axios
        .post("/tickets", this.form)
        .then(() => {
          this.$router.push({ name: "Tickets" })
            .then(() => {
              alert("You have successfully added the ticket.");
            });
        })
        .catch((error) => {
          this.errorMessage = "Error adding ticket: " + error.message;
        });
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
