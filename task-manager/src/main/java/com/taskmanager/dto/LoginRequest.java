package com.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Simple request body for login. Just username and password.
 */
@Data
public class LoginRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}
