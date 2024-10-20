package com.example.DATN_WebFiveTus.config.security;

import com.example.DATN_WebFiveTus.config.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class CheckRole {
    @Autowired
    JwtUtils jwtUtils;

    public boolean checkUser(HttpServletRequest request){
        List<?> roles =getListRole(request);
        return roles != null && roles.contains("ROLE_USER");
    }
    public boolean checkAdmin(HttpServletRequest request){
        List<?> roles =getListRole(request);
        return roles != null && roles.contains("ROLE_ADMIN");
    }
    public boolean checkManager(HttpServletRequest request){
        List<?> roles =getListRole(request);
        return roles != null && roles.contains("ROLE_MANAGER");
    }
    public boolean checkEmployee(HttpServletRequest request){
        List<?> roles =getListRole(request);
        return roles != null && roles.contains("ROLE_EMPLOYEE");
    }

    public List<?> getListRole(HttpServletRequest request){
        String token = CookieUtils.getCookie(request, "authToken");
        if (token != null && jwtUtils.validateJwtToken(token) && jwtUtils.checkBlackList(token) ) {
            return jwtUtils.getRolesFromToken(token);
        }
        return null;
    }
}
