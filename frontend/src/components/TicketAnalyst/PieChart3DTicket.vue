<template>
  <div class="pie-chart-3d">
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
    chartData: {
      type: Array,
      required: true,
    },
  },
  setup(props) {
    const pieChartCanvas = ref(null);

    onMounted(() => {
      if (pieChartCanvas.value) {
        new Chart(pieChartCanvas.value, {
          type: 'pie',
          data: {
            labels: props.chartData.map((data) => data.label),
            datasets: [{
              data: props.chartData.map((data) => data.value),
              backgroundColor: props.chartData.map((data) => data.color),
              borderColor: '#fff',
              borderWidth: 1,
            }],
          },
          options: {
            responsive: true,
            plugins: {
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
.pie-chart-3d {
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
}
</style>
