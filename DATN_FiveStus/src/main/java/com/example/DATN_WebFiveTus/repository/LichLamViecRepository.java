package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.LichLamViec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface LichLamViecRepository extends JpaRepository<LichLamViec, Integer> {

    @Query(value = """
                SELECT * FROM duantotnghiep.lich_lam_viec 
                WHERE (:date IS NULL OR lich_lam_viec.ngay = :date) 
                 AND (:time IS NULL OR lich_lam_viec.gio_bat_dau = :time)
        """, nativeQuery = true)
    List<LichLamViec> findByOptionalDateSearchAndTime(@Param("date") String date, @Param("time") LocalTime time);

}
