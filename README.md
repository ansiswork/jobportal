Job Portal REST API

Project Overview

The Job Portal REST API is a backend application built using Java and Spring Boot. It provides functionality for users to register, log in, browse job postings, and submit job applications.

The application also includes administrative functionality that allows administrators to create, update, and delete job postings, view submitted applications, and manage application statuses.

The project uses Spring Security and JSON Web Tokens (JWT) to provide secure authentication and role-based authorization. MySQL is used to store application data, while Spring Data JPA handles database operations.

## Technology Stack

The following technologies and tools were used to develop this project:

- **Java 17** – Primary programming language.
- **Spring Boot 4.1.0** – Application framework.
- **Spring Web MVC** – Building REST endpoints.
- **Spring Data JPA** – Database access and persistence.
- **Spring Security** – Authentication and authorization.
- **JWT (JSON Web Token)** – Token-based authentication.
- **BCrypt** – Password hashing.
- **MySQL** – Relational database.
- **Maven** – Dependency management and project build.
- **Jakarta Bean Validation** – Request data validation.
- **JUnit 5 and Mockito** – Automated unit testing.
- **MockMvc** – Controller and HTTP endpoint testing.
- **Eclipse IDE** – Development environment.
- **Postman** – API testing.

## Features

### 1. User Authentication and Authorization
- User registration with name, email, and password.
- Secure password hashing using BCrypt.
- User login with JWT generation.
- Token-based authentication.
- Role-based access control for USER and ADMIN roles.

### 2. Job Management
- Create new job postings.
- Retrieve all available jobs.
- Retrieve individual job postings by ID.
- Update existing job postings.
- Delete job postings.
- Search jobs by title.
- Filter job listings.
- Paginate job results.

### 3. Job Application Management
- Authenticated users can apply for jobs.
- Prevent users from applying to the same job more than once.
- Users can view their own applications.
- Administrators can view all applications.
- Administrators can update application statuses.
- Application statuses include PENDING, ACCEPTED, and REJECTED.

### 4. Validation and Error Handling
- Validate incoming request data.
- Handle invalid requests.
- Handle duplicate job applications.
- Handle missing resources through centralized exception handling.

### 5. Automated Testing
- Unit tests for service-layer functionality.
- Controller tests for REST endpoints.
- Security integration tests.
- Mocking dependencies using Mockito.

## API Endpoints

Base URL: `http://localhost:8080`

### Authentication

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/auth/register` | Register a new user | Public |
| POST | `/api/auth/login` | Authenticate and receive JWT | Public |

### Job Management

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/jobs` | Create a job posting | ADMIN |
| GET | `/api/jobs` | Retrieve paginated job listings | Authenticated |
| GET | `/api/jobs/{id}` | Retrieve a job by ID | Authenticated |
| PUT | `/api/jobs/{id}` | Update a job posting | ADMIN |
| DELETE | `/api/jobs/{id}` | Delete a job posting | ADMIN |

### Job Applications

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/applications` | Apply for a job | Authenticated |
| GET | `/api/applications/my` | View your applications | Authenticated |
| GET | `/api/applications/admin/all` | View all applications | ADMIN |
| PUT | `/api/applications/admin/{applicationId}/status` | Update application status | ADMIN |

### Authentication Header

For protected endpoints, include the JWT in the HTTP Authorization header:

`Authorization: Bearer YOUR_JWT_TOKEN`

Replace `YOUR_JWT_TOKEN` with the token received from the login endpoint.

## Installation and Setup

### Prerequisites

Before running the application, make sure you have installed:

- Java 17 or later
- MySQL Server
- MySQL Workbench (optional, for database management)
- Eclipse IDE or another Java IDE
- Maven

### 1. Clone the Repository

Clone the project from GitHub:

```bash
git clone YOUR_GITHUB_REPOSITORY_URL
```

Navigate into the project directory:

```bash
cd jobportal
```

### 2. Create the MySQL Database

Open MySQL Workbench and execute:

```sql
CREATE DATABASE jobportal;
```

### 3. Configure Database Credentials

Update `src/main/resources/application.properties` with your local MySQL configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/jobportal
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

app.jwt.secret=${JWT_SECRET}
app.jwt.expiration-ms=3600000
```

Set the following environment variables before running the application:

- `DB_USERNAME` – Your MySQL username.
- `DB_PASSWORD` – Your MySQL password.
- `JWT_SECRET` – A secure Base64-encoded secret of at least 32 bytes.

Never commit actual passwords or JWT secrets to GitHub.

### 4. Run the Application

Using Eclipse:
1. Import the project as an existing Maven project.
2. Wait for Maven dependencies to download.
3. Configure the required environment variables in the Run Configurations.
4. Run `JobportalApplication.java` as a Java Application.

Alternatively, use Maven:

```bash
mvn spring-boot:run
```

The application will start at:

`http://localhost:8080`

### 5. Run Automated Tests

Execute the following command:

```bash
mvn test
```

This runs the project's automated test suite.