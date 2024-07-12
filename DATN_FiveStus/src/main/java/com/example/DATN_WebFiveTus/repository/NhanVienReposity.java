package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface NhanVienReposity extends JpaRepository<NhanVien,Integer> {

    @Query("select a from NhanVien  a where  a.trangThai='active'")
    Collection<NhanVien> findAllActive();
    @Query("select a from NhanVien  a where  a.trangThai='inactive'")
    Collection<NhanVien> findAllInActive();
}
