package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.ApiResponseDto;

import com.example.DATN_WebFiveTus.dto.request.ChangePassRequest;
import com.example.DATN_WebFiveTus.dto.request.OtpRequest;
import com.example.DATN_WebFiveTus.dto.request.SignInRequestDto;
import com.example.DATN_WebFiveTus.dto.request.SignUpRequestDto;
import com.example.DATN_WebFiveTus.exception.RoleNotFoundException;
import com.example.DATN_WebFiveTus.exception.UserAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    ResponseEntity<ApiResponseDto<?>> signUp(SignUpRequestDto signUpRequestDto) throws UserAlreadyExistsException, RoleNotFoundException;
    ResponseEntity<ApiResponseDto<?>> signIn(SignInRequestDto signInRequestDto);
    ResponseEntity<?> logout(String userName);
    ResponseEntity<?> changePass(HttpServletRequest request, ChangePassRequest changePassRequest);
    ResponseEntity<?> getRole(HttpServletRequest request);

    ResponseEntity<?> getOtp(String email) throws UserAlreadyExistsException;

    ResponseEntity<?> checkOtp(OtpRequest request);
}
