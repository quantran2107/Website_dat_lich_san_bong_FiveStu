package com.example.DATN_WebFiveTus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class HoaDonChiTietDTO {

    private Integer id;

    private Integer idHoaDon;

    private String maHoaDon;

    private Integer idSanCa;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date ngayTaoHoaDon;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date ngayDenSan;

    private String maHoaDonChiTiet;

    private String tienSan;

    private String ghiChu;

    private String trangThai;

    private String kieuNgayDat;

    private String tenSanBong;

    private String tenCa;

    private LocalDateTime thoiGianBatDauCa;

    private LocalDateTime thoiGianKetThucCa;

    @JsonIgnore
    private Integer idKhachHang;

    private String hoVaTenKhachHang;

    private String soDienThoaiKhachHang;

    private String emailKhachHang;


    private Boolean deletedAt;

    private String tenLoaiSan;



}
