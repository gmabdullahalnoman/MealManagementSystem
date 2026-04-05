package com.mealmanager.MealManagementSystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "meal_records", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"session_id", "member_id", "meal_date", "meal_type"})
})
public class MealRecord {
    
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
    
    @NotNull(message = "Meal date is required")
    @Column(name = "meal_date", nullable = false)
    private LocalDate mealDate;
    
    @NotNull(message = "Meal type is required")
    @Column(name = "meal_type", nullable = false, length = 20)
    private String mealType; // LUNCH, DINNER, BOTH, GUEST
    
    @Column(name = "guest_count")
    private Integer guestCount = 0;
    
    @ManyToOne
    @JoinColumn(name = "host_member_id")
    private Member hostMember; // for GUEST meals only
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Constructors
    public MealRecord() {}
    
    public MealRecord(Session session, Member member, LocalDate mealDate, String mealType) {
        this.session = session;
        this.member = member;
        this.mealDate = mealDate;
        this.mealType = mealType;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }
    
    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }
    
    public LocalDate getMealDate() { return mealDate; }
    public void setMealDate(LocalDate mealDate) { this.mealDate = mealDate; }
    
    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    
    public Integer getGuestCount() { return guestCount; }
    public void setGuestCount(Integer guestCount) { this.guestCount = guestCount; }
    
    public Member getHostMember() { return hostMember; }
    public void setHostMember(Member hostMember) { this.hostMember = hostMember; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}