package com.mealmanager.MealManagementSystem.controller;

import com.mealmanager.MealManagementSystem.dto.ApiResponse;
import com.mealmanager.MealManagementSystem.dto.MemberDTO;
import com.mealmanager.MealManagementSystem.entity.Member;
import com.mealmanager.MealManagementSystem.exception.ResourceNotFoundException;
import com.mealmanager.MealManagementSystem.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // ========== Thymeleaf Views ==========
    
    @GetMapping
    public String listMembers(Model model) {
        List<Member> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        model.addAttribute("activeMembers", memberService.getAllActiveMembers());
        return "members";
    }
    
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("member", new Member());
        return "member-form";
    }
    
    @PostMapping("/create")
    public String createMember(@RequestParam String name, @RequestParam(required = false) String phone) {
        memberService.createMember(name, phone);
        return "redirect:/members";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Member member = memberService.getMemberById(id)
                .orElseThrow(() -> new com.mealmanager.MealManagementSystem.exception.ResourceNotFoundException("Member with ID " + id + " not found"));
        model.addAttribute("member", member);
        return "member-form";
    }
    
    @PostMapping("/update/{id}")
    public String updateMember(@PathVariable Long id, @RequestParam String name, @RequestParam(required = false) String phone) {
        memberService.updateMember(id, name, phone);
        return "redirect:/members";
    }
    
    @PostMapping("/{id}/deactivate")
    public String deactivateMember(@PathVariable Long id) {
        memberService.deactivateMember(id);
        return "redirect:/members";
    }
    
    @PostMapping("/{id}/activate")
    public String activateMember(@PathVariable Long id) {
        memberService.activateMember(id);
        return "redirect:/members";
    }
    
    // ========== REST API Endpoints ==========
    
    @GetMapping("/api/all")
    public ResponseEntity<ApiResponse<List<Member>>> getAllMembersApi() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(ApiResponse.success(members, "Members retrieved successfully"));
    }
    
    @GetMapping("/api/active")
    public ResponseEntity<ApiResponse<List<Member>>> getActiveMembersApi() {
        List<Member> members = memberService.getAllActiveMembers();
        return ResponseEntity.ok(ApiResponse.success(members, "Active members retrieved successfully"));
    }
    
    @GetMapping("/api/{id}")
    public ResponseEntity<ApiResponse<Member>> getMemberByIdApi(@PathVariable Long id) {
        Member member = memberService.getMemberById(id)
                .orElseThrow(() -> new com.mealmanager.MealManagementSystem.exception.ResourceNotFoundException("Member with ID " + id + " not found"));
        return ResponseEntity.ok(ApiResponse.success(member, "Member retrieved successfully"));
    }
    
    @PostMapping("/api/create")
    public ResponseEntity<ApiResponse<Member>> createMemberApi(@Valid @RequestBody MemberDTO memberDTO) {
        Member member = memberService.createMember(memberDTO.getName(), memberDTO.getPhone());
        return ResponseEntity.ok(ApiResponse.success(member, "Member created successfully"));
    }
    
    @PutMapping("/api/{id}")
    public ResponseEntity<ApiResponse<Member>> updateMemberApi(@PathVariable Long id, @Valid @RequestBody MemberDTO memberDTO) {
        Member member = memberService.updateMember(id, memberDTO.getName(), memberDTO.getPhone());
        return ResponseEntity.ok(ApiResponse.success(member, "Member updated successfully"));
    }
    
    @DeleteMapping("/api/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateMemberApi(@PathVariable Long id) {
        memberService.deactivateMember(id);
        return ResponseEntity.ok(ApiResponse.success("Member deactivated successfully"));
    }
    
    @PostMapping("/api/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activateMemberApi(@PathVariable Long id) {
        memberService.activateMember(id);
        return ResponseEntity.ok(ApiResponse.success("Member activated successfully"));
    }
}