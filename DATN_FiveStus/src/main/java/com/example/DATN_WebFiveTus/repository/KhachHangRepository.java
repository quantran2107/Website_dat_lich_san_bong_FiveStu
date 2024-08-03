package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.KhachHang;

import com.example.DATN_WebFiveTus.entity.PhieuGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;


@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang,Integer> {
    @Query("SELECT kh FROM KhachHang kh WHERE kh.hoVaTen LIKE %:query% OR kh.soDienThoai LIKE %:query%")
    List<KhachHang> searchByNameOrPhone(@Param("query") String query);

    @Query("SELECT kh FROM KhachHang kh WHERE kh.trangThai = :status")
    List<KhachHang> filterByStatus(@Param("status") String status);
}
