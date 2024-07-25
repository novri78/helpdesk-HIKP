<template>
  <header>
    <h1 class="text-center white">Form Add User</h1>
  </header>
  <main>
    <table class="table text-center">
      <th>
        <tr>
          <td>
            <form>
              Departement: <br /><input
                type="text"
                v-model="departement"
              /><br />
              Email: <br /><input type="text" v-model="email" /><br />
              Full Name: <br /><input type="text" v-model="full_name" /><br />
              Phone: <br /><input type="text" v-model="phone_number" /><br />
              Position: <br /><input type="text" v-model="position" /><br />
              Username: <br /><input type="text" v-model="position" /><br />
            </form>
          </td>
        </tr>
      </th>
    </table>
  </main>
  <footer></footer>
</template>

<script>
export default {
    
}
</script>