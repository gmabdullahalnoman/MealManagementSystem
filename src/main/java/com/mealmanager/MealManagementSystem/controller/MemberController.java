package com.mealmanager.MealManagementSystem.controller;

import com.mealmanager.MealManagementSystem.entity.Member;
import com.mealmanager.MealManagementSystem.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
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
                .orElseThrow(() -> new RuntimeException("Member not found"));
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
    @ResponseBody
    public List<Member> getAllMembersApi() {
        return memberService.getAllMembers();
    }
    
    @GetMapping("/api/active")
    @ResponseBody
    public List<Member> getActiveMembersApi() {
        return memberService.getAllActiveMembers();
    }
    
    @GetMapping("/api/{id}")
    @ResponseBody
    public Member getMemberByIdApi(@PathVariable Long id) {
        return memberService.getMemberById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));
    }
    
    @PostMapping("/api/create")
    @ResponseBody
    public Member createMemberApi(@RequestParam String name, @RequestParam(required = false) String phone) {
        return memberService.createMember(name, phone);
    }
    
    @PutMapping("/api/{id}")
    @ResponseBody
    public Member updateMemberApi(@PathVariable Long id, @RequestParam String name, @RequestParam(required = false) String phone) {
        return memberService.updateMember(id, name, phone);
    }
    
    @DeleteMapping("/api/{id}/deactivate")
    @ResponseBody
    public Member deactivateMemberApi(@PathVariable Long id) {
        return memberService.deactivateMember(id);
    }
}