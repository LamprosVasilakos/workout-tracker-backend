package com.github.lamprosvasilakos.workouttracker.controller;

import com.github.lamprosvasilakos.workouttracker.dto.request.UpdateWorkoutExerciseRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.WorkoutExerciseResponse;
import com.github.lamprosvasilakos.workouttracker.entity.User;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectNotFoundException;
import com.github.lamprosvasilakos.workouttracker.exception.ValidationException;
import com.github.lamprosvasilakos.workouttracker.service.WorkoutExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/workout-exercises")
@RequiredArgsConstructor
public class WorkoutExerciseController {

    private final WorkoutExerciseService workoutExerciseService;

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutExerciseResponse> getWorkoutExerciseById(@PathVariable UUID id) throws AppObjectNotFoundException {
        UUID userId = getAuthenticatedUserId();
        WorkoutExerciseResponse response = workoutExerciseService.getWorkoutExerciseById(id, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutExerciseResponse> updateWorkoutExercise(@PathVariable UUID id, @Valid @RequestBody UpdateWorkoutExerciseRequest request, BindingResult bindingResult) throws ValidationException, AppObjectNotFoundException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        UUID userId = getAuthenticatedUserId();
        WorkoutExerciseResponse response = workoutExerciseService.updateWorkoutExercise(id, request, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkoutExercise(@PathVariable UUID id) throws AppObjectNotFoundException {
        UUID userId = getAuthenticatedUserId();
        workoutExerciseService.deleteWorkoutExercise(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<WorkoutExerciseResponse>> getAllWorkoutExercisesByWorkoutId(@RequestParam UUID workoutId) {
        UUID userId = getAuthenticatedUserId();
        List<WorkoutExerciseResponse> response = workoutExerciseService.getAllWorkoutExercisesByWorkoutId(workoutId, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
