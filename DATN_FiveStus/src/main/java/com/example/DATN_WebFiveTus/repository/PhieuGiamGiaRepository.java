package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.HoaDon;
import com.example.DATN_WebFiveTus.entity.PhieuGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, Integer> {

    @Query("SELECT pgg FROM PhieuGiamGia pgg   where pgg.deletedAt=false")
    Page<PhieuGiamGia> getAllJoinFetch(Pageable pageable);

//    @Query("SELECT pgg FROM PhieuGiamGia pgg " +
//            "JOIN FETCH pgg.khachHang " +
//            "WHERE (pgg.tenPhieuGiamGia LIKE %:query% OR pgg.maPhieuGiamGia LIKE %:query%) " +
//            "AND pgg.deletedAt = false")
//    List<PhieuGiamGia> searchByNameOrCode(@Param("query") String query);

//    @Query("SELECT pgg FROM PhieuGiamGia pgg " +
//            "JOIN FETCH pgg.khachHang " +
//            "WHERE pgg.trangThai = :status " +
//            "AND pgg.deletedAt = false")
//    List<PhieuGiamGia> filterByStatus(@Param("status") boolean status);

//    @Query("SELECT pgg FROM PhieuGiamGia pgg " +
//            "JOIN FETCH pgg.khachHang " +
//            "WHERE pgg.deletedAt = false")
//    List<PhieuGiamGia> getAllJoinFetch();

    @Query(value = "SELECT * FROM phieu_giam_gia pg " +
            "WHERE (:ma_phieu_giam_gia IS NULL OR pg.ma_phieu_giam_gia LIKE %:maPhieuGiamGia%) " +
            "AND (:ten_phieu_giam_gia IS NULL OR pg.ten_phieu_giam_gia LIKE %:tenPhieuGiamGia%) " +
            "AND (:hinh_thuc_giam_gia IS NULL OR pg.hinh_thuc_giam_gia LIKE %:hinhThucGiamGia%) " +
            "AND (:doi_tuong_ap_dung IS NULL OR pg.doi_tuong_ap_dung LIKE %:doiTuongApDung%) " +
            "AND (:ngay_bat_dau IS NULL OR pg.ngay_bat_dau = :ngayBatDau) " +
            "AND (:ngay_ket_thuc IS NULL OR pg.ngay_ket_thuc = :ngayKetThuc)",
            nativeQuery = true)
    List<PhieuGiamGia> searchPhieuGiamGia(
            @Param("maPhieuGiamGia") String maPhieuGiamGia,
            @Param("tenPhieuGiamGia") String tenPhieuGiamGia,
            @Param("hinhThucGiamGia") String hinhThucGiamGia,
            @Param("doiTuongApDung") String doiTuongApDung,
            @Param("ngayBatDau") Date ngayBatDau,
            @Param("ngayKetThuc") Date ngayKetThuc);

}
