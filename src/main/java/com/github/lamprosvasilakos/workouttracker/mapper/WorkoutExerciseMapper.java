package com.github.lamprosvasilakos.workouttracker.mapper;

import com.github.lamprosvasilakos.workouttracker.dto.request.CreateWorkoutExerciseRequest;
import com.github.lamprosvasilakos.workouttracker.dto.request.UpdateWorkoutExerciseRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.ExerciseResponse;
import com.github.lamprosvasilakos.workouttracker.dto.response.SetResponse;
import com.github.lamprosvasilakos.workouttracker.dto.response.WorkoutExerciseResponse;
import com.github.lamprosvasilakos.workouttracker.entity.Set;
import com.github.lamprosvasilakos.workouttracker.entity.WorkoutExercise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
                    .collect(Collectors.toList());
            workoutExercise.setSets(sets);
        }

        return workoutExercise;
    }

    public void updateEntityFromRequest(UpdateWorkoutExerciseRequest request, WorkoutExercise entity) {
        if (request.exerciseOrder() != null) {
            entity.setExerciseOrder(request.exerciseOrder());
        }
        if (request.sets() != null && !request.sets().isEmpty()) {
            List<Set> sets = request.sets().stream()
                    .map(setMapper::toEntity)
                    .toList();
            entity.getSets().clear();
            entity.getSets().addAll(sets);
        }
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
