package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.LichSuHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LichSuHoaDonRepository extends JpaRepository<LichSuHoaDon,Integer> {
}
