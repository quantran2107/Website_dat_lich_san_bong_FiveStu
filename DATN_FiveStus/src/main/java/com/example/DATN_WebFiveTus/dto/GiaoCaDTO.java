package com.example.DATN_WebFiveTus.dto;

import com.example.DATN_WebFiveTus.entity.NhanVien;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class GiaoCaDTO {

    private Integer id;

    private NhanVien nhanVienGiao;

    private NhanVien nhanVienNhan;

    private Float tienPhatSinh;

    private LocalDateTime thoiGianKetCa;

    private Float tienBanDau;

    private Float tongTienKhac;

    private Float tongTienMat;

    private Float tongTienMatCaTruoc;

    private Float tongTienMatRut;

    private Float tongTienTrongCa;

    private String ghiChuPhatSinh;

    private String trangThai;
}
