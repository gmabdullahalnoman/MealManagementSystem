package com.mealmanager.MealManagementSystem.repository;

import com.mealmanager.MealManagementSystem.entity.Member;
import com.mealmanager.MealManagementSystem.entity.MemberOpeningBalance;
import com.mealmanager.MealManagementSystem.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberOpeningBalanceRepository extends JpaRepository<MemberOpeningBalance, Long> {
    
    Optional<MemberOpeningBalance> findByMemberAndSession(Member member, Session session);
    
    List<MemberOpeningBalance> findBySession(Session session);
    
    List<MemberOpeningBalance> findByMember(Member member);
    
    @Query("SELECT SUM(mob.amount) FROM MemberOpeningBalance mob WHERE mob.session = :session")
    Double sumBySession(@Param("session") Session session);
    
    @Query("SELECT mob FROM MemberOpeningBalance mob WHERE mob.session = :session AND mob.member.isActive = true")
    List<MemberOpeningBalance> findBySessionWithActiveMembers(@Param("session") Session session);
}