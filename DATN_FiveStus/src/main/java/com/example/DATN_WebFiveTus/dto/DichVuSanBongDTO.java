package com.example.DATN_WebFiveTus.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private int id;

    private Integer idDoThue;

    private String tenDoThue;

    private Double donGiasDoThue;

    private Integer idNuocUong;

    private String tenNuocUong;

    private Double donGiasNuocUong;

    private Integer idHoaDonChiTiet;

    private Double donGia;

    private int soLuongsDoThue;

    private int soLuongDoThue;

    private int soLuongNuocUong;

    private int soLuongsNuocUong;



    private String trangThai;

//    @JsonIgnore
    private boolean deletedAt;

//    @Temporal(TemporalType.TIMESTAMP)
//    private LocalDateTime createdAt;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    private LocalDateTime updatedAt;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    private boolean deletedAt;
}

