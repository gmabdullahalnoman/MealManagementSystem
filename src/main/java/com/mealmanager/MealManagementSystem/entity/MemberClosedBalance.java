package com.mealmanager.MealManagementSystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "member_closed_balances", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"session_id", "member_id"})
})
public class MemberClosedBalance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Session is required")
    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;
    
    @NotNull(message = "Member is required")
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @Column(name = "total_deposit")
    private Double totalDeposit = 0.0;
    
    @Column(name = "total_meals")
    private Integer totalMeals = 0;
    
    @Column(name = "total_cost")
    private Double totalCost = 0.0;
    
    private Double balance = 0.0;
    
    // Constructors
    public MemberClosedBalance() {}
    
    public MemberClosedBalance(Session session, Member member, Double totalDeposit, Integer totalMeals, Double totalCost, Double balance) {
        this.session = session;
        this.member = member;
        this.totalDeposit = totalDeposit;
        this.totalMeals = totalMeals;
        this.totalCost = totalCost;
        this.balance = balance;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }
    
    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }
    
    public Double getTotalDeposit() { return totalDeposit; }
    public void setTotalDeposit(Double totalDeposit) { this.totalDeposit = totalDeposit; }
    
    public Integer getTotalMeals() { return totalMeals; }
    public void setTotalMeals(Integer totalMeals) { this.totalMeals = totalMeals; }
    
    public Double getTotalCost() { return totalCost; }
    public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }
    
    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }
}