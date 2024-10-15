package com.example.DATN_WebFiveTus.dto;

import com.example.DATN_WebFiveTus.entity.HoaDon;
import com.example.DATN_WebFiveTus.entity.KhachHang;
import com.example.DATN_WebFiveTus.entity.NhanVien;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class HoaDonRequest {

    private String nguoiTao;

    private NhanVien nhanVien;

    private KhachHang khachHang;

    private String maHoaDon;

    private String trangThai;

    private Double tienCoc;

    private Double tongTien;

    private LocalDateTime createdAt;

    public HoaDonRequest(HoaDon hoaDon){
        this.nhanVien = hoaDon.getNhanVien();
        this.khachHang =hoaDon.getKhachHang();
        this.maHoaDon =hoaDon.getMaHoaDon();
        this.tienCoc =hoaDon.getTienCoc();
        this.tongTien =hoaDon.getTongTien();
        this.createdAt =hoaDon.getCreatedAt();
        this.trangThai =hoaDon.getTrangThai();
    }

}
