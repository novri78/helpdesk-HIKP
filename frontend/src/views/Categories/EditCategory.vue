<template>
  <header>
    <h1 class="text-center white">Form Edit User</h1>
  </header>
  <main>
    <div class="form-container">
      <form @submit.prevent="editCategory">
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
          <button type="button" class="btn btn-secondary" @click="cancelEdit">Cancel</button>
          <button type="submit" class="btn btn-primary" @click="editCategory">Save</button>
        </div>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      </form>
    </div>
  </main>
  <footer></footer>
</template>


<script>
export default {
  name: 'EditCategory',
  data() {
    return {
      form: {
        id: this.$route.params.id,
        name: '',
        description: '',
      },
      category: [],
      errorMessage: ''
    };
  },
  mounted() {
    this.fetchCategoryData();
  },
  methods: {
    cancelEdit() {
      this.$router.push({ name: "Categories" });
    },
    fetchCategoryData() {
      const id = this.$route.params.id; // Assuming the user ID is passed as a route parameter
      this.$axios
        .get(`/category/${id}`)
        .then((response) => {
        this.form = response.data; // Simpan data category
        console.log("Data Category by Id", this.form); // Log data category
        })
        .catch((error) => {
          this.errorMessage = "Error fetching user data: " + error.message;
        });
    },
    editCategory() {
    const categoryData = this.form;
    this.$axios
        .put(`/category/${this.form.id}`, categoryData)
        .then(() => {
          this.$router.push({ name: "Categories" }).then(() => {
            alert("Category data updated successfully.");
          });
        })
        .catch((error) => {
          this.errorMessage = "Error updating category data: " + error.message;
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
