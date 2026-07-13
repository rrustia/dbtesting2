# Task Manager API

A RESTful task management API built with Spring Boot 3, Spring Security (JWT), and Spring Data JPA.

## Quick Start

```bash
mvn spring-boot:run
```

The app starts on port 8080 with an in-memory H2 database.

### Sample Credentials

The application seeds these demo accounts on startup for local testing and API exploration.

| Username   | Password  | Role   |
|------------|-----------|--------|
| admin      | admin123  | ADMIN  |
| johndoe    | pass123   | USER   |
| janedoe    | pass123   | USER   |

## How to Test It

### 1. Start the application

```bash
mvn spring-boot:run
```

The app runs on port 8080 and uses an in-memory H2 database.

### 2. Try the API

#### In a web browser

If you are using Brave, you can test the API without leaving the browser by using the built-in developer tools. This approach is helpful when you want to inspect requests and responses visually.

1. Start the application and make sure it is running on port 8080.
2. Open Brave and go to `http://localhost:8080` or any page that loads successfully.
3. Open the developer tools by pressing `F12` or selecting the Brave menu, then `More tools`, then `Developer tools`.
4. Switch to the `Network` tab so you can see HTTP traffic.
5. In the network tab, keep the `Preserve log` option enabled if you want to inspect multiple requests in one session.
6. To send a `POST` request using the network panel, open a simple browser-based REST client or extension, or use a small HTML form served from a local file. In that tool, enter the URL `http://localhost:8080/api/auth/login`, choose `POST`, and set the request content type to `application/json`.
7. Paste the following JSON body into the request body field:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

8. Click `Send` or `Submit`. Brave will then show the request in the Network tab, and the response panel will display the server's JSON response.
9. After the request completes, look for the response body in the network panel. The server should return a JWT token.
10. Copy that token and use it in the next request as an authorization header:

```http
Authorization: Bearer <token>
```

11. Send a `GET` request to `http://localhost:8080/api/projects/owner/1` with that header included.
12. If the request is successful, the response will show the projects owned by user `1` in JSON format.

This Brave-based workflow is useful for testing the API visually, inspecting the HTTP headers, and confirming that authentication and protected endpoints are working correctly.

#### In Postman

1. Create a new `POST` request to `http://localhost:8080/api/auth/login`.
2. Set the body to JSON with the sample credentials above.
3. Copy the returned `token`.
4. Create a new `GET` request to `http://localhost:8080/api/projects/owner/1` and add the header `Authorization: Bearer <token>`.

#### With curl

Login:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

Use the token to call a protected endpoint:

```bash
curl -X GET http://localhost:8080/api/projects/owner/1 \
  -H "Authorization: Bearer <token>"
```

## API Endpoints

### Authentication (public)

- `POST /api/auth/register` - Register a new account
- `POST /api/auth/login` - Login and receive a JWT token

Include the token in subsequent requests as: `Authorization: Bearer <token>`

### Projects

- `GET /api/projects/owner/{ownerId}` - Get projects for a user
- `GET /api/projects/{id}` - Get project details
- `POST /api/projects/owner/{ownerId}` - Create a project
- `PUT /api/projects/{id}` - Update a project
- `DELETE /api/projects/{id}` - Delete a project

### Tasks

- `GET /api/projects/{projectId}/tasks` - Get tasks in a project
- `GET /api/projects/{projectId}/tasks/{taskId}` - Get a specific task
- `POST /api/projects/{projectId}/tasks` - Create a task
- `PUT /api/projects/{projectId}/tasks/{taskId}` - Update a task
- `PATCH /api/projects/{projectId}/tasks/{taskId}/status/{status}` - Change task status
- `DELETE /api/projects/{projectId}/tasks/{taskId}` - Delete a task
- `GET /api/projects/{projectId}/tasks/assignee/{assigneeId}` - Tasks assigned to a user
- `GET /api/projects/{projectId}/tasks/overdue` - Overdue tasks

## Tech Stack

- Java 17
- Spring Boot 3.2
- Spring Security with JWT (jjwt)
- Spring Data JPA
- H2 Database (in-memory, swap to PostgreSQL for production)
- Maven

## Database Inspection

### H2 Console

Visit `http://localhost:8080/h2-console` while the app is running. Use `jdbc:h2:mem:taskdb` as the JDBC URL with username `sa` and no password.

### Example queries

Once connected, you can run queries like these:

```sql
SELECT * FROM users;
SELECT * FROM projects;
SELECT * FROM tasks;
```

To view all projects owned by a specific user, use:

```sql
SELECT id, name, description, owner_id
FROM projects
WHERE owner_id = 1;
```

### Web GUI request examples

If you prefer a browser-based API client instead of the H2 console, use this flow:

1. Open `http://localhost:8080/api/auth/login`.
2. Set the method to `POST` and send:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

3. Copy the returned token.
4. Open `http://localhost:8080/api/projects/owner/1` and set:
   - Method: `GET`
   - Header: `Authorization: Bearer <token>`

This returns the projects for owner `1` after authentication.
