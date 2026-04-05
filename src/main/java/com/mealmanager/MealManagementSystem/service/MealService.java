package com.mealmanager.MealManagementSystem.service;

import com.mealmanager.MealManagementSystem.entity.MealRecord;
import com.mealmanager.MealManagementSystem.entity.Member;
import com.mealmanager.MealManagementSystem.entity.Session;
import com.mealmanager.MealManagementSystem.repository.MealRecordRepository;
import com.mealmanager.MealManagementSystem.repository.MemberRepository;
import com.mealmanager.MealManagementSystem.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
//import java.util.Optional;

@Service
public class MealService {

    @Autowired
    private MealRecordRepository mealRecordRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private MemberRepository memberRepository;

    // Add a meal record
    @Transactional
    public MealRecord addMeal(Long sessionId, Long memberId, LocalDate mealDate, String mealType, 
                               Integer guestCount, Long hostMemberId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.getIsClosed()) {
            throw new RuntimeException("Cannot add meal to a closed session");
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (!member.getIsActive()) {
            throw new RuntimeException("Cannot add meal for inactive member");
        }

        // Check for duplicate meal entry
        boolean exists = mealRecordRepository.existsBySessionAndMemberAndMealDateAndMealType(
                session, member, mealDate, mealType);
        if (exists) {
            throw new RuntimeException("Duplicate meal entry: Member already has " + mealType + " on this date");
        }

        MealRecord mealRecord = new MealRecord(session, member, mealDate, mealType);
        
        if (guestCount != null && guestCount > 0) {
            mealRecord.setGuestCount(guestCount);
        }
        
        if (hostMemberId != null && "GUEST".equals(mealType)) {
            Member hostMember = memberRepository.findById(hostMemberId)
                    .orElseThrow(() -> new RuntimeException("Host member not found"));
            mealRecord.setHostMember(hostMember);
        }

        return mealRecordRepository.save(mealRecord);
    }

    public List<MealRecord> getMealsBySession(Long sessionId) {    // Get meals by session
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        return mealRecordRepository.findBySession(session);
    }

    // Get meals by session and date
    public List<MealRecord> getMealsBySessionAndDate(Long sessionId, LocalDate mealDate) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        return mealRecordRepository.findBySessionAndMealDate(session, mealDate);
    }

    // Get total meal count for a session (LUNCH=1, DINNER=1, BOTH=2, GUEST=guestCount)
    public Integer getTotalMealCountBySession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        Integer total = mealRecordRepository.sumTotalMealsBySession(session);
        return total != null ? total : 0;
    }

    // Get meal count per member for a session
    public List<Object[]> getMealCountPerMember(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        return mealRecordRepository.getMealCountPerMember(session);
    }

    // Update meal record
    @Transactional
    public MealRecord updateMeal(Long mealId, String mealType, Integer guestCount) {
        MealRecord mealRecord = mealRecordRepository.findById(mealId)
                .orElseThrow(() -> new RuntimeException("Meal record not found"));

        if (mealRecord.getSession().getIsClosed()) {
            throw new RuntimeException("Cannot update meal in a closed session");
        }

        if (mealType != null) {
            mealRecord.setMealType(mealType);
        }
        if (guestCount != null) {
            mealRecord.setGuestCount(guestCount);
        }

        return mealRecordRepository.save(mealRecord);
    }

    // Delete meal record
    @Transactional
    public void deleteMeal(Long mealId) {
        MealRecord mealRecord = mealRecordRepository.findById(mealId)
                .orElseThrow(() -> new RuntimeException("Meal record not found"));

        if (mealRecord.getSession().getIsClosed()) {
            throw new RuntimeException("Cannot delete meal from a closed session");
        }

        mealRecordRepository.delete(mealRecord);
    }
}