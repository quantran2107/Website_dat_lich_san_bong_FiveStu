package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.PhieuGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia,Integer> {
}
