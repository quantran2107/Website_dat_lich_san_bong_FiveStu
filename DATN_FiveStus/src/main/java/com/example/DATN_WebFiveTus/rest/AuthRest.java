package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.JwtAuthResponse;
import com.example.DATN_WebFiveTus.dto.LoginDTO;
import com.example.DATN_WebFiveTus.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthRest {

    private AuthService authService;

    @Autowired
    public AuthRest(AuthService authService) {
        this.authService = authService;
    }

    // Build Login REST API

    @PostMapping("login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDTO loginDto){
        try {
            JwtAuthResponse jwtAuthResponse = authService.login(loginDto);
            return new ResponseEntity<>(jwtAuthResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("test")
    public ResponseEntity<String> demo(){
        return ResponseEntity.ok("Nguyễn Thị Thuý Hằng 2502");
    }
}
