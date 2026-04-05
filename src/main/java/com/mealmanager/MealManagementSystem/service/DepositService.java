package com.mealmanager.MealManagementSystem.service;

import com.mealmanager.MealManagementSystem.entity.Deposit;
import com.mealmanager.MealManagementSystem.entity.Member;
import com.mealmanager.MealManagementSystem.entity.Session;
import com.mealmanager.MealManagementSystem.repository.DepositRepository;
import com.mealmanager.MealManagementSystem.repository.MemberRepository;
import com.mealmanager.MealManagementSystem.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DepositService {

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Transactional   // Add a new deposit
    public Deposit addDeposit(Long sessionId, Long memberId, Double amount, LocalDate depositDate, String type, String note) {
        Session session = sessionRepository.findById(sessionId) 
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.getIsClosed()) {
            throw new RuntimeException("Cannot add deposit to a closed session");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (!member.getIsActive()) {
            throw new RuntimeException("Cannot add deposit for inactive member");
        }

        Deposit deposit = new Deposit(session, member, amount, depositDate, type);
        deposit.setNote(note);

        return depositRepository.save(deposit);
    }


    public List<Deposit> getDepositsBySession(Long sessionId) {    // Get all deposits for a session
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        return depositRepository.findBySession(session);
    }


    public Double getTotalDepositsBySession(Long sessionId) {    // Get total deposits for a session
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        Double total = depositRepository.sumBySession(session);
        return total != null ? total : 0.0;
    }


    public Double getMemberDeposits(Long sessionId, Long memberId) {    // Get total deposits for a specific member in a session
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Double total = depositRepository.sumBySessionAndMember(session, member);
        return total != null ? total : 0.0;
    }


    public List<Deposit> getDepositsByMember(Long memberId) {    // Get deposits by member
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        return depositRepository.findByMember(member);
    }


    @Transactional
    public void deleteDeposit(Long depositId) {    // Delete deposit (only if session not closed)
        Deposit deposit = depositRepository.findById(depositId)
                .orElseThrow(() -> new RuntimeException("Deposit not found"));

        if (deposit.getSession().getIsClosed()) {
            throw new RuntimeException("Cannot delete deposit from a closed session");
        }

        depositRepository.delete(deposit);
    }
}