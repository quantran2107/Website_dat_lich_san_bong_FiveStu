package com.example.DATN_WebFiveTus.dto;

import com.example.DATN_WebFiveTus.entity.ChiTietHinhThucThanhToan;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class HTTTDto {
    private int idHD;
    private int idHttt;
    private BigDecimal soTien;

    public HTTTDto(ChiTietHinhThucThanhToan ctHttt) {
        this.idHttt = ctHttt.getHinhThucThanhToan().getId();
        this.idHD = ctHttt.getHoaDonChiTiet().getId();
        this.soTien = ctHttt.getSoTien();
    }
}
