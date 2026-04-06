package com.mealmanager.MealManagementSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * DTO for Meal entity
 */
public class MealDTO {
    
    @NotNull(message = "Session ID is required")
    private Long sessionId;
    
    @NotNull(message = "Member ID is required")
    private Long memberId;
    
    @NotNull(message = "Meal date is required")
    private LocalDate mealDate;
    
    @NotBlank(message = "Meal type is required")
    private String mealType;
    
    private Integer guestCount;
    
    private Long hostMemberId;

    // Constructors
    public MealDTO() {}

    public MealDTO(Long sessionId, Long memberId, LocalDate mealDate, String mealType) {
        this.sessionId = sessionId;
        this.memberId = memberId;
        this.mealDate = mealDate;
        this.mealType = mealType;
    }

    // Getters and Setters
    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public LocalDate getMealDate() {
        return mealDate;
    }

    public void setMealDate(LocalDate mealDate) {
        this.mealDate = mealDate;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public Long getHostMemberId() {
        return hostMemberId;
    }

    public void setHostMemberId(Long hostMemberId) {
        this.hostMemberId = hostMemberId;
    }
}
