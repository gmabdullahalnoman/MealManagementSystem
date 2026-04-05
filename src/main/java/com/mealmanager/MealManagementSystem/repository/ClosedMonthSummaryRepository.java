package com.mealmanager.MealManagementSystem.repository;

import com.mealmanager.MealManagementSystem.entity.ClosedMonthSummary;
import com.mealmanager.MealManagementSystem.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClosedMonthSummaryRepository extends JpaRepository<ClosedMonthSummary, Long> {
    
    Optional<ClosedMonthSummary> findBySession(Session session);
    
    @Query("SELECT c FROM ClosedMonthSummary c WHERE c.session.isClosed = true ORDER BY c.calculatedAt DESC")
    java.util.List<ClosedMonthSummary> findAllClosedMonthsSummary();
    
    boolean existsBySession(Session session);
    
    @Query("SELECT c.mealRate FROM ClosedMonthSummary c WHERE c.session = :session")
    Double findMealRateBySession(@Param("session") Session session);
}