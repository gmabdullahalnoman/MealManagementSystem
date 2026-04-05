package com.mealmanager.MealManagementSystem.repository;

import com.mealmanager.MealManagementSystem.entity.Deposit;
import com.mealmanager.MealManagementSystem.entity.Member;
import com.mealmanager.MealManagementSystem.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {
    
    List<Deposit> findBySession(Session session);
    
    List<Deposit> findByMember(Member member);
    
    List<Deposit> findBySessionAndMember(Session session, Member member);
    
    @Query("SELECT SUM(d.amount) FROM Deposit d WHERE d.session = :session")
    Double sumBySession(@Param("session") Session session);
    
    @Query("SELECT SUM(d.amount) FROM Deposit d WHERE d.session = :session AND d.member = :member")
    Double sumBySessionAndMember(@Param("session") Session session, @Param("member") Member member);
    
    List<Deposit> findByDepositDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<Deposit> findBySessionAndType(Session session, String type);
}