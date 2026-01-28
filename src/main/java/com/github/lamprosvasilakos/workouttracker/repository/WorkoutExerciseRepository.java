package com.github.lamprosvasilakos.workouttracker.repository;

import com.github.lamprosvasilakos.workouttracker.entity.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, UUID> {

    List<WorkoutExercise> findAllByWorkoutIdOrderByExerciseOrderAsc(UUID workoutId);

}
