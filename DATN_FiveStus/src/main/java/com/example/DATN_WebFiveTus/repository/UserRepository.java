package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    @Query("select u  from User u where u.email=:username or u.username=:username")
    Optional<User> findByEmailOrUseName(@Param("username") String username);
}
