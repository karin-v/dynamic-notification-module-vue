# Dynamic Notification Module

A full-stack notification management system built to explore real-time system status
communication between a backend and a public-facing frontend. Administrators can
create, edit, activate and delete notifications through a protected admin panel; the
public view displays the currently active notification, or confirms that all systems
are operational.

## Tech Stack

**Backend**
- Java 21
- Spring Boot 3.5
- Spring Security (Basic Authentication)
- Spring Data JPA
- PostgreSQL
- Gradle (Kotlin DSL)

**Frontend**
- Vue 3 (Options API)
- Vue Router
- Vite

## Prerequisites

- Java 21
- Node.js 24+
- PostgreSQL (running locally)

## Setup

### 1. Database

Make sure PostgreSQL is running, then create the database:

```bash
createdb dnm
```

If `createdb` is not available, create it from the `psql` shell instead:

```sql
CREATE DATABASE dnm;
```

The tables are created automatically on first backend startup (`ddl-auto=update`),
so no manual schema setup is needed.

### 2. Backend

From the project root:

1. Copy the example config and fill in your values:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

2. Edit `application.properties` with your database credentials and the admin
   login you want to use:

```properties
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.security.user.name=YOUR_ADMIN_USERNAME
spring.security.user.password=YOUR_ADMIN_PASSWORD
```

3. Run the backend:

```bash
./gradlew bootRun
```

Backend starts on `http://localhost:8080`

### 3. Frontend

From the project root:

```bash
cd dnm-frontend
npm install
npm run dev
```

Frontend starts on `http://localhost:5173`

## Authentication

Admin credentials are configured in `application.properties` via
`spring.security.user.name` and `spring.security.user.password`. Use these
credentials to log in to the admin view.

The public endpoint `/api/notification` requires no authentication.

## Usage

- **User view:** `http://localhost:5173` — displays the active notification, or an
  "all systems operational" message
- **Admin view:** `http://localhost:5173/admin` — manage notifications (login required)

## API Endpoints

**Public**
- `GET /api/notification` — Get the active notification (or operational status if none is active)

**Admin** (Basic Auth required)
- `GET /api/admin/notification` — Get all notifications
- `GET /api/admin/notification/{id}` — Get a notification by ID
- `POST /api/admin/notification` — Create a notification
- `PUT /api/admin/notification/{id}` — Update a notification
- `DELETE /api/admin/notification/{id}` — Delete a notification

## Possible Improvements

- **Authentication** — the frontend currently stores Base64-encoded credentials in
  `localStorage` and sends them via HTTP Basic Auth on each request. Base64 is
  encoding, not encryption, so this is not secure for production. A token-based
  approach (e.g. JWT with short-lived tokens) would avoid keeping reusable
  credentials in the browser.
- **Testing** — no unit or integration tests yet. Would add JUnit tests for
  service-layer logic (e.g. only one notification can be active at a time,
  validation rules) and Vue component/API tests on the frontend.
- **Input constraints** — title/content currently only reject blank values;
  a maximum length would prevent excessively long notifications.
