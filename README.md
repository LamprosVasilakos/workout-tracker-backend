# Workout Tracker API - Coding Factory Graduation Project

This is graduation project for the **Coding Factory** bootcamp hosted by Athens University of Economics and Business. A minimalist workout tracker backend for your daily gym sessions.

## Tech Stack

- **Backend**: Java 21 & Spring Boot 4.0.2
- **Database**: MySQL 8.0
- **Security**: Spring Security with JWT
- **Persistence**: Spring Data JPA
- **Mapping**: Custom Mappers

## Getting Started

### Prerequisites
- JDK 21+
- Maven 3.8+
- MySQL 8.0+

### Local Setup
1. **Clone the repository**
2. **Create the database**:
   ```sql
   CREATE DATABASE workouttrackerdb;
   ```
3. **Configure environment**:
   Set these variables or update `src/main/resources/application.properties`:
   - `MYSQL_USER`: Your username
   - `MYSQL_PASSWORD`: Your password
4. **Run**:
   ```bash
   ./mvnw spring-boot:run
   ```
The API runs at `http://localhost:8080/api/v1.0`.

## Architecture

The application uses a **Workout-Centric Aggregate Root** design. 

- **Simplified API**: I removed standalone controllers for sets and workout-exercises. All updates happen through the `/workouts` and `/exercises` endpoints to ensure consistency and reduce client complexity.
- **Anemic Entities**: Entities store data. Mappers manage bidirectional relationships using Java functional streams.
- **JWT Auth**: A custom filter validates JWT tokens for all protected requests.

## API Documentation

Protected endpoints require an `Authorization: Bearer <token>` header.

### Authentication (`/auth`)
- `POST /auth/register`: Create an account.
- `POST /auth/login`: Exchange credentials for a JWT.

### Exercises (`/exercises`)
- `GET /exercises`: List exercises (requires `muscleGroup` filter).
- `GET /exercises/{id}`: Get an exercise template.
- `POST /exercises`: Create an exercise template.
- `PUT /exercises/{id}`: Update a template.
- `DELETE /exercises/{id}`: Remove a template.

### Workouts (`/workouts`)
- `GET /workouts`: List workouts for a date range (requires `startDate` and `endDate` in ISO format).
- `GET /workouts/{id}`: Get a workout with its exercises and sets.
- `POST /workouts`: Log a workout with nested data.
- `PUT /workouts/{id}`: Update a workout and its nested exercises/sets.
- `DELETE /workouts/{id}`: Remove a workout record.

## What I Learned

- **Aggregate Root Design**: Simplified the API by managing nested entities (Sets, Exercises) exclusively through the Workout root.
- **JPA Relationship Management**: Implemented complex bidirectional associations while keeping entity classes clean and anemic.
- **Security & Mapping**: Integrated custom JWT authentication and used functional mappers to decouple DTOs from the database model.
- **Global Error Handling**: Implemented a centralized exception handling strategy using `@ControllerAdvice` to ensure consistent API error responses.

