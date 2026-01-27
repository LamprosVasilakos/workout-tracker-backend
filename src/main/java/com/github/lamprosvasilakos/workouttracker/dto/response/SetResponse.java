package com.github.lamprosvasilakos.workouttracker.dto.response;

import com.github.lamprosvasilakos.workouttracker.entity.SetType;

import java.util.UUID;

public record SetResponse(
        UUID id,
        Integer reps,
        Double weight,
        String notes,
        SetType setType
) {
}
