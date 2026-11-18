package com.st_albanito.solarsizer.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Email Requires")
        @Email(message = "Must be a Valid Email Address")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8,message = "password should be at least 8 characters")
        String password,

        @NotBlank(message = "Display name is required")
        @Size(max = 100,message = "Display name cannot exceed 100 characters")
        String displayName
){}
