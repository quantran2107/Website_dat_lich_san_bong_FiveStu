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
public class ChiTietDichVuSanBongDTO {


    private int id;

    private Integer idDichVuSanBong;

    private Integer idHoaDonChiTiet;


    private String trangThai;

//    @Temporal(TemporalType.TIMESTAMP)
//    private LocalDateTime createdAt;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    private LocalDateTime updatedAt;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    private boolean deletedAt;
}
