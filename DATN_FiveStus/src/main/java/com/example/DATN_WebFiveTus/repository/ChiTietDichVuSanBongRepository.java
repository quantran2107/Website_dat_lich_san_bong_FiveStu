package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.ChiTietDichVuSanBong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChiTietDichVuSanBongRepository extends JpaRepository<ChiTietDichVuSanBong,Integer> {
}
