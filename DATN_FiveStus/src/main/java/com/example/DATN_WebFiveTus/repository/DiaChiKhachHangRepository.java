package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.DiaChi;
import com.example.DATN_WebFiveTus.entity.DiaChiKhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaChiKhachHangRepository extends JpaRepository<DiaChiKhachHang,Integer> {
    @Query("SELECT dckh FROM DiaChiKhachHang dckh JOIN FETCH dckh.khachHang")
    List<DiaChi> getAllJoinFetch();
}
