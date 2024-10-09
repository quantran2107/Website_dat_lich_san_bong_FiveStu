package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository  extends JpaRepository<Roles,Integer> {

    @Query("SELECT r FROM Roles r where r.account.ma=:ma")
    Roles findByMa(String ma);
}