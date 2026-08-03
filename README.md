# Task Manager

A browser-based task manager built with Spring Boot 3.2.1, Spring Security with JWT, and Spring Data JPA. The recommended experience is to open the app in a browser and use the built-in UI for registration, sign-in, project creation, and task management. The REST API remains available for integrations and direct testing.

## Requirements

This project is intended for Windows users running the commands below in PowerShell.

- Windows 10 or newer
- Java 17
- Maven 3.9+
- A terminal such as PowerShell

## From start to finish

Follow this sequence in PowerShell. Open a PowerShell window in the project folder and type each command in that window unless a step says to use a second window.

1. Start the application:

```powershell
mvn spring-boot:run
```

2. Open a web browser and go to http://localhost:8080/. The landing page includes the browser UI for:

   - registering a new account
   - signing in
   - creating projects
   - creating and viewing tasks

3. Use the demo accounts or create your own account:

   - rrustia / password123
   - mike / password123
   - mary / password123

4. If you want to inspect the data directly, open the separate H2 database browser at http://localhost:8080/h2-console. This is not part of the main task manager GUI. On that page, fill in the form with these values: in the JDBC URL field, type jdbc:h2:file:./taskdb; in the Username field, type sa; leave the Password field empty, then press Connect.

5. If you need to work with the API directly, the auth and project/task endpoints are available as documented below. A typical PowerShell login example is:

```powershell
$body = '{"username":"rrustia","password":"password123"}'
$response = Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/auth/login" -ContentType "application/json" -Body $body
$response.token
```

6. Use the returned token in the Authorization header for protected requests when you need to call the API manually.

7. To see the data the app is storing, open the H2 console and type queries into the SQL box. For example, type SELECT * FROM users; to view users, SELECT * FROM projects; to view projects, or SELECT * FROM tasks; to view tasks. Then click Run to see the rows stored by the app.

The application starts on port 8080 and uses a local file-based H2 database stored in the project root. On the first start it seeds demo users, projects, and tasks so the browser UI has sample data to work with.

### Default URLs

- API base: http://localhost:8080
- H2 console: http://localhost:8080/h2-console
  - JDBC URL: jdbc:h2:file:./taskdb
  - Username: sa
  - Password: blank

The H2 database file is created in the project root of the workspace as taskdb.mv.db.

## Demo accounts

The application seeds these accounts on startup.

| Username | Password | Role | Notes |
|----------|----------|------|-------|
| rrustia | password123 | USER | Seeded user who owns the demo projects |
| mike | password123 | USER | Seeded user with assigned tasks |
| mary | password123 | USER | Seeded user for additional demo activity |

## Authentication and request examples

The auth endpoints are public:

- POST /api/auth/register
- POST /api/auth/login

The browser UI uses the same JWT-based authentication flow as the API. In practice, the browser handles sign-in and token storage for you, while create, update, and delete requests still require a bearer token.

### Log in with PowerShell

```powershell
$body = '{"username":"rrustia","password":"password123"}'
$response = Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/auth/login" -ContentType "application/json" -Body $body
$response.token
```

The response returns a JWT token, the user id, and the username.

### Call a project endpoint with the token

```powershell
$token = $response.token
$headers = @{ Authorization = "Bearer $token" }
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api/projects/owner/1" -Headers $headers
```

Using the `userId` from the login response is usually a better choice than hard-coding the id.

## Run the tests

From the project folder in PowerShell, run:

```powershell
mvn test
```

## API Endpoints

### Authentication (public)

- POST /api/auth/register - Register a new account
- POST /api/auth/login - Login and receive a JWT token

Include the token in later requests as: Authorization: Bearer <token>

### Projects

- GET /api/projects/owner/{ownerId} - Get projects for a user
- GET /api/projects/{id} - Get project details
- POST /api/projects/owner/{ownerId} - Create a project (requires auth)
- PUT /api/projects/{id} - Update a project (requires auth)
- DELETE /api/projects/{id} - Delete a project (requires auth)

### Tasks

- GET /api/projects/{projectId}/tasks - Get tasks in a project
- GET /api/projects/{projectId}/tasks/{taskId} - Get a specific task
- POST /api/projects/{projectId}/tasks - Create a task (requires auth)
- PUT /api/projects/{projectId}/tasks/{taskId} - Update a task (requires auth)
- PATCH /api/projects/{projectId}/tasks/{taskId}/status/{status} - Change task status (requires auth)
- DELETE /api/projects/{projectId}/tasks/{taskId} - Delete a task (requires auth)
- GET /api/projects/{projectId}/tasks/assignee/{assigneeId} - Tasks assigned to a user
- GET /api/projects/{projectId}/tasks/overdue - Overdue tasks

## Technologies Used

| Category | Technology |
|----------|------------|
| Language | Java 17 |
| Build Tool | Maven |
| Framework | Spring Boot 3.2.1 |
| Web/API | Spring Boot Starter Web |
| Security | Spring Security |
| Authentication | JWT (jjwt 0.12.3) |
| Data Access | Spring Data JPA |
| Validation | Spring Boot Starter Validation |
| Database | H2 Database (file-based) |
| Boilerplate Reduction | Lombok |
| Testing | Spring Boot Starter Test, Spring Security Test |

## Database Inspection

### H2 Console

While the app is running, open a web browser and go to http://localhost:8080/h2-console. Use jdbc:h2:file:./taskdb as the JDBC URL with username sa and no password.

### Example queries

Once connected, queries like these can be used:

```sql
SELECT * FROM users;
SELECT * FROM projects;
SELECT * FROM tasks;
```

To view all projects owned by a specific user, use:

```sql
SELECT id, name, description, owner_id
FROM projects
WHERE owner_id = 2;
```
