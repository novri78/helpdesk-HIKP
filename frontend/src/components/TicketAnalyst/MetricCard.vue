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
  border-radius: 12px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
  text-align: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.metric-card:hover {
  transform: scale(1.05);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.metric-card h2 {
  margin: 0;
  font-size: 1.5rem;
}

.metric-card .count {
  font-size: 2rem;
  margin: 10px 0;
}

.metric-card .subtitle {
  font-size: 0.9rem;
}
</style>