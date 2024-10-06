package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.DoThue;
import com.example.DATN_WebFiveTus.entity.NuocUong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NuocUongRepository extends JpaRepository<NuocUong,Integer> {
    @Query("SELECT nc FROM NuocUong nc  " +
            "where nc.deletedAt=false and nc.soLuongs>0")
    Page<NuocUong> getAllJoinFetch(Pageable pageable);

    @Query("SELECT nc FROM NuocUong nc  " +
            "where nc.deletedAt=false and nc.soLuongs>0")
    List<NuocUong> getAllJoinFetch2();

    @Query("SELECT nu FROM NuocUong nu " +
            "WHERE (:keyword IS NULL OR nu.tenNuocUong LIKE %:keyword%) " +
            "AND (:trangThai IS NULL OR nu.trangThai = :trangThai) " +
            "AND (:donGiaMin IS NULL OR :donGiaMax IS NULL OR nu.donGias BETWEEN :donGiaMin AND :donGiaMax) " +
            "AND nu.deletedAt=false")
    Page<NuocUong> searchNuocUong(
            @Param("keyword") String keyword,
            @Param("trangThai") String trangThai,
            @Param("donGiaMin") Float donGiaMin,
            @Param("donGiaMax") Float donGiaMax,
            Pageable pageable);

    @Query("SELECT nc FROM NuocUong nc  " +
            "where nc.deletedAt=false")
    List<NuocUong> getAll();

    @Query("SELECT nc FROM NuocUong nc  " +
            "WHERE nc.id = :id AND nc.id IN  (SELECT  dvsb.nuocUong.id FROM DichVuSanBong dvsb" +
            " where nc.deletedAt=false and dvsb.hoaDonChiTiet.id=:idHoaDonChiTiet and dvsb.nuocUong.id=:id)")
    Optional<NuocUong> checkIdDichVuNuocUong(Integer id, Integer idHoaDonChiTiet);
}
