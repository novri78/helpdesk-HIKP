<template>
  <div class="categories-container">
    <header class="categories-list">
      <h2>Categories</h2>
    </header>
    <button class="btn btn-primary mb-3" @click="routeToAddCategory">
      +Category
    </button>
    <div class="table-responsive">
      <table class="table">
        <thead>
          <tr>
            <th>No.</th>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(cat, index) in categories" :key="cat.id">
            <td>{{ index + 1 }}</td>
            <td>{{ cat.id }}</td>
            <td>{{ cat.name }}</td>
            <td>{{ cat.description }}</td>
            <td>
              <div class="dropdown">
                <button
                  class="btn btn-secondary btn-sm dropdown-toggle"
                  type="button"
                  id="dropdownMenuButton{{ cat.id }}"
                  data-bs-toggle="dropdown"
                  aria-expanded="false"
                >
                  Actions
                </button>
                <ul class="dropdown-menu"
                  :aria-labelledby="'dropdownMenuButton' + cat.id"
                >
                  <li>
                    <a 
                      class="dropdown-item"
                      href="#"
                      @click.prevent="routeToEditCategory(cat.id)"
                      >Edit</a>
                  </li>
                  <li>
                    <a 
                      class="dropdown-item"
                      href="#"
                      @click.prevent="confirmDeleteCategory(cat.id)"
                      >Delete</a>
                  </li>
                </ul>
              </div>              
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
  name: "Categories",
  data() {
    return {
      categories: [],
      errorMessage: "",
    };
  },
  mounted() {
    this.fetchCategory();
  },
  methods: {
    routeToAddCategory() {
      this.$router.push({ name: "AddCategory" });
    },
    fetchCategory() {
      this.$axios
        .get("/category")
        .then((res) => {
          console.log("API Response", res.data); // Log the entire response
          this.categories = res.data.map((cat) => ({
            id: cat.id,
            name: cat.name,
            description: cat.description,
          }));
          console.log("Mapped Categories", this.categories); // Log the mapped users
        })
        .catch((error) => {
          this.errorMessage = "Failed to fetch category: " + error.message;
        });
    },
    routeToEditCategory(id) {
      this.$router.push({ name: "EditCategory", params: { id } });
      console.log("no id", id);
    },
    confirmDeleteCategory(id) {
      if (
        confirm("Are you sure you want to delete this category with ID: " + id)
      ) {
        this.$axios
          .delete(`/category/${id}`)
          .then(() => {
            this.fetchCategories(); // Refresh the list after deletion
          })
          .catch((error) => {
            this.errorMessage = "Failed to delete category: " + error.message;
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

.categories-container {
  padding: 10px;
}

header.categories-list {
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
  margin-right: 3px;
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
