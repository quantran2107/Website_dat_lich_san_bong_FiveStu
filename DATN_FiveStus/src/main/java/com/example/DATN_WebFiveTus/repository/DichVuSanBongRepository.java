package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.DichVuSanBong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DichVuSanBongRepository extends JpaRepository<DichVuSanBong,Integer> {

    @Query("SELECT dvsb FROM DichVuSanBong dvsb where dvsb.trangThai like '%Đang hoạt động%' and dvsb.deletedAt=false")
    List<DichVuSanBong> getAllJoinFetch();

    @Query("SELECT DISTINCT dvsb FROM DichVuSanBong dvsb" +
            " where dvsb.hoaDonChiTiet.id=:idHoaDonChiTiet and (dvsb.soLuongDoThue>0 or dvsb.soLuongNuocUong >0) " +
            "and dvsb.trangThai like '%Đã đặt%' and dvsb.deletedAt=false")
    List<DichVuSanBong> searchDichVuSanBong(Integer idHoaDonChiTiet);

}
