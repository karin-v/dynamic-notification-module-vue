const BASE = 'http://localhost:8080'

export function setCredentials(username, password) {
    localStorage.setItem('credentials', btoa(`${username}:${password}`))
}
export function clearCredentials() {
    localStorage.removeItem('credentials')
}
export function isLoggedIn() {
    return !!localStorage.getItem('credentials')
}
function authHeaders() {
    return { Authorization: 'Basic ' + localStorage.getItem('credentials') }
}

export async function testAuth(username, password) {
    const res = await fetch(`${BASE}/api/admin/notification`, {
        headers: { Authorization: 'Basic ' + btoa(`${username}:${password}`) },
    })
    if (!res.ok) throw new Error()
}

export async function getAllNotifications() {
    const res = await fetch(`${BASE}/api/admin/notification`, { headers: authHeaders() })
    if (!res.ok) throw new Error()
    return res.json()
}
export async function createNotification(n) {
    const res = await fetch(`${BASE}/api/admin/notification`, {
        method: 'POST',
        headers: { ...authHeaders(), 'Content-Type': 'application/json' },
        body: JSON.stringify(n),
    })
    if (!res.ok) throw new Error()
    return res.json()
}
export async function updateNotification(id, n) {
    const res = await fetch(`${BASE}/api/admin/notification/${id}`, {
        method: 'PUT',
        headers: { ...authHeaders(), 'Content-Type': 'application/json' },
        body: JSON.stringify(n),
    })
    if (!res.ok) throw new Error()
    return res.json()
}
export async function deleteNotification(id) {
    const res = await fetch(`${BASE}/api/admin/notification/${id}`, {
        method: 'DELETE',
        headers: authHeaders(),
    })
    if (!res.ok) throw new Error()
}
