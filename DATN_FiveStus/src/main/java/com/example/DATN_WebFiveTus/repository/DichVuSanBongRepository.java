package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.DichVuSanBong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DichVuSanBongRepository extends JpaRepository<DichVuSanBong,Integer> {
}
