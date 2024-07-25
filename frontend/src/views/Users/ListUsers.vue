<template>
    <header></header>
    <main>
        <div>
            <h1>Users</h1>
            <UserList/>            
        </div>
    </main>    
    <footer></footer>
</template>

<script>
import UserList from '../../components/UserList.vue'

export default {
    name: 'Users',
    components: {
        UserList
    }
}
</script>