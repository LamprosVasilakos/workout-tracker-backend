package com.github.lamprosvasilakos.workouttracker.dto.request;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

public record UpdateWorkoutRequest(
        LocalDate date,

        @Valid
        List<CreateWorkoutExerciseRequest> workoutExercises
) {
}
