package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet,Integer> {
    @Query("SELECT hdct FROM HoaDonChiTiet hdct " +
            "JOIN FETCH hdct.hoaDon " +
            "JOIN FETCH hdct.sanCa ")
    List<HoaDonChiTiet> getAllJoinFetch();

    @Query("SELECT hdct FROM HoaDonChiTiet hdct " +
            "JOIN FETCH hdct.hoaDon " +
            "JOIN FETCH hdct.sanCa WHERE hdct.hoaDon.id = :idHoaDon")
    List<HoaDonChiTiet> searchFromHoaDon(@Param("idHoaDon") Integer idHoaDon);
}
