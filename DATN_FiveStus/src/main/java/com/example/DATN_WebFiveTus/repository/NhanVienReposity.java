package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhanVienReposity extends JpaRepository<NhanVien,Integer> {
}
