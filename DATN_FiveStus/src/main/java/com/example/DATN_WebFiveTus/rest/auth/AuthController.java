package com.example.DATN_WebFiveTus.rest.auth;

import com.example.DATN_WebFiveTus.config.security.jwt.JwtUtils;
import com.example.DATN_WebFiveTus.dto.ApiResponseDto;

import com.example.DATN_WebFiveTus.dto.request.ChangePassRequest;
import com.example.DATN_WebFiveTus.dto.request.SignInRequestDto;
import com.example.DATN_WebFiveTus.dto.request.SignUpRequestDto;
import com.example.DATN_WebFiveTus.exception.RoleNotFoundException;
import com.example.DATN_WebFiveTus.exception.UserAlreadyExistsException;
import com.example.DATN_WebFiveTus.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponseDto<?>> registerUser(@RequestBody @Valid SignUpRequestDto signUpRequestDto)
            throws UserAlreadyExistsException, RoleNotFoundException {
        return authService.signUp(signUpRequestDto);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<ApiResponseDto<?>> signInUser(@RequestBody @Valid SignInRequestDto signInRequestDto){
        return authService.signIn(signInRequestDto);
    }
    @PutMapping("/change-pass")
    public ResponseEntity<?>changepass(HttpServletRequest http, @RequestBody ChangePassRequest request){
        return authService.changePass(http,request);
    }
}
