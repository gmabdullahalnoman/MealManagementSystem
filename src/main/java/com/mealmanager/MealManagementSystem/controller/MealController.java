package com.mealmanager.MealManagementSystem.controller;

import com.mealmanager.MealManagementSystem.dto.ApiResponse;
import com.mealmanager.MealManagementSystem.dto.MealDTO;
import com.mealmanager.MealManagementSystem.entity.MealRecord;
import com.mealmanager.MealManagementSystem.service.MealService;
import com.mealmanager.MealManagementSystem.service.SessionService;
import com.mealmanager.MealManagementSystem.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/meals")
public class MealController {

    @Autowired
    private MealService mealService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private MemberService memberService;

    // ========== Thymeleaf Views ==========
    
    @GetMapping
    public String listMeals(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                            Model model) {
        Long activeSessionId = sessionService.getActiveSession()
                .orElseThrow(() -> new RuntimeException("No active session found")).getId();
        
        LocalDate selectedDate = date != null ? date : LocalDate.now();
        
        List<MealRecord> meals = mealService.getMealsBySessionAndDate(activeSessionId, selectedDate);
        Integer totalMeals = mealService.getTotalMealCountBySession(activeSessionId);
        
        model.addAttribute("meals", meals);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("totalMeals", totalMeals);
        model.addAttribute("activeSessionId", activeSessionId);
        model.addAttribute("members", memberService.getAllActiveMembers());
        
        return "meals";
    }
    
    @PostMapping("/add")
    public String addMeal(@RequestParam Long memberId,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate mealDate,
                          @RequestParam String mealType,
                          @RequestParam(required = false) Integer guestCount,
                          @RequestParam(required = false) Long hostMemberId) {
        Long activeSessionId = sessionService.getActiveSession()
                .orElseThrow(() -> new RuntimeException("No active session found")).getId();
        
        mealService.addMeal(activeSessionId, memberId, mealDate, mealType, guestCount, hostMemberId);
        return "redirect:/meals?date=" + mealDate;
    }
    
    @PostMapping("/{id}/delete")
    public String deleteMeal(@PathVariable Long id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate mealDate) {
        mealService.deleteMeal(id);
        return "redirect:/meals?date=" + mealDate;
    }
    
    @GetMapping("/summary")
    public String mealSummary(Model model) {
        Long activeSessionId = sessionService.getActiveSession()
                .orElseThrow(() -> new RuntimeException("No active session found")).getId();
        
        Integer totalMeals = mealService.getTotalMealCountBySession(activeSessionId);
        List<Object[]> mealsPerMember = mealService.getMealCountPerMember(activeSessionId);
        
        model.addAttribute("totalMeals", totalMeals);
        model.addAttribute("mealsPerMember", mealsPerMember);
        
        return "meal-summary";
    }
    
    // ========== REST API Endpoints ==========
    
    @GetMapping("/api/session/{sessionId}")
    public ResponseEntity<ApiResponse<List<MealRecord>>> getMealsBySessionApi(@PathVariable Long sessionId) {
        List<MealRecord> meals = mealService.getMealsBySession(sessionId);
        return ResponseEntity.ok(ApiResponse.success(meals, "Meals retrieved successfully"));
    }
    
    @GetMapping("/api/session/{sessionId}/date/{date}")
    public ResponseEntity<ApiResponse<List<MealRecord>>> getMealsBySessionAndDateApi(@PathVariable Long sessionId,
                                                         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<MealRecord> meals = mealService.getMealsBySessionAndDate(sessionId, date);
        return ResponseEntity.ok(ApiResponse.success(meals, "Meals retrieved successfully for date: " + date));
    }
    
    @GetMapping("/api/session/{sessionId}/total")
    public ResponseEntity<ApiResponse<Integer>> getTotalMealCountApi(@PathVariable Long sessionId) {
        Integer total = mealService.getTotalMealCountBySession(sessionId);
        return ResponseEntity.ok(ApiResponse.success(total, "Total meal count retrieved successfully"));
    }
    
    @GetMapping("/api/session/{sessionId}/per-member")
    public ResponseEntity<ApiResponse<List<Object[]>>> getMealCountPerMemberApi(@PathVariable Long sessionId) {
        List<Object[]> mealsPerMember = mealService.getMealCountPerMember(sessionId);
        return ResponseEntity.ok(ApiResponse.success(mealsPerMember, "Meals per member retrieved successfully"));
    }
    
    @PostMapping("/api/add")
    public ResponseEntity<ApiResponse<MealRecord>> addMealApi(@Valid @RequestBody MealDTO mealDTO) {
        MealRecord meal = mealService.addMeal(mealDTO.getSessionId(), mealDTO.getMemberId(), 
                mealDTO.getMealDate(), mealDTO.getMealType(), mealDTO.getGuestCount(), mealDTO.getHostMemberId());
        return ResponseEntity.ok(ApiResponse.success(meal, "Meal added successfully"));
    }
    
    @PutMapping("/api/{id}")
    public ResponseEntity<ApiResponse<MealRecord>> updateMealApi(@PathVariable Long id, @Valid @RequestBody MealDTO mealDTO) {
        MealRecord meal = mealService.updateMeal(id, mealDTO.getMealType(), mealDTO.getGuestCount());
        return ResponseEntity.ok(ApiResponse.success(meal, "Meal updated successfully"));
    }
    
    @DeleteMapping("/api/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMealApi(@PathVariable Long id) {
        mealService.deleteMeal(id);
        return ResponseEntity.ok(ApiResponse.success("Meal deleted successfully"));
    }
}