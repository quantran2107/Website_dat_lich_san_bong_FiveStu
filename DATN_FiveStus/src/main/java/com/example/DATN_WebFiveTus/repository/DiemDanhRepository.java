package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.DiemDanh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiemDanhRepository extends JpaRepository<DiemDanh,Integer> {
}
