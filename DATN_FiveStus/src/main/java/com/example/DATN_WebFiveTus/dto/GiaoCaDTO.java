package com.example.DATN_WebFiveTus.dto;

import com.example.DATN_WebFiveTus.entity.NhanVien;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class GiaoCaDTO {

    private Integer id;

    private NhanVien nhanVien;

    private BigDecimal tienMatCaTruoc;

    private BigDecimal tienMatTrongCa;

    private BigDecimal tienChuyenKhoanTrongCa;

    private BigDecimal tongTienTrongCa;

    private BigDecimal tongTienMatThucTe;

    private BigDecimal tongTienPhatSinh;

    private String ghiChu;

    private Boolean trangThai;
}
