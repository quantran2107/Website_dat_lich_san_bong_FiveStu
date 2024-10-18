package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.GiaoCa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;


public interface GiaoCaRepository extends JpaRepository<GiaoCa, Integer> {

    @Query("select a from GiaoCa a where a.nhanVienNhan.id=:id and DATE(a.createdAt) = DATE(:today) and a.trangThai=true ")
    GiaoCa findByIdNhanVien(@Param("id") int id,@Param("today") LocalDate today);

    @Query(value = """
        SELECT * FROM duantotnghiep.giao_ca
            ORDER BY id DESC
            LIMIT 1;
        """,nativeQuery = true)
    GiaoCa getRowLast();
}
