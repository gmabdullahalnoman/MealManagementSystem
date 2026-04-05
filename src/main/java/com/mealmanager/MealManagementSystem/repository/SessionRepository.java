package com.mealmanager.MealManagementSystem.repository;

import com.mealmanager.MealManagementSystem.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    
    Optional<Session> findByIsActiveTrue();
    
    Optional<Session> findByIsClosedFalseAndIsActiveTrue();
    
    @Query("SELECT s FROM Session s WHERE s.isClosed = false ORDER BY s.startDate DESC")
    Optional<Session> findCurrentOpenSession();
    
    boolean existsByIsActiveTrueAndIsClosedFalse();
    
    long countByIsClosedFalse();
}