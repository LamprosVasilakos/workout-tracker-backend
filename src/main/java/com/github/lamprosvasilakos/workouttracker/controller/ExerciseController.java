package com.github.lamprosvasilakos.workouttracker.controller;

import com.github.lamprosvasilakos.workouttracker.dto.request.CreateExerciseRequest;
import com.github.lamprosvasilakos.workouttracker.dto.request.UpdateExerciseRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.ExerciseResponse;
import com.github.lamprosvasilakos.workouttracker.entity.MuscleGroup;
import com.github.lamprosvasilakos.workouttracker.entity.User;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectAlreadyExistsException;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectNotFoundException;
import com.github.lamprosvasilakos.workouttracker.exception.ValidationException;
import com.github.lamprosvasilakos.workouttracker.service.ExerciseService;
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
@RequestMapping("/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @PostMapping
    public ResponseEntity<ExerciseResponse> createExercise(@Valid @RequestBody CreateExerciseRequest request, BindingResult bindingResult) throws ValidationException, AppObjectAlreadyExistsException, AppObjectNotFoundException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        UUID userId = getAuthenticatedUserId();
        ExerciseResponse response = exerciseService.createExercise(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponse> getExerciseById(@PathVariable UUID id) throws AppObjectNotFoundException {
        UUID userId = getAuthenticatedUserId();
        ExerciseResponse response = exerciseService.getExerciseById(id, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ExerciseResponse>> getExercisesByMuscleGroup(@RequestParam MuscleGroup muscleGroup) {
        UUID userId = getAuthenticatedUserId();
        List<ExerciseResponse> response = exerciseService.getExercisesByMuscleGroup(muscleGroup, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseResponse> updateExercise(@PathVariable UUID id, @Valid @RequestBody UpdateExerciseRequest request, BindingResult bindingResult) throws ValidationException, AppObjectNotFoundException, AppObjectAlreadyExistsException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        UUID userId = getAuthenticatedUserId();
        ExerciseResponse response = exerciseService.updateExercise(id, request, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable UUID id) throws AppObjectNotFoundException {
        UUID userId = getAuthenticatedUserId();
        exerciseService.deleteExercise(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private UUID getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
