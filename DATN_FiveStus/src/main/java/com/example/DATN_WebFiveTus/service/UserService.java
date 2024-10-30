package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.ApiResponseDto;
import com.example.DATN_WebFiveTus.entity.auth.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    boolean existByUsername(String username);
    boolean existByEmail(String email);
    void save(User user);

    ResponseEntity<ApiResponseDto<?>> getUserInRequest(HttpServletRequest request);

}
