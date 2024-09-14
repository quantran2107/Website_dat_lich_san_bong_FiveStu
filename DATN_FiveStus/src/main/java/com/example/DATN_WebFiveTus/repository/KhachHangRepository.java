package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.KhachHang;

import com.example.DATN_WebFiveTus.entity.PhieuGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;


@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang,Integer> {
    @Query("SELECT kh FROM KhachHang kh WHERE kh.hoVaTen LIKE %:query% OR kh.soDienThoai LIKE %:query% OR kh.email LIKE %:query%")
    Page<KhachHang> searchByNamePhoneOrEmail(@Param("query") String query, Pageable pageable);

    @Query("SELECT kh FROM KhachHang kh WHERE kh.trangThai = :status")
    Page<KhachHang> filterByStatus(@Param("status") String status, Pageable pageable);

    @Query("SELECT kh FROM KhachHang kh WHERE kh.gioiTinh = :gender")
    Page<KhachHang> filterByGender(@Param("gender") boolean gender, Pageable pageable);

    @Query("SELECT kh FROM KhachHang kh WHERE kh.trangThai = :status AND kh.gioiTinh = :gender")
    Page<KhachHang> findByStatusAndGender(@Param("status") String status, @Param("gender") boolean gender, Pageable pageable);

    @Query("SELECT kh FROM KhachHang kh WHERE kh.soDienThoai = :soDienThoai ")
    KhachHang findKhachHangBySoDienThoai(@Param("soDienThoai") String soDienThoai);

}
