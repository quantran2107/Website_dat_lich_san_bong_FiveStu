package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.ChiTietHinhThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChiTietHinhThucThanhToanRepository extends JpaRepository<ChiTietHinhThucThanhToan,Integer> {
}
