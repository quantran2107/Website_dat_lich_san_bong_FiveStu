package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.DichVuSanBong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DichVuSanBongRepository extends JpaRepository<DichVuSanBong,Integer> {


    @Query("SELECT dvsb FROM DichVuSanBong dvsb where dvsb.trangThai like '%Đã đặt%' and dvsb.deletedAt=false")
    List<DichVuSanBong> getAllJoinFetch();

    @Query("SELECT DISTINCT dvsb FROM DichVuSanBong dvsb" +
            " where dvsb.hoaDonChiTiet.id=:idHoaDonChiTiet and (dvsb.soLuong>0 ) " +
            "and dvsb.trangThai like '%Đã đặt%' and dvsb.deletedAt=false")
    List<DichVuSanBong> searchDichVuSanBong(Integer idHoaDonChiTiet);

    //Ly viet
    @Query("SELECT dvsb FROM DichVuSanBong dvsb " +
            "JOIN FETCH dvsb.hoaDonChiTiet hdct " +
            "WHERE hdct.id = :idHDCT  AND dvsb.deletedAt = false")
    List<DichVuSanBong> findDichVuSanBongsByIdHoaDonChiTiet(@Param("idHDCT") Integer idHDCT);

    @Query("SELECT dvsb FROM DichVuSanBong dvsb " +
            "JOIN FETCH dvsb.hoaDonChiTiet hdct " +
            "JOIN FETCH dvsb.doThue dt " +
            "WHERE hdct.id = :idHDCT " +
            "AND dt.id = :idDoThue " +
            "AND dvsb.deletedAt = false")
    DichVuSanBong findDVSBbyIdHDCTandIdDoThue(@Param("idHDCT") Integer idHDCT,
                                                    @Param("idDoThue") Integer idDoThue);

    @Query("SELECT dvsb FROM DichVuSanBong dvsb " +
            "JOIN FETCH dvsb.hoaDonChiTiet hdct " +
            "JOIN FETCH dvsb.nuocUong nu " +
            "WHERE hdct.id = :idHDCT " +
            "AND nu.id = :idNuocUong " +
            "AND dvsb.deletedAt = false")
    DichVuSanBong findDVSBbyIdHDCTandIdNuocUong(@Param("idHDCT") Integer idHDCT,
                                                      @Param("idNuocUong") Integer idNuocUong);

}
