package com.mealmanager.MealManagementSystem.controller;

import com.mealmanager.MealManagementSystem.dto.ApiResponse;
import com.mealmanager.MealManagementSystem.dto.DepositDTO;
import com.mealmanager.MealManagementSystem.entity.Deposit;
import com.mealmanager.MealManagementSystem.service.DepositService;
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
@RequestMapping("/deposits")
public class DepositController {

    @Autowired
    private DepositService depositService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private MemberService memberService;

    // ========== Thymeleaf Views ==========
    
    @GetMapping
    public String listDeposits(Model model) {
        Long activeSessionId = sessionService.getActiveSession()
                .orElseThrow(() -> new RuntimeException("No active session found")).getId();
        
        List<Deposit> deposits = depositService.getDepositsBySession(activeSessionId);
        Double totalDeposits = depositService.getTotalDepositsBySession(activeSessionId);
        
        model.addAttribute("deposits", deposits);
        model.addAttribute("totalDeposits", totalDeposits);
        model.addAttribute("activeSessionId", activeSessionId);
        model.addAttribute("members", memberService.getAllActiveMembers());
        
        return "deposits";
    }
    
    @PostMapping("/add")
    public String addDeposit(@RequestParam Long memberId,
                             @RequestParam Double amount,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate depositDate,
                             @RequestParam(defaultValue = "REGULAR") String type,
                             @RequestParam(required = false) String note) {
        Long activeSessionId = sessionService.getActiveSession()
                .orElseThrow(() -> new RuntimeException("No active session found")).getId();
        
        depositService.addDeposit(activeSessionId, memberId, amount, depositDate, type, note);
        return "redirect:/deposits";
    }
    
    @PostMapping("/{id}/delete")
    public String deleteDeposit(@PathVariable Long id) {
        depositService.deleteDeposit(id);
        return "redirect:/deposits";
    }
    
    // ========== REST API Endpoints ==========
    
    @GetMapping("/api/session/{sessionId}")
    public ResponseEntity<ApiResponse<List<Deposit>>> getDepositsBySessionApi(@PathVariable Long sessionId) {
        List<Deposit> deposits = depositService.getDepositsBySession(sessionId);
        return ResponseEntity.ok(ApiResponse.success(deposits, "Deposits retrieved successfully"));
    }
    
    @GetMapping("/api/session/{sessionId}/total")
    public ResponseEntity<ApiResponse<Double>> getTotalDepositsBySessionApi(@PathVariable Long sessionId) {
        Double total = depositService.getTotalDepositsBySession(sessionId);
        return ResponseEntity.ok(ApiResponse.success(total, "Total deposits calculated successfully"));
    }
    
    @GetMapping("/api/member/{memberId}")
    public ResponseEntity<ApiResponse<List<Deposit>>> getDepositsByMemberApi(@PathVariable Long memberId) {
        List<Deposit> deposits = depositService.getDepositsByMember(memberId);
        return ResponseEntity.ok(ApiResponse.success(deposits, "Member deposits retrieved successfully"));
    }
    
    @GetMapping("/api/session/{sessionId}/member/{memberId}/total")
    public ResponseEntity<ApiResponse<Double>> getMemberDepositsApi(@PathVariable Long sessionId, @PathVariable Long memberId) {
        Double total = depositService.getMemberDeposits(sessionId, memberId);
        return ResponseEntity.ok(ApiResponse.success(total, "Member deposit total calculated successfully"));
    }
    
    @PostMapping("/api/add")
    public ResponseEntity<ApiResponse<Deposit>> addDepositApi(@Valid @RequestBody DepositDTO depositDTO) {
        Deposit deposit = depositService.addDeposit(depositDTO.getSessionId(), depositDTO.getMemberId(),
                depositDTO.getAmount(), depositDTO.getDepositDate(), depositDTO.getType(), depositDTO.getNote());
        return ResponseEntity.ok(ApiResponse.success(deposit, "Deposit added successfully"));
    }
    
    @DeleteMapping("/api/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepositApi(@PathVariable Long id) {
        depositService.deleteDeposit(id);
        return ResponseEntity.ok(ApiResponse.success("Deposit deleted successfully"));
    }
}