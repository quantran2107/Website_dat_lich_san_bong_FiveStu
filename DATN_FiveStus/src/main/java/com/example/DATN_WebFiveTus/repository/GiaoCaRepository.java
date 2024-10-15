package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.GiaoCa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;


public interface GiaoCaRepository extends JpaRepository<GiaoCa, Integer> {

    @Query("select a from GiaoCa a where a.nhanVienNhan.id=:id and DATE(a.createdAt) = DATE(:today) and a.trangThai=:status ")
    GiaoCa findByIdNhanVien(@Param("id") int id,@Param("today") LocalDate today,@Param("status") String status);
}
