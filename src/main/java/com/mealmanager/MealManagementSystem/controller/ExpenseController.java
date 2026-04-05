package com.mealmanager.MealManagementSystem.controller;

import com.mealmanager.MealManagementSystem.entity.Expense;
import com.mealmanager.MealManagementSystem.service.ExpenseService;
import com.mealmanager.MealManagementSystem.service.SessionService;
import com.mealmanager.MealManagementSystem.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private MemberService memberService;

    // ========== Thymeleaf Views ==========
    
    @GetMapping
    public String listExpenses(Model model) {
        Long activeSessionId = sessionService.getActiveSession()
                .orElseThrow(() -> new RuntimeException("No active session found")).getId();
        
        List<Expense> expenses = expenseService.getExpensesBySession(activeSessionId);
        Double totalExpenses = expenseService.getTotalExpensesBySession(activeSessionId);
        
        model.addAttribute("expenses", expenses);
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("activeSessionId", activeSessionId);
        model.addAttribute("members", memberService.getAllActiveMembers());
        
        return "expenses";
    }
    
    @PostMapping("/add")
    public String addExpense(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expenseDate,
                             @RequestParam Double amount,
                             @RequestParam String description,
                             @RequestParam(required = false) Long memberId) {
        Long activeSessionId = sessionService.getActiveSession()
                .orElseThrow(() -> new RuntimeException("No active session found")).getId();
        
        expenseService.addExpense(activeSessionId, expenseDate, amount, description, memberId);
        return "redirect:/expenses";
    }
    
    @PostMapping("/{id}/delete")
    public String deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return "redirect:/expenses";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Expense expense = expenseService.getExpensesBySession(
                sessionService.getActiveSession().orElseThrow(() -> new RuntimeException("No active session")).getId())
                .stream().filter(e -> e.getId().equals(id)).findFirst()
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        model.addAttribute("expense", expense);
        model.addAttribute("members", memberService.getAllActiveMembers());
        return "expense-form";
    }
    
    @PostMapping("/update/{id}")
    public String updateExpense(@PathVariable Long id, @RequestParam Double amount, @RequestParam String description) {
        expenseService.updateExpense(id, amount, description);
        return "redirect:/expenses";
    }
    
    // ========== REST API Endpoints ==========
    
    @GetMapping("/api/session/{sessionId}")
    @ResponseBody
    public List<Expense> getExpensesBySessionApi(@PathVariable Long sessionId) {
        return expenseService.getExpensesBySession(sessionId);
    }
    
    @GetMapping("/api/session/{sessionId}/total")
    @ResponseBody
    public Double getTotalExpensesBySessionApi(@PathVariable Long sessionId) {
        return expenseService.getTotalExpensesBySession(sessionId);
    }
    
    @GetMapping("/api/date-range")
    @ResponseBody
    public List<Expense> getExpensesByDateRangeApi(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return expenseService.getExpensesByDateRange(startDate, endDate);
    }
    
    @PostMapping("/api/add")
    @ResponseBody
    public Expense addExpenseApi(@RequestParam Long sessionId,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expenseDate,
                                 @RequestParam Double amount,
                                 @RequestParam String description,
                                 @RequestParam(required = false) Long memberId) {
        return expenseService.addExpense(sessionId, expenseDate, amount, description, memberId);
    }
    
    @PutMapping("/api/{id}")
    @ResponseBody
    public Expense updateExpenseApi(@PathVariable Long id, @RequestParam Double amount, @RequestParam String description) {
        return expenseService.updateExpense(id, amount, description);
    }
    
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public String deleteExpenseApi(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return "Expense deleted successfully";
    }
}