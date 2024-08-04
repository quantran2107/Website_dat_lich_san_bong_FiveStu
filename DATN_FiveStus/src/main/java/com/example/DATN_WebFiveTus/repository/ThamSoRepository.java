package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.SanCa;
import com.example.DATN_WebFiveTus.entity.ThamSo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ThamSoRepository extends JpaRepository<ThamSo,Integer> {

    @Query("SELECT ts FROM ThamSo ts where ts.ma=:ma")
    ThamSo findByTenThamSo(String ma);

    @Query("SELECT ts FROM ThamSo ts")
    Page<ThamSo> findByPageThamSo(Pageable pageable);
}
