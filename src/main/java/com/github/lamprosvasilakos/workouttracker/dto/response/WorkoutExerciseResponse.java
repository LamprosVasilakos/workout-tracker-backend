package com.github.lamprosvasilakos.workouttracker.dto.response;

import java.util.List;
import java.util.UUID;

public record WorkoutExerciseResponse(
        UUID id,
        ExerciseResponse exercise,
        Integer exerciseOrder,
        List<SetResponse> sets
) {
}
