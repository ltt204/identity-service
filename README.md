# Identity Service
This identity service provides comprehensive user authentication and management capabilities using JWT 2.0 and Refresh Token Rotation.

## Features
User management with administrative privileges
Authentication using JWT 2.0
Token management with Refresh Token Rotation technique
Role-based access control

## Technology Stack
Java
Spring Boot
Spring Security
Spring Data JPA
PostgreSQL
Docker

## Prerequisites
JDK 11 or higher
Docker and Docker Compose
Gradle

## Setup Instructions
1. Clone the repository:

```
git clone https://github.com/ltt204/identity-service.git
cd identity-service
```

2. Start the PostgreSQL database using Docker Compose:

``` 
docker compose up -d
```

3. Initialize the database schema: Navigate to `src/main/resources/db/migration` and execute all SQL files to set up the database structure.

4. Build and run the application:
```
./gradlew bootRun
```

5. The application will be available at:
```
http://localhost:8080/identity
```

## Configuration
The application uses the following configuration:

- Server port: `8080`
- Context path: `/identity`
- Database: PostgreSQL running locally on port `5432:5432`
- JWT token validity: 3600 seconds (1 hour)
- Refresh token validity: 360000 seconds (100 hours)
- You can modify these settings in the `application.yml` file.

## Security
This project implements security best practices:

## JWT authentication
Refresh Token Rotation to prevent token theft
Role-based authorization

## Project Purpose
This project was developed as a learning exercise for:

- Java
- Spring Boot
- Spring Data JPA
- Spring Security