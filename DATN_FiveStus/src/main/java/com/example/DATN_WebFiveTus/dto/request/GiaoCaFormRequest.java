package com.example.DATN_WebFiveTus.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GiaoCaFormRequest {
    private int id;

    private BigDecimal tienMatTrongCa;

    private BigDecimal tienChuyenKhoan;

    private BigDecimal tongTienTrongCa;

    private BigDecimal tongTienMatThucTe;

    private BigDecimal tienPhatSinh;

    private String ghiChu;
}
