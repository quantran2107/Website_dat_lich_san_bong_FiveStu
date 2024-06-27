package com.example.DATN_WebFiveTus.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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

    private int id;

    private String tenCa;

    private Float giaCa;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime thoiGianBatDau;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime  thoiGianKetThuc;

    private String trangThai;
}
