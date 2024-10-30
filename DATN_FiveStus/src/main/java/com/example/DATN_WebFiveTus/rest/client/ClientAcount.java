package com.example.DATN_WebFiveTus.rest.client;

import com.example.DATN_WebFiveTus.config.security.CookieUtils;
import com.example.DATN_WebFiveTus.config.security.jwt.JwtUtils;
import com.example.DATN_WebFiveTus.dto.ApiResponseDto;
import com.example.DATN_WebFiveTus.exception.UserAlreadyExistsException;
import com.example.DATN_WebFiveTus.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customer")
public class ClientAcount {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @GetMapping("get-customer")
    public ResponseEntity<ApiResponseDto<?>> getCustomerInRequest(HttpServletRequest request) {
        return userService.getUserInRequest(request);
    }

    @GetMapping("check-login")
    public ResponseEntity<?> checkLogin(HttpServletRequest request) {
        String token = CookieUtils.getCookie(request, "authToken");
        if (token != null && jwtUtils.validateJwtToken(token) && jwtUtils.checkBlackList(token) ) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(new UserAlreadyExistsException("User don't login"));
    }
}
