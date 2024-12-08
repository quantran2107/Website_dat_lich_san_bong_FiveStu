package com.example.DATN_WebFiveTus.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@NoArgsConstructor
@AllArgsConstructor
@Data

public class BanGiaoCaResponse {

    private String hinhThuc;
    private BigDecimal tongTien;

}
