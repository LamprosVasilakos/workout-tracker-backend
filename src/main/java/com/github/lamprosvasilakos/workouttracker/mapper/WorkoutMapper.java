package com.github.lamprosvasilakos.workouttracker.mapper;

import com.github.lamprosvasilakos.workouttracker.dto.request.CreateWorkoutRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.WorkoutExerciseResponse;
import com.github.lamprosvasilakos.workouttracker.dto.response.WorkoutResponse;
import com.github.lamprosvasilakos.workouttracker.dto.response.WorkoutSummaryResponse;
import com.github.lamprosvasilakos.workouttracker.entity.Workout;
import com.github.lamprosvasilakos.workouttracker.entity.WorkoutExercise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WorkoutMapper {

    private final WorkoutExerciseMapper workoutExerciseMapper;

    public Workout toEntity(CreateWorkoutRequest request) {
        Workout workout = Workout.builder()
                .date(request.date())
                .build();

        if (request.workoutExercises() != null && !request.workoutExercises().isEmpty()) {
            List<WorkoutExercise> workoutExercises = request.workoutExercises().stream()
                    .map(workoutExerciseMapper::toEntity)
                    .peek(we -> we.setWorkout(workout))
                    .toList();
            workout.setWorkoutExercises(workoutExercises);
        }

        return workout;
    }

    public WorkoutResponse toResponse(Workout workout) {
        List<WorkoutExerciseResponse> workoutExerciseResponses = workout.getWorkoutExercises().stream()
                .map(workoutExerciseMapper::toResponse)
                .toList();

        return new WorkoutResponse(
                workout.getId(),
                workout.getDate(),
                workoutExerciseResponses
        );
    }

    public WorkoutSummaryResponse toSummaryResponse(Workout workout) {
        return new WorkoutSummaryResponse(
                workout.getId(),
                workout.getDate()
        );
    }
}
