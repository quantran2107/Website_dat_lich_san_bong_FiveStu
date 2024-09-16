package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.SanBong;
import com.example.DATN_WebFiveTus.entity.SanCa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SanBongRepository extends JpaRepository<SanBong,Integer> {

    @Query("SELECT sb FROM SanBong sb where sb.deletedAt=false ")
    List<SanBong> getAllJoinFetch();

    @Query("SELECT sb FROM SanBong sb where sb.deletedAt=false")
    Page<SanBong> getAllJoinFetchPageable(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE SanBong sb SET sb.deletedAt = TRUE WHERE sb.id = :id")
    void deletedAt(Integer id);

    @Query("SELECT sb FROM SanBong sb WHERE sb.loaiSan.id = :loaiSanId")
    List<SanBong> findByLoaiSanId(@Param("loaiSanId") Integer loaiSanId);



    @Query("SELECT sb FROM SanBong sb")
    Page<SanBong> findBySanBongPage(Pageable  pageable);

    @Query("SELECT sb FROM SanBong sb WHERE sb.id = :id OR sb.tenSanBong=:keyWords OR sb.loaiSan.tenLoaiSan=:keyWords OR sb.trangThai=:keyWords")
    Page<SanBong> search(@Param("id") Integer id, @Param("keyWords") String keyWords, Pageable pageable);
}
