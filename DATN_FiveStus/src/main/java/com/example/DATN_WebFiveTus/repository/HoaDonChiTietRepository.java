package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.HoaDon;
import com.example.DATN_WebFiveTus.entity.HoaDonChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
            "And hd.deletedAt = false ")
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


    @Modifying
    @Transactional
    @Query("update HoaDonChiTiet hdct set hdct.trangThai='Đang hoạt động'  where hdct.id=:id")
    void  updateTrangThai(Integer id);

    @Modifying
    @Transactional
    @Query("update HoaDonChiTiet hdct set hdct.trangThai='Đã thanh toán' where hdct.id=:id")
    void updateTrangThaiThanhToan(Integer id);


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

    @Query("SELECT hdct FROM HoaDonChiTiet hdct join fetch HoaDon hd ON hdct.hoaDon.id = hd.id WHERE hd.id = :idHoaDon")
    HoaDonChiTiet findByIdHoaDon(@Param("idHoaDon") Integer idHoaDon);



}
