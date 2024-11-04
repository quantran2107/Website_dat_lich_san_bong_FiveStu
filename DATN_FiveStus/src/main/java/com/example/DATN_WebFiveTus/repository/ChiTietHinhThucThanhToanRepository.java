package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.ChiTietHinhThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChiTietHinhThucThanhToanRepository extends JpaRepository<ChiTietHinhThucThanhToan,Integer> {
    @Query("SELECT c FROM ChiTietHinhThucThanhToan c WHERE c.hoaDonChiTiet.id = :idHoaDonChiTiet")
    List<ChiTietHinhThucThanhToan> findByIdHdct(@Param("idHoaDonChiTiet") int id);
    List<ChiTietHinhThucThanhToan> findByHoaDonChiTiet_Id(int hoaDonChiTietId);

    @Query(value = "SELECT * FROM duantotngiep.chi_tiet_hinh_thuc_thanh_toan WHERE id_nhan_vien = :id AND created_at = :createdAt", nativeQuery = true)
    List<ChiTietHinhThucThanhToan> findListByIdNhanVien(@Param("id") int id, @Param("createdAt")LocalDateTime createdAt);

    @Query("SELECT c FROM ChiTietHinhThucThanhToan c WHERE c.createdAt >=:createdAt and c.hinhThucThanhToan.id=1")
    List<ChiTietHinhThucThanhToan> findByCreatedAndChuyenKhoan(@Param("createdAt") LocalDateTime createdAt);

    @Query("SELECT c FROM ChiTietHinhThucThanhToan c WHERE c.createdAt >=:createdAt and c.hinhThucThanhToan.id=2")
    List<ChiTietHinhThucThanhToan> findByCreatedAndTienMat(@Param("createdAt") LocalDateTime createdAt);

}
