package com.example.DATN_WebFiveTus.dto;

import com.example.DATN_WebFiveTus.entity.NhanVien;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class GiaoCaRequest {

    private BigDecimal tienMatTrongCa;

    private BigDecimal tienChuyenKhoanTrongCa;

    private BigDecimal tongTienTrongCa;

    private BigDecimal tongTienMatThucTe;

    private BigDecimal tongTienPhatSinh;

    private String ghiChu;

    private Boolean trangThai;
}
