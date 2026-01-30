package com.github.lamprosvasilakos.workouttracker.repository;

import com.github.lamprosvasilakos.workouttracker.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, UUID> {


    Optional<Workout> findByIdAndUserId(UUID workoutId, UUID userId);

    List<Workout> findByUserIdAndDateBetweenOrderByDateDesc(UUID userId, LocalDate startDate, LocalDate endDate);

    boolean existsByUserIdAndDate(UUID userId, LocalDate date);

}
