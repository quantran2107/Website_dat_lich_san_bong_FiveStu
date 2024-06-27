package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.Ca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaRepository extends JpaRepository<Ca,Integer> {
}
