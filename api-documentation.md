# API Documentation: Workout Tracker

## Base URL

All endpoints are prefixed with `/api/v1.0`.

## Authentication

The API uses JWT Bearer token authentication. After successful login, include the token in the `Authorization` header:

```
Authorization: Bearer <token>
```

- Token expiration: 24 hours (86400000 ms) by default, configurable via `JWT_EXPIRATION_MS`
- Public endpoints: `/auth/login`, `/auth/register`
- All other endpoints require authentication
- Invalid/expired tokens return `401 Unauthorized`
- Missing tokens on protected endpoints return `401 Unauthorized`

## Endpoints

### POST /auth/register
- **Purpose**: Create a new user account
- **Auth**: Public
- **Request**:
  ```json
  {
    "username": "string (3-50 chars, required)",
    "password": "string (6-50 chars, required)"
  }
  ```
- **Response** (success, 201 Created):
  ```json
  {
    "id": "uuid",
    "username": "string",
    "createdAt": "2024-01-29T10:30:00" // ISO 8601 datetime
  }
  ```
- **Response** (error):
  - `400 Bad Request`: Validation errors (field-level messages)
  - `409 Conflict`: Username already exists
    ```json
    {
      "code": "Username",
      "description": "User with username <username> already exists"
    }
    ```
- **Notes**: Username uniqueness is enforced. Passwords are hashed with BCrypt.

### POST /auth/login
- **Purpose**: Authenticate user and receive JWT token
- **Auth**: Public
- **Request**:
  ```json
  {
    "username": "string (required)",
    "password": "string (required)"
  }
  ```
- **Response** (success, 200 OK):
  ```json
  {
    "username": "string",
    "token": "string (JWT token)"
  }
  ```
- **Response** (error):
  - `400 Bad Request`: Validation errors
  - `401 Unauthorized`: Invalid credentials
    ```json
    {
      "code": "InvalidCredentials",
      "description": "Wrong username or password"
    }
    ```
- **Notes**: Store the token securely. Include it in all subsequent requests.

### POST /exercises
- **Purpose**: Create a new exercise for the authenticated user
- **Auth**: Authenticated user (own data only)
- **Request**:
  ```json
  {
    "name": "string (3-50 chars, required)",
    "muscleGroup": "CHEST | BACK | SHOULDERS | BICEPS | TRICEPS | LEGS | CORE | OTHER (required)"
  }
  ```
- **Response** (success, 201 Created):
  ```json
  {
    "id": "uuid",
    "name": "string",
    "muscleGroup": "CHEST"
  }
  ```
- **Response** (error):
  - `400 Bad Request`: Validation errors
  - `409 Conflict`: Exercise with same name already exists for this muscle group
    ```json
    {
      "code": "Exercise",
      "description": "Exercise with name <name> already exists for muscle group <muscleGroup>"
    }
    ```
- **Notes**: Exercise names are case-insensitive. Uniqueness is per user per muscle group.

### GET /exercises/{id}
- **Purpose**: Get a specific exercise by ID
- **Auth**: Authenticated user (own data only)
- **Request**: Path parameter `id` (UUID)
- **Response** (success, 200 OK):
  ```json
  {
    "id": "uuid",
    "name": "string",
    "muscleGroup": "CHEST"
  }
  ```
- **Response** (error):
  - `404 Not Found`: Exercise not found or doesn't belong to user
    ```json
    {
      "code": "Exercise",
      "description": "Exercise with ID <id> not found for user <userId>"
    }
    ```

### GET /exercises?muscleGroup={muscleGroup}
- **Purpose**: Get all exercises for a specific muscle group
- **Auth**: Authenticated user (own data only)
- **Request**: Query parameter `muscleGroup` (required, enum value)
- **Response** (success, 200 OK):
  ```json
  [
    {
      "id": "uuid",
      "name": "string",
      "muscleGroup": "CHEST"
    }
  ]
  ```
- **Notes**: Results are sorted alphabetically by exercise name. Empty array if no exercises found.

### PUT /exercises/{id}
- **Purpose**: Update an existing exercise
- **Auth**: Authenticated user (own data only)
- **Request**: Path parameter `id` (UUID), body:
  ```json
  {
    "name": "string (3-50 chars, optional)",
    "muscleGroup": "CHEST | BACK | ... (optional)"
  }
  ```
- **Response** (success, 200 OK):
  ```json
  {
    "id": "uuid",
    "name": "string",
    "muscleGroup": "CHEST"
  }
  ```
- **Response** (error):
  - `400 Bad Request`: Validation errors
  - `404 Not Found`: Exercise not found or doesn't belong to user
  - `409 Conflict`: Another exercise with the updated name already exists for the muscle group
- **Notes**: Partial updates supported. Omitted fields remain unchanged. Uniqueness check applies to the updated values.

### DELETE /exercises/{id}
- **Purpose**: Delete an exercise
- **Auth**: Authenticated user (own data only)
- **Request**: Path parameter `id` (UUID)
- **Response** (success, 204 No Content): Empty body
- **Response** (error):
  - `404 Not Found`: Exercise not found or doesn't belong to user
- **Notes**: Deleting an exercise may affect existing workouts that reference it (check cascade behavior).

### POST /workouts
- **Purpose**: Create a new workout
- **Auth**: Authenticated user (own data only)
- **Request**:
  ```json
  {
    "date": "2024-01-29", // ISO 8601 date (YYYY-MM-DD), required
    "workoutExercises": [
      {
        "exerciseId": "uuid (required)",
        "exerciseOrder": 1, // integer (required)
        "sets": [
          {
            "reps": 10, // integer, min 1 (required)
            "weight": 50.5, // double, min 0 (required)
            "notes": "string (max 250 chars, optional)",
            "setType": "WARM_UP | WORKING (required)"
          }
        ]
      }
    ]
  }
  ```
- **Response** (success, 201 Created):
  ```json
  {
    "id": "uuid",
    "date": "2024-01-29",
    "workoutExercises": [
      {
        "id": "uuid",
        "exercise": {
          "id": "uuid",
          "name": "string",
          "muscleGroup": "CHEST"
        },
        "exerciseOrder": 1,
        "sets": [
          {
            "id": "uuid",
            "reps": 10,
            "weight": 50.5,
            "notes": "string or null",
            "setType": "WORKING"
          }
        ]
      }
    ]
  }
  ```
- **Response** (error):
  - `400 Bad Request`: Validation errors
  - `404 Not Found`: Referenced exercise not found or doesn't belong to user
  - `409 Conflict`: Workout already exists for this date
    ```json
    {
      "code": "Workout",
      "description": "A workout already exists for date <date>"
    }
    ```
- **Notes**: Only one workout per date per user. All referenced exercises must belong to the authenticated user.

### GET /workouts?startDate={startDate}&endDate={endDate}
- **Purpose**: Get workout summaries within a date range
- **Auth**: Authenticated user (own data only)
- **Request**: Query parameters (both required):
  - `startDate`: ISO 8601 date (YYYY-MM-DD)
  - `endDate`: ISO 8601 date (YYYY-MM-DD)
- **Response** (success, 200 OK):
  ```json
  [
    {
      "id": "uuid",
      "date": "2024-01-29"
    }
  ]
  ```
- **Notes**: Results sorted by date descending (newest first). Returns lightweight summaries without exercise details. Use this for listing, then fetch full details with GET /workouts/{id}.

### GET /workouts/{id}
- **Purpose**: Get full workout details by ID
- **Auth**: Authenticated user (own data only)
- **Request**: Path parameter `id` (UUID)
- **Response** (success, 200 OK):
  ```json
  {
    "id": "uuid",
    "date": "2024-01-29",
    "workoutExercises": [
      {
        "id": "uuid",
        "exercise": {
          "id": "uuid",
          "name": "string",
          "muscleGroup": "CHEST"
        },
        "exerciseOrder": 1,
        "sets": [
          {
            "id": "uuid",
            "reps": 10,
            "weight": 50.5,
            "notes": "string or null",
            "setType": "WORKING"
          }
        ]
      }
    ]
  }
  ```
- **Response** (error):
  - `404 Not Found`: Workout not found or doesn't belong to user
    ```json
    {
      "code": "Workout",
      "description": "Workout with ID <id> not found for user <userId>"
    }
    ```

### PUT /workouts/{id}
- **Purpose**: Update an existing workout
- **Auth**: Authenticated user (own data only)
- **Request**: Path parameter `id` (UUID), body:
  ```json
  {
    "date": "2024-01-29", // optional
    "workoutExercises": [ // optional, but if provided replaces all existing exercises
      {
        "exerciseId": "uuid (required)",
        "exerciseOrder": 1,
        "sets": [
          {
            "reps": 10,
            "weight": 50.5,
            "notes": "string or null",
            "setType": "WORKING"
          }
        ]
      }
    ]
  }
  ```
- **Response** (success, 200 OK): Same as GET /workouts/{id}
- **Response** (error):
  - `400 Bad Request`: Validation errors
  - `404 Not Found`: Workout or referenced exercise not found or doesn't belong to user
- **Notes**: Providing `workoutExercises` replaces all existing exercises (clear and rebuild). Omit the field to keep existing exercises unchanged.

### DELETE /workouts/{id}
- **Purpose**: Delete a workout
- **Auth**: Authenticated user (own data only)
- **Request**: Path parameter `id` (UUID)
- **Response** (success, 204 No Content): Empty body
- **Response** (error):
  - `404 Not Found`: Workout not found or doesn't belong to user

## Data Models / DTOs

### ExerciseResponse
```typescript
interface ExerciseResponse {
  id: string; // UUID
  name: string;
  muscleGroup: MuscleGroup;
}
```

### WorkoutResponse
```typescript
interface WorkoutResponse {
  id: string; // UUID
  date: string; // ISO 8601 date (YYYY-MM-DD)
  workoutExercises: WorkoutExerciseResponse[];
}
```

### WorkoutSummaryResponse
```typescript
interface WorkoutSummaryResponse {
  id: string; // UUID
  date: string; // ISO 8601 date (YYYY-MM-DD)
}
```

### WorkoutExerciseResponse
```typescript
interface WorkoutExerciseResponse {
  id: string; // UUID
  exercise: ExerciseResponse;
  exerciseOrder: number;
  sets: SetResponse[];
}
```

### SetResponse
```typescript
interface SetResponse {
  id: string; // UUID
  reps: number;
  weight: number;
  notes: string | null;
  setType: SetType;
}
```

### AuthenticationResponse
```typescript
interface AuthenticationResponse {
  username: string;
  token: string; // JWT token
}
```

### CreateUserResponse
```typescript
interface CreateUserResponse {
  id: string; // UUID
  username: string;
  createdAt: string; // ISO 8601 datetime
}
```

### ErrorMessageResponse
```typescript
interface ErrorMessageResponse {
  code: string;
  description: string;
}
```

## Enums & Constants

### MuscleGroup
| Value | Meaning |
|-------|---------|
| `CHEST` | Chest muscles |
| `BACK` | Back muscles |
| `SHOULDERS` | Shoulder muscles |
| `BICEPS` | Bicep muscles |
| `TRICEPS` | Tricep muscles |
| `LEGS` | Leg muscles |
| `CORE` | Core/abdominal muscles |
| `OTHER` | Other muscle groups |

### SetType
| Value | Meaning |
|-------|---------|
| `WARM_UP` | Warm-up set |
| `WORKING` | Working set |

## Validation Rules

### User Registration
- `username`: Required, 3-50 characters, unique
- `password`: Required, 6-50 characters

### Exercise
- `name`: Required, 3-50 characters, unique per user per muscle group (case-insensitive)
- `muscleGroup`: Required, must be valid enum value

### Workout
- `date`: Required (on create), ISO 8601 format (YYYY-MM-DD), unique per user per date
- `workoutExercises`: Optional array, but if provided must contain valid exercise references

### WorkoutExercise
- `exerciseId`: Required, must be a valid UUID of an exercise belonging to the user
- `exerciseOrder`: Required, integer
- `sets`: Optional array

### Set
- `reps`: Required, integer, minimum 1
- `weight`: Required, double, minimum 0
- `notes`: Optional, maximum 250 characters
- `setType`: Required, must be `WARM_UP` or `WORKING`

## Business Logic & Edge Cases

- **User Data Isolation**: All endpoints automatically scope data to the authenticated user. Users cannot access or modify other users' exercises or workouts.
- **Exercise Uniqueness**: Exercise names are unique per user per muscle group, case-insensitive. "Bench Press" and "bench press" are considered duplicates.
- **Workout Date Uniqueness**: Only one workout can exist per date per user. Attempting to create a second workout for the same date returns `409 Conflict`.
- **Exercise References**: When creating or updating a workout, all referenced `exerciseId` values must belong to the authenticated user. Invalid references return `404 Not Found`.
- **Workout Update Behavior**: When updating a workout with `workoutExercises`, the entire list is replaced (existing exercises are cleared). To keep existing exercises, omit the `workoutExercises` field.
- **Exercise Deletion**: Deleting an exercise may affect existing workouts. Check cascade behavior—workouts may retain references or be updated accordingly.
- **Date Range Queries**: The `getWorkouts` endpoint requires both `startDate` and `endDate`. Results are inclusive of both dates and sorted by date descending.
- **Token Expiration**: JWT tokens expire after 24 hours by default. Frontend should handle token refresh or re-authentication.
- **CORS**: API allows requests from `http://localhost:3000` with credentials. Update CORS configuration for production.

## Integration Notes

- **Recommended Flow**:
  1. Register/Login → Store JWT token
  2. Fetch exercises by muscle group → Display in exercise selector
  3. Create workout → Select date, add exercises with sets
  4. List workouts by date range → Display summaries
  5. Fetch workout details → Show full workout

- **Optimistic UI**: 
  - Safe for: Exercise list updates, workout list updates
  - Not safe for: Creating workouts (date uniqueness check), creating exercises (name uniqueness check)
  - Always validate on the backend before showing success

- **Caching**: 
  - No explicit cache headers provided
  - Consider caching exercise lists by muscle group (they change infrequently)
  - Workout lists may change frequently—cache with short TTL or invalidate on create/update/delete

- **Real-time**: 
  - No websocket support
  - Use polling for workout lists if real-time updates are needed

- **Error Handling**:
  - Validation errors (`400`) return field-level messages: `{ "fieldName": "error message" }`
  - Business logic errors (`404`, `409`, `401`) return `ErrorMessageResponse` with `code` and `description`
  - Always check response status codes and handle accordingly

- **Token Management**:
  - Store token securely (e.g., httpOnly cookie or secure storage)
  - Include token in `Authorization: Bearer <token>` header for all authenticated requests
  - Handle `401` responses by redirecting to login