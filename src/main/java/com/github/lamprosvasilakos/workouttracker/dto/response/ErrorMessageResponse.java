package com.github.lamprosvasilakos.workouttracker.dto.response;

public record ErrorMessageResponse(String code, String description) {

    public ErrorMessageResponse(String code) {
        this(code, "");     // Calls the canonical constructor
    }
}
