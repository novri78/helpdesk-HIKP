<template>
  <div class="chat-container">
    <h1 class="text-center white">Chat Room</h1>
    <div class="messages">
      <div v-for="message in messages" :key="message.id"
        :class="['message', { 'my-message': message.sender_id === userId }]">
        <div class="message-content">
          <p class="message-text">
            <strong>{{ message.sender_name }}:</strong> {{ message.message }}
          </p>
          <span class="message-time">{{ formatTime(message.created_at) }}</span>
        </div>
      </div>
    </div>
    <div class="input-container">
      <input v-model="newMessage" @keyup.enter="sendMessage" class="message-input" placeholder="Type your message..." />
      <button class="send-button" @click="sendMessage">
        <i class="mdi mdi-send"></i>
      </button>
      <button class="clear-button" @click="clearChat">
        Clear Chat
      </button>
    </div>
  </div>
</template>

<script>
import supabase from "@/supabase.js";
import cookie from "js-cookie";
import Swal from "sweetalert2";

export default {
  data() {
    return {
      newMessage: "",
      messages: [],
      userId: null,
      userName: "",
    };
  },
  async mounted() {
    const userdata = JSON.parse(cookie.get("userdata"));
    this.userId = userdata.user.id;
    this.userName = userdata.user.name;
    console.log("USERNAME", this.userName);

    const { data, error } = await supabase.from("chats").select("*");
    if (error) {
      console.error("Error fetching messages:", error.message);
      return;
    }
    this.messages = data;

    supabase
      .channel("chats")
      .on(
        "postgres_changes",
        { event: "INSERT", schema: "public", table: "chats" },
        (payload) => {
          this.messages.push(payload.new);
        }
      )
      .subscribe();
  },
  methods: {
    formatTime(timestamp) {
      const date = new Date(timestamp);
      return date.toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
      });
    },
    async sendMessage() {
      if (!this.newMessage) return;

      const { error } = await supabase.from("chats").insert({
        sender_id: this.userId.id,
        sender_name: this.userName,
        message: this.newMessage,
      });

      console.log("sender_name", this.userName); // Corrected log statement

      if (error) {
        console.error("Error inserting message:", error.message);
        return;
      }

      this.newMessage = "";
    },
    async clearChat() {
      const { data, error } = await supabase
        .from("chats")
        // .delete()
        // .neq("sender_id", "")
        .select("id")
        ;
      if (error) {
        console.error("Error clearing messages:", error.message);
        return;
      }
      // If there are no messages, stop the operation
      if (!data || data.length === 0) {
        console.log("No messages to delete.");
        return;
      }

      // Delete each message by its ID
      for (let message of data) {
        const { error: deleteError } = await supabase
          .from("chats")
          .delete()
          .eq("id", message.id);

        if (deleteError) {
          console.error(`Error deleting message with ID ${message.id}:`, deleteError.message);
          return;
        }
      }
      Swal.fire({
        title: "Clear Chat Successful",
        text: "You have successfully clear chats.",
        icon: "success",
        confirmButtonText: "OK",
      });
      this.messages = [];
    }
  },
};
</script>

<style scoped lang="scss">
@import url("https://cdn.materialdesignicons.com/5.4.55/css/materialdesignicons.min.css");

.content-wrapper {
  margin: 30px;
}

.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #f7f7f7;
  padding: 20px;
  margin: 25px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding-right: 10px;
  margin-bottom: 15px;
  display: flex;
  flex-direction: column-reverse;
  max-height: calc(100vh - 200px);
  /* Adjust this value as needed */
}

.message {
  display: flex;
  margin-bottom: 10px;
  animation: fadeIn 0.3s ease;
}

.clear-button {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 10px 15px;
  margin-top: auto;
  margin-left: 1px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
  align-self: center;
}

.clear-button:hover {
  background-color: #c82333;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.my-message {
  justify-content: flex-end;
}

.message-content {
  max-width: 70%;
  padding: 10px 15px;
  background-color: #19b16f;
  color: white;
  border-radius: 10px;
  position: relative;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.my-message .message-content {
  background-color: #28a745;
}

.message-text {
  margin: 0;
}

.message-time {
  font-size: 0.75rem;
  opacity: 0.7;
  margin-top: 5px;
  text-align: right;
}

.input-container {
  display: flex;
  align-items: center;
  background-color: #ffffff;
  padding: 10px;
  border-top: 1px solid #e7e7e7;
  border-radius: 5px;
}

.message-input {
  flex: 1;
  padding: 10px;
  border: none;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  font-size: 1rem;
}

.message-input:focus {
  outline: none;
}

.send-button {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 10px 15px;
  margin-left: 10px;
  border-radius: 5px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.3s;
}

.send-button i {
  font-size: 1.2rem;
}

.send-button:hover {
  background-color: #0056b3;
}
</style>
