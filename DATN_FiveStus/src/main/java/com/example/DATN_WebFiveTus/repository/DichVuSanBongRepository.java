package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.DichVuSanBong;
import com.example.DATN_WebFiveTus.entity.LoaiSan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DichVuSanBongRepository extends JpaRepository<DichVuSanBong,Integer> {

    //Ly viet
    @Query("SELECT dvsb FROM DichVuSanBong dvsb " +
            "JOIN FETCH dvsb.hoaDonChiTiet hdct " +
            "WHERE hdct.id = :idHDCT  AND dvsb.deletedAt = false")
    List<DichVuSanBong> findDichVuSanBongsByIdHoaDonChiTiet(@Param("idHDCT") Integer idHDCT);

}
