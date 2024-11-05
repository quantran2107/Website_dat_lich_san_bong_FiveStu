package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.LoaiSan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LoaiSanRepository extends JpaRepository<LoaiSan,Integer> {

    @Query("SELECT ls FROM LoaiSan ls where ls.deletedAt=false")
    List<LoaiSan> getAllJoinFetch();

    @Modifying
    @Transactional
    @Query("UPDATE LoaiSan ls SET ls.deletedAt = TRUE WHERE ls.id = :id")
    void deletedAt(Integer id);

    Boolean existsByTenLoaiSan(String tenLoaiSan);
}
