package com.mealmanager.MealManagementSystem.service;

import com.mealmanager.MealManagementSystem.entity.Member;
import com.mealmanager.MealManagementSystem.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public Member createMember(String name, String phone) { // Create new member
        Member member = new Member(name, phone);
        member.setIsActive(true);
        return memberRepository.save(member);
    }

    public List<Member> getAllActiveMembers() {  // Get all active members
        return memberRepository.findByIsActiveTrue();
    }

    public List<Member> getAllMembers() {  // Get all members (including inactive)
        return memberRepository.findAll();
    }

    public Optional<Member> getMemberById(Long id) {    // Get member by ID
        return memberRepository.findById(id);
    }

    @Transactional
    public Member updateMember(Long id, String name, String phone) {    // Update member
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        
        if (name != null && !name.isEmpty()) {
            member.setName(name);
        }
        if (phone != null) {
            member.setPhone(phone);
        }
        
        return memberRepository.save(member);
    }

    @Transactional
    public Member deactivateMember(Long id) {     // Deactivate member (soft delete)
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        
        member.setIsActive(false);
        return memberRepository.save(member);
    }

    @Transactional
    public Member activateMember(Long id) {    // Activate member
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        
        member.setIsActive(true);
        return memberRepository.save(member);
    }

    public Optional<Member> getMemberByName(String name) {    // Get member by name
        return memberRepository.findByName(name);
    }
}