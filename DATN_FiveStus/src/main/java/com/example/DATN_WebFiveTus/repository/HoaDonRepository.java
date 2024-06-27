package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon,Integer> {
}
