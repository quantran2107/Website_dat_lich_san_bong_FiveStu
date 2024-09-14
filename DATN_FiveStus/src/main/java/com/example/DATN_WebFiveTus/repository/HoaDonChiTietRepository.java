package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.HoaDonChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Integer> {
    @Query("SELECT hdct FROM HoaDonChiTiet hdct " +
            "JOIN FETCH hdct.hoaDon " +
            "JOIN FETCH hdct.sanCa " +
            "JOIN FETCH hdct.sanCa.sanBong " +
            "JOIN FETCH hdct.sanCa.ca " +
            "JOIN FETCH hdct.sanCa.ngayTrongTuan ")
    List<HoaDonChiTiet> getAllJoinFetch();

    @Query("SELECT hdct FROM HoaDonChiTiet hdct " +
            "JOIN FETCH hdct.hoaDon " +
            "JOIN FETCH hdct.sanCa WHERE hdct.hoaDon.id = :idHoaDon")
    List<HoaDonChiTiet> searchFromHoaDon(@Param("idHoaDon") Integer idHoaDon);

    @Query("SELECT hdct FROM HoaDonChiTiet hdct " +
            "JOIN FETCH hdct.hoaDon hd " +
            "JOIN FETCH hd.khachHang kh " +
            "JOIN FETCH hdct.sanCa sc " +
            "JOIN FETCH sc.sanBong sb " +
            "JOIN FETCH sc.ca c " +
            "JOIN FETCH sc.ngayTrongTuan nt " +
            "WHERE hdct.trangThai = :trangThai " +
            "AND hdct.ngayDenSan = CURRENT_DATE " +
            "AND hdct.deletedAt = false " +
            "AND hd.trangThai = 'Chờ thanh toán' And hd.deletedAt = false ")
    Page<HoaDonChiTiet> findByTrangThai(@Param("trangThai") String trangThai, Pageable pageable);

    @Query("SELECT hdct FROM HoaDonChiTiet hdct " +
            "JOIN FETCH hdct.hoaDon hd " +
            "JOIN FETCH hd.khachHang kh " +
            "JOIN FETCH hdct.sanCa sc " +
            "JOIN FETCH sc.sanBong sb " +
            "JOIN FETCH sc.ca c " +
            "JOIN FETCH sc.ngayTrongTuan nt " +
            "WHERE hdct.id = :id")
    HoaDonChiTiet findHoaDonChiTietById(@Param("id") Integer id);


    @Query("SELECT hdct FROM HoaDonChiTiet hdct " +
            "JOIN FETCH hdct.hoaDon hd " +
            "JOIN FETCH hd.khachHang kh " +
            "JOIN FETCH hdct.sanCa sc " +
            "JOIN FETCH sc.sanBong sb " +
            "JOIN FETCH sc.ca c " +
            "JOIN FETCH sc.ngayTrongTuan nt " +
            "WHERE " +
            "hdct.ngayDenSan = :ngayDenSan " +
            "AND hdct.deletedAt = false " +
            "And hd.deletedAt = false ")
    List<HoaDonChiTiet> findByNgayDenSan(@Param("ngayDenSan") Date ngayDenSan);

    @Query("SELECT COUNT(hdct) FROM HoaDonChiTiet hdct " +
            "JOIN hdct.sanCa sc " +
            "WHERE sc.id = :idSanCa " +
            "AND hdct.ngayDenSan = :ngayDenSan")
    Long countByIdSanCaAndNgayDenSan(@Param("idSanCa") Long idSanCa,
                                     @Param("ngayDenSan") LocalDate ngayDenSan);

}
