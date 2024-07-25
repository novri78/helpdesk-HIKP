<template>
    <header></header>
    <main>
        <div>
            <ul>
                <li v-for="user in users" :key="user.id">{{ user.fullName }}</li>
            </ul>
        </div>
    </main>
    <footer></footer>
</template>

<script>
export default {
    data() {
        return {
            users: []
        };
    },
    mounted() {
        this.$axios.get('/users')
        .then(response => {
            this.users = response.data;
        }) 
    }
    
}
</script>