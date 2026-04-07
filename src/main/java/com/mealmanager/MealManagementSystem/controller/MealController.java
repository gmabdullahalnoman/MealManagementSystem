package com.mealmanager.MealManagementSystem.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mealmanager.MealManagementSystem.dto.ApiResponse;
import com.mealmanager.MealManagementSystem.dto.MealDTO;
import com.mealmanager.MealManagementSystem.entity.MealRecord;
import com.mealmanager.MealManagementSystem.service.MealService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    @Autowired
    private MealService mealService;

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<ApiResponse<List<MealRecord>>> getMealsBySession(@PathVariable Long sessionId) {
        List<MealRecord> meals = mealService.getMealsBySession(sessionId);
        return ResponseEntity.ok(ApiResponse.success(meals, "Meals retrieved successfully"));
    }

    @GetMapping("/session/{sessionId}/date/{date}")
    public ResponseEntity<ApiResponse<List<MealRecord>>> getMealsBySessionAndDate(@PathVariable Long sessionId,
                                                         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<MealRecord> meals = mealService.getMealsBySessionAndDate(sessionId, date);
        return ResponseEntity.ok(ApiResponse.success(meals, "Meals retrieved successfully for date: " + date));
    }

    @GetMapping("/session/{sessionId}/total")
    public ResponseEntity<ApiResponse<Integer>> getTotalMealCount(@PathVariable Long sessionId) {
        Integer total = mealService.getTotalMealCountBySession(sessionId);
        return ResponseEntity.ok(ApiResponse.success(total, "Total meal count retrieved successfully"));
    }

    @GetMapping("/session/{sessionId}/per-member")
    public ResponseEntity<ApiResponse<List<Object[]>>> getMealCountPerMember(@PathVariable Long sessionId) {
        List<Object[]> mealsPerMember = mealService.getMealCountPerMember(sessionId);
        return ResponseEntity.ok(ApiResponse.success(mealsPerMember, "Meals per member retrieved successfully"));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MealRecord>> addMeal(@Valid @RequestBody MealDTO mealDTO) {
        MealRecord meal = mealService.addMeal(mealDTO.getSessionId(), mealDTO.getMemberId(), 
                mealDTO.getMealDate(), mealDTO.getMealType(), mealDTO.getGuestCount(), mealDTO.getHostMemberId());
        return ResponseEntity.status(201).body(ApiResponse.success(meal, "Meal added successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MealRecord>> updateMeal(@PathVariable Long id, @Valid @RequestBody MealDTO mealDTO) {
        MealRecord meal = mealService.updateMeal(id, mealDTO.getMealType(), mealDTO.getGuestCount());
        return ResponseEntity.ok(ApiResponse.success(meal, "Meal updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMeal(@PathVariable Long id) {
        mealService.deleteMeal(id);
        return ResponseEntity.ok(ApiResponse.success("Meal deleted successfully"));
    }
}