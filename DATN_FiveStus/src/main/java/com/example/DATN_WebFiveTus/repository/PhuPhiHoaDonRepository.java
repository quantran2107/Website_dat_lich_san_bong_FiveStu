package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.PhuPhiHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhuPhiHoaDonRepository extends JpaRepository<PhuPhiHoaDon,Integer> {
}
