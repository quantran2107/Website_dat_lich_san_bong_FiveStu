package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.LichSu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LichSuRepository extends JpaRepository<LichSu,Integer> {
}
