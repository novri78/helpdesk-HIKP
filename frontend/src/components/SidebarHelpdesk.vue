<template>
  <div :class="['sidebar', { sidebar_on: toggleSwitch }]" id="toggle_sidebar">
    <div class="toggle" @click="setToggle">
      <i :class="toggleSwitch ? 'mdi mdi-menu' : 'mdi mdi-menu'"></i>
    </div>
    <div class="navbar-brand">
      <a href="">
        <img src="@/assets/img/LOGO HIKP.png" alt="Logo" class="navbar-logo" />
      </a>
    </div>
    <ul class="nav flex-column mt-4">
      <router-link to="/dashboard" class="nav-link" exact>
        Dashboard
      </router-link>
      <router-link to="/tickets" class="nav-link">Ticket</router-link>
      <!-- <li class="nav-item">
        <a class="nav-link" @click="toggleDropdown('users')">
          List Users
          <i :class="dropdowns.users ? 'mdi mdi-chevron-up' : 'mdi mdi-chevron-down'
            "></i>
        </a>
        <ul v-if="dropdowns.users" class="sidebar-item-dropdown">
          <li>
            <router-link to="/users" class="dropdown-item">List User</router-link>
            <router-link to="/register" class="dropdown-item"
              >Register New User</router-link
            >
          </li>
        </ul>
      </li> -->
      <router-link v-if="userRole === 'ADMIN'" to="/users" class="nav-link">List User</router-link>
      <router-link v-if="userRole === 'ADMIN'" to="/categories" class="nav-link">Category</router-link>
      <router-link to="/chat" class="nav-link">Chat</router-link>
      <router-link to="/ask_wikipadia" class="nav-link">Ask Wikipedia</router-link>
      <li class="nav-link" @click="confirmLogout">Logout</li>
    </ul>
  </div>
</template>

<script>
import { mapActions } from "vuex";
import Swal from "sweetalert2";
import store from "@/store";

export default {
  props: {
    toggleSwitch: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      dropdowns: {
        users: false,
      },
    };
  },
  computed: {
    isAuthenticated() {
      return this.$store.getters.isAuthenticated;
    },
    userRole() {
      const userRole = store.getters.userRole
      return userRole
    }
  },
  methods: {
    setToggle() {
      this.$emit("toggle-sidebar");
    },
    toggleDropdown(name) {
      this.dropdowns[name] = !this.dropdowns[name];
    },
    ...mapActions(["logout"]),
    confirmLogout() {
      if (confirm("Are you sure you want to logout?")) {
        this.handleLogout();
      }
    },
    handleLogout() {
      this.logout();
      this.$router.push("/").then(() => {
        setTimeout(() => {
          Swal.fire({
            title: "Logged Out",
            text: "You have been logged out successfully.",
            icon: "info",
            confirmButtonText: "OK",
            timer: 3000, // Auto-close after 3 seconds
            timerProgressBar: true,
          });
        }, 300); // Delay of 100ms
      });
    },
  },
};
</script>

<style scoped lang="scss">
@import url("https://cdn.materialdesignicons.com/5.4.55/css/materialdesignicons.min.css");
@import url("https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap");

.sidebar {
  position: fixed;
  z-index: 1001;
  left: -250px;
  top: 0;
  width: 250px;
  height: 100vh;
  background-color: #19b16f;
  transition: left 0.3s ease-in-out;
  padding-top: 70px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.15);

  &.sidebar_on {
    left: 0;
  }

  .nav-link {
    margin-top: 0.6rem;
    color: #fff;
    text-decoration: none;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 20px;
    transition: background 0.2s ease;

    &:hover {
      color: #495057;
      background-color: rgba(255, 255, 255, 0.1);
      border-radius: 5px;
    }
  }

  .toggle {
    position: absolute;
    right: -30px;
    top: 10px;
    width: 40px;
    height: 50px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #19b16f;
    border-radius: 0 5px 5px 0;
    color: #fff;

    i {
      font-size: 24px;
    }
  }

  .sidebar-item-dropdown {
    background-color: #19b16f;
    list-style: none;
    padding-left: 0;
    margin-left: 10px;

    /* Menambahkan margin untuk nested dropdown */
    .dropdown-item {
      padding: 10px 20px;
      color: #fff;
      text-decoration: none;
      display: block;
      transition: background 0.2s ease;

      &:hover {
        color: #495057;
        background-color: rgba(255, 255, 255, 0.1);
        border-radius: 5px;
      }
    }
  }

  .navbar-brand {
    display: flex;
    align-items: center;
    margin: -35px 0 0 15px;
  }

  .navbar-logo {
    margin: -25px 0;
    width: 150px;
    height: auto;
  }
}

@media (max-width: 768px) {
  .sidebar {
    width: 100%;
    height: auto;
    left: -100%;

    &.sidebar_on {
      left: 0;
    }
  }

  .toggle {
    right: 0;
  }
}
</style>
