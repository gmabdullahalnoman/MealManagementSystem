package com.mealmanager.MealManagementSystem.repository;

import com.mealmanager.MealManagementSystem.entity.MealRecord;
import com.mealmanager.MealManagementSystem.entity.Member;
import com.mealmanager.MealManagementSystem.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MealRecordRepository extends JpaRepository<MealRecord, Long> {
    
    List<MealRecord> findBySession(Session session);
    
    List<MealRecord> findBySessionAndMember(Session session, Member member);
    
    List<MealRecord> findBySessionAndMealDate(Session session, LocalDate mealDate);
    
    Optional<MealRecord> findBySessionAndMemberAndMealDateAndMealType(
            Session session, Member member, LocalDate mealDate, String mealType);
    
    @Query("SELECT SUM(CASE WHEN m.mealType = 'BOTH' THEN 2 ELSE 1 END + m.guestCount) FROM MealRecord m WHERE m.session = :session")
    Integer sumTotalMealsBySession(@Param("session") Session session);
    
    @Query("SELECT SUM(m.guestCount) FROM MealRecord m WHERE m.session = :session AND m.hostMember = :member")
    Integer sumGuestCountBySessionAndHost(@Param("session") Session session, @Param("member") Member member);
    
    @Query("SELECT m.member, SUM(CASE WHEN m.mealType = 'BOTH' THEN 2 ELSE 1 END + m.guestCount) as mealCount " +
           "FROM MealRecord m WHERE m.session = :session GROUP BY m.member")
    List<Object[]> getMealCountPerMember(@Param("session") Session session);
    
    boolean existsBySessionAndMemberAndMealDateAndMealType(
            Session session, Member member, LocalDate mealDate, String mealType);
}