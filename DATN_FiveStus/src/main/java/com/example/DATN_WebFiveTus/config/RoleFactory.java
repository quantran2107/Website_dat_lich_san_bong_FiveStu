package com.example.DATN_WebFiveTus.config;

import com.example.DATN_WebFiveTus.entity.auth.ERole;
import com.example.DATN_WebFiveTus.entity.auth.Role;
import com.example.DATN_WebFiveTus.exception.RoleNotFoundException;
import com.example.DATN_WebFiveTus.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleFactory {
    @Autowired
    RoleRepository roleRepository;

    public Role getInstance(String role) throws RoleNotFoundException {
        switch (role) {
            case "admin" -> {
                return roleRepository.findByName(ERole.ROLE_ADMIN);
            }
            case "manager" -> {
                return roleRepository.findByName(ERole.ROLE_MANAGER);
            }
            case "employee" -> {
                return roleRepository.findByName(ERole.ROLE_EMPLOYEE);
            }
            case "user" -> {
                return roleRepository.findByName(ERole.ROLE_USER);
            }
            default -> throw  new RoleNotFoundException("No role found for " +  role);
        }
    }
}
