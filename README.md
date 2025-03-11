# Identity Service
This identity service provides comprehensive user authentication and management capabilities using JWT 2.0 and Refresh Token Rotation.

## Table of Contents
- [Features](#features)
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

## Technology Stack
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- Docker
- Redis

## Prerequisites
- JDK 11 or higher
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
>  docker inspect [container id] | grep IPAddress
>  ```


## API Documentation 
- With Postman, [here](https://web.postman.co/workspace/My-Workspace~c548a04e-af06-4da2-893c-6d798885d848/collection/38352708-d661f49f-f537-40f6-bbac-ff86302da1a1?action=share&source=copy-link&creator=38352708&active-environment=dafc7c87-238c-434b-93a1-89aaa98c0047).
- Available endpoints.

    |Endpoint |	Method	| Description	| Required Role|
    |---|---|---|---|
    /identity/auth/login|	POST|	User login	|None|
    /identity/auth/register|	POST|	User registration	|None|
    /identity/auth/refresh|	POST|	Refresh access token	|None|
    /identity/auth/logout|	POST|	User logout	|User|
    /identity/users	|GET|	List all users	|Admin|
    /identity/users/{id}	|GET|	Get user details	|Admin|
    /identity/users/{id}	|PUT|	Update user	|Admin|
    /identity/users/{id}	|DELETE|	Delete user	|Admin|

- Error code:

    Error Code|	Name|	Message|
    ---|---|---|
    9999|	UNCATEGORIZED|	Uncategorized exception|
    _1xxx_|	System Errors	|	||
    1001|	INVALID_ERROR_KEY|	Invalid error key|
    1002|	INVALID_TOKEN_FORMAT|	Invalid token format|
    _2xxx_	|Validation Errors |||	
    2000|	FIRSTNAME_REQUIRED|	Firstname is required||
    2001|	LASTNAME_REQUIRED|	Lastname is required|	
    2002|	INVALID_USERNAME|	Username must be at least {min} characters|	
    _3xxx_|   User errors |||	
    3000|	CONFLICT|	Resource already existed|
    3001|	NOT_FOUND|	Resource not found|
    _4xxx_|   Auth errors |||
    4000|	UNAUTHENTICATED|	Username must be at least {min} characters|
    4001|	UNAUTHORIZED|	Username must be at least {min} characters|
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
