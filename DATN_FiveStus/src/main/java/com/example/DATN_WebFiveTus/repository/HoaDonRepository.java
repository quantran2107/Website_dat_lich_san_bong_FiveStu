package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon,Integer> {

    @Query("SELECT hd FROM HoaDon hd " +
            "JOIN FETCH hd.khachHang " +
            "JOIN FETCH hd.nhanVien " +
            "JOIN FETCH hd.phieuGiamGia")
    List<HoaDon> getAllJoinFetch();

    @Query("SELECT hd FROM HoaDon hd " +
            "JOIN FETCH hd.khachHang kh " +
            "JOIN FETCH hd.nhanVien " +
            "JOIN FETCH hd.phieuGiamGia " +
            "WHERE hd.maHoaDon LIKE %:key% OR kh.hoVaTen LIKE %:key%")
    List<HoaDon> searchHD(@Param("key") String key);

}
