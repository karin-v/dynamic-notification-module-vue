<template>
  <header class="header">
    <RouterLink to="/">Pealehele</RouterLink>
  </header>

  <main>
  <div class="card login-card">
    <h2>Admin sisselogimine</h2>
    <form @submit.prevent="login">
      <input type="text" class="text-input" v-model="username" placeholder="Kasutajanimi" required />
      <input type="password" class="text-input" v-model="password" placeholder="Parool" required />
      <p v-if="error" class="error">{{ error }}</p>
      <button type="submit" class="btn-primary">Logi sisse</button>
    </form>
  </div>
  </main>
</template>

<script>
import { testAuth, setCredentials } from '../api.js'

export default {
  data() {
    return { username: '', password: '', error: '' }
  },
  methods: {
    async login() {
      try {
        await testAuth(this.username, this.password)
        setCredentials(this.username, this.password)
        this.$router.push('/admin')
      } catch {
        this.error = 'Vale kasutajanimi või parool'
      }
    },
  },
}
</script>

<style scoped>
.login-card {
  margin-top: 8rem;
  max-width: 400px;
}

.btn-primary {
  width: auto;
  padding: 8px 24px;
  display: block;
  margin: 20px auto;

}
</style>
