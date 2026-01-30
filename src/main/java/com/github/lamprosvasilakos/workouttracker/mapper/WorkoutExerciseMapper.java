package com.github.lamprosvasilakos.workouttracker.mapper;

import com.github.lamprosvasilakos.workouttracker.dto.request.CreateWorkoutExerciseRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.ExerciseResponse;
import com.github.lamprosvasilakos.workouttracker.dto.response.SetResponse;
import com.github.lamprosvasilakos.workouttracker.dto.response.WorkoutExerciseResponse;
import com.github.lamprosvasilakos.workouttracker.entity.Set;
import com.github.lamprosvasilakos.workouttracker.entity.WorkoutExercise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WorkoutExerciseMapper {

    private final SetMapper setMapper;
    private final ExerciseMapper exerciseMapper;

    public WorkoutExercise toEntity(CreateWorkoutExerciseRequest request) {
        WorkoutExercise workoutExercise = WorkoutExercise.builder()
                .exerciseOrder(request.exerciseOrder())
                .build();

        if (request.sets() != null && !request.sets().isEmpty()) {
            List<Set> sets = request.sets().stream()
                    .map(setMapper::toEntity)
                    .toList();
            
            for (Set set : sets) {
                set.setWorkoutExercise(workoutExercise);
            }
            
            workoutExercise.getSets().addAll(sets);
        }

        return workoutExercise;
    }

    public WorkoutExerciseResponse toResponse(WorkoutExercise workoutExercise) {
        ExerciseResponse exerciseResponse = exerciseMapper.toResponse(workoutExercise.getExercise());

        List<SetResponse> setResponses = workoutExercise.getSets().stream()
                .map(setMapper::toResponse)
                .toList();

        return new WorkoutExerciseResponse(
                workoutExercise.getId(),
                exerciseResponse,
                workoutExercise.getExerciseOrder(),
                setResponses
        );
    }
}
