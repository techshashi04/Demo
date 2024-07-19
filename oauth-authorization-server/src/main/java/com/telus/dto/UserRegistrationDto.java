package com.telus.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;


public record UserRegistrationDto (
        @NotEmpty(message = "User Name must not be empty")
        String userName,
        String userMobileNo,
        @NotEmpty(message = "User email must not be empty") //Neither null nor 0 size
        @Email(message = "Invalid email format")
        String userEmail,

        @NotEmpty(message = "User password must not be empty")
        String userPassword,
        @NotEmpty(message = "User role must not be empty")
        List<String> userRoles
){ }
