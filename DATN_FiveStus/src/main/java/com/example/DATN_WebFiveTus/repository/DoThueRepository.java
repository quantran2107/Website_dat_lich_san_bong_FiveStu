package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.DoThue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoThueRepository extends JpaRepository<DoThue,Integer> {
    @Query("SELECT dt FROM DoThue dt  " +
            "where dt.deletedAt=false and dt.soLuongs>0")
    Page<DoThue> getAllJoinFetch(Pageable pageable);

    @Query("SELECT dt FROM DoThue dt  " +
            "where dt.deletedAt=false and dt.soLuongs>0")
    List<DoThue> getAllJoinFetch2();

    @Query("SELECT dt FROM DoThue dt " +
            "WHERE (:keyword IS NULL OR dt.tenDoThue LIKE %:keyword%) " +
            "AND (:trangThai IS NULL OR dt.trangThai = :trangThai) " +
            "AND (:donGiaMin IS NULL OR :donGiaMax IS NULL OR dt.donGias BETWEEN :donGiaMin AND :donGiaMax) " +
            "AND dt.deletedAt=false")
    Page<DoThue> searchDoThue(
            @Param("keyword") String keyword,
            @Param("trangThai") String trangThai,
            @Param("donGiaMin") Float donGiaMin,
            @Param("donGiaMax") Float donGiaMax,
            Pageable pageable);



    @Query("SELECT dt FROM DoThue dt  " +
            "where dt.deletedAt=false")
    List<DoThue> getAll();

    @Query("SELECT dt FROM DoThue dt  " +
            "WHERE dt.id = :id AND dt.id IN  (SELECT  dvsb.doThue.id FROM DichVuSanBong dvsb" +
            " where dt.deletedAt=false and dvsb.hoaDonChiTiet.id=:idHoaDonChiTiet and dvsb.doThue.id=:id)")
    Optional<DoThue> checkIdDichVuDoThue(Integer id,Integer idHoaDonChiTiet);


}
