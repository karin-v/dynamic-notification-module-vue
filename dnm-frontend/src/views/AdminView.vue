<template>
  <header class="header">
    <RouterLink to="/">Pealehele</RouterLink>
    <button class="btn btn-logout" @click="logout">Logi välja</button>
  </header>

  <main>
  <div v-for="n in notifications" :key="n.id" class="card">
    <form v-if="editingId === n.id" @submit.prevent="saveEdit">
      <div class="notif-header">
        <input type="text" class="text-input" v-model="editTitle" placeholder="Pealkiri" required />
      </div>
      <textarea class="text-input" v-model="editContent" placeholder="Sisu" required></textarea>
      <p v-if="saveError" class="error">{{ saveError }}</p>
      <div class="card-actions">
        <button type="button" class="btn" @click="cancelEdit">Tühista</button>
        <button type="submit" class="btn">Salvesta</button>
      </div>
    </form>

    <template v-else>
      <div class="notif-header">
        <h3>{{ n.title }}</h3>
      </div>
      <p>{{ n.content }}</p>
      <div class="card-actions">
        <label>
          <input type="checkbox" :checked="n.active" @change="toggleActive(n)" />
          Aktiivne
        </label>
        <div class="btn-group">
          <button class="btn" @click="editNotification(n)">Muuda</button>
          <button class="btn btn-danger" @click="removeNotification(n.id)">Kustuta</button>
        </div>
      </div>
    </template>
  </div>

  <div class="card form-card">
    <h2>Lisa uus teadaanne</h2>
    <form @submit.prevent="addNotification">
      <input type="text" class="text-input" v-model="newTitle" placeholder="Pealkiri" required />
      <textarea class="text-input" v-model="newContent" placeholder="Sisu" required></textarea>
      <p v-if="addError" class="error">{{ addError }}</p>
      <div class="card-actions">
        <label>
          <input type="checkbox" v-model="newActive" />
          Aktiivne
        </label>
        <button type="submit" class="btn btn-add">Lisa</button>
      </div>
    </form>
  </div>
  </main>
</template>

<script>
import {
  getAllNotifications,
  createNotification,
  updateNotification,
  deleteNotification,
  clearCredentials,
} from '../api.js'

export default {
  data() {
    return {
      notifications: [],
      newTitle: '',
      newContent: '',
      newActive: false,
      editingId: null,
      editTitle: '',
      editContent: '',
      saveError: '',
      addError: '',
    }
  },
  mounted() {
    this.loadNotifications()
  },
  methods: {
    async loadNotifications() {
      try {
        this.notifications = await getAllNotifications()
      } catch {
        this.$router.push('/login')
      }
    },
    async addNotification() {
      if (!this.newTitle.trim() || !this.newContent.trim()) {
        this.addError = 'Pealkiri ja sisu ei tohi olla tühjad'
        return
      }
      this.addError = ''
      await createNotification({
        title: this.newTitle,
        content: this.newContent,
        active: this.newActive,
      })
      this.newTitle = ''
      this.newContent = ''
      this.newActive = false
      this.loadNotifications()
    },
    async removeNotification(id) {
      await deleteNotification(id)
      this.loadNotifications()
    },
    async toggleActive(n) {
      await updateNotification(n.id, {
        title: n.title,
        content: n.content,
        active: !n.active,
      })
      this.loadNotifications()
    },
    editNotification(n) {
      this.editingId = n.id
      this.editTitle = n.title
      this.editContent = n.content
      this.saveError = ''
    },
    async saveEdit() {
      if (!this.editTitle.trim() || !this.editContent.trim()) {
        this.saveError = 'Pealkiri ja sisu ei tohi olla tühjad'
        return
      }
      this.saveError = ''
      const current = this.notifications.find((x) => x.id === this.editingId)
      await updateNotification(this.editingId, {
        title: this.editTitle,
        content: this.editContent,
        active: current.active,
      })
      this.editingId = null
      this.loadNotifications()
    },
    cancelEdit() {
      this.editingId = null
      this.saveError = ''
    },
    logout() {
      clearCredentials()
      this.$router.push('/login')
    },
  },
}
</script>

<style scoped>
.header {
  justify-content: space-between;
}

.notif-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
  margin-bottom: 0.5rem;
}

.notif-header .text-input {
  flex: 1;
  margin-bottom: 0;
}

.card-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 2rem;
  margin-top: 1rem;
  font-size: 15px;
}

.notif-header .btn,
.card-actions .btn {
  min-width: 90px;
}

.form-card {
  padding: 1rem 2rem;
}

form .btn {
  padding: 4px 20px;
}

.btn-group {
  display: flex;
  gap: 0.5rem;
}

.btn {
  background: #f0f4f0;
}

.btn:hover {
  background: var(--color-primary-dark);
  color: #fff;
  border-color: var(--color-primary-dark);
}

.btn-add {
  background: var(--color-primary);
  color: #fff;
}

.btn-add:hover {
  background: var(--color-primary-dark);
  color: #fff;
  border-color: var(--color-primary-dark);
}

.btn-danger {
  border-color: #b91c1c;
  background: #fdecec;
  color: #b91c1c;
}

.btn-danger:hover {
  background: #b91c1c;
  border-color: #b91c1c;
  color: #fff;
}

</style>
