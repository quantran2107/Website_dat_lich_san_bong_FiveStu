package com.example.DATN_WebFiveTus.dto;

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
public class SanCaDTO {

    private Integer id;

    private Double gia;

    private Integer idCa;

//    @JsonIgnore
    private String tenCa;

    private Integer idSanBong;

//    @JsonIgnore
    private String tenSanBong;


    private Integer idNgayTrongTuan;

//    @JsonIgnore
    private String thuTrongTuan;

    private String trangThai;

    private String tenLoaiSan;

    private LocalDateTime thoiGianBatDauCa;

    private LocalDateTime thoiGianKetThucCa;

    private Date ngayDenSanHoaDonChiTiet;
}
