package com.mealmanager.MealManagementSystem.controller;

import com.mealmanager.MealManagementSystem.dto.ApiResponse;
import com.mealmanager.MealManagementSystem.dto.ExpenseDTO;
import com.mealmanager.MealManagementSystem.entity.Expense;
import com.mealmanager.MealManagementSystem.service.ExpenseService;
import com.mealmanager.MealManagementSystem.service.SessionService;
import com.mealmanager.MealManagementSystem.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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
    
    // ========== REST API Endpoints ==========
    
    @GetMapping("/api/session/{sessionId}")
    public ResponseEntity<ApiResponse<List<Expense>>> getExpensesBySessionApi(@PathVariable Long sessionId) {
        List<Expense> expenses = expenseService.getExpensesBySession(sessionId);
        return ResponseEntity.ok(ApiResponse.success(expenses, "Expenses retrieved successfully"));
    }
    
    @GetMapping("/api/session/{sessionId}/total")
    public ResponseEntity<ApiResponse<Double>> getTotalExpensesBySessionApi(@PathVariable Long sessionId) {
        Double total = expenseService.getTotalExpensesBySession(sessionId);
        return ResponseEntity.ok(ApiResponse.success(total, "Total expenses calculated successfully"));
    }
    
    @GetMapping("/api/date-range")
    public ResponseEntity<ApiResponse<List<Expense>>> getExpensesByDateRangeApi(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Expense> expenses = expenseService.getExpensesByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(expenses, "Expenses in date range retrieved successfully"));
    }
    
    @PostMapping("/api/add")
    public ResponseEntity<ApiResponse<Expense>> addExpenseApi(@Valid @RequestBody ExpenseDTO expenseDTO) {
        Expense expense = expenseService.addExpense(expenseDTO.getSessionId(), expenseDTO.getExpenseDate(),
                expenseDTO.getAmount(), expenseDTO.getDescription(), expenseDTO.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(expense, "Expense added successfully"));
    }
    
    @PutMapping("/api/{id}")
    public ResponseEntity<ApiResponse<Expense>> updateExpenseApi(@PathVariable Long id, @Valid @RequestBody ExpenseDTO expenseDTO) {
        Expense expense = expenseService.updateExpense(id, expenseDTO.getAmount(), expenseDTO.getDescription());
        return ResponseEntity.ok(ApiResponse.success(expense, "Expense updated successfully"));
    }
    
    @DeleteMapping("/api/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExpenseApi(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok(ApiResponse.success("Expense deleted successfully"));
    }
}