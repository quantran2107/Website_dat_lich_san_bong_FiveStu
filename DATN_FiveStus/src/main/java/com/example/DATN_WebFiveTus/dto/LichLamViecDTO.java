package com.example.DATN_WebFiveTus.dto;

import com.example.DATN_WebFiveTus.entity.NhanVien;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString

public class LichLamViecDTO {

    private int id;

    private NhanVien nhanVien;

    private String viTri;

    private LocalTime gioBatDau;

    private LocalTime gioKetThuc;

    private LocalDate ngay;

}
