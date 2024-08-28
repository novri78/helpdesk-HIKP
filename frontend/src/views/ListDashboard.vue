<template>
  <div class="dashboard">
    <header></header>
    <main>
      <div class="cards-container">
        <MetricCard
          title="Unassigned tickets"
          :count="unassignedTickets"
          subtitle="without assignee"
          backgroundColor="#f57c00"
        />
        <MetricCard
          title="Open tickets"
          :count="openTickets"
          subtitle="with assignee"
          backgroundColor="#66bb6a"
        />
        <!-- <MetricCard
          title="Violated SLA"
          :count="violatedSLA"
          subtitle="support tickets"
          backgroundColor="#ab47bc"
        /> -->
        <MetricCard
          title="In Progress tickets"
          :count="inProgressTickets"
          subtitle="in progress support tickets"
          backgroundColor="#ab47bc"
        />
        <MetricCard
          title="Solved tickets"
          :count="solvedTickets"
          subtitle="closed support tickets"
          backgroundColor="#42a5f5"
        />
      </div>
    </main>
    <footer>
      <p>&copy; 2024 - Helpdesk App. Hak cipta dilindungi.</p>
    </footer>
  </div>
</template>

<script>
import MetricCard from '@/components/TicketAnalyst/MetricCard.vue';

export default {
  data() {
    return {
      tickets: [],
    };
  },
  computed: {
    unassignedTickets() {
      return this.tickets.filter(ticket => !ticket.assignTo).length;
    },
    openTickets() {
      return this.tickets.filter(ticket => ticket.ticketStatus === 'OPEN').length;
    },
    // violatedSLA() {
    //   return this.tickets.filter(ticket => this.hasViolatedSLA(ticket)).length;
    // },
    inProgressTickets() {
      return this.tickets.filter(ticket => ticket.ticketStatus === 'IN_PROGRESS').length;
    },
    solvedTickets() {
      return this.tickets.filter(ticket => ticket.ticketStatus === 'CLOSED').length;
    },
  },
  mounted() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      this.$axios.get('/tickets').then(res => {
        this.tickets = res.data;
      });
    },
    // hasViolatedSLA(ticket) {
    //   const currentTime = new Date();
    //   const slaDeadline = new Date(ticket.sla);
    //   return currentTime > slaDeadline;
    // },
  },
  components: {
    MetricCard,
  },
};
</script>

<style scoped>
.cards-container {
  margin: 70px;
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: space-around;
}

footer {
  text-align: center;
  margin-top: 20px;
}
</style>
