package com.example.DATN_WebFiveTus.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInRequestDto {
    @NotBlank(message = "Email or Username is required!")
    private String username;

    @NotBlank(message = "Password is required!")
    private String password;
}
