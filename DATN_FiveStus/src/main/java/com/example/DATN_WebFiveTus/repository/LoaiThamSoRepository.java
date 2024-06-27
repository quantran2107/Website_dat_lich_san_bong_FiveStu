package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.LoaiThamSo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoaiThamSoRepository extends JpaRepository<LoaiThamSo,Integer> {
}
