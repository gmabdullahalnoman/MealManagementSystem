package com.mealmanager.MealManagementSystem.repository;

import com.mealmanager.MealManagementSystem.entity.Member;
import com.mealmanager.MealManagementSystem.entity.MemberClosedBalance;
import com.mealmanager.MealManagementSystem.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberClosedBalanceRepository extends JpaRepository<MemberClosedBalance, Long> {
    
    Optional<MemberClosedBalance> findBySessionAndMember(Session session, Member member);
    
    List<MemberClosedBalance> findBySession(Session session);
    
    List<MemberClosedBalance> findByMember(Member member);
    
    @Query("SELECT mcb FROM MemberClosedBalance mcb WHERE mcb.session = :session ORDER BY mcb.balance DESC")
    List<MemberClosedBalance> findBySessionOrderByBalanceDesc(@Param("session") Session session);
    
    @Query("SELECT SUM(mcb.balance) FROM MemberClosedBalance mcb WHERE mcb.session = :session")
    Double sumBalanceBySession(@Param("session") Session session);
    
    @Query("SELECT mcb FROM MemberClosedBalance mcb WHERE mcb.session = :session AND mcb.balance < 0")
    List<MemberClosedBalance> findMembersWithDues(@Param("session") Session session);
}