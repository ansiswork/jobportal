# Job Portal REST API

A backend REST API for a job portal, built with Java 17 and Spring Boot. The application supports user registration and JWT-based login, job posting management, and job application workflows, with MySQL persistence and role-based authorization.

## Features

### Authentication and authorization
- Register users with a name, email, and password.
- Hash passwords using BCrypt.
- Authenticate users and issue JSON Web Tokens (JWT).
- Protect API routes using Spring Security.
- Restrict job administration and application administration to users with the `ADMIN` role.

### Job management
- Create, update, and delete job postings (admin only).
- Retrieve a job by ID.
- Retrieve paginated job listings.
- Store job title, description, company, location, and salary.

### Job applications
- Authenticated users can apply for a job.
- Prevent a user from applying to the same job more than once.
- Users can retrieve their own applications.
- Admins can retrieve all applications and update application status.
- Application statuses supported by the status-update request: `ACCEPTED` and `REJECTED`; new applications start as `PENDING`.

### Validation and error handling
- Validate incoming request data.
- Handle missing jobs, invalid credentials, and invalid request data through centralized exception handling.

## Technology stack

- Java 17
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA / Hibernate
- Spring Security
- JSON Web Token (JJWT)
- MySQL
- Maven
- Jakarta Bean Validation
- JUnit 5, Mockito, MockMvc, and Spring Security Test

## API reference

Base URL: `http://localhost:8080`

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/api/auth/register` | Register a user | Public |
| `POST` | `/api/auth/login` | Log in and receive a JWT | Public |
| `GET` | `/api/jobs?page=0&size=5` | Get a page of job listings | Authenticated |
| `GET` | `/api/jobs/{id}` | Get a job by ID | Authenticated |
| `POST` | `/api/jobs` | Create a job | `ADMIN` |
| `PUT` | `/api/jobs/{id}` | Update a job | `ADMIN` |
| `DELETE` | `/api/jobs/{id}` | Delete a job | `ADMIN` |
| `POST` | `/api/applications` | Apply for a job | Authenticated |
| `GET` | `/api/applications/my` | Get the current user's applications | Authenticated |
| `GET` | `/api/applications/admin/all` | Get all applications | `ADMIN` |
| `PUT` | `/api/applications/admin/{applicationId}/status` | Update an application status | `ADMIN` |

For protected endpoints, include the token returned by login:

```http
Authorization: Bearer YOUR_JWT_TOKEN
```

### Example requests

Register:

```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "Alex Developer",
  "email": "alex@example.com",
  "password": "change-this-password"
}
```

Log in:

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "alex@example.com",
  "password": "change-this-password"
}
```

Create a job (admin token required):

```http
POST /api/jobs
Authorization: Bearer YOUR_ADMIN_JWT
Content-Type: application/json

{
  "title": "Java Developer",
  "description": "Build and maintain Java applications.",
  "company": "Example Company",
  "location": "Atlanta",
  "salary": 95000
}
```

Apply for a job (authenticated user token required):

```http
POST /api/applications
Authorization: Bearer YOUR_USER_JWT
Content-Type: application/json

{
  "jobId": 1
}
```

## Run locally

### Prerequisites

- JDK 17
- MySQL Server
- Git
- An IDE such as Eclipse (optional)

### 1. Clone the repository

```bash
git clone https://github.com/ansiswork/jobportal
cd jobportal
```

### 2. Create the database

Run this in MySQL:

```sql
CREATE DATABASE jobportal;
```

### 3. Configure environment variables

The application reads database credentials and the JWT signing key from environment variables. Set these values in your shell or in the Eclipse Run Configuration:

| Variable | Purpose |
|---|---|
| `DB_USERNAME` | MySQL username |
| `DB_PASSWORD` | MySQL password |
| `JWT_SECRET` | Base64-encoded JWT signing key (at least 32 bytes before Base64 encoding) |

Do not commit real credentials, tokens, or signing keys to source control.

A Base64 secret can be generated using the project's `GenerateSecret` Java class. Keep the generated value private.

### 4. Run the application

On macOS/Linux:

```bash
./mvnw spring-boot:run
```

On Windows:

```bat
mvnw.cmd spring-boot:run
```

Or import the folder into Eclipse using **File → Import → Maven → Existing Maven Projects**, select the project folder, and run `JobportalApplication.java` after setting the environment variables in its Run Configuration.

The API is available at `http://localhost:8080`.

### 5. Run tests

On macOS/Linux:

```bash
./mvnw test
```

On Windows:

```bat
mvnw.cmd test
```

The repository includes controller, service, and security tests. Run the test suite locally and confirm it passes before relying on the result in a portfolio or resume.

## Project structure

```text
src/
├── main/
│   ├── java/com/example/jobportal/
│   │   ├── config/         # Security configuration
│   │   ├── controller/     # REST controllers
│   │   ├── dto/            # Request and response objects
│   │   ├── entity/         # JPA entities
│   │   ├── exception/      # Exception handling
│   │   ├── repository/     # Spring Data repositories
│   │   ├── security/       # JWT authentication
│   │   ├── service/        # Business logic
│   │   └── specification/  # Job query specifications
│   └── resources/
│       └── application.properties
└── test/
    └── java/               # Controller, service, and security tests
```

## Current implementation notes

- This repository contains the backend REST API; no frontend application is included.
- Job listings currently expose pagination through the REST controller. Search-related service/repository methods exist in the code, but search parameters are not currently exposed by a controller endpoint.
- Authorization currently uses `USER` and `ADMIN` roles; there is no separate employer role in the current implementation.
- Database schema generation is configured with Hibernate `ddl-auto=update`. For production deployments, consider using a database migration tool such as Flyway or Liquibase and reviewing the schema strategy.

## License

No license has been selected yet. Add a `LICENSE` file before presenting this repository as open source.
