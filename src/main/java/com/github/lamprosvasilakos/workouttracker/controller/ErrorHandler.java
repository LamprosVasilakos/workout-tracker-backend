package com.github.lamprosvasilakos.workouttracker.controller;

import com.github.lamprosvasilakos.workouttracker.dto.response.ErrorMessageResponse;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectAlreadyExistsException;
import com.github.lamprosvasilakos.workouttracker.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException e) {
        log.warn("validation failed. Message={}", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();

        Map<String , String > errors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AppObjectAlreadyExistsException.class)
    public ResponseEntity<ErrorMessageResponse> handleAppObjectAlr(AppObjectAlreadyExistsException e) {
        log.warn("Entity already exists. Message={}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorMessageResponse(e.getCode(), e.getMessage()));
    }
}
