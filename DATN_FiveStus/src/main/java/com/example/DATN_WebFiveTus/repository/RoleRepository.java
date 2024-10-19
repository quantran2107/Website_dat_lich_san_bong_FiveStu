package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.auth.ERole;
import com.example.DATN_WebFiveTus.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(ERole name);
}
