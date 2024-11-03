package com.example.DATN_WebFiveTus.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@NoArgsConstructor
@AllArgsConstructor
@Data

public class BanGiaoCaResponse {

    private int id;

    private BigDecimal tienMatDauCa;

    private BigDecimal tienMatSB;

    private BigDecimal chuyenKhoanSB;

    private BigDecimal tienMatDatCoc;

    private BigDecimal chuyenKhoanDatCoc;

}
