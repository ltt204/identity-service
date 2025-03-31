# Identity Service

A robust, secure authentication and authorization system built with Clean Architecture principles. This identity service implements JWT 2.0 authentication with Refresh Token Rotation, comprehensive user management, and fine-grained role-based access control.

The project demonstrates best practices in software architecture by strictly separating concerns into independent layers (domain, application, infrastructure, and presentation) while maintaining a rich feature set including token blacklisting with Redis, comprehensive user administration, and sophisticated permission management.

Built with Java 17, Spring Boot, and modern security practices, this project serves as both a practical microservice component and a reference implementation of Clean Architecture in a security-focused application.

## Table of Contents
- [Features](#features)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [API Documentation](#api-documentation)
- [Configuration](#configuration)
- [Security](#security)
- [Project Purpose](#project-purpose)


## Features
- User management with administrative privileges
- Authentication using JWT 2.0
- Token management with Refresh Token Rotation technique
- Role-based access control

## Architecture

This project follows Clean Architecture principles with the following layers:

### Domain Layer (Core)
- Contains enterprise business rules and entities
- Has no dependencies on outer layers
- Located in `src/main/java/org/ltt204/identityservice/domain/`
  - `entities/` - Core domain models
  - `repositories/` - Repository interfaces
  - `services/` - Domain service interfaces

### Application Layer
- Contains application-specific business rules
- Orchestrates the flow of data between domain entities and outer layers
- Located in `src/main/java/org/ltt204/identityservice/application/`
  - `ports/` - Input and output ports (interfaces)
  - `services/` - Use case implementations
  - `dtos/` - Data Transfer Objects for application layer
  - `repositories/` - Repository adapters implementing domain interfaces

### Infrastructure Layer
- Contains frameworks, drivers, and tools
- Implements interfaces defined in inner layers
- Located in `src/main/java/org/ltt204/identityservice/infra/`
  - `persist/` - Database implementation
    - `jpa/` - JPA entities and repositories
    - `mappers/` - Entity mappers
  - `security/` - Security configurations
  - `config/` - Other configurations

### Presentation Layer
- Contains UI or API controllers
- Presents data to the user and handles input
- Located in `src/main/java/org/ltt204/identityservice/presentations/`
  - `web/` - Web controllers
  - `dtos/` - Request and response DTOs
  - `mappers/` - Mappers between DTOs and application models
  - `advices/` - Exception handlers

## Technology Stack
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- Docker
- Redis

## Prerequisites
- JDK 17 or higher
- Docker and Docker Compose
- MySQL workbench

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

3. In MySQL workbench, connect to database service at "localhost:3306".

4. Initialize the database schema: Navigate to `src/main/resources/db/migration` and execute all SQL files to set up the database structure in MySQL workbench.

5. Build and run the application:
    ```
    ./gradlew bootRun
    ```

6. The application will be available at:
    ```
    http://localhost:8080/identity
    ```

7. You can use RedisInsight at `172.19.0.4:5540`.
    > Note: The IP address might be different, you can check it in terminal with:
    >  ```bash
    >  docker inspect [redis container id] | grep IPAddress
    >  ```


## API Documentation 
### Available Endpoints
|Endpoint |	Method	| Description	| Required Role|
|---|---|---|---|
`/identity/auth/login`|	POST|	User login	|None|
`/identity/auth/register`|	POST|	User registration	|None|
`/identity/auth/refresh`|	POST|	Refresh access token	|None|
`/identity/auth/logout`|	POST|	User logout	|User|
`/identity/users`	|GET|	List all users	|Admin|
`/identity/users/{id}`	|GET|	Get jpaUser details	|Admin|
`/identity/users/{id}`	|PUT|	Update jpaUser	|Admin|
`/identity/users/{id}`	|DELETE|	Delete jpaUser	|Admin|

- [Postman Documentation](https://documenter.getpostman.com/view/38352708/2sB2cRBP58).

### Error code:
Error Code|	Name|	Message|
---|---|---|
9999|	UNCATEGORIZED|	Uncategorized exception|
_1xxx_|	System Errors	|	||
1001|	INVALID_ERROR_KEY|	Invalid error key|
1002|	INVALID_TOKEN_FORMAT|	Invalid token format|
_2xxx_	|**Validation Errors** |||	
2000|	FIRSTNAME_REQUIRED|	Firstname is required||
2001|	LASTNAME_REQUIRED|	Lastname is required|	
2002|	INVALID_USERNAME|	Username must be at least {min} characters|	
_3xxx_|   **User errors** |||	
3000|	CONFLICT|	Resource already existed|
3001|	NOT_FOUND|	Resource not found|
_4xxx_|   **Auth errors** |||
4000|	UNAUTHENTICATED|	Unauthenticated|
4001|	UNAUTHORIZED|	You are not authorized for this action|

## Configuration
The application uses the following configuration:

- Server port: `8080`
- Context path: `/identity`
- Database: MySQL running locally on port `3306:3306`
- Redis: Redis running locally on port `6379`
- Redis Insight: Redis Insight running locally on port `5540`
- JWT token validity: 3600 seconds (1 hour)
- Refresh token validity: 360000 seconds (100 hours)
- You can modify these settings in the `application.yml` file.

## Security
This project implements security best practices:
- JWT-based authentication
- Refresh Token Rotation to prevent token theft
- Redis-based token blacklisting
- Role-based authorization
- Password hashing

## JWT authentication
Refresh Token Rotation to prevent token theft
Role-based authorization

## Project Purpose
This project was developed as a learning exercise for:

- Java
- Spring Boot
- Spring Data JPA/Redis
- Spring Security
- Redis
