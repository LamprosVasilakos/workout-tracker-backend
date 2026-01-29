package com.github.lamprosvasilakos.workouttracker.controller;

import com.github.lamprosvasilakos.workouttracker.dto.request.UpdateSetRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.SetResponse;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectNotFoundException;
import com.github.lamprosvasilakos.workouttracker.exception.ValidationException;
import com.github.lamprosvasilakos.workouttracker.service.SetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sets")
@RequiredArgsConstructor
public class SetController {

    private final SetService setService;

    @GetMapping("/{id}")
    public ResponseEntity<SetResponse> getSetById(@PathVariable UUID id) throws AppObjectNotFoundException {
        SetResponse response = setService.getSetById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SetResponse> updateSet(@PathVariable UUID id, @Valid @RequestBody UpdateSetRequest request, BindingResult bindingResult) throws ValidationException, AppObjectNotFoundException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        SetResponse response = setService.updateSet(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSet(@PathVariable UUID id) throws AppObjectNotFoundException {
        setService.deleteSet(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<SetResponse>> getAllSetsByWorkoutExerciseId(@RequestParam UUID workoutExerciseId) {
        List<SetResponse> response = setService.getAllSetsByWorkoutExerciseId(workoutExerciseId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
