<template>
  <div>
    <header>
      <h2>Add New Category</h2>
    </header>
    <main>
      <div class="form-container">
        <form @submit.prevent="addCategory">
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
            name: '',
      description: '',
        },      
      errorMessage: ''
    };
  },
  methods: {
    goBack() {
      this.$router.push('/categories');
    },
    addCategory() {
      this.$axios
      .post("/category", this.form)
      .then(() => {
        this.$router.push( { name: "Categories"} )
        .then(() => {
            return alert("You have been succeed add data category.");
        });
      })  
      .catch((error) => {
        this.errorMessage = "Error adding category: " + error.message;
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
