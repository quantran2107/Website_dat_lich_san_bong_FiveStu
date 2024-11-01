package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.ThamSo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThamSoRepository extends JpaRepository<ThamSo, Integer> {

    @Query("SELECT ts FROM ThamSo ts WHERE ts.ma LIKE %:ma%")
   ThamSo findByMaThamSo(@Param("ma") String maThamSo);


    @Query("SELECT ts FROM ThamSo ts WHERE " +
            "(:ma IS NULL OR ts.ma LIKE %:ma%) AND " +
            "(:ten IS NULL OR ts.ten LIKE %:ten%) AND " +
            "(:typeGiaTri IS NULL OR ts.typeGiaTri LIKE %:typeGiaTri%) AND " +
            "(:trangThai IS NULL OR ts.trangThai = :trangThai) AND ts.deletedAt = false")
    Page<ThamSo> searchThamSoss(@Param("ma") String ma,
                                @Param("ten") String ten,
                                @Param("typeGiaTri") String typeGiaTri,
                                @Param("trangThai") Boolean trangThai,
                                Pageable pageable);


    @Query("SELECT ts FROM ThamSo ts where ts.ma=:ma")
    ThamSo findByTenThamSo(String ma);

    @Query("SELECT ts FROM ThamSo ts")
    Page<ThamSo> findByPageThamSo(Pageable pageable);

    @Query("SELECT ts FROM ThamSo ts where ts.ten like '%:keyword%' or ts.ma like '%:keyword%' ")
    Page<ThamSo> searchThamSo(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT ts FROM ThamSo ts where ts.isActive=true and ts.deletedAt=false ")
    List<ThamSo> getAll2();



}
