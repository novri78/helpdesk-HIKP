<template>
  <div class="metric-card-container">
    <div class="metric-card">
      <h3>Top 5 Categories</h3>
      <ul>
        <li v-for="category in topCategories" :key="category.name">
          <span>{{ category.name }}</span>
          <span>{{ category.percentage }}%</span>
        </li>
      </ul>
    </div>
    <div class="pie-chart">
      <!-- Conditionally render the PieChart3D component based on isChartVisible -->
      <PieChart3D v-if="isChartVisible" :topCategories="topCategories" :categoriesData="categoriesData"/>
    </div>
  </div>
</template>

<script>
import PieChart3D from "@/components/TicketAnalyst/PieChart3D.vue";

export default {
  components: {
    PieChart3D,
  },
  props: {
    topCategories: {
      type: Array,
      default: () => [],
    },
    categoriesData: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      isChartVisible: false, // Initially set to false to control the chart rendering
    };
  },
  mounted() {
    // Delay the rendering of the PieChart3D component by 1 second (1000ms)
    setTimeout(() => {
      this.isChartVisible = true;
    }, 1000);
  },
};
</script>

<style scoped>
.metric-card-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 30px;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.metric-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  padding: 16px;
  width: 50%;
}

.metric-card h3 {
  margin-bottom: 16px;
  text-align: center;
}

.metric-card ul {
  list-style-type: none;
  padding: 0;
}

.metric-card li {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.metric-card li:last-child {
  margin-bottom: 0;
}

.pie-chart {
  width: 50%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
