package com.github.lamprosvasilakos.workouttracker.mapper;

import com.github.lamprosvasilakos.workouttracker.dto.request.CreateExerciseRequest;
import com.github.lamprosvasilakos.workouttracker.dto.request.UpdateExerciseRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.ExerciseResponse;
import com.github.lamprosvasilakos.workouttracker.entity.Exercise;
import org.springframework.stereotype.Component;

@Component
public class ExerciseMapper {


    public Exercise toEntity(CreateExerciseRequest request) {
        return Exercise.builder()
                .exerciseName(request.name())
                .muscleGroup(request.muscleGroup())
                .build();
    }


    public void updateEntityFromRequest(UpdateExerciseRequest request, Exercise entity) {
        if (request.name() != null) {
            entity.setExerciseName(request.name());
        }
        if (request.muscleGroup() != null) {
            entity.setMuscleGroup(request.muscleGroup());
        }
    }


    public ExerciseResponse toResponse(Exercise exercise) {
        return new ExerciseResponse(
                exercise.getId(),
                exercise.getExerciseName(),
                exercise.getMuscleGroup()
        );
    }
}
