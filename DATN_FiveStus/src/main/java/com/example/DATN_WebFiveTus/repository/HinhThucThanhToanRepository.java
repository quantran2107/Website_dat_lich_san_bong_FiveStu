package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.HinhThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HinhThucThanhToanRepository extends JpaRepository<HinhThucThanhToan,Integer> {
}
