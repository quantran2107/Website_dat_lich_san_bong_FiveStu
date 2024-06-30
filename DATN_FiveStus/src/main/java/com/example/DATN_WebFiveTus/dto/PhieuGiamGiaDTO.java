package com.example.DATN_WebFiveTus.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PhieuGiamGiaDTO {

    private int id;

    private Integer idKhachHang;

    private String hoVaTenKhachHang;

    private String maPhieuGiamGia;


    private String tenPhieuGiamGia;


    private String mucGiam;


    private Boolean hinhThucGiamGia;


    private String dieuKienSuDung;


    private Date ngayBatDau;


    private Date ngayKetThuc;


    private Boolean trangThai;

    private Boolean deletedAt;
}
