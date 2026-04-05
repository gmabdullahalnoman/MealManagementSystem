package com.mealmanager.MealManagementSystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "member_opening_balances")
public class MemberOpeningBalance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Member is required")
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @NotNull(message = "Session is required")
    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;
    
    @NotNull(message = "Opening balance amount is required")
    @Column(nullable = false)
    private Double amount = 0.0;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructors
    public MemberOpeningBalance() {}
    
    public MemberOpeningBalance(Member member, Session session, Double amount) {
        this.member = member;
        this.session = session;
        this.amount = amount;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }
    
    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }
    
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}