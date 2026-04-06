package com.mealmanager.MealManagementSystem.controller;

import com.mealmanager.MealManagementSystem.entity.Deposit;
import com.mealmanager.MealManagementSystem.entity.Expense;
import com.mealmanager.MealManagementSystem.entity.Session;
import com.mealmanager.MealManagementSystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private DepositService depositService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private CalculationService calculationService;

    @Autowired
    private ReportService reportService;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        Session activeSession = sessionService.getActiveSession().orElse(null);
        
        if (activeSession == null) {
            model.addAttribute("error", "No active session found. Please create a session first.");
            model.addAttribute("totalDeposit", 0.0);
            model.addAttribute("totalExpense", 0.0);
            model.addAttribute("totalMeals", 0);
            model.addAttribute("mealRate", 0.0);
            model.addAttribute("recentDeposits", List.of());
            model.addAttribute("recentExpenses", List.of());
            model.addAttribute("memberReports", List.of());
            return "dashboard";
        }
        
        Long sessionId = activeSession.getId();
        
        // Summary data
        Double totalDeposit = calculationService.calculateTotalDeposit(sessionId);
        Double totalExpense = calculationService.calculateTotalExpense(sessionId);
        Integer totalMeals = calculationService.calculateTotalMeals(sessionId);
        Double mealRate = calculationService.calculateMealRate(sessionId);
        
        // Recent transactions (last 5)
        List<Deposit> recentDeposits = depositService.getDepositsBySession(sessionId);
        recentDeposits = recentDeposits.size() > 5 ? recentDeposits.subList(0, 5) : recentDeposits;
        
        List<Expense> recentExpenses = expenseService.getExpensesBySession(sessionId);
        recentExpenses = recentExpenses.size() > 5 ? recentExpenses.subList(0, 5) : recentExpenses;
        
        // Member reports
        Map<String, Object> report = reportService.getSessionReport(sessionId);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> memberReports = (List<Map<String, Object>>) report.get("memberReports");
        
        model.addAttribute("activeSession", activeSession);
        model.addAttribute("totalDeposit", totalDeposit);
        model.addAttribute("totalExpense", totalExpense);
        model.addAttribute("totalMeals", totalMeals);
        model.addAttribute("mealRate", mealRate);
        model.addAttribute("recentDeposits", recentDeposits);
        model.addAttribute("recentExpenses", recentExpenses);
        model.addAttribute("memberReports", memberReports);
        
        return "dashboard";
    }
}