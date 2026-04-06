package com.mealmanager.MealManagementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealmanager.MealManagementSystem.dto.ApiResponse;
import com.mealmanager.MealManagementSystem.dto.DepositDTO;
import com.mealmanager.MealManagementSystem.entity.Deposit;
import com.mealmanager.MealManagementSystem.service.DepositService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/deposits")
public class DepositController {

    @Autowired
    private DepositService depositService;

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<ApiResponse<List<Deposit>>> getDepositsBySessionApi(@PathVariable Long sessionId) {
        List<Deposit> deposits = depositService.getDepositsBySession(sessionId);
        return ResponseEntity.ok(ApiResponse.success(deposits, "Deposits retrieved successfully"));
    }

    @GetMapping("/session/{sessionId}/total")
    public ResponseEntity<ApiResponse<Double>> getTotalDepositsBySessionApi(@PathVariable Long sessionId) {
        Double total = depositService.getTotalDepositsBySession(sessionId);
        return ResponseEntity.ok(ApiResponse.success(total, "Total deposits calculated successfully"));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<ApiResponse<List<Deposit>>> getDepositsByMemberApi(@PathVariable Long memberId) {
        List<Deposit> deposits = depositService.getDepositsByMember(memberId);
        return ResponseEntity.ok(ApiResponse.success(deposits, "Member deposits retrieved successfully"));
    }

    @GetMapping("/session/{sessionId}/member/{memberId}/total")
    public ResponseEntity<ApiResponse<Double>> getMemberDepositsApi(@PathVariable Long sessionId, @PathVariable Long memberId) {
        Double total = depositService.getMemberDeposits(sessionId, memberId);
        return ResponseEntity.ok(ApiResponse.success(total, "Member deposit total calculated successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Deposit>> addDepositApi(@Valid @RequestBody DepositDTO depositDTO) {
        Deposit deposit = depositService.addDeposit(depositDTO.getSessionId(), depositDTO.getMemberId(),
                depositDTO.getAmount(), depositDTO.getDepositDate(), depositDTO.getType(), depositDTO.getNote());
        return ResponseEntity.status(201).body(ApiResponse.success(deposit, "Deposit added successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepositApi(@PathVariable Long id) {
        depositService.deleteDeposit(id);
        return ResponseEntity.ok(ApiResponse.success("Deposit deleted successfully"));
    }
}