<template>
  <div class="dashboard">
    <header class="header"></header>
    <main class="main-content">
      <h1 class="text-center white">Helpdesk Dashboard</h1>
      <div class="cards-container">
        <MetricCard
          title="Grand Total Tickets"
          :count="totalTickets"
          subtitle="total tickets"
          backgroundColor="#42a5f5"
        />
        <MetricCard
          title="Unassigned tickets"
          :count="unassignedTickets"
          subtitle="without assignee"
          backgroundColor="#f57c00"
        />
        <MetricCard
          title="Open status tickets"
          :count="openTickets"
          subtitle="with assignee"
          backgroundColor="#66bb6a"
        />
        <MetricCard
          title="In Progress tickets"
          :count="inProgressTickets"
          subtitle="in progress support tickets"
          backgroundColor="#ab47bc"
        />
        <MetricCard
          title="Solved status tickets"
          :count="solvedTickets"
          subtitle="closed support tickets"
          backgroundColor="#42a5f5"
        />
        <div class="pie-chart-3d-container">
          <h3 class="text-center white">Ticket Graph</h3>
          <PieChart3DTicket v-if="isChartVisible" :chartData="pieChart3DData" />
        </div>
        <div class="top-categories-card">
          <TopCategoriesMetricCard
            :topCategories="topCategories"
            :categoriesData="categoriesData"
          />
        </div>
      </div>
    </main>
    <footer class="footer">
      <p>&copy; 2024 - Helpdesk App. Hak cipta dilindungi.</p>
    </footer>
  </div>
</template>

<script>
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
    };
  },
  computed: {
    totalTickets() {
      return this.tickets.length;
    },
    unassignedTickets() {
      return this.tickets.filter((ticket) => !ticket.assignTo).length;
    },
    openTickets() {
      return this.tickets.filter((ticket) => ticket.ticketStatus === "OPEN")
        .length;
    },
    inProgressTickets() {
      return this.tickets.filter(
        (ticket) => ticket.ticketStatus === "IN_PROGRESS"
      ).length;
    },
    solvedTickets() {
      return this.tickets.filter((ticket) => ticket.ticketStatus === "CLOSED")
        .length;
    },
  },
  mounted() {
    // Delay the rendering of the PieChart3D component by 1 second (1000ms)
    setTimeout(() => {
      this.isChartVisible = true;
      this.pieChart3DData = this.getPieChartData();
      console.log(this.pieChart3DData);
    }, 1000);
    this.fetchData();
  },
  methods: {
    fetchData() {
      Promise.all([this.fetchTickets(), this.fetchCategories()])
        .then(() => {
          this.topCategories = this.calculateTopCategories();
          this.categoriesData = this.topCategories.map((category) => ({
            name: category.name,
            percentage: parseFloat(category.percentage),
            color: this.getRandomColor(),
          }));
          this.pieChart3DData = this.getPieChartData(); // Assign the pie chart data here
          console.log("Pie Chart Data:", this.pieChart3DData); // Check the pie chart data
          console.log("Data fetched successfully", this.topCategories);
        })
        .catch((error) => {
          console.error("Failed to fetch data:", error.message);
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
      const categoryCount = this.tickets.reduce((acc, ticket) => {
        acc[ticket.categoryId] = (acc[ticket.categoryId] || 0) + 1;
        return acc;
      }, {});

      const totalTickets = this.tickets.length;

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
    getRandomColor() {
      const letters = "0123456789ABCDEF";
      let color = "#";
      for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
      }
      return color;
    },
    getPieChartData() {
      const statusCount = this.tickets.reduce(
        (acc, ticket) => {
          if (ticket.ticketStatus === "OPEN") acc.open++;
          else if (ticket.ticketStatus === "IN_PROGRESS") acc.inProgress++;
          else if (ticket.ticketStatus === "CLOSED") acc.closed++;
          return acc;
        },
        { open: 0, inProgress: 0, closed: 0 }
      );

      const total = this.totalTickets;

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
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  font-family: Arial, sans-serif;
}

.header {
  background-color: #42a5f5;
  color: white;
  padding: 20px;
  text-align: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.main-content {
  flex: 1;
  padding: 20px;
}

.cards-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
  margin-bottom: 20px;
}

.pie-chart-3d-container {
  margin-top: 20px;
  margin-left: 20px;
  display: flex;
  justify-content: center;
}

.top-categories-card {
  margin-top: 20px;
  max-width: 1000px;
  margin-left: auto;
  margin-right: auto;
}

.footer {
  background-color: #f1f1f1;
  text-align: center;
  padding: 10px;
  margin-top: 20px;
  box-shadow: 0 -4px 8px rgba(0, 0, 0, 0.1);
}
</style>
