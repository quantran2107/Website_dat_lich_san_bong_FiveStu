package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.ChiTietHinhThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietHinhThucThanhToanRepository extends JpaRepository<ChiTietHinhThucThanhToan,Integer> {
    @Query("SELECT c FROM ChiTietHinhThucThanhToan c WHERE c.hoaDonChiTiet.id = :idHoaDonChiTiet")
    List<ChiTietHinhThucThanhToan> findByIdHdct(@Param("idHoaDonChiTiet") int id);
    List<ChiTietHinhThucThanhToan> findByHoaDonChiTiet_Id(int hoaDonChiTietId);
}
