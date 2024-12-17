package com.example.DATN_WebFiveTus.dto.response;


import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

public interface BanGiaoCaResponse {

    @Value("#{target.hinh_thuc_thanh_toan}")
    String getHinhThucThanhToan();

    @Value("#{target.tong_so_tien}")
    BigDecimal getTongTien();


}
