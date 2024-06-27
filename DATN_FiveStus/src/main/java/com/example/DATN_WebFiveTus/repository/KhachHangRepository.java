package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang,Integer> {
}
