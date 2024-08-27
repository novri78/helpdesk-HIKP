<template>
  <nav class="navbar">
    <button class="navbar-toggler" @click="toggleMenu">
      <span class="navbar-toggler-icon">&#9776;</span>
    </button>
    <div :class="['navbar-collapse', { show: isMenuOpen }]">
      <ul class="navbar-nav ml-auto">
        <li v-if="user.name" class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" @click="toggleDropdown">{{
            user.name
          }}</a>
          <ul class="dropdown-menu" :class="{ show: isDropdownOpen }">
            <li class="dropdown-item" @click="confirmLogout">Logout</li>
          </ul>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
import { mapActions } from "vuex";
import Cookies from "js-cookie";
import Swal from "sweetalert2";

export default {
  name: "Nav",
  data() {
    return {
      user: {
        name: "",
      },
      isMenuOpen: false,
      isDropdownOpen: false,
    };
  },
  methods: {
    toggleMenu() {
      this.isMenuOpen = !this.isMenuOpen;
    },
    toggleDropdown() {
      this.isDropdownOpen = !this.isDropdownOpen;
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
        }
        // , 300
        ); // Delay of 100ms
      });
    },
    getUserData() {
      const cookieData = Cookies.get("userdata"); // Ambil data dari Cookies

      if (cookieData) {
        const parsedData = JSON.parse(cookieData); // Parse data JSON
        this.user.name = parsedData.user.name;
      }
    },
  },
  mounted() {
    this.getUserData();
  },
};
</script>

<style scoped lang="scss">
.navbar {
  position: fixed;
  top: 0;
  right: 0;
  width: 100%;
  z-index: 1000;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #007bff;
  padding: 10px 5px;
  height: 60px;
  box-sizing: border-box;
}

.navbar-toggler {
  background: none;
  border: none;
  font-size: 1.5rem;
  color: #fff;
  cursor: pointer;
  display: none;
}

.navbar-toggler-icon {
  display: inline-block;
}

.navbar-collapse {
  display: flex;
  flex-grow: 1;
  justify-content: flex-end;
  align-items: center;
  transition: height 0.3s ease;
  height: auto;
}

.navbar-collapse.show {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  background-color: #007bff;
  width: 100%;
  padding: 10px 0;
}

.navbar-nav {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  align-items: center;
}

.nav-item {
  position: relative;
}

.nav-link {
  color: #fff;
  text-decoration: none;
  padding: 10px;
  cursor: pointer;
}

@media (max-width: 768px) {
  .navbar-toggler {
    display: block;
  }

  .navbar-collapse {
    display: none;
    flex-direction: column;
    align-items: flex-start;
    background-color: #007bff;
    width: 100%;
    padding: 10px 0;
  }

  .navbar-collapse.show {
    display: flex;
  }

  .nav-item {
    width: 100%;
    text-align: center;
  }

  .nav-link {
    display: block;
    width: 100%;
    text-align: center;
    padding: 10px;
  }
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  left: 0;
  background-color: #fff;
  list-style: none;
  padding: 0;
  margin: 0;
  width: 150px;
  border: 1px solid #007bff;
  border-radius: 5px;
  display: none;
}

.dropdown-menu.show {
  display: block;
}

.dropdown-item {
  padding: 10px;
  color: #007bff;
  cursor: pointer;
  &:hover {
    background-color: #007bff;
    color: #fff;
  }
}
</style>
