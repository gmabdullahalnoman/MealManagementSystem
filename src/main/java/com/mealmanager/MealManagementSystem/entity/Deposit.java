package com.mealmanager.MealManagementSystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "deposits")
public class Deposit {
    
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
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @Column(nullable = false)
    private Double amount;
    
    @NotNull(message = "Deposit date is required")
    @Column(name = "deposit_date", nullable = false)
    private LocalDate depositDate;
    
    @Column(length = 20)
    private String type = "REGULAR"; // REGULAR, EXTRA
    
    private String note;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructors
    public Deposit() {}
    
    public Deposit(Session session, Member member, Double amount, LocalDate depositDate, String type) {
        this.session = session;
        this.member = member;
        this.amount = amount;
        this.depositDate = depositDate;
        this.type = type;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }
    
    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }
    
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    
    public LocalDate getDepositDate() { return depositDate; }
    public void setDepositDate(LocalDate depositDate) { this.depositDate = depositDate; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}