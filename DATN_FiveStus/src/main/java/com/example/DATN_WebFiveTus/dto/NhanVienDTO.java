package com.example.DATN_WebFiveTus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class NhanVienDTO {

    private int id;

    private String tenNhanVien;

    private String maNhanVien;

    private String matKhau;

    private String hoTen;

    private String email;

    private LocalDate ngaySinh;

    private LocalDateTime createdAt;

    private Boolean gioiTinh;

    private String soDienThoai;

    private String diaChi;

    private String trangThai;


}
