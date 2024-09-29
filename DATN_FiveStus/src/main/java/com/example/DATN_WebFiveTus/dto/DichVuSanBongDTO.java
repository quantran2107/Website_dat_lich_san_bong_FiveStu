package com.example.DATN_WebFiveTus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class DichVuSanBongDTO {

    private Integer id;

    private Integer idDoThue;

    private Integer idNuocUong;

    private Integer idHoaDonChiTiet;

    private Double tongTien;

    private Integer soLuong;

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

