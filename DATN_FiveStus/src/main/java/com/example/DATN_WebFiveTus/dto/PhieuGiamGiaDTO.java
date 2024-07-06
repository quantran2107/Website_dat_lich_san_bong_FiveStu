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

    private String maPhieuGiamGia;

    private String tenPhieuGiamGia;

    private Integer soLuong;

    private Float mucGiam;

    private Boolean hinhThucGiamGia;

    private Float dieuKienSuDung;

    private Float giaTriToiDa;

    private Boolean doiTuongApDung;

    private Date ngayBatDau;

    private Date ngayKetThuc;

    private String trangThai;

    private String ghiChu;

    private Boolean deletedAt;
}
