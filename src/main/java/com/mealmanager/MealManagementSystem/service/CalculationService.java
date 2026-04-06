package com.mealmanager.MealManagementSystem.service;

import com.mealmanager.MealManagementSystem.entity.Member;
import com.mealmanager.MealManagementSystem.entity.Session;
import com.mealmanager.MealManagementSystem.exception.ResourceNotFoundException;
import com.mealmanager.MealManagementSystem.repository.DepositRepository;
import com.mealmanager.MealManagementSystem.repository.ExpenseRepository;
import com.mealmanager.MealManagementSystem.repository.MealRecordRepository;
import com.mealmanager.MealManagementSystem.repository.MemberRepository;
import com.mealmanager.MealManagementSystem.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculationService {

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private MealRecordRepository mealRecordRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SessionRepository sessionRepository;

    // Calculate meal rate for a session
    // Formula: total expense ÷ total meals
    public Double calculateMealRate(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session with ID " + sessionId + " not found"));

        Double totalExpense = expenseRepository.sumBySession(session);
        Integer totalMeals = mealRecordRepository.sumTotalMealsBySession(session);

        if (totalExpense == null) totalExpense = 0.0;
        if (totalMeals == null || totalMeals == 0) return 0.0;

        return totalExpense / totalMeals;
    }

    // Calculate total expense for a session
    public Double calculateTotalExpense(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session with ID " + sessionId + " not found"));
        Double total = expenseRepository.sumBySession(session);
        return total != null ? total : 0.0;
    }

    // Calculate total deposit for a session
    public Double calculateTotalDeposit(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session with ID " + sessionId + " not found"));
        Double total = depositRepository.sumBySession(session);
        return total != null ? total : 0.0;
    }

    // Calculate total meals for a session
    public Integer calculateTotalMeals(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session with ID " + sessionId + " not found"));
        Integer total = mealRecordRepository.sumTotalMealsBySession(session);
        return total != null ? total : 0;
    }

    // Calculate individual member balance for a session
    // Formula: member_deposit - (member_meals × meal_rate)
    public Double calculateMemberBalance(Long sessionId, Long memberId, Double mealRate) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session with ID " + sessionId + " not found"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member with ID " + memberId + " not found"));

        Double memberDeposit = depositRepository.sumBySessionAndMember(session, member);
        if (memberDeposit == null) memberDeposit = 0.0;

        Integer memberMeals = getMemberMealCount(session, member);
        
        Double memberCost = memberMeals * mealRate;
        
        return memberDeposit - memberCost;
    }

    // Get individual member meal count
    public Integer getMemberMealCount(Session session, Member member) {
        List<Object[]> results = mealRecordRepository.getMealCountPerMember(session);
        
        for (Object[] result : results) {
            Member m = (Member) result[0];
            Long mealCount = (Long) result[1];
            if (m.getId().equals(member.getId())) {
                return mealCount.intValue();
            }
        }
        return 0;
    }
}