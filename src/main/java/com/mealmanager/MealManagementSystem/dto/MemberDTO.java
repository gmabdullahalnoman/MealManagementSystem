package com.mealmanager.MealManagementSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO for Member entity
 */
public class MemberDTO {
    
    @NotBlank(message = "Member name is required")
    private String name;
    
    @Pattern(regexp = "^[0-9]{11}$|^$", message = "Phone must be 11 digits or empty")
    private String phone;

    // Constructors
    public MemberDTO() {}

    public MemberDTO(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
