package com.example.DATN_WebFiveTus.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class NuocUongDTO {
    private int id;


    private float donGia;


    private String image;


    private int soLuong;


    private String tenNuocUong;


    private String trangThai;
}
