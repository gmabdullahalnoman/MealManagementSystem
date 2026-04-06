package com.mealmanager.MealManagementSystem.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mealmanager.MealManagementSystem.dto.ApiResponse;
import com.mealmanager.MealManagementSystem.dto.ExpenseDTO;
import com.mealmanager.MealManagementSystem.entity.Expense;
import com.mealmanager.MealManagementSystem.service.ExpenseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // sessionService not required for REST endpoints

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<ApiResponse<List<Expense>>> getExpensesBySessionApi(@PathVariable Long sessionId) {
        List<Expense> expenses = expenseService.getExpensesBySession(sessionId);
        return ResponseEntity.ok(ApiResponse.success(expenses, "Expenses retrieved successfully"));
    }

    @GetMapping("/session/{sessionId}/total")
    public ResponseEntity<ApiResponse<Double>> getTotalExpensesBySessionApi(@PathVariable Long sessionId) {
        Double total = expenseService.getTotalExpensesBySession(sessionId);
        return ResponseEntity.ok(ApiResponse.success(total, "Total expenses calculated successfully"));
    }

    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<List<Expense>>> getExpensesByDateRangeApi(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Expense> expenses = expenseService.getExpensesByDateRange(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(expenses, "Expenses in date range retrieved successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Expense>> addExpenseApi(@Valid @RequestBody ExpenseDTO expenseDTO) {
        Expense expense = expenseService.addExpense(expenseDTO.getSessionId(), expenseDTO.getExpenseDate(),
                expenseDTO.getAmount(), expenseDTO.getDescription(), expenseDTO.getMemberId());
        return ResponseEntity.status(201).body(ApiResponse.success(expense, "Expense added successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Expense>> updateExpenseApi(@PathVariable Long id, @Valid @RequestBody ExpenseDTO expenseDTO) {
        Expense expense = expenseService.updateExpense(id, expenseDTO.getAmount(), expenseDTO.getDescription());
        return ResponseEntity.ok(ApiResponse.success(expense, "Expense updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExpenseApi(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.ok(ApiResponse.success("Expense deleted successfully"));
    }
}