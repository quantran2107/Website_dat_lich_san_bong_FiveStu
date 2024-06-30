package com.example.DATN_WebFiveTus.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DiaChiDTO {

    private Integer id;

    private String tenDiaChi;

    private String ghiChu;

    private Integer idKhachHang;


}
