package com.mealmanager.MealManagementSystem.repository;

import com.mealmanager.MealManagementSystem.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    Optional<Member> findByName(String name);
    
    List<Member> findByIsActiveTrue();
    
    List<Member> findByIsActiveFalse();
    
    @Query("SELECT m FROM Member m WHERE m.isActive = true ORDER BY m.name")
    List<Member> findAllActiveMembersSorted();
    
    boolean existsByNameAndIsActiveTrue(String name);
}