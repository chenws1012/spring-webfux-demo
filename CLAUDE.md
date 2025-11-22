# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring WebFlux 用户管理服务 - A reactive user management service built with Spring WebFlux that demonstrates complete CRUD operations, pagination, search functionality, and password security.

## Technology Stack

- **Spring Boot 3.3.0** - Application framework
- **Spring WebFlux** - Reactive web framework with Project Reactor
- **R2DBC** - Reactive database connectivity
- **PostgreSQL** - Primary database (with H2 available for testing)
- **Maven** - Build tool
- **Lombok** - Reduce boilerplate code
- **SpringDoc OpenAPI** - API documentation
- **Spring Security Crypto** - Password encryption

## Development Commands

### Build and Run
```bash
# Compile the project
mvn clean compile

# Run the application (requires PostgreSQL running)
mvn spring-boot:run

# Build JAR package
mvn clean package

# Run the packaged JAR
java -jar target/webflux-demo-1.0.0.jar
```

### Testing
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run tests with coverage
mvn clean test jacoco:report
```

### Database Setup
```bash
# Start PostgreSQL with Docker
docker run -d --name postgres-webflux \
  -e POSTGRES_DB=webflux_demo \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=123456 \
  -p 5432:5432 \
  postgres:13

# Initialize database schema (runs automatically on startup)
# Manual execution: psql -d webflux_demo -f src/main/resources/schema.sql
```

## Architecture

### Layer Structure
- **Controller Layer** (`UserController`) - REST API endpoints with OpenAPI documentation
- **Service Layer** (`UserService`) - Business logic with transaction management
- **Repository Layer** (`UserRepository`) - Reactive data access using R2DBC
- **Model Layer** (`User`) - Domain entities with validation

### Key Design Patterns
- **Reactive Programming** - All methods return `Mono` or `Flux` types
- **Dependency Injection** - Constructor-based injection throughout
- **Transaction Management** - `@Transactional` at service layer
- **Validation** - JSR-303 annotations with custom business rules

### Security Features
- **Password Encryption** - BCrypt encoding with strength validation
- **Data Validation** - Comprehensive input validation at model level
- **Duplicate Prevention** - Unique constraints on username and email

## Database Configuration

The application uses PostgreSQL by default with R2DBC. Configuration is in `application.yml`:

```yaml
spring:
  r2dbc:
    url: r2dbc:postgresql://localhost:5432/webflux_demo
    username: postgres
    password: 123456
```

For development, H2 in-memory database is available but commented out in the configuration.

## API Endpoints

All endpoints are prefixed with `/api/users` and return standardized JSON responses:

- `POST /api/users` - Create user
- `GET /api/users` - Get all users
- `GET /api/users/page` - Get paginated users
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `GET /api/users/search/username` - Search by username
- `GET /api/users/search/email` - Search by email
- `GET /api/users/count` - Get user count

## Development Notes

### Reactive Programming
- All database operations are non-blocking
- Use `Mono` for single results, `Flux` for multiple results
- Handle errors with `onErrorResume` operators
- Use `flatMap` for sequential reactive operations

### Password Handling
- Passwords are automatically BCrypt-encoded
- Password strength validation requires: uppercase, lowercase, digits, and special characters
- Use `User.setPasswordEncoded()` and `User.verifyPassword()` methods

### Data Validation
- User model uses comprehensive validation annotations
- Custom password strength validation in `PasswordUtils`
- Duplicate checking for username and email

### Testing Strategy
- Reactive tests use `reactor-test` for testing reactive streams
- Integration tests with embedded PostgreSQL or H2
- Mock external dependencies where possible

## Monitoring

Spring Boot Actuator endpoints are available:
- `/actuator/health` - Application health
- `/actuator/info` - Application information
- `/actuator/metrics` - Performance metrics

## Code Style

- Use Lombok annotations to reduce boilerplate
- Follow reactive programming best practices
- Maintain consistent error handling patterns
- Use SLF4J for logging with appropriate log levels