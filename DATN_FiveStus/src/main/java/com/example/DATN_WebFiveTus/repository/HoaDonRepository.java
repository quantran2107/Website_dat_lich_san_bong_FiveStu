package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.HoaDon;
import com.example.DATN_WebFiveTus.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {

    @Query("SELECT hd FROM HoaDon hd " +
            "JOIN FETCH hd.khachHang " +
            "JOIN FETCH hd.nhanVien " +
            "JOIN FETCH hd.phieuGiamGia")
    List<HoaDon> getAllJoinFetch();

    @Query("SELECT hd FROM HoaDon hd " +
            "JOIN FETCH hd.khachHang kh " +
            "JOIN FETCH hd.nhanVien nv " +
            "JOIN FETCH hd.phieuGiamGia pg " +
            "WHERE (:loai IS NULL OR hd.loai = :loai) " +
            "AND (:keyword IS NULL OR hd.maHoaDon LIKE %:keyword% " +
            "OR nv.maNhanVien LIKE %:keyword% " +
            "OR kh.hoVaTen LIKE %:keyword% " +
            "OR kh.soDienThoai LIKE %:keyword%) " +
            "AND (:tongTienMin IS NULL OR hd.tongTien >= :tongTienMin) " +
            "AND (:tongTienMax IS NULL OR hd.tongTien <= :tongTienMax)" +
            "AND (:trangThai IS NULL OR hd.trangThai LIKE :trangThai)")
    List<HoaDon> searchAndFilter(
            @Param("loai") Boolean loai,
            @Param("trangThai") String trangThai,
            @Param("keyword") String keyword,
            @Param("tongTienMin") Float tongTienMin,
            @Param("tongTienMax") Float tongTienMax);

    @Query("SELECT hd FROM HoaDon hd LEFT JOIN FETCH hd.khachHang WHERE hd.id = :id")
    HoaDon findByIdWithKhachHang(@Param("id") Integer id);

}
