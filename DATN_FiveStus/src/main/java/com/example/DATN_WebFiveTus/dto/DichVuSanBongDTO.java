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

    private String tenDoThue;

    private Double donGiasDoThue;

    private Integer idNuocUong;

    private String tenNuocUong;

    private Double donGiasNuocUong;

    private Integer idHoaDonChiTiet;

    private Double tongTien;

    private int soLuongsDoThue;

//    private int soLuongDoThue;

//    private int soLuongNuocUong;

    private Integer soLuong;

    private int soLuongsNuocUong;

    private String trangThai;

//    private  String imageData;
//    @JsonIgnore
    private boolean deletedAt;

    private String imageDataDoThue;

    private String imageDataNuocUong;


}

