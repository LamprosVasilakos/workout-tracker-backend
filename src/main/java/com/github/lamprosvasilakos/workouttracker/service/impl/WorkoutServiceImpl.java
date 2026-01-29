package com.github.lamprosvasilakos.workouttracker.service.impl;

import com.github.lamprosvasilakos.workouttracker.dto.request.CreateWorkoutRequest;
import com.github.lamprosvasilakos.workouttracker.dto.request.UpdateWorkoutRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.WorkoutResponse;
import com.github.lamprosvasilakos.workouttracker.dto.response.WorkoutSummaryResponse;
import com.github.lamprosvasilakos.workouttracker.entity.User;
import com.github.lamprosvasilakos.workouttracker.entity.Workout;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectNotFoundException;
import com.github.lamprosvasilakos.workouttracker.mapper.WorkoutMapper;
import com.github.lamprosvasilakos.workouttracker.repository.UserRepository;
import com.github.lamprosvasilakos.workouttracker.repository.WorkoutRepository;
import com.github.lamprosvasilakos.workouttracker.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutMapper workoutMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public WorkoutResponse createWorkout(CreateWorkoutRequest request, UUID userId) throws AppObjectNotFoundException {
        User user = findUserById(userId);

        Workout workout = workoutMapper.toEntity(request);
        workout.setUser(user);

        if (workout.getWorkoutExercises() != null) {
            workout.getWorkoutExercises().forEach(we -> {
                we.setWorkout(workout);
                if (we.getSets() != null) {
                    we.getSets().forEach(set -> set.setWorkoutExercise(we));
                }
            });
        }

        Workout savedWorkout = workoutRepository.save(workout);
        log.info("User {} created a new workout for date {}", userId, savedWorkout.getDate());
        return workoutMapper.toResponse(savedWorkout);
    }

    @Override
    @Transactional
    public WorkoutResponse updateWorkout(UUID workoutId, UpdateWorkoutRequest request, UUID userId) throws AppObjectNotFoundException {
        Workout workout = findWorkoutByIdAndUserId(workoutId, userId);

        workoutMapper.updateEntityFromRequest(request, workout);

        if (workout.getWorkoutExercises() != null) {
            workout.getWorkoutExercises().forEach(we -> {
                we.setWorkout(workout);
                if (we.getSets() != null) {
                    we.getSets().forEach(set -> set.setWorkoutExercise(we));
                }
            });
        }

        Workout updatedWorkout = workoutRepository.save(workout);
        log.info("User {} updated workout with ID {}", userId, workoutId);
        return workoutMapper.toResponse(updatedWorkout);
    }

    @Override
    @Transactional
    public void deleteWorkout(UUID workoutId, UUID userId) throws AppObjectNotFoundException {
        Workout workout = findWorkoutByIdAndUserId(workoutId, userId);
        workoutRepository.delete(workout);
        log.info("User {} deleted workout with ID {}", userId, workoutId);
    }

    @Override
    @Transactional(readOnly = true)
    public WorkoutResponse getWorkoutById(UUID workoutId, UUID userId) throws AppObjectNotFoundException {
        Workout workout = findWorkoutByIdAndUserId(workoutId, userId);
        return workoutMapper.toResponse(workout);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutSummaryResponse> getWorkoutsByDateRange(UUID userId, LocalDate start, LocalDate end) {
        return workoutRepository.findByUserIdAndDateBetweenOrderByDateDesc(userId, start, end).stream()
                .map(workoutMapper::toSummaryResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutResponse> getWorkoutsByDate(UUID userId, LocalDate date) {
        return workoutRepository.findByUserIdAndDate(userId, date).stream()
                .map(workoutMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocalDate> getWorkoutDatesBetween(UUID userId, LocalDate start, LocalDate end) {
        return workoutRepository.findDatesByUserIdAndDateBetween(userId, start, end);
    }

    private User findUserById(UUID userId) throws AppObjectNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User with ID " + userId + " not found"));
    }

    private Workout findWorkoutByIdAndUserId(UUID workoutId, UUID userId) throws AppObjectNotFoundException {
        return workoutRepository.findByIdAndUserId(workoutId, userId)
                .orElseThrow(() -> new AppObjectNotFoundException("Workout", 
                    "Workout with ID " + workoutId + " not found for user " + userId));
    }
}
