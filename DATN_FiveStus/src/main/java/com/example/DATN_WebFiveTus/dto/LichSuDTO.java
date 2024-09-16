package com.example.DATN_WebFiveTus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class LichSuDTO {

    private Integer id;

    private Integer idHoaDonChiTiet;

    private String maHoaDonChiTiet;

    private LocalDateTime thoiGianCheckin;


    private String moTa;
}
