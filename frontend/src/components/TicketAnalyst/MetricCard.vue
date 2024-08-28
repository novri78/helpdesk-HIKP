<template>
  <div class="metric-card" :style="{ backgroundColor }">
    <h2>{{ title }}</h2>
    <p class="count">{{ count }}</p>
    <p class="subtitle">{{ subtitle }}</p>
  </div>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      required: true,
    },
    count: {
      type: Number,
      required: true,
    },
    subtitle: {
      type: String,
      required: false,
    },
    backgroundColor: {
      type: String,
      required: true,
    },
  },
};
</script>

<style scoped>
.metric-card {
  width: 220px;
  padding: 20px;
  color: white;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.metric-card h2 {
  margin: 0 0 10px;
}

.metric-card .count {
  font-size: 2.5rem;
  margin: 10px 0;
}

.metric-card .subtitle {
  font-size: 0.9rem;
}
</style>
