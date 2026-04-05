package com.mealmanager.MealManagementSystem.repository;

import com.mealmanager.MealManagementSystem.entity.Expense;
import com.mealmanager.MealManagementSystem.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    
    List<Expense> findBySession(Session session);
    
    List<Expense> findByExpenseDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.session = :session")
    Double sumBySession(@Param("session") Session session);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.session = :session AND e.expenseDate BETWEEN :startDate AND :endDate")
    Double sumBySessionAndDateRange(@Param("session") Session session, 
                                     @Param("startDate") LocalDate startDate, 
                                     @Param("endDate") LocalDate endDate);
    
    List<Expense> findBySessionOrderByExpenseDateDesc(Session session);
}