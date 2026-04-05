package com.mealmanager.MealManagementSystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "closed_month_summaries")
public class ClosedMonthSummary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Session is required")
    @OneToOne
    @JoinColumn(name = "session_id", nullable = false, unique = true)
    private Session session;
    
    @Column(name = "total_deposit")
    private Double totalDeposit;
    
    @Column(name = "total_expense")
    private Double totalExpense;
    
    @Column(name = "total_meals")
    private Integer totalMeals;
    
    @Column(name = "meal_rate")
    private Double mealRate;
    
    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt = LocalDateTime.now();
    
    // Constructors
    public ClosedMonthSummary() {}
    
    public ClosedMonthSummary(Session session, Double totalDeposit, Double totalExpense, Integer totalMeals, Double mealRate) {
        this.session = session;
        this.totalDeposit = totalDeposit;
        this.totalExpense = totalExpense;
        this.totalMeals = totalMeals;
        this.mealRate = mealRate;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }
    
    public Double getTotalDeposit() { return totalDeposit; }
    public void setTotalDeposit(Double totalDeposit) { this.totalDeposit = totalDeposit; }
    
    public Double getTotalExpense() { return totalExpense; }
    public void setTotalExpense(Double totalExpense) { this.totalExpense = totalExpense; }
    
    public Integer getTotalMeals() { return totalMeals; }
    public void setTotalMeals(Integer totalMeals) { this.totalMeals = totalMeals; }
    
    public Double getMealRate() { return mealRate; }
    public void setMealRate(Double mealRate) { this.mealRate = mealRate; }
    
    public LocalDateTime getCalculatedAt() { return calculatedAt; }
    public void setCalculatedAt(LocalDateTime calculatedAt) { this.calculatedAt = calculatedAt; }
}