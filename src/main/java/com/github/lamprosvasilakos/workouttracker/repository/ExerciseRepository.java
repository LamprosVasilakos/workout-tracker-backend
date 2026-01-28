package com.github.lamprosvasilakos.workouttracker.repository;

import com.github.lamprosvasilakos.workouttracker.entity.Exercise;
import com.github.lamprosvasilakos.workouttracker.entity.MuscleGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {


    List<Exercise> findByUserIdAndMuscleGroupOrderByExerciseNameAsc(UUID userId, MuscleGroup muscleGroup);

    Optional<Exercise> findByIdAndUserId(UUID exerciseId, UUID userId);

    boolean existsByUserIdAndMuscleGroupAndExerciseNameIgnoreCase(UUID userId, MuscleGroup muscleGroup, String exerciseName);

    Optional<Exercise> findByUserIdAndMuscleGroupAndExerciseNameIgnoreCase(UUID userId, MuscleGroup muscleGroup, String exerciseName);

}
