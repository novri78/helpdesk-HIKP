<template>
  <div :class="['dashboard', { dark: isDarkMode }]">
    <header class="header">
      <div class="header-content">
        <h1 class="text-center">Helpdesk Dashboard</h1>
        <button @click="toggleDarkMode" class="dark-mode-toggle">
          {{ isDarkMode ? "Light Mode" : "Dark Mode" }}
        </button>
      </div>
    </header>
    <main class="main-content">
      <!-- Dropdown for selecting month -->
      <div class="header-container">
        <div class="month-selector">
          <label for="month-select" class="white">Select Month:</label>
          <input type="month" id="month-select" v-model="selectedMonth" class="month-input" @change="fetchData" />
        </div>
        <div class="download-btn-container">
          <button class="download-csv-btn" @click="downloadCSV">Download CSV</button>
        </div>
      </div>

      <div class="cards-container">
        <MetricCard title="Grand Total Tickets" :count="totalTickets" subtitle="total tickets"
          backgroundColor="#f57c00" />
        <!-- <MetricCard
          title="Unassigned tickets"
          :count="unassignedTickets"
          subtitle="without assignee"
          backgroundColor="#f57c00"
        /> -->
        <MetricCard title="Open status tickets" :count="openTickets" subtitle="with assignee"
          backgroundColor="#66bb6a" />
        <MetricCard title="In Progress tickets" :count="inProgressTickets" subtitle="in progress support tickets"
          backgroundColor="#ab47bc" />
        <MetricCard title="Solved status tickets" :count="solvedTickets" subtitle="closed support tickets"
          backgroundColor="#42a5f5" />
        <!-- SLA dalam Menit -->
        <MetricCard title="Average SLA (Minutes)" :count="averageSLAMinutes"
          subtitle="Average time to resolve tickets in minutes" backgroundColor="#f44336" />

        <div class="pie-card-container">
          <div class="pie-chart-3d-container">
            <h3 class="text-center">Ticket Graph</h3>
            <PieChart3DTicket v-if="isChartVisible" :chartData="pieChart3DData" />
          </div>
        </div>

        <div class="top-categories-card">
          <TopCategoriesMetricCard :topCategories="topCategories" :categoriesData="categoriesData" />
        </div>
      </div>
    </main>
    <footer class="footer">
      <p>&copy; 2024 - Helpdesk App. Hak cipta dilindungi.</p>
    </footer>
  </div>
</template>

<script>
import moment from "moment";
import MetricCard from "@/components/TicketAnalyst/MetricCard.vue";
import PieChart3DTicket from "@/components/TicketAnalyst/PieChart3DTicket.vue";
import TopCategoriesMetricCard from "@/components/TicketAnalyst/TopCategoriesMetricCard.vue";

export default {
  data() {
    return {
      isChartVisible: false,
      tickets: [],
      categories: [],
      topCategories: [],
      categoriesData: [],
      pieChart3DData: [],
      selectedMonth: moment().format("YYYY-MM"), // Default to the current month
      isDarkMode: false, // For dark mode toggle
      averageSLAMinutes: 0,
    };
  },
  computed: {
    filteredTickets() {
      return this.tickets.filter(
        (ticket) =>
          moment(ticket.createDate).format("YYYY-MM") === this.selectedMonth
      );
    },
    totalTickets() {
      return this.filteredTickets.length; // Update to use filteredTickets
    },
    // unassignedTickets() {
    //   return this.filteredTickets.filter((ticket) => !ticket.assignTo).length; // Update to use filteredTickets
    // },
    openTickets() {
      return this.filteredTickets.filter(
        (ticket) => ticket.ticketStatus === "OPEN"
      ).length; // Update to use filteredTickets
    },
    inProgressTickets() {
      return this.filteredTickets.filter(
        (ticket) => ticket.ticketStatus === "IN_PROGRESS"
      ).length; // Update to use filteredTickets
    },
    solvedTickets() {
      return this.filteredTickets.filter(
        (ticket) => ticket.ticketStatus === "CLOSED"
      ).length; // Update to use filteredTickets
    },
  },
  watch: {
    selectedMonth() {
      // Call fetchData every time selectedMonth changes
      this.fetchData();
    },
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      // Reset isChartVisible before updating data
      this.isChartVisible = false;

      Promise.all([this.fetchTickets(), this.fetchCategories(), this.fetchUsers()])
        .then(() => {
          this.topCategories = this.calculateTopCategories();
          this.categoriesData = this.topCategories.map((category) => ({
            name: category.name,
            percentage: parseFloat(category.percentage),
            color: this.getRandomColor(),
          }));
          this.updatePieChartData(); // Update pieChart3DData here

          // Calculate average SLA
          this.calculateAverageSLA();

          this.$nextTick(() => {
            // Show the chart after data has been updated
            this.isChartVisible = true;
          });
        })
        .catch((error) => {
          console.error("Failed to fetch data:", error.message);
        });
    },
    downloadCSV() {
      const csvContent = this.generateCSV(this.filteredTickets);
      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
      const url = URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.setAttribute('href', url);
      link.setAttribute('download', `tickets_${this.selectedMonth}.csv`);
      link.style.visibility = 'hidden';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },
    generateCSV(tickets) {
      const headers = ['Ticket ID', 'Title', 'Status', 'Created Date', 'Closed Date', 'Category', 'Assign To', 'User'];
      const rows = tickets.map(ticket => [
        ticket.id,
        ticket.title,
        ticket.ticketStatus,
        moment(ticket.createDate).format('YYYY-MM-DD HH:mm:ss'),
        ticket.closeDate ? moment(ticket.closeDate).format('YYYY-MM-DD HH:mm:ss') : '',
        this.categories ? this.categories.find(category => category.id === ticket.categoryId)?.name || 'Unknown' : 'Unknown',
        this.users ? this.users.find(user => user.id === ticket.assignTo)?.name || 'Unknown' : 'Unknown',
        this.users ? this.users.find(user => user.id === ticket.userId)?.name || 'Unknown' : 'Unknown',
      ]);
      const csvContent = [
        headers.join(','),
        ...rows.map(row => row.join(','))
      ].join('\n');
      return csvContent;
    },
    fetchUsers() {
      return this.$axios
        .get("/users")
        .then((res) => {
          this.users = res.data;
        });
    },
    fetchTickets() {
      return this.$axios
        .get("/tickets")
        .then((res) => {
          this.tickets = res.data;
        })
        .catch((error) => {
          console.error("Error fetching tickets:", error);
        });
    },
    fetchCategories() {
      return this.$axios
        .get("/category")
        .then((res) => {
          this.categories = res.data;
        })
        .catch((error) => {
          console.error("Error fetching categories:", error);
        });
    },
    calculateTopCategories() {
      const categoryCount = this.filteredTickets.reduce((acc, ticket) => {
        acc[ticket.categoryId] = (acc[ticket.categoryId] || 0) + 1;
        return acc;
      }, {});

      const totalTickets = this.filteredTickets.length;

      const topCategories = Object.entries(categoryCount)
        .map(([categoryId, count]) => {
          const category = this.categories.find(
            (cat) => cat.id === parseInt(categoryId)
          );
          return {
            name: category ? category.name : "Unknown Category",
            percentage: ((count / totalTickets) * 100).toFixed(2),
          };
        })
        .sort((a, b) => b.percentage - a.percentage)
        .slice(0, 5);

      return topCategories;
    },
    calculateAverageSLA() {
      const closedTickets = this.filteredTickets.filter(
        (ticket) => ticket.ticketStatus === "CLOSED"
      );

      if (closedTickets.length === 0) {
        this.averageSLAMinutes = 0;
        return;
      };

      const totalMinutes = closedTickets.reduce((total, ticket) => {
        const createDate = moment(ticket.createDate);
        const closeDate = moment(ticket.closeDate);
        const duration = moment.duration(closeDate.diff(createDate));

        return total + duration.asMinutes();
      }, 0);

      this.averageSLAMinutes = (totalMinutes / closedTickets.length).toFixed(2);
      this.averageSLAMinutes = parseFloat(this.averageSLAMinutes);
    },
    getRandomColor() {
      const letters = "0123456789ABCDEF";
      let color = "#";
      for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
      }
      return color;
    },
    updatePieChartData() {
      this.pieChart3DData = this.getPieChartData(); // Update pieChart3DData with the correct month data

      // Trigger a manual update
      this.$nextTick(() => {
        this.isChartVisible = false; // Hide it to trigger rerender
        this.$nextTick(() => {
          this.isChartVisible = true; // Show it again to ensure rerender
        });
      });
    },
    getPieChartData() {
      if (this.filteredTickets.length === 0) {
        return [
          {
            label: "No Data",
            value: 100,
            color: "#cccccc", // Gray color for "No Data"
          },
        ];
      }

      const statusCount = this.filteredTickets.reduce(
        (acc, ticket) => {
          if (ticket.ticketStatus === "OPEN") acc.open++;
          else if (ticket.ticketStatus === "IN_PROGRESS") acc.inProgress++;
          else if (ticket.ticketStatus === "CLOSED") acc.closed++;
          return acc;
        },
        { open: 0, inProgress: 0, closed: 0 }
      );

      const total = this.filteredTickets.length;

      return [
        {
          label: "Open",
          value: ((statusCount.open / total) * 100).toFixed(2),
          color: "#66bb6a",
        },
        {
          label: "In Progress",
          value: ((statusCount.inProgress / total) * 100).toFixed(2),
          color: "#ab47bc",
        },
        {
          label: "Closed",
          value: ((statusCount.closed / total) * 100).toFixed(2),
          color: "#42a5f5",
        },
      ];
    },
    toggleDarkMode() {
      this.isDarkMode = !this.isDarkMode;
    },
  },
  components: {
    MetricCard,
    TopCategoriesMetricCard,
    PieChart3DTicket,
  },
};
</script>

<style scoped>
.dashboard {
  background-color: #f0f2f5;
  margin: 55px;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  font-family: "Inter", sans-serif;
  transition: background-color 0.3s ease;
}

.header {
  background: linear-gradient(90deg, #19b16f, #1cc88a);
  color: white;
  padding: 20px;
  text-align: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.month-selector {
  margin-left: -125px;
  display: flex;
  align-items: center;
}

.month-selector .white {
  color: #fff;
  margin-right: 10px;
}

.month-selector .month-input {
  padding: 8px 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
}

.download-btn-container {
  display: flex;
  justify-content: flex-end;
}

.download-csv-btn {
  background-color: #f57c00;
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

.download-csv-btn:hover {
  background-color: #e65100;
}

.dark .dashboard {
  background-color: #2c3e50;
  color: #ecf0f1;
}

.dark-mode-toggle {
  background: none;
  border: 1px solid white;
  color: white;
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  transition: background 0.3s ease;
}

.dark-mode-toggle:hover {
  background: rgba(255, 255, 255, 0.2);
}

.main-content {
  margin: 0 auto;
  flex: 1;
  padding: 20px;
  background-color: #f8f9fc;
  transition: background-color 0.3s;
}

.cards-container {
  margin-top: 20px;
  margin-left: 20px;
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
  margin-bottom: 20px;
}

.pie-card-container,
.top-categories-card {
  width: 100%;
  max-width: 950px;
  margin: 5px auto;
}

.pie-chart-3d-container {
  margin-top: 20px;
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.footer {
  background-color: #f1f1f1;
  text-align: center;
  padding: 10px;
  margin-top: 20px;
  box-shadow: 0 -4px 8px rgba(0, 0, 0, 0.1);
}

.dark .main-content {
  background-color: #2c2f33;
}

.dark .header {
  background: linear-gradient(90deg, #19b16f, #2c2f33);
}

.dark .footer {
  background-color: #23272a;
  color: #f1f1f1;
}

.dark .cards-container label {
  color: white;
}

.dark .month-selector label {
  color: white;
}

.dark .top-categories-card {
  color: #23272a;
}
</style>
