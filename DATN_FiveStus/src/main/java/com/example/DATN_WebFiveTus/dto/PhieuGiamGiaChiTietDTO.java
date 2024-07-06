package com.example.DATN_WebFiveTus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PhieuGiamGiaChiTietDTO {

    private int id;

    private Integer idKhachHang;

    private Integer idPhieuGiamGia;

    private String hoVaTenKhachHang;

    private Boolean trangThai;

    private String emailKhachHang;

    private String soDienThoaiKhachHang;

}
