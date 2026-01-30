# Workout Tracker

A RESTful API for tracking workouts, exercises, and training progress. Built as my graduation project for the **Full Stack Web Development Bootcamp** at **Coding Factory** (Athens University of Economics and Business).

## What This Does

I built this to solve a personal problem: tracking my gym workouts was scattered across notes and apps that didn't fit my needs. This API lets me create custom exercises, log workouts with detailed sets (weight, reps, rest periods), and review my training history over time.

The application demonstrates a complete backend implementation with JWT authentication, RESTful API design, and proper separation of concerns using Spring Boot best practices.

## Tech Stack

- **Java 21** - Latest LTS with modern language features
- **Spring Boot 4.0.2** - Rapid development framework with auto-configuration
- **Spring Security + JWT** - Stateless authentication for API-first architecture
- **Spring Data JPA** - Simplified database operations with repository pattern
- **MySQL** - Relational database for structured workout data
- **Lombok** - Reduces boilerplate code
- **Maven** - Dependency management and build automation 

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.6+
- MySQL 8.0+

### Setup

1. **Clone the repository**
```bash
git clone <repository-url>
cd workout-tracker
```

2. **Create the database**
```sql
CREATE DATABASE workouttrackerdb;
CREATE USER 'workouttrackeruser'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON workouttrackerdb.* TO 'workouttrackeruser'@'localhost';
FLUSH PRIVILEGES;
```

3. **Configure environment variables**

Set the following environment variables before running the application:

- `MYSQL_HOST` (default: `localhost`)
- `MYSQL_PORT` (default: `3306`)
- `MYSQL_DB` (default: `workouttrackerdb`)
- `MYSQL_USER` (default: `workouttrackeruser`)
- `MYSQL_PASSWORD` (required)
- `JWT_SECRET` (required, minimum 64 characters)
- `JWT_EXPIRATION_MS` (default: `86400000`)

You can set these in your system environment variables, IDE run configuration, or terminal session. Sensitive values (`MYSQL_PASSWORD` and `JWT_SECRET`) must be provided as they have no defaults.

4. **Run the application**
```bash
mvn clean install
mvn spring-boot:run
```

The API will be available at `http://localhost:8080/api/v1.0`


## How It Works

The application follows a layered architecture:

- **Controllers** handle HTTP requests and delegate to services
- **Services** contain business logic and orchestrate operations
- **Repositories** abstract data access using Spring Data JPA
- **Mappers** convert between entities and DTOs, keeping API contracts separate from domain models
- **Security** is handled via a JWT filter that validates tokens on protected endpoints

The data model supports users creating their own exercises, organizing them by muscle groups, and building workouts that contain multiple exercises with sets. Each set tracks weight, reps, notes and set type (e.g., warm-up vs. working sets).

## API Endpoints

### Authentication
- `POST /api/v1.0/auth/register` - Create a new account
- `POST /api/v1.0/auth/login` - Get JWT token

### Exercises
- `POST /api/v1.0/exercises` - Create exercise
- `GET /api/v1.0/exercises/{id}` - Get exercise by ID
- `GET /api/v1.0/exercises?muscleGroup={group}` - List exercises by muscle group
- `PUT /api/v1.0/exercises/{id}` - Update exercise
- `DELETE /api/v1.0/exercises/{id}` - Delete exercise

### Workouts
- `POST /api/v1.0/workouts` - Create workout
- `GET /api/v1.0/workouts/{id}` - Get workout by ID
- `GET /api/v1.0/workouts?startDate={date}&endDate={date}` - List workouts in date range
- `PUT /api/v1.0/workouts/{id}` - Update workout
- `DELETE /api/v1.0/workouts/{id}` - Delete workout

Protected endpoints require: `Authorization: Bearer <jwt-token>`

## Future Ideas

- [ ] Add workout templates/presets for common routines
- [ ] Implement progress tracking with charts (weight progression, volume over time)
- [ ] Support for workout sharing between users
- [ ] REST API documentation with Springdoc OpenAPI (Swagger)
- [ ] Database migrations with Flyway for production deployments
- [ ] Unit and integration test coverage expansion
- [ ] Docker containerization for easier deployment


## Project Structure

```
workout-tracker/
├── src/main/java/.../workouttracker/
│   ├── authentication/     # JWT and auth services
│   ├── controller/         # REST endpoints
│   ├── dto/                # Request/response DTOs
│   ├── entity/             # JPA entities
│   ├── exception/          # Custom exceptions
│   ├── mapper/             # Entity-DTO mappers
│   ├── repository/         # Data access
│   ├── security/           # Spring Security config
│   └── service/            # Business logic
└── src/main/resources/
    ├── application.properties
    └── logback-spring.xml
```

## Author

**Lampros Vasilakos**  
[GitHub Profile](https://github.com/lamprosvasilakos)

Graduation Project - Full Stack Web Development Bootcamp  
**Coding Factory** | Athens University of Economics and Business

---

*This project demonstrates backend development skills using Spring Boot, RESTful API design, and modern Java practices.*
