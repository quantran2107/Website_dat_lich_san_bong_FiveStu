package com.example.DATN_WebFiveTus.config;

import com.example.DATN_WebFiveTus.entity.NhanVien;
import com.example.DATN_WebFiveTus.entity.auth.ERole;
import com.example.DATN_WebFiveTus.entity.auth.Role;
import com.example.DATN_WebFiveTus.entity.auth.User;
import com.example.DATN_WebFiveTus.repository.NhanVienReposity;
import com.example.DATN_WebFiveTus.repository.RoleRepository;
import com.example.DATN_WebFiveTus.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final NhanVienReposity nhanVienReposity;

    @Value("${db.generator.is-generated}")
    private String isGenerated;

    @PostConstruct
    public void init() {
        if ("true".equals(isGenerated)) generateData();
    }
    private void generateData() {
        Optional<User> userOptional = userRepository.findById(1L);
        for (ERole role : ERole.values()) {
            if (roleRepository.findByName(role) == null) {
                roleRepository.save(new Role(role));
            }
        }
        if (userOptional.isEmpty()) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setEmail("admin@gmail.com");
            Role role = roleRepository.findByName(ERole.ROLE_ADMIN);
            if (role == null) {
                throw new IllegalStateException("Vai trò admin không tồn tại trong csdl.");
            }
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
            user.setEnabled(true);
            userRepository.save(user);

            NhanVien nhanVien = new NhanVien();
            nhanVien.setEmail("admin@gmail.com");
            nhanVien.setTrangThai("active");
            nhanVien.setTenNhanVien("admin");
            nhanVien.setMatKhau(passwordEncoder.encode("admin123"));
            nhanVien.setMaNhanVien("admin");
            nhanVien.setDiaChi("admin");
            nhanVienReposity.save(nhanVien);
        }

    }
}
