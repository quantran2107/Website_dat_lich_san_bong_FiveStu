package com.example.DATN_WebFiveTus.dto.response;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;


public interface HistoryCustomerBookFieldResponse {
    @Value("#{target.tenSanBong}")
    String getTenSanBong();

    @Value("#{target.thoiGianBatDau}")
    LocalDateTime getThoiGianBatDau();

    @Value("#{target.ngayDat}")
    LocalDateTime getNgayDat();

    @Value("#{target.trangThaiCheckIn}")
    String getTrangThaiCheckIn();

    @Value("#{target.maHoaDon}")
    String getMaHoaDon();

    @Value("#{target.tienCoc}")
    Double getTienCoc();

    @Value("#{target.trangThaiHoaDon}")
    String getTrangThaiHoaDon();

    @Value("#{target.tongTien}")
    Double getTongTien();

    @Value("#{target.tongGiamGia}")
    Double getTongGiamGia();
}
