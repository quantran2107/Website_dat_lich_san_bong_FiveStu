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
    @Query("SELECT pg FROM PhieuGiamGia pg " +
            "WHERE (:keyword IS NULL OR pg.maPhieuGiamGia LIKE %:keyword% OR pg.tenPhieuGiamGia LIKE %:keyword%) " +
            "AND (:doiTuongApDung IS NULL OR pg.doiTuongApDung = :doiTuongApDung) " +
            "AND (:hinhThucGiamGia IS NULL OR pg.hinhThucGiamGia = :hinhThucGiamGia) " +
            "AND (:trangThai IS NULL OR pg.trangThai = :trangThai) " +
            "AND (:ngayBatDau IS NULL OR pg.ngayBatDau >= :ngayBatDau) " +
            "AND (:ngayKetThuc IS NULL OR pg.ngayKetThuc <= :ngayKetThuc)")
    List<PhieuGiamGia> searchPhieuGiamGia(
            @Param("keyword") String keyword,
            @Param("doiTuongApDung") Boolean doiTuongApDung,
            @Param("hinhThucGiamGia") Boolean hinhThucGiamGia,
            @Param("trangThai") String trangThai,
            @Param("ngayBatDau") Date ngayBatDau,
            @Param("ngayKetThuc") Date ngayKetThuc,
            Pageable pageable);

}
