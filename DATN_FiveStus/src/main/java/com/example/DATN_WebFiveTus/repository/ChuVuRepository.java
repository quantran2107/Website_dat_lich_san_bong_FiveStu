package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.ChuVu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ChuVuRepository extends JpaRepository<ChuVu,Integer> {
}
