package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.dto.DiaChiKhachHangDTO;
import com.example.DATN_WebFiveTus.entity.DiaChiKhachHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaChiKhachHangRepository extends JpaRepository<DiaChiKhachHang,Integer> {
    @Query("SELECT dckh FROM DiaChiKhachHang dckh JOIN FETCH dckh.idKhachHang")
    List<DiaChiKhachHang> getAllJoinFetch();

    List<DiaChiKhachHang> findByIdKhachHang_Id(Integer id);
    @Query("SELECT dckh FROM DiaChiKhachHang dckh WHERE dckh.idKhachHang.id = :idKhachHang AND dckh.deletedAt = false")
    Page<DiaChiKhachHang> findByIdKhachHang_Id(Integer idKhachHang, Pageable pageable);

    @Query("SELECT dckh FROM DiaChiKhachHang dckh WHERE dckh.idKhachHang.email = :email AND dckh.deletedAt = false")
    List<DiaChiKhachHang> findActiveAddressesByCustomerId(@Param("email") String email);

    // Repository method để tìm địa chỉ cụ thể theo `email` của khách hàng và `id` của địa chỉ
    @Query("SELECT dckh FROM DiaChiKhachHang dckh WHERE dckh.idKhachHang.email = :email AND dckh.id = :id")
    Optional<DiaChiKhachHang> findDiaChiByEmailAndId(@Param("email") String email, @Param("id") Integer id);
}
