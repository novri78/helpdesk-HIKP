<template>
  <div class="pie-chart-container">
    <canvas ref="pieChartCanvas"></canvas>
  </div>
</template>

<script>
import { defineComponent, onMounted, ref } from 'vue';
import { Chart, ArcElement, Tooltip, Legend } from 'chart.js';
import ChartDataLabels from 'chartjs-plugin-datalabels';

Chart.register(ArcElement, Tooltip, Legend, ChartDataLabels);

export default defineComponent({
  props: {
    categoriesData: {
      type: Array,
      default: () => [],
    },
  },
  setup(props) {
    const pieChartCanvas = ref(null);

    onMounted(() => {
      if (pieChartCanvas.value) {
        new Chart(pieChartCanvas.value, {
          type: 'pie',
          data: {
            labels: props.categoriesData.map(category => category.name),
            datasets: [{
              data: props.categoriesData.map(category => category.percentage),
              backgroundColor: props.categoriesData.map(category => category.color || '#FF6384'),
              borderColor: '#fff',
              borderWidth: 1,
            }],
          },
          options: {
            responsive: true,
            plugins: {
              datalabels: {
                color: '#fff',
                display: true,
                formatter: (value) => `${value}%`,
                font: {
                  weight: 'bold',
                  size: 16,
                },
              },
              legend: {
                display: true,
                position: 'right',
              },
            },
          },
          
        });
      }
    });

    return {
      pieChartCanvas,
    };
  },
});
</script>

<style scoped>
.pie-chart-container {
  width: 100%;
  max-width: 400px;
  margin: auto;
}
</style>
