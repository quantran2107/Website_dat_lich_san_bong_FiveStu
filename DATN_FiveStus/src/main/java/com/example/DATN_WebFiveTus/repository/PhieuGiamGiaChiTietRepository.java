package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.PhieuGiamGia;
import com.example.DATN_WebFiveTus.entity.PhieuGiamGiaChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhieuGiamGiaChiTietRepository extends JpaRepository<PhieuGiamGiaChiTiet,Integer> {
    @Query("SELECT pggct FROM PhieuGiamGiaChiTiet pggct JOIN FETCH  pggct.khachHang " +
            "JOIN FETCH  pggct.phieuGiamGia where pggct.deletedAt=false")
    Page<PhieuGiamGia> getAllJoinFetch(Pageable pageable);

    @Query("SELECT pggct FROM PhieuGiamGiaChiTiet pggct " +
            "JOIN FETCH pggct.khachHang " +
            "JOIN FETCH pggct.phieuGiamGia " +
            "WHERE pggct.phieuGiamGia.id = :idPhieuGiamGia " +
            "AND pggct.deletedAt = false")
    List<PhieuGiamGiaChiTiet> findAllByIdPhieuGiamGia(@Param("idPhieuGiamGia") Integer idPhieuGiamGia);

    @Query("SELECT pggct FROM PhieuGiamGiaChiTiet pggct " +
            "JOIN FETCH pggct.khachHang " +
            "JOIN FETCH pggct.phieuGiamGia " +
            "WHERE pggct.khachHang.id = :idKhachHang " )
    List<PhieuGiamGiaChiTiet> findAllByIdKhachHang(@Param("idKhachHang") Integer idKhachHang);

    @Query("SELECT pggct FROM PhieuGiamGiaChiTiet pggct WHERE pggct.id = :id AND pggct.khachHang.id = :idKhachHang")
    Optional<PhieuGiamGiaChiTiet> findByIdAndKhachHangId(@Param("id") Integer id, @Param("idKhachHang") Integer idKhachHang);

}
