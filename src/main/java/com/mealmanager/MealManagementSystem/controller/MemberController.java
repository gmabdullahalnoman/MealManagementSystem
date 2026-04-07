package com.mealmanager.MealManagementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealmanager.MealManagementSystem.dto.ApiResponse;
import com.mealmanager.MealManagementSystem.dto.MemberDTO;
import com.mealmanager.MealManagementSystem.entity.Member;
import com.mealmanager.MealManagementSystem.exception.ResourceNotFoundException;
import com.mealmanager.MealManagementSystem.service.MemberService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Member>>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(ApiResponse.success(members, "Members retrieved successfully"));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<Member>>> getActiveMembers() {
        List<Member> members = memberService.getAllActiveMembers();
        return ResponseEntity.ok(ApiResponse.success(members, "Active members retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Member>> getMemberById(@PathVariable Long id) {
        Member member = memberService.getMemberById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member with ID " + id + " not found"));
        return ResponseEntity.ok(ApiResponse.success(member, "Member retrieved successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Member>> createMember(@Valid @RequestBody MemberDTO memberDTO) {
        Member member = memberService.createMember(memberDTO.getName(), memberDTO.getPhone());
        return ResponseEntity.status(201).body(ApiResponse.success(member, "Member created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Member>> updateMember(@PathVariable Long id, @Valid @RequestBody MemberDTO memberDTO) {
        Member member = memberService.updateMember(id, memberDTO.getName(), memberDTO.getPhone());
        return ResponseEntity.ok(ApiResponse.success(member, "Member updated successfully"));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateMember(@PathVariable Long id) {
        memberService.deactivateMember(id);
        return ResponseEntity.ok(ApiResponse.success("Member deactivated successfully"));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activateMember(@PathVariable Long id) {
        memberService.activateMember(id);
        return ResponseEntity.ok(ApiResponse.success("Member activated successfully"));
    }
}