package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.PhieuGiamGia;
import com.example.DATN_WebFiveTus.entity.PhieuGiamGiaChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PhieuGiamGiaChiTietRepository extends JpaRepository<PhieuGiamGiaChiTiet,Integer> {
    @Query("SELECT pggct FROM PhieuGiamGiaChiTiet pggct JOIN FETCH  pggct.khachHang " +
            "JOIN FETCH  pggct.phieuGiamGia where pggct.deletedAt=false")
    Page<PhieuGiamGia> getAllJoinFetch(Pageable pageable);
}
