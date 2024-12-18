package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.GiaoCa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;


public interface GiaoCaRepository extends JpaRepository<GiaoCa, Integer> {



    @Query(value = "select * from duantotnghiep.giao_ca  where id_nhan_vien=:id and trang_thai=1 ",nativeQuery = true)
    GiaoCa getRowLast(@Param("id") int id);

    @Query(value = "SELECT * FROM duantotnghiep.giao_ca gc where  DATE(gc.created_at) = CURDATE() ORDER BY gc.id DESC LIMIT 1", nativeQuery = true)
    GiaoCa findDataLast();
}
