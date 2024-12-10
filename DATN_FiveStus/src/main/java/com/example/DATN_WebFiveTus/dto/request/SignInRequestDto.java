package com.example.DATN_WebFiveTus.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignInRequestDto {
    @NotBlank(message = "Email người dùng là bắt buộc!")
    private String username;

    @NotBlank(message = "Mật khẩu là bắt buộc!")
    private String password;
}
