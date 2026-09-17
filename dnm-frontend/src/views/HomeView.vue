<template>
  <header class="header">
    <RouterLink to="/admin">Admin</RouterLink>
  </header>

  <main>
  <div class="banner-area">
    <div v-if="notification && notification.status === 'ACTIVE'" class="notif-banner">
      <h2>⚠️ {{ notification.title }}</h2>
      <p>
        {{ notification.content }}
        <span class="timestamp">({{ formatDate(notification.timestamp) }})</span>
      </p>
    </div>

    <div v-if="showOkMessage" class="notif-banner success">
      <h2>✅ Kõik korras!</h2>
      <p>Süsteemid töötavad ootuspäraselt!</p>
    </div>
  </div>

  <div class="main-content">
    <p class="quote">"It's still magic even if you know how it's done"</p>
    <p class="emoji">🌿</p>
    <p class="author">- Terry Pratchett</p>
  </div>
  </main>
</template>

<script>
export default {
  data() {
    return {
      notification: null,
      showOkMessage: false,
    }
  },
  async mounted() {
    const res = await fetch('http://localhost:8080/api/notification')
    this.notification = await res.json()

    if (this.notification.status === 'OPERATIONAL') {
      this.showOkMessage = true
      setTimeout(() => { this.showOkMessage = false }, 3000)
    }
  },
  methods: {
    formatDate(ts) {
      const d = new Date(ts)
      const p = (n) => String(n).padStart(2, '0')
      return `${p(d.getDate())}.${p(d.getMonth() + 1)}.${d.getFullYear()} ${p(d.getHours())}:${p(d.getMinutes())}`
    },
  },
}
</script>

<style scoped>
.banner-area {
  min-height: 110px;
}

.notif-banner {
  padding: 1rem 1rem;
  border-radius: var(--radius-card);
  margin: 0 auto;
  max-width: var(--content-width);
  background-color: #fdf9f0;
  border: 1px solid #f0dfa8;
}

.notif-banner h2 {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 0.5rem;
}

.notif-banner.success {
  background-color: var(--color-surface);
  border-color: #cdddce;
}

.timestamp {
  color: var(--color-text);
  opacity: 0.65;
}

.main-content {
  text-align: center;
  margin: 6rem auto;
  padding: 3rem;
}

.quote {
  font-size: 1.2rem;
  font-style: italic;
  color: var(--color-text-accent);
}

.author {
  font-size: 15px;
  color: var(--color-text-accent);
  margin-top: 0.5rem;
}

.emoji {
  font-size: 2rem;
  margin: 1rem 0;
}
</style>
