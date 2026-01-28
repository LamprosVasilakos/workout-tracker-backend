package com.github.lamprosvasilakos.workouttracker.repository;

import com.github.lamprosvasilakos.workouttracker.entity.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SetRepository extends JpaRepository<Set, UUID> {

    List<Set> findAllByWorkoutExerciseId(UUID workoutExerciseId);

}
