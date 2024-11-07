package com.example.DATN_WebFiveTus.controller.auth;

import com.example.DATN_WebFiveTus.config.security.CookieUtils;
import com.example.DATN_WebFiveTus.config.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthControllerLog {

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/login")
    public String login() {
        return "/auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "/auth/register";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "/auth/forgot-password";
    }

    @GetMapping("/client/logout")
    public String clientLogout(HttpServletRequest request) {
        String token = CookieUtils.getCookie(request, "authToken");
        jwtUtils.deleteToken(jwtUtils.getUserNameFromJwtToken(token));
        return "redirect:/khach-hang/trang-chu";
    }
    @GetMapping("/admin/logout")
    public String adminLogout(HttpServletRequest request) {
        String token = CookieUtils.getCookie(request, "authToken");
        jwtUtils.deleteToken(jwtUtils.getUserNameFromJwtToken(token));
        return "redirect:/login";
    }
}
