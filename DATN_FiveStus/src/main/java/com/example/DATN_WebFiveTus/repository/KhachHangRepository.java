package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
import com.example.DATN_WebFiveTus.entity.KhachHang;

import com.example.DATN_WebFiveTus.entity.PhieuGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang,Integer> {
    @Query("SELECT kh FROM KhachHang kh WHERE (LOWER(kh.hoVaTen) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(kh.soDienThoai) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(kh.email) LIKE LOWER(CONCAT('%', :query, '%'))) ORDER BY kh.createdAt DESC")
    Page<KhachHang> searchByNamePhoneOrEmail(@Param("query") String query, Pageable pageable);

    @Query("SELECT kh FROM KhachHang kh WHERE (LOWER(kh.hoVaTen) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(kh.soDienThoai) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(kh.email) LIKE LOWER(CONCAT('%', :query, '%'))) AND kh.gioiTinh = :gender ORDER BY kh.createdAt DESC")
    Page<KhachHang> searchByNamePhoneOrEmailAndGender(@Param("query") String query, @Param("gender") boolean gender, Pageable pageable);

    @Query("SELECT kh FROM KhachHang kh WHERE (LOWER(kh.hoVaTen) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(kh.soDienThoai) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(kh.email) LIKE LOWER(CONCAT('%', :query, '%'))) AND kh.trangThai = :status ORDER BY kh.createdAt DESC")
    Page<KhachHang> searchByNamePhoneOrEmailAndStatus(@Param("query") String query, @Param("status") String status, Pageable pageable);

    @Query("SELECT kh FROM KhachHang kh WHERE (LOWER(kh.hoVaTen) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(kh.soDienThoai) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(kh.email) LIKE LOWER(CONCAT('%', :query, '%'))) AND kh.trangThai = :status AND kh.gioiTinh = :gender ORDER BY kh.createdAt DESC")
    Page<KhachHang> searchByNamePhoneOrEmailAndStatusAndGender(@Param("query") String query, @Param("status") String status, @Param("gender") boolean gender, Pageable pageable);

    @Query("SELECT kh FROM KhachHang kh WHERE kh.trangThai = :status ORDER BY kh.createdAt DESC")
    Page<KhachHang> filterByStatus(@Param("status") String status, Pageable pageable);

    @Query("SELECT kh FROM KhachHang kh WHERE kh.gioiTinh = :gender ORDER BY kh.createdAt DESC")
    Page<KhachHang> filterByGender(@Param("gender") boolean gender, Pageable pageable);

    @Query("SELECT kh FROM KhachHang kh WHERE kh.trangThai = :status AND kh.gioiTinh = :gender ORDER BY kh.createdAt DESC")
    Page<KhachHang> findByStatusAndGender(@Param("status") String status, @Param("gender") boolean gender, Pageable pageable);

    @Query("SELECT kh FROM KhachHang kh WHERE kh.soDienThoai = :soDienThoai ")
    KhachHang findKhachHangBySoDienThoai(@Param("soDienThoai") String soDienThoai);

    @Query("SELECT kh FROM KhachHang kh WHERE (kh.hoVaTen LIKE %:query% OR kh.soDienThoai " +
            "LIKE %:query% OR kh.email LIKE %:query%) AND kh.trangThai = :trangThai")
    List<KhachHang> searchByNamePhoneOrEmailActive(@Param("query") String query,
                                                   @Param("trangThai") String trangThai,
                                                   Pageable pageable);
    @Query("SELECT kh FROM KhachHang kh WHERE kh.email =:email")
    KhachHang findKhachHangByEmail(@Param("email") String email);

    Optional<KhachHang> findByEmail(String email);

    @Query("SELECT kh FROM KhachHang kh WHERE kh.email = :email")
    Optional<KhachHang> findKhachHangByEmail1(@Param("email") String email);

    @Query("SELECT COUNT(kh) > 0 FROM KhachHang kh WHERE kh.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT COUNT(kh) > 0 FROM KhachHang kh WHERE kh.soDienThoai = :soDienThoai")
    boolean existsBySoDienThoai(@Param("soDienThoai") String soDienThoai);

    @Query("SELECT COUNT(kh) > 0 FROM KhachHang kh WHERE kh.maKhachHang = :maKhachHang")
    boolean existsByMaKhachHang(@Param("maKhachHang") String maKhachHang);
}
