package com.example.DATN_WebFiveTus.repository;

import com.example.DATN_WebFiveTus.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet,Integer> {
    @Query("SELECT hdct FROM HoaDonChiTiet hdct " +
            "JOIN FETCH hdct.hoaDon " +
            "JOIN FETCH hdct.sanCa " +
            "JOIN FETCH hdct.sanCa.sanBong " +
            "JOIN FETCH hdct.sanCa.ca " +
            "JOIN FETCH hdct.sanCa.ngayTrongTuan ")
    List<HoaDonChiTiet> getAllJoinFetch();

    @Query("SELECT hdct FROM HoaDonChiTiet hdct " +
            "JOIN FETCH hdct.hoaDon " +
            "JOIN FETCH hdct.sanCa WHERE hdct.hoaDon.id = :idHoaDon")
    List<HoaDonChiTiet> searchFromHoaDon(@Param("idHoaDon") Integer idHoaDon);

    //Từ HDCT nối CTDVSB nối DVSB nối DoThue,NươcUong,phuPhu,phuPhiHD
    // lấy: tenDoThue,tenNuocUong, donGiaDoThue,donGiaNuocUong, soLuong ở DVSB, giaPhuPhiHD,tenPhuPhi
//    from HDCT// bảng chính

//    @Query("SELECT ctdvsb  from ChiTietDichVuSanBong ctdvsb join  ctdvsb.hoaDonChiTiet hdct join ctdvsb.dichVuSanBong dvsb join dvsb.doThue dt join dvsb.nuocUong nu")
//    List<HoaDonChiTiet> getAll();

}
