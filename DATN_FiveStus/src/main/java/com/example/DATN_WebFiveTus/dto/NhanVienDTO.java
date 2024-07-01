package com.example.DATN_WebFiveTus.dto;

import lombok.*;

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

    private Boolean gioiTinh;

    private String soDienThoai;

    private String diaChi;

    private String trangThai;
}
