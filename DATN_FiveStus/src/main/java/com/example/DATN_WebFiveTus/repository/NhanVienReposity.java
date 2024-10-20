package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface NhanVienReposity extends JpaRepository<NhanVien,Integer> {

    @Query("select a from NhanVien  a where  a.trangThai='active'")
    Collection<NhanVien> findAllActive();
    @Query("select a from NhanVien  a where  a.trangThai='inactive'")
    Collection<NhanVien> findAllInActive();

    @Query("select a from NhanVien a where a.maNhanVien = :maNhanVien")
    NhanVien findByMaNhanVien(@Param("maNhanVien") String maNhanVien);

    @Query("select a from NhanVien a where a.email=:username or a.tenNhanVien=:username")
    NhanVien findByUsername(@Param("username") String username);
}
