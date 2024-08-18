<template>
  <div>
    <header>
      
    </header>
    <main>      
      <div class="form-container">
        <h2 class="text-center white">Add New Ticket</h2>
        <form @submit.prevent="addTicket">
          <div class="form-group" v-for="(field, key) in formFields" :key="key">
            <label :for="key">{{ field.label }}:</label>
            <component
              :is="field.type"
              v-model="form[key]"
              :id="key"
              :class="['form-control', field.class]"
              :required="field.required"
              :placeholder="field.placeholder"
              :type="field.inputType"
              v-if="field.type === 'input' || field.type === 'textarea'"
            ></component>
            <select
              v-model="form[key]"
              :id="key"
              class="form-control"
              :required="field.required"
              v-if="field.type === 'select'"
            >
              <option v-for="option in field.options" :key="option.value" :value="option.value">
                {{ option.text }}
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
        priority: 2,
        status: 1,
        createdBy: '',
        creationDate:'',
        closureDate:'',
        userId: '',
        categoryId: '',
      },
      users: [],
      categories: [],
      errorMessage: '',
      formFields: {
        title: { label: 'Title', type: 'input', required: true, placeholder: 'Enter title' },
        description: { label: 'Description', type: 'textarea', required: true, placeholder: 'Enter description' },
        priority: { 
          label: 'Priority', 
          type: 'select', 
          required: true, 
          options: [
            { value: '1', text: 'HIGH' },
            { value: '2', text: 'MEDIUM' },
            { value: '3', text: 'LOW' },
          ],
        },
        status: {
          label: 'Status',
          type: 'select',
          required: true,
          options: [
            { value: '1', text: 'Open' },
            { value: '2', text: 'In Progress' },
            { value: '3', text: 'Closed' },
          ],
        },
        createdBy: { label: 'Created By', type: 'input', required: true, placeholder: 'Enter your name' },
        creationDate: { label: 'Creation Date', type: 'input', inputType: 'datetime-local', required: true },
        closureDate: { label: 'Closure Date', type: 'input', inputType: 'datetime-local' },
        userId: { label: 'User', type: 'select', required: true, options: [] },
        categoryId: { label: 'Category', type: 'select', required: true, options: [] },
      },
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
            value: user.id,
            text: user.name,
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
          console.log("API Response", res.data); // Log the entire response
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
