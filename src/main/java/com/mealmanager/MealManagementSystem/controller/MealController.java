package com.mealmanager.MealManagementSystem.controller;

import com.mealmanager.MealManagementSystem.entity.MealRecord;
import com.mealmanager.MealManagementSystem.service.MealService;
import com.mealmanager.MealManagementSystem.service.SessionService;
import com.mealmanager.MealManagementSystem.service.MemberService;
import com.mealmanager.MealManagementSystem.service.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

    @Autowired
    private CalculationService calculationService;

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
    @ResponseBody
    public List<MealRecord> getMealsBySessionApi(@PathVariable Long sessionId) {
        return mealService.getMealsBySession(sessionId);
    }
    
    @GetMapping("/api/session/{sessionId}/date/{date}")
    @ResponseBody
    public List<MealRecord> getMealsBySessionAndDateApi(@PathVariable Long sessionId,
                                                         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return mealService.getMealsBySessionAndDate(sessionId, date);
    }
    
    @GetMapping("/api/session/{sessionId}/total")
    @ResponseBody
    public Integer getTotalMealCountApi(@PathVariable Long sessionId) {
        return mealService.getTotalMealCountBySession(sessionId);
    }
    
    @GetMapping("/api/session/{sessionId}/per-member")
    @ResponseBody
    public List<Object[]> getMealCountPerMemberApi(@PathVariable Long sessionId) {
        return mealService.getMealCountPerMember(sessionId);
    }
    
    @PostMapping("/api/add")
    @ResponseBody
    public MealRecord addMealApi(@RequestParam Long sessionId,
                                 @RequestParam Long memberId,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate mealDate,
                                 @RequestParam String mealType,
                                 @RequestParam(required = false) Integer guestCount,
                                 @RequestParam(required = false) Long hostMemberId) {
        return mealService.addMeal(sessionId, memberId, mealDate, mealType, guestCount, hostMemberId);
    }
    
    @PutMapping("/api/{id}")
    @ResponseBody
    public MealRecord updateMealApi(@PathVariable Long id,
                                    @RequestParam(required = false) String mealType,
                                    @RequestParam(required = false) Integer guestCount) {
        return mealService.updateMeal(id, mealType, guestCount);
    }
    
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public String deleteMealApi(@PathVariable Long id) {
        mealService.deleteMeal(id);
        return "Meal record deleted successfully";
    }
}