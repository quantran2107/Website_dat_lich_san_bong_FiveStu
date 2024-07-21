package com.example.DATN_WebFiveTus.dto;

import com.example.DATN_WebFiveTus.entity.SanCa;
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
public class HoaDonChiTietDTO {

    private int id;

    private Integer idHoaDon;

    private String maHoaDon;

    private Integer idSanCa;

    private String tienSan;

    private String ghiChu;

    private String trangThai;

    private String tenSanBong;

    private String tenCa;

}
