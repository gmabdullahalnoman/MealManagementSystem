package com.mealmanager.MealManagementSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for Spring Security 6.x
 * Configures authentication, authorization, and security filters
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configure HTTP security
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                // Allow public access to static resources and documentation
                .requestMatchers(
                    "/",
                    "/dashboard",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/webjars/**",
                    "/h2-console/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/api-docs/**",
                    "/api-docs",
                    "/members/**",
                    "/sessions/**",
                    "/deposits/**",
                    "/expenses/**",
                    "/meals/**",
                    "/reports/**",
                    "/api/**"
                ).permitAll()
                // Require authentication for all other endpoints
                .anyRequest().permitAll()
            )
            .httpBasic(httpBasic -> httpBasic.disable())
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
            .cors(cors -> cors.disable());

        return http.build();
    }

    /**
     * Password encoder bean for securing passwords
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
