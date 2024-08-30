<template>
  <div id="app">
    <Sidebar v-if="isAuthenticated" @toggle-sidebar="handleSidebarToggle" :toggleSwitch="isSidebarOpen" />
    <div :class="['main-content', { 'sidebar-open': isAuthenticated && isSidebarOpen }]">
      <Nav v-if="isAuthenticated" />
      <div class="content-wrapper">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Nav from "./components/NavHelpDesk.vue";
import Sidebar from "./components/SidebarHelpdesk.vue";

export default {
  name: "App",
  components: {
    Nav,
    Sidebar,
  },
  data() {
    return {
      isSidebarOpen: false,
    };
  },
  computed: {
    ...mapGetters(["isAuthenticated"]),
  },
  watch: {
    isAuthenticated(newValue) {
      if (newValue) {
        this.isSidebarOpen = true;
      } else {
        this.isSidebarOpen = false;
      }
    },
  },
  methods: {
    handleSidebarToggle() {
      this.isSidebarOpen = !this.isSidebarOpen;
    },
  },
  mounted() {
    if (this.isAuthenticated) {
      this.isSidebarOpen = true;
    }
  },
};
</script>

<style lang="scss">

.main-content {
  flex-grow: 1;
  overflow-y: auto;
  padding: 20px; /* Menambahkan padding */
}

.sidebar-open {
  margin-left: 200px; /* Menyesuaikan lebar sidebar */
  transition: margin-left 0.3s ease-in-out;
}

@media (max-width: 768px) {
  .main-content {
    padding: 15px; /* Menyesuaikan padding untuk layar kecil */
    margin-left: 0;
  }
  .sidebar-open {
    margin-left: 0;
  }
}
</style>
