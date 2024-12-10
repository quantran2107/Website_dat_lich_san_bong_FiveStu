package com.example.DATN_WebFiveTus.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {
    @NotBlank(message = "Tên người dùng là bắt buộc!")
    @Size(min= 3, message = "Tên người dùng phải có ít nhất 3 ký tự!")
    @Size(max= 20, message = "Tên người dùng có thể có tối đa 20 ký tự!")
    private String username;

    @Email(message = "Email không đúng định dạng!")
    @NotBlank(message = "Email là bắt buộc!")
    private String email;

    @NotBlank(message = "Mật khẩu là bắt buộc!")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự!")
    @Size(max = 20, message = "Mật khẩu có thể có tối đa 20 ký tự!")
    private String password;

    @NotBlank(message = "Tên là bắt buộc!")
    @Size(min= 5, message = "Tên phải có ít nhất 3 ký tự!")
    @Size(max= 50, message = "Tên có thể có tối đa 50 ký tự!")
    private String name;

    private String phoneNumber;

    private Set<String> roles;
}
