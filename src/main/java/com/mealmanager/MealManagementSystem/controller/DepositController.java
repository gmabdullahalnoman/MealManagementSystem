package com.mealmanager.MealManagementSystem.controller;

import com.mealmanager.MealManagementSystem.entity.Deposit;
import com.mealmanager.MealManagementSystem.service.DepositService;
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
    @ResponseBody
    public List<Deposit> getDepositsBySessionApi(@PathVariable Long sessionId) {
        return depositService.getDepositsBySession(sessionId);
    }
    
    @GetMapping("/api/session/{sessionId}/total")
    @ResponseBody
    public Double getTotalDepositsBySessionApi(@PathVariable Long sessionId) {
        return depositService.getTotalDepositsBySession(sessionId);
    }
    
    @GetMapping("/api/member/{memberId}")
    @ResponseBody
    public List<Deposit> getDepositsByMemberApi(@PathVariable Long memberId) {
        return depositService.getDepositsByMember(memberId);
    }
    
    @GetMapping("/api/session/{sessionId}/member/{memberId}/total")
    @ResponseBody
    public Double getMemberDepositsApi(@PathVariable Long sessionId, @PathVariable Long memberId) {
        return depositService.getMemberDeposits(sessionId, memberId);
    }
    
    @PostMapping("/api/add")
    @ResponseBody
    public Deposit addDepositApi(@RequestParam Long sessionId,
                                 @RequestParam Long memberId,
                                 @RequestParam Double amount,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate depositDate,
                                 @RequestParam(defaultValue = "REGULAR") String type,
                                 @RequestParam(required = false) String note) {
        return depositService.addDeposit(sessionId, memberId, amount, depositDate, type, note);
    }
    
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public String deleteDepositApi(@PathVariable Long id) {
        depositService.deleteDeposit(id);
        return "Deposit deleted successfully";
    }
}