// src/main/java/com/project/society/dto/LoginRequest.java
package com.project.society.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    // new: optional remember me flag
    private Boolean rememberMe = false;
}
