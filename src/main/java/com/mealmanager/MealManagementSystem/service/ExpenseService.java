package com.mealmanager.MealManagementSystem.service;

import com.mealmanager.MealManagementSystem.entity.Expense;
import com.mealmanager.MealManagementSystem.entity.Member;
import com.mealmanager.MealManagementSystem.entity.Session;
import com.mealmanager.MealManagementSystem.exception.InvalidOperationException;
import com.mealmanager.MealManagementSystem.exception.ResourceNotFoundException;
import com.mealmanager.MealManagementSystem.exception.SessionClosedException;
import com.mealmanager.MealManagementSystem.repository.ExpenseRepository;
import com.mealmanager.MealManagementSystem.repository.MemberRepository;
import com.mealmanager.MealManagementSystem.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private MemberRepository memberRepository;


    @Transactional    // Add a new expense
    public Expense addExpense(Long sessionId, LocalDate expenseDate, Double amount, String description, Long memberId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session with ID " + sessionId + " not found"));

        if (session.getIsClosed()) {
            throw new SessionClosedException("Cannot add expense to a closed session");
        }

        Expense expense = new Expense(session, expenseDate, amount, description);

        if (memberId != null) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new ResourceNotFoundException("Member with ID " + memberId + " not found"));
            expense.setMember(member);
        }

        return expenseRepository.save(expense);
    }

    public List<Expense> getExpensesBySession(Long sessionId) {    // Get all expenses for a session
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session with ID " + sessionId + " not found"));
        return expenseRepository.findBySessionOrderByExpenseDateDesc(session);
    }

    public Double getTotalExpensesBySession(Long sessionId) {    // Get total expenses for a session
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session with ID " + sessionId + " not found"));
        Double total = expenseRepository.sumBySession(session);
        return total != null ? total : 0.0;
    }

    public List<Expense> getExpensesByDateRange(LocalDate startDate, LocalDate endDate) {    // Get expenses by date range
        return expenseRepository.findByExpenseDateBetween(startDate, endDate);
    }

    @Transactional
    public void deleteExpense(Long expenseId) {    // Delete expense (only if session not closed)
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with ID " + expenseId + " not found"));

        if (expense.getSession().getIsClosed()) {
            throw new SessionClosedException("Cannot delete expense from a closed session");
        }

        expenseRepository.delete(expense);
    }


    @Transactional    // Update expense
    public Expense updateExpense(Long expenseId, Double amount, String description) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with ID " + expenseId + " not found"));

        if (expense.getSession().getIsClosed()) {
            throw new SessionClosedException("Cannot update expense in a closed session");
        }

        if (amount != null && amount > 0) {
            expense.setAmount(amount);
        }
        if (description != null) {
            expense.setDescription(description);
        }

        return expenseRepository.save(expense);
    }
}