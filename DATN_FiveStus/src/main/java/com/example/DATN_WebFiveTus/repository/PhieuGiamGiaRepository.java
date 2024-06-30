package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.HoaDon;
import com.example.DATN_WebFiveTus.entity.PhieuGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia,Integer> {
    @Query("SELECT pgg FROM PhieuGiamGia pgg " +
            "JOIN FETCH pgg.khachHang where pgg.deletedAt=false")
    Page<PhieuGiamGia> getAllJoinFetch(Pageable pageable);
}
