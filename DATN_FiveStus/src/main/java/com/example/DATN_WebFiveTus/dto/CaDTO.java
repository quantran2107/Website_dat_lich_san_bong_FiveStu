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
public class CaDTO {

    private Integer id;

    private String tenCa;

//    private Float giaCa;

    private LocalDateTime thoiGianBatDau;

    private LocalDateTime thoiGianKetThuc;

    private String trangThai;
}
