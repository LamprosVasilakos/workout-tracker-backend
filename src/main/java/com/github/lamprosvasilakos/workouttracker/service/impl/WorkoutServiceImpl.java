package com.github.lamprosvasilakos.workouttracker.service.impl;

import com.github.lamprosvasilakos.workouttracker.dto.request.CreateWorkoutExerciseRequest;
import com.github.lamprosvasilakos.workouttracker.dto.request.CreateWorkoutRequest;
import com.github.lamprosvasilakos.workouttracker.dto.request.UpdateWorkoutRequest;
import com.github.lamprosvasilakos.workouttracker.dto.response.WorkoutResponse;
import com.github.lamprosvasilakos.workouttracker.dto.response.WorkoutSummaryResponse;
import com.github.lamprosvasilakos.workouttracker.entity.User;
import com.github.lamprosvasilakos.workouttracker.entity.Workout;
import com.github.lamprosvasilakos.workouttracker.entity.WorkoutExercise;
import com.github.lamprosvasilakos.workouttracker.exception.AppObjectNotFoundException;
import com.github.lamprosvasilakos.workouttracker.mapper.WorkoutExerciseMapper;
import com.github.lamprosvasilakos.workouttracker.mapper.WorkoutMapper;
import com.github.lamprosvasilakos.workouttracker.repository.ExerciseRepository;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final WorkoutMapper workoutMapper;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutExerciseMapper workoutExerciseMapper;

    @Override
    @Transactional
    public WorkoutResponse createWorkout(CreateWorkoutRequest request, UUID userId) throws AppObjectNotFoundException {
        User user = findUserById(userId);

        Workout workout = workoutMapper.toEntity(request);
        workout.setUser(user);

        if (workout.getWorkoutExercises() != null && request.workoutExercises() != null) {
            loadAndSetExercises(workout.getWorkoutExercises(), request.workoutExercises(), userId);
        }

        Workout savedWorkout = workoutRepository.save(workout);
        log.info("User {} created a new workout for date {}", userId, savedWorkout.getDate());
        return workoutMapper.toResponse(savedWorkout);
    }

    @Override
    @Transactional
    public WorkoutResponse updateWorkout(UUID workoutId, UpdateWorkoutRequest request, UUID userId) throws AppObjectNotFoundException {
        Workout workout = findWorkoutByIdAndUserId(workoutId, userId);

        if (request.date() != null) {
            workout.setDate(request.date());
        }

        if (request.workoutExercises() != null) {
            workout.getWorkoutExercises().clear();
            
            for (int i = 0; i < request.workoutExercises().size(); i++) {
                var requestWe = request.workoutExercises().get(i);
                
                var exercise = exerciseRepository.findByIdAndUserId(requestWe.exerciseId(), userId)
                        .orElseThrow(() -> new AppObjectNotFoundException("Exercise",
                            "Exercise with ID " + requestWe.exerciseId() + " not found for user " + userId));
                
                var we = workoutExerciseMapper.toEntity(requestWe);
                we.setWorkout(workout);
                we.setExercise(exercise);
                
                workout.getWorkoutExercises().add(we);
            }
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
    public List<WorkoutSummaryResponse> getWorkoutsBetweenDates(UUID userId, LocalDate startDate, LocalDate endDate) {
        List<Workout> workouts = workoutRepository.findByUserIdAndDateBetweenOrderByDateDesc(userId, startDate, endDate);
        return workouts.stream()
                .map(workout -> new WorkoutSummaryResponse(workout.getId(), workout.getDate()))
                .toList();
    }

    private void loadAndSetExercises(List<WorkoutExercise> workoutExercises,
                                     List<CreateWorkoutExerciseRequest> requests,
                                     UUID userId) throws AppObjectNotFoundException {
        for (int i = 0; i < workoutExercises.size(); i++) {
            var we = workoutExercises.get(i);
            var requestWe = requests.get(i);

            var exercise = exerciseRepository.findByIdAndUserId(requestWe.exerciseId(), userId)
                    .orElseThrow(() -> new AppObjectNotFoundException("Exercise",
                        "Exercise with ID " + requestWe.exerciseId() + " not found for user " + userId));

            we.setExercise(exercise);
        }
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
