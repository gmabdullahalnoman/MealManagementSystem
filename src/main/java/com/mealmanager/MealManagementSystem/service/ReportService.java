package com.mealmanager.MealManagementSystem.service;

import com.mealmanager.MealManagementSystem.entity.*;
import com.mealmanager.MealManagementSystem.repository.*;
import com.mealmanager.MealManagementSystem.exception.ResourceNotFoundException;
import com.mealmanager.MealManagementSystem.exception.InvalidOperationException;
import com.mealmanager.MealManagementSystem.exception.SessionClosedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class ReportService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private MealRecordRepository mealRecordRepository;

    @Autowired
    private ClosedMonthSummaryRepository closedMonthSummaryRepository;

    @Autowired
    private MemberClosedBalanceRepository memberClosedBalanceRepository;

    @Autowired
    private MemberOpeningBalanceRepository memberOpeningBalanceRepository;

    @Autowired
    private CalculationService calculationService;

    // Get complete report for a session (including member-wise details)
    public Map<String, Object> getSessionReport(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        Map<String, Object> report = new HashMap<>();
        
        // Summary data
        Double totalDeposit = calculationService.calculateTotalDeposit(sessionId);
        Double totalExpense = calculationService.calculateTotalExpense(sessionId);
        Integer totalMeals = calculationService.calculateTotalMeals(sessionId);
        Double mealRate = calculationService.calculateMealRate(sessionId);
        
        report.put("session", session);
        report.put("totalDeposit", totalDeposit);
        report.put("totalExpense", totalExpense);
        report.put("totalMeals", totalMeals);
        report.put("mealRate", mealRate);
        
        // Member-wise report
        List<Map<String, Object>> memberReports = new ArrayList<>();
        List<Member> activeMembers = memberRepository.findByIsActiveTrue();
        
        for (Member member : activeMembers) {
            Map<String, Object> memberReport = new HashMap<>();
            
            Double memberDeposit = depositRepository.sumBySessionAndMember(session, member);
            if (memberDeposit == null) memberDeposit = 0.0;
            
            Integer memberMeals = calculationService.getMemberMealCount(session, member);
            Double memberCost = memberMeals * mealRate;
            Double memberBalance = memberDeposit - memberCost;
            
            memberReport.put("member", member);
            memberReport.put("totalDeposit", memberDeposit);
            memberReport.put("totalMeals", memberMeals);
            memberReport.put("totalCost", memberCost);
            memberReport.put("balance", memberBalance);
            
            memberReports.add(memberReport);
        }
        
        report.put("memberReports", memberReports);
        
        return report;
    }

    // Close a month: calculate final rates, save summaries, prevent further edits
    @Transactional
    public Map<String, Object> closeMonth(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session with ID " + sessionId + " not found"));
        
        if (session.getIsClosed()) {
            throw new SessionClosedException("Session " + sessionId + " is already closed");
        }
        
        // Calculate final values
        Double totalDeposit = calculationService.calculateTotalDeposit(sessionId);
        Double totalExpense = calculationService.calculateTotalExpense(sessionId);
        Integer totalMeals = calculationService.calculateTotalMeals(sessionId);
        Double mealRate = calculationService.calculateMealRate(sessionId);
        
        // Save closed month summary
        ClosedMonthSummary summary = new ClosedMonthSummary(session, totalDeposit, totalExpense, totalMeals, mealRate);
        closedMonthSummaryRepository.save(summary);
        
        // Save member-wise closed balances and prepare carry forward
        List<Member> activeMembers = memberRepository.findByIsActiveTrue();
        List<MemberClosedBalance> memberBalances = new ArrayList<>();
        
        for (Member member : activeMembers) {
            Double memberDeposit = depositRepository.sumBySessionAndMember(session, member);
            if (memberDeposit == null) memberDeposit = 0.0;
            
            Integer memberMeals = calculationService.getMemberMealCount(session, member);
            Double memberCost = memberMeals * mealRate;
            Double memberBalance = memberDeposit - memberCost;
            
            MemberClosedBalance memberBalanceRecord = new MemberClosedBalance(
                session, member, memberDeposit, memberMeals, memberCost, memberBalance
            );
            memberBalances.add(memberClosedBalanceRepository.save(memberBalanceRecord));
        }
        
        // Close the session
        session.setIsClosed(true);
        session.setIsActive(false);
        session.setEndDate(LocalDate.now());
        sessionRepository.save(session);
        
        // Create next session automatically (carry forward positive balances)
        createNextSessionWithCarryForward(session, memberBalances);
        
        // Return report
        return getSessionReport(sessionId);
    }
    
    // Create next month session with carry forward balances
    @Transactional
    public Session createNextSessionWithCarryForward(Session closedSession, List<MemberClosedBalance> memberBalances) {
        // Create new session name (e.g., "April 2025" from "March 2025")
        String newSessionName = getNextMonthName(closedSession.getName());
        LocalDate newStartDate = closedSession.getEndDate().plusDays(1);
        
        // Deactivate any active session
        Optional<Session> activeSession = sessionRepository.findByIsActiveTrue();
        if (activeSession.isPresent()) {
            Session currentActive = activeSession.get();
            currentActive.setIsActive(false);
            sessionRepository.save(currentActive);
        }
        
        Session newSession = new Session(newSessionName, newStartDate);
        newSession.setIsActive(true);
        newSession.setIsClosed(false);
        Session savedNewSession = sessionRepository.save(newSession);
        
        // Carry forward positive balances as opening balance for next month
        for (MemberClosedBalance balance : memberBalances) {
            if (balance.getBalance() > 0) {
                // Positive balance carried forward
                MemberOpeningBalance openingBalance = new MemberOpeningBalance(
                    balance.getMember(), savedNewSession, balance.getBalance()
                );
                memberOpeningBalanceRepository.save(openingBalance);
            }
            // Negative balances (dues) - will need manual adjustment or separate tracking
            // For now, we don't carry forward negative balances automatically
        }
        
        return savedNewSession;
    }
    
    // Helper: Get next month name (simple implementation)
    private String getNextMonthName(String currentName) {
        // This is a placeholder. Implement proper month parsing based on your naming convention
        // Example: "March 2025" -> "April 2025"
        // For now, just append " (Next)"
        return currentName + " (Next)";
    }
    
    // Get report for closed session (from saved summaries)
    public Map<String, Object> getClosedSessionReport(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session with ID " + sessionId + " not found"));
        
        if (!session.getIsClosed()) {
            throw new InvalidOperationException("Session " + sessionId + " is not closed yet. Use getSessionReport for active session.");
        }
        
        ClosedMonthSummary summary = closedMonthSummaryRepository.findBySession(session)
                .orElseThrow(() -> new ResourceNotFoundException("Summary for session " + sessionId + " not found"));
        
        List<MemberClosedBalance> memberBalances = memberClosedBalanceRepository.findBySession(session);
        
        Map<String, Object> report = new HashMap<>();
        report.put("session", session);
        report.put("totalDeposit", summary.getTotalDeposit());
        report.put("totalExpense", summary.getTotalExpense());
        report.put("totalMeals", summary.getTotalMeals());
        report.put("mealRate", summary.getMealRate());
        report.put("memberBalances", memberBalances);
        
        return report;
    }
    
    // Get all closed months for dropdown
    public List<ClosedMonthSummary> getAllClosedMonths() {
        return closedMonthSummaryRepository.findAllClosedMonthsSummary();
    }
}