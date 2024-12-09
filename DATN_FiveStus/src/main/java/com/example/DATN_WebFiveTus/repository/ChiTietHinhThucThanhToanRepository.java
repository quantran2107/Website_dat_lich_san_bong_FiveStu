package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.dto.response.BanGiaoCaResponse;
import com.example.DATN_WebFiveTus.entity.ChiTietHinhThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChiTietHinhThucThanhToanRepository extends JpaRepository<ChiTietHinhThucThanhToan,Integer> {
    @Query("SELECT c FROM ChiTietHinhThucThanhToan c WHERE c.hoaDonChiTiet.id = :idHoaDonChiTiet and c.deletedAt = false")
    List<ChiTietHinhThucThanhToan> findByIdHdct(@Param("idHoaDonChiTiet") int id);
    List<ChiTietHinhThucThanhToan> findByHoaDonChiTiet_Id(int hoaDonChiTietId);

    @Query(value = "SELECT * FROM duantotngiep.chi_tiet_hinh_thuc_thanh_toan WHERE id_nhan_vien = :id AND created_at = :createdAt", nativeQuery = true)
    List<ChiTietHinhThucThanhToan> findListByIdNhanVien(@Param("id") int id, @Param("createdAt")LocalDateTime createdAt);

    @Query(value = """
        SELECT
            ht.hinh_thuc_thanh_toan AS hinh_thuc_thanh_toan,
            SUM(ctht.so_tien) AS tong_so_tien
        FROM
            duantotnghiep.chi_tiet_hinh_thuc_thanh_toan ctht
        JOIN
            duantotnghiep.hoa_don_chi_tiet cthd ON ctht.id_hoa_don_chi_tiet = cthd.id
        JOIN
           duantotnghiep.hinh_thuc_thanh_toan ht ON ctht.id_hinh_thuc_thanh_toan = ht.id
        JOIN
            duantotnghiep.nhan_vien nv ON cthd.id_nhan_vien = nv.id
        WHERE
            nv.id = :id
             AND DATE(ctht.created_at) = CURDATE()
        GROUP BY
            ht.hinh_thuc_thanh_toan;
""", nativeQuery = true)
    List<BanGiaoCaResponse> getHinhThucThanhToanTongTien(@Param("id") Integer id);
}
