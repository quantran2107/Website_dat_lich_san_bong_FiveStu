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
public class KhachHangDTO {

    private Integer id;


    private String maKhachHang;


    private String matKhau;


    private String hoVaTen;


    private String email;


    private boolean gioiTinh;


    private String soDienThoai;


    private String trangThai;

}
