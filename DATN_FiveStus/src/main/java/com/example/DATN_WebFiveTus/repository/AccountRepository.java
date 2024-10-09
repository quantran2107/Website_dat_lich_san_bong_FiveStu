package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,String> {

    Optional<Account> findByUsernameOrEmail(String username, String email);

}