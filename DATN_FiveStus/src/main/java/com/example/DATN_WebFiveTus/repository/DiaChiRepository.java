package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.DiaChi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi,Integer> {
    @Query("SELECT dckh FROM DiaChiKhachHang dckh ")
    List<DiaChi> getAllJoinFetch();
}
