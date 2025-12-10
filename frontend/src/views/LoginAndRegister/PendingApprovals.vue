<template>
  <div class="pending-approvals">
    <div class="header-section">
      <h1>Pending User Approvals</h1>
      <p class="subtitle">Review and approve new user registrations</p>
    </div>

    <div class="content-section">
      <!-- Loading state -->
      <div v-if="loading" class="loading">
        <p>Loading pending users...</p>
      </div>

      <!-- Error message -->
      <div v-if="errorMessage" class="alert alert-danger">
        {{ errorMessage }}
      </div>

      <!-- No pending users -->
      <div v-if="!loading && pendingUsers.length === 0" class="no-data">
        <p>✓ No pending approvals. All users have been reviewed.</p>
      </div>

      <!-- Table of pending users -->
      <div v-if="!loading && pendingUsers.length > 0" class="table-wrapper">
        <table class="users-table">
          <thead>
            <tr>
              <th>Email</th>
              <th>Full Name</th>
              <th>Role</th>
              <th>Department</th>
              <th>Phone</th>
              <th>Applied</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in pendingUsers" :key="user.id" class="user-row">
              <td>{{ user.email }}</td>
              <td>{{ user.name }}</td>
              <td><span class="badge role">{{ user.role }}</span></td>
              <td>{{ user.department }}</td>
              <td>{{ user.phoneNumber }}</td>
              <td>{{ formatDate(user.createdAt) }}</td>
              <td class="action-buttons">
                <button 
                  class="btn btn-approve" 
                  @click="approveUser(user.id)"
                  :disabled="approvingId === user.id"
                >
                  {{ approvingId === user.id ? 'Approving...' : 'Approve' }}
                </button>
                <button 
                  class="btn btn-reject" 
                  @click="rejectUser(user.id)"
                  :disabled="approvingId === user.id"
                >
                  {{ approvingId === user.id ? 'Processing...' : 'Reject' }}
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Summary stats -->
      <div v-if="!loading && pendingUsers.length > 0" class="stats">
        <p><strong>Total Pending:</strong> {{ pendingUsers.length }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import Swal from 'sweetalert2';

export default {
  name: 'PendingApprovals',
  data() {
    return {
      pendingUsers: [],
      loading: false,
      errorMessage: '',
      approvingId: null,
    };
  },
  methods: {
    fetchPendingUsers() {
      this.loading = true;
      this.errorMessage = '';

      // Fetch all users and filter those not approved
      this.$axios
        .get('/users')
        .then((response) => {
          // Filter users where isApproved is false
          this.pendingUsers = response.data.filter(
            (user) => !user.isApproved || user.isApproved === false
          );
        })
        .catch((error) => {
          this.errorMessage =
            error.response?.data?.message ||
            'Failed to load pending users. Please try again.';
          console.error('Error fetching users:', error);
        })
        .finally(() => {
          this.loading = false;
        });
    },

    approveUser(userId) {
      Swal.fire({
        title: 'Approve User?',
        text: 'Are you sure you want to approve this user?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonText: 'Yes, Approve',
        cancelButtonText: 'Cancel',
      }).then((result) => {
        if (result.isConfirmed) {
          this.approvingId = userId;

          // Find the user to get full details
          const user = this.pendingUsers.find((u) => u.id === userId);
          if (!user) {
            this.errorMessage = 'User not found';
            this.approvingId = null;
            return;
          }

          // Send approval request
          this.$axios
            .put(`/users/${userId}/approve`, {
              ...user,
              isApproved: true,
            })
            .then(() => {
              Swal.fire({
                title: 'Success',
                text: `User ${user.email} has been approved.`,
                icon: 'success',
              });
              // Remove from pending list
              this.pendingUsers = this.pendingUsers.filter(
                (u) => u.id !== userId
              );
            })
            .catch((error) => {
              Swal.fire({
                title: 'Error',
                text:
                  error.response?.data?.message ||
                  'Failed to approve user. Please try again.',
                icon: 'error',
              });
            })
            .finally(() => {
              this.approvingId = null;
            });
        }
      });
    },

    rejectUser(userId) {
      Swal.fire({
        title: 'Reject User?',
        text: 'Are you sure you want to reject this registration? This action cannot be undone.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Yes, Reject',
        cancelButtonText: 'Cancel',
        confirmButtonColor: '#dc3545',
      }).then((result) => {
        if (result.isConfirmed) {
          this.approvingId = userId;

          const user = this.pendingUsers.find((u) => u.id === userId);
          if (!user) {
            this.errorMessage = 'User not found';
            this.approvingId = null;
            return;
          }

          // Send rejection request (delete user)
          this.$axios
            .delete(`/users/${userId}`)
            .then(() => {
              Swal.fire({
                title: 'Rejected',
                text: `User ${user.email} has been rejected and removed.`,
                icon: 'success',
              });
              // Remove from pending list
              this.pendingUsers = this.pendingUsers.filter(
                (u) => u.id !== userId
              );
            })
            .catch((error) => {
              Swal.fire({
                title: 'Error',
                text:
                  error.response?.data?.message ||
                  'Failed to reject user. Please try again.',
                icon: 'error',
              });
            })
            .finally(() => {
              this.approvingId = null;
            });
        }
      });
    },

    formatDate(dateString) {
      if (!dateString) return '-';
      const date = new Date(dateString);
      return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
    },
  },

  mounted() {
    this.fetchPendingUsers();
  },
};
</script>

<style scoped>
.pending-approvals {
  padding: 20px;
  background-color: #f8f9fa;
  min-height: 100vh;
}

.header-section {
  margin-bottom: 30px;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.header-section h1 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 2rem;
}

.subtitle {
  margin: 0;
  color: #666;
  font-size: 1rem;
}

.content-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.loading,
.no-data {
  text-align: center;
  padding: 40px;
  color: #666;
  font-size: 1.1rem;
}

.alert {
  padding: 15px;
  margin-bottom: 20px;
  border-radius: 4px;
}

.alert-danger {
  background-color: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}

.table-wrapper {
  overflow-x: auto;
  margin-bottom: 20px;
}

.users-table {
  width: 100%;
  border-collapse: collapse;
}

.users-table thead {
  background-color: #f1f3f5;
}

.users-table th {
  padding: 12px;
  text-align: left;
  font-weight: 600;
  color: #333;
  border-bottom: 2px solid #dee2e6;
}

.users-table td {
  padding: 12px;
  border-bottom: 1px solid #dee2e6;
  color: #555;
}

.user-row:hover {
  background-color: #f9f9f9;
}

.badge {
  display: inline-block;
  padding: 5px 10px;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 500;
}

.badge.role {
  background-color: #e7f3ff;
  color: #0066cc;
}

.action-buttons {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-approve {
  background-color: #28a745;
  color: white;
}

.btn-approve:hover:not(:disabled) {
  background-color: #218838;
}

.btn-reject {
  background-color: #dc3545;
  color: white;
}

.btn-reject:hover:not(:disabled) {
  background-color: #c82333;
}

.stats {
  padding: 15px;
  background-color: #f1f3f5;
  border-radius: 4px;
  text-align: right;
  color: #555;
}

@media (max-width: 768px) {
  .pending-approvals {
    padding: 10px;
  }

  .header-section {
    padding: 15px;
  }

  .header-section h1 {
    font-size: 1.5rem;
  }

  .content-section {
    padding: 15px;
  }

  .users-table th,
  .users-table td {
    padding: 8px;
    font-size: 0.9rem;
  }

  .action-buttons {
    flex-direction: column;
  }

  .btn {
    width: 100%;
  }
}
</style>
