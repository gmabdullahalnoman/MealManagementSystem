package com.mealmanager.MealManagementSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

/**
 * DTO for Deposit entity
 */
public class DepositDTO {
    
    @NotNull(message = "Session ID is required")
    private Long sessionId;
    
    @NotNull(message = "Member ID is required")
    private Long memberId;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;
    
    @NotNull(message = "Deposit date is required")
    private LocalDate depositDate;
    
    private String type = "REGULAR";
    
    private String note;

    // Constructors
    public DepositDTO() {}

    public DepositDTO(Long sessionId, Long memberId, Double amount, LocalDate depositDate, String type) {
        this.sessionId = sessionId;
        this.memberId = memberId;
        this.amount = amount;
        this.depositDate = depositDate;
        this.type = type;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(LocalDate depositDate) {
        this.depositDate = depositDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
