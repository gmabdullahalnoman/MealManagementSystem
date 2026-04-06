package com.mealmanager.MealManagementSystem.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * DTO for Report/Session Report data
 */
public class ReportDTO {
    
    private Long sessionId;
    private String sessionName;
    private LocalDate sessionStartDate;
    private LocalDate sessionEndDate;
    private Double totalDeposit;
    private Double totalExpense;
    private Integer totalMeals;
    private Double mealRate;
    private boolean sessionClosed;
    private List<MemberReportDTO> memberReports;

    // Constructors
    public ReportDTO() {}

    public ReportDTO(Long sessionId, String sessionName) {
        this.sessionId = sessionId;
        this.sessionName = sessionName;
    }

    // Getters and Setters
    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public LocalDate getSessionStartDate() {
        return sessionStartDate;
    }

    public void setSessionStartDate(LocalDate sessionStartDate) {
        this.sessionStartDate = sessionStartDate;
    }

    public LocalDate getSessionEndDate() {
        return sessionEndDate;
    }

    public void setSessionEndDate(LocalDate sessionEndDate) {
        this.sessionEndDate = sessionEndDate;
    }

    public Double getTotalDeposit() {
        return totalDeposit;
    }

    public void setTotalDeposit(Double totalDeposit) {
        this.totalDeposit = totalDeposit;
    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public Integer getTotalMeals() {
        return totalMeals;
    }

    public void setTotalMeals(Integer totalMeals) {
        this.totalMeals = totalMeals;
    }

    public Double getMealRate() {
        return mealRate;
    }

    public void setMealRate(Double mealRate) {
        this.mealRate = mealRate;
    }

    public boolean isSessionClosed() {
        return sessionClosed;
    }

    public void setSessionClosed(boolean sessionClosed) {
        this.sessionClosed = sessionClosed;
    }

    public List<MemberReportDTO> getMemberReports() {
        return memberReports;
    }

    public void setMemberReports(List<MemberReportDTO> memberReports) {
        this.memberReports = memberReports;
    }

    /**
     * Nested DTO for individual member report data
     */
    public static class MemberReportDTO {
        private Long memberId;
        private String memberName;
        private Double totalDeposit;
        private Integer totalMeals;
        private Double totalCost;
        private Double balance;

        // Constructors
        public MemberReportDTO() {}

        public MemberReportDTO(Long memberId, String memberName, Double totalDeposit, Integer totalMeals, Double totalCost, Double balance) {
            this.memberId = memberId;
            this.memberName = memberName;
            this.totalDeposit = totalDeposit;
            this.totalMeals = totalMeals;
            this.totalCost = totalCost;
            this.balance = balance;
        }

        // Getters and Setters
        public Long getMemberId() {
            return memberId;
        }

        public void setMemberId(Long memberId) {
            this.memberId = memberId;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public Double getTotalDeposit() {
            return totalDeposit;
        }

        public void setTotalDeposit(Double totalDeposit) {
            this.totalDeposit = totalDeposit;
        }

        public Integer getTotalMeals() {
            return totalMeals;
        }

        public void setTotalMeals(Integer totalMeals) {
            this.totalMeals = totalMeals;
        }

        public Double getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(Double totalCost) {
            this.totalCost = totalCost;
        }

        public Double getBalance() {
            return balance;
        }

        public void setBalance(Double balance) {
            this.balance = balance;
        }
    }
}
