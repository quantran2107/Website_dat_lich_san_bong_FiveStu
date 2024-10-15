package com.example.DATN_WebFiveTus.config;

import com.example.DATN_WebFiveTus.entity.auth.ERole;
import com.example.DATN_WebFiveTus.entity.auth.Role;
import com.example.DATN_WebFiveTus.entity.auth.User;
import com.example.DATN_WebFiveTus.exception.RoleNotFoundException;
import com.example.DATN_WebFiveTus.repository.RoleRepository;
import com.example.DATN_WebFiveTus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RoleDataSeeder {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleFactory roleFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener
    @Transactional
    public void LoadRoles(ContextRefreshedEvent event) throws RoleNotFoundException {

        List<ERole> roles = Arrays.stream(ERole.values()).toList();

        for (ERole erole : roles) {
            if (roleRepository.findByName(erole) == null) {
                roleRepository.save(new Role(erole));
            }
        }

        if (userRepository.findAll().isEmpty()) {
            Set<Role> roles1 = new HashSet<>();
            roles1.add(roleFactory.getInstance("admin"));

            User user = new User();
            user.setUsername("root123456");
            user.setEmail("root@gmail.com");
            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(roles1);

            userRepository.save(user);
        }

    }

}
