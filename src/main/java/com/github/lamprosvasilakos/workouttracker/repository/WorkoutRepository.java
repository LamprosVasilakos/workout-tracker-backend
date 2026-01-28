package com.github.lamprosvasilakos.workouttracker.repository;

import com.github.lamprosvasilakos.workouttracker.entity.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, UUID> {


    Optional<Workout> findByIdAndUserId(UUID workoutId, UUID userId);


    List<Workout> findByUserIdAndDateBetweenOrderByDateDesc(UUID userId, LocalDate startDate, LocalDate endDate);


    List<Workout> findByUserIdAndDate(UUID userId, LocalDate date);


    boolean existsByUserIdAndDate(UUID userId, LocalDate date);

    @Query("SELECT w.date FROM Workout w WHERE w.user.id = :userId AND w.date BETWEEN :startDate AND :endDate GROUP BY w.date ORDER BY w.date")
    List<LocalDate> findDatesByUserIdAndDateBetween(@Param("userId") UUID userId,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);

}
